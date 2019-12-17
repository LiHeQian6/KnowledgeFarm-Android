package com.farm.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.farm.model.Admin;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class AdminInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		
		HttpServletRequest request = inv.getController().getRequest();
		HttpServletResponse response = inv.getController().getResponse();
		HttpSession session = request.getSession();

		Admin admin = (Admin) session.getAttribute("admin");
		if(admin == null) {
			System.out.println("aaa");
			try {
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		inv.invoke();
		
		System.out.println("AdminOver");
	}
}
