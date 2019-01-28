package com.javaweb.funding.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javaweb.funding.bean.Member;
import com.javaweb.funding.bean.Permission;
import com.javaweb.funding.bean.User;
import com.javaweb.funding.manager.service.UserService;
import com.javaweb.funding.potal.service.MemberService;
import com.javaweb.funding.util.AjaxResult;
import com.javaweb.funding.util.Const;
import com.javaweb.funding.util.MD5Util;

@Controller
public class DispatcherController {
	@Autowired
	private UserService userService;
	@Autowired
	private MemberService memberService;
	
	
	@RequestMapping("/index")
	public String index(){	
		System.out.println("aaaaaa");
		return "index";
	}

	
	@RequestMapping("/login")
	public String login(HttpServletRequest request, HttpSession session){	
		
		boolean needLogin = true;
		Cookie[] cookies = request.getCookies();
		String logintype = null;
		
		if(cookies != null){
			for(Cookie cookie : cookies){
				if("logincode".equals(cookie.getName())){
					String logincode = cookie.getValue();
					
					String[] split = logincode.split("&");
					if(split.length == 3){
						String loginacct = split[0].split("=")[1];
						String userpwd = split[1].split("=")[1];
						logintype = split[2].split("=")[1];
						
						Map<String, Object> map = new HashMap<>();
						map.put("loginacct", loginacct);
						map.put("userpswd", userpwd);
						map.put("logintype", logintype);
						
						if("user".equals(logintype)){
							User dbLogin = userService.queryUserLogin(map);
							
							if(dbLogin != null){
								session.setAttribute(Const.LOGIN_USER, dbLogin);
								needLogin = false;
							}
							
							//加载当前登录用户的所拥有的许可权限
							
							
							List<Permission> myPermissions = userService.queryPermissionByUserid(dbLogin.getId());
							
							
							
							Permission permissionRoot = null;
							
							Map<Integer, Permission> map1 = new HashMap<Integer, Permission>();
							
							for(Permission innerpermission : myPermissions){
								map1.put(innerpermission.getId(), innerpermission);
							}
							
							for(Permission permission : myPermissions){
								Permission child = permission;
								if(child.getPid() == null){
									permissionRoot = permission;
								}else{
									Permission parent = map1.get(child.getPid());
									parent.getChildren().add(child);
								}
							}
							session.setAttribute("permissionRoot", permissionRoot);
							
							
						}else if("member".equals(logintype)){
								Member dbLogin = memberService.queryMebmerlogin(map);
								if(dbLogin != null){
									session.setAttribute(Const.LOGIN_MEMBER, dbLogin);
									needLogin = false;
								}
							}
						
					}
				}
			}
		}
		if(needLogin){
			return "login";
		}else{
			if("user".equals(logintype)){

			return "redirect:/main.htm";

		}else if("member".equals(logintype)){

			return "redirect:/member.htm";
			}

		}
		return "login";
	}
	
	@RequestMapping("/member")
	public String member(){	
		return "member/member";
	}
	
	
	
	@RequestMapping("/main")
	public String main(HttpSession session){
		
		//加载当前登录用户的所拥有的许可权限
		User user = (User)session.getAttribute(Const.LOGIN_USER);
		
		List<Permission> myPermissions = userService.queryPermissionByUserid(user.getId());
		
		
		
		Permission permissionRoot = null;
		
		Map<Integer, Permission> map = new HashMap<Integer, Permission>();
		
		for(Permission innerpermission : myPermissions){
			map.put(innerpermission.getId(), innerpermission);
		}
		
		for(Permission permission : myPermissions){
			Permission child = permission;
			if(child.getPid() == null){
				permissionRoot = permission;
			}else{
				Permission parent = map.get(child.getPid());
				parent.getChildren().add(child);
			}
		}
		session.setAttribute("permissionRoot", permissionRoot);
		
		System.out.println("1111"+permissionRoot);
		
		return "main";
	}
	
	
	
	@RequestMapping("/logout")
	public String logout(HttpSession session){	
		
		return "redirect:/index.htm";
	}
	
	
	//异步请求
	@ResponseBody
	@RequestMapping("/dologin")
	public Object dologin(String loginacct, String userpswd, String type, String rememberme, HttpSession session, HttpServletResponse response){
		
		AjaxResult result = new AjaxResult();
		try {
	
			Map<String, Object> map = new HashMap<>();
			map.put("loginacct", loginacct);
			map.put("userpswd", MD5Util.digest(userpswd));
			map.put("type", type);
			
			if("user".equals(type)){
				User user = userService.queryUserLogin(map);
				
				session.setAttribute(Const.LOGIN_USER, user);
				
				if("1".equals(rememberme)){
					String logincode = "loginacct="+ user.getLoginacct() +"&userpwd="+ user.getUserpswd() +"&logintype=user";
					
					Cookie c = new Cookie("logincode", logincode);
					
					c.setMaxAge(60*60*24*14); //2周时间Cookie过期， 单位秒
					
					c.setPath("/"); // 表示任何请求路径都可以访问Cookie
					
					response.addCookie(c);
				}
				
				
				//-------------------
				//加载当前登录用户的所拥有的许可权限
				List<Permission> myPermissions = userService.queryPermissionByUserid(user.getId());
				
				Permission permissionRoot = null;
				
				Map<Integer, Permission> map2 = new HashMap<>();
				
				Set<String> myUris = new HashSet<>();//用于拦截器拦截许可权限
				
				for(Permission innerpermission : myPermissions){
					map2.put(innerpermission.getId(), innerpermission);
					myUris.add("/"+innerpermission.getUrl());
				}
				
				session.setAttribute(Const.MY_URIS, myUris);
				
				for(Permission permission:myPermissions){
					Permission child = permission;
					if(child.getPid() == null){
						permissionRoot = permission;
					}else{
						Permission parent = map2.get(child.getPid());//上面的循环已经把所有的父元素和子元素都放进map集合中了，现在用child.getPid()作为条件获取父元素，再存进集合中
						parent.getChildren().add(child);
					}
				}
			}else if("member".equals(type)){
				Member member = memberService.queryMebmerlogin(map);
				
				session.setAttribute(Const.LOGIN_MEMBER, member);
				
				if("1".equals(rememberme)){
					String logincode = "loginacct="+ member.getLoginacct() +"&userpswd="+ member.getUserpswd() +"&logintype=member";
					
					Cookie c = new Cookie("logincode", logincode);
					
					c.setMaxAge(60*60*24*14); //2周时间Cookie过期， 单位秒
					
					c.setPath("/"); // 表示任何请求路径都可以访问Cookie
					
					response.addCookie(c);
				}
			}
			
			result.setData(type);
			result.setSuccess(true);
			
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("登录失败！");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	//同步请求
	/*@RequestMapping("/dologin")
	public String dologin(String loginacct, String userpswd, String type, HttpSession session){
		Map<String, Object> map = new HashMap<>();
		map.put("loginacct", loginacct);
		map.put("userpswd", userpswd);
		map.put("type", type);
		
		User user = userService.queryUserLogin(map);
		
		session.setAttribute(Const.LOGIN_USER, user);
		return "redirect:/main.htm";
	}*/
}
