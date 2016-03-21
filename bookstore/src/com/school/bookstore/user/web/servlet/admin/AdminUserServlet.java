package com.school.bookstore.user.web.servlet.admin;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstoreTools.servlet.BaseServlet;

@SuppressWarnings("serial")
public class AdminUserServlet extends BaseServlet {
	  public String login(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	      
		    String user = request.getParameter("adminname");
		    String password = request.getParameter("password");
		    if(!user.equals("ssduter")){
		    	request.setAttribute("msg", "对不起，您不是管理员！");
		    	return "f:/adminjsps/login.jsp";
		    }
		    if(!password.equals("123456")){
		    	request.setAttribute("msg", "管理员密码错误");
		    	return "f:/adminjsps/login.jsp";
		    }
	        return "r:/index.jsp";
	    }
	

}
