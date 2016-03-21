<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>用户登录</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
	  #title{
	   font-family:"楷体"; 
	   font-size:20px;
	   color:#5CADAD;
	  }
	   #login{
	  position: relative; 
	  left:260px; 
	  top: 50px;
	  }
	</style>
	  

  </head>
  
  
  <body>
  <div id="login">
  <div id="title">用户登录</div>
<p style="color: red; font-weight: 900">${msg }</p>
       <form action="<c:url value='/UserServlet'/>" method="post" target=_parent>
	      <input type="hidden" name="method" value="login"/>
	                    用户名：<input type="text" name="username" value="${form.username}"/><br/>
	                    密　码：<input type="password" name="password" value="${form.password}"/><br/>
	      <input type="submit" value="登录"/>
       </form>
   </div>
  </body>
</html>
