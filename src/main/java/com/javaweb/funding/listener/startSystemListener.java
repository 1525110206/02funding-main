package com.javaweb.funding.listener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.javaweb.funding.bean.Permission;
import com.javaweb.funding.manager.service.PermissionService;
import com.javaweb.funding.util.Const;
import com.vaadin.terminal.gwt.server.WebApplicationContext;

public class startSystemListener implements ServletContextListener {
	//在服务器启动时，创建服务器需要执行的方法
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//将项目上下文路径防止到application域中
		ServletContext application = sce.getServletContext();
		String contextPath = application.getContextPath();
		application.setAttribute("APP_PATH", contextPath);
		System.out.println("applicationListener");
		
		//加载所有许可路径
		//用 ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
		//用上面这个方法是来创建ioc容器的，但是ssm项目一开始已经创建了ioc容器，不能创建两个，下面这个方法是spring用来
		//获取web ioc容器的方法
		ApplicationContext ioc = WebApplicationContextUtils.getWebApplicationContext(application);
		PermissionService permissionService = ioc.getBean(PermissionService.class);
		List<Permission> queryAllPermission = permissionService.queryAllPermission();
		
		Set<String> allURIs = new HashSet<String>();
		
		for(Permission permission : queryAllPermission){
			allURIs.add("/"+permission.getUrl());
		}
		application.setAttribute(Const.ALL_PERMISSION_URI, allURIs);
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
