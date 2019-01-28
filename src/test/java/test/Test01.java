package test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.javaweb.funding.bean.User;
import com.javaweb.funding.manager.service.UserService;
import com.javaweb.funding.util.MD5Util;

public class Test01 {

	public static void main(String[] args){
		ApplicationContext ioc = new ClassPathXmlApplicationContext("spring/spring*.xml");
		
		UserService userService = ioc.getBean(UserService.class);
		
		for(int i = 0; i < 100; i++){
			User user = new User();
			user.setLoginacct("test" + i);
			user.setUsername("test0" + i);
			user.setEmail("test0" + i + "@qq.com");
			user.setCreatetime("2018-08-06 14:34:00");
			user.setUserpswd(MD5Util.digest16("123"));
			userService.saveUser(user);
		}
		
		
	}
	
}
