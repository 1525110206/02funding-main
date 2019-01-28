package com.javaweb.funding.interceptor;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.javaweb.funding.bean.Member;
import com.javaweb.funding.bean.User;
import com.javaweb.funding.util.Const;

public class LoginInterceptor extends HandlerInterceptorAdapter{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	throws Exception {
		
		//1.定义哪些路径是不需要拦截（将这些路径称为白名单）
		Set<String> uri = new HashSet<String>();
		
		uri.add("/user/reg.do");
		uri.add("/user/reg.htm");
		uri.add("/login.htm");
		uri.add("/dologin.do");
		uri.add("/logout.do");
		uri.add("/index.htm");
		
		
		//获取请求路径
		String servletPath = request.getServletPath();
		
		if(uri.contains(servletPath)){
			return true;//这个拦截器结束，继续下一个拦截器
		}
		//判断用户是否登录，如果登录就放行
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute(Const.LOGIN_USER);
		Member member = (Member)session.getAttribute(Const.LOGIN_MEMBER);
		
		
		if(user != null || member != null){
			return true;
		}else{
			response.sendRedirect(request.getContextPath()+"/login.htm");
			//有一个问题？执行重定向的代码之后，后面的代码还执行吗？
			//是执行的，只有服务器代码全部走完再跳，那个页面才能出来
			return false;//执行这段代码表示后面的拦截器不执行
		}
		
	}
}
