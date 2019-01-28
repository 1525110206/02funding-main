package com.javaweb.funding.interceptor;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.javaweb.funding.util.Const;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//查询所有许可，如果每次拦截请求都查询数据库表的所有许可，效率比较低
		//改进效率：在服务器启动时加载所有许可路径，并存放application域中
		Set<String> allURIs = (Set<String>)request.getSession().getServletContext().getAttribute(Const.ALL_PERMISSION_URI);
		
		//判断请求路径是否在所有许可范围内
		String servletPath = request.getServletPath();
		System.out.println(servletPath);
		if(allURIs.contains(servletPath)){	
			//判断请求路径是否在用户所拥有的权限内
			Set<String> myURIs = (Set<String>)request.getSession().getAttribute(Const.MY_URIS);
			if(myURIs.contains(servletPath)){
				return true;
			}else{
				response.sendRedirect(request.getContextPath()+"/login.htm");
				return false;
			}
		}else{//不在拦截范围内，放行
			return true;
		}
	}
}
