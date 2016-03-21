<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>图书分类</title>
    
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
	body {
		font-size: 10pt;
		background: rgb(254,238,189);
	}
	.icon {
		margin:5px;
		border: solid 2px gray;
		width: 160px;
		height: 190px;
		text-align: center;
		float: left;
	}
	.category{
		margin:5px;
		width: 1400px;
		height: 260px;
		text-align: left;
		float: left;
	}
</style>
  </head>
  
  <body>
  <c:forEach items="${categoryList}" var="c">
	  <div class="category">
	    <h3>${c.cname}</h3>
        <c:forEach items="${bookList}" var="book">
		  <c:if test="${c.cid eq book.category.cid}">
            <div class="icon">
              <a href="<c:url value='/admin/AdminBookServlet?method=load&bid=${book.bid}'/>"><img src="<c:url value='/${book.image}'/>" border="0"/></a>
              <br/>
     	      <a href="<c:url value='/admin/AdminBookServlet?method=load&bid=${book.bid}'/>">${book.bname}</a>
			</div>
		    </c:if>
		</c:forEach>
	  </div>
  </c:forEach>
  </body>
 
</html>

