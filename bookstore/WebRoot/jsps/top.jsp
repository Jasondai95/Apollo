<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>top</title>

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
            background:	#5CADAD;
            height:120px
        }
        span{
         font-size:15px;
        color:#FFFFFF;
        font-family:"微软雅黑";
        }

        a {
            text-transform: none;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }
        #login{
        position:absolute;
	    left:25px;
	    top:123px;
        } 
        #content{
        font-size:35px;
        height:105px;
        line-height:105px;
        text-align:center;
        color:#FFFFFF;
        font-family:"微软雅黑";
        position:absoulte;
        }
    </style>
</head>

<body>
<div id="content">WEB校园书城</div>
<div id="login" style="font-size: 10pt;" >
 
    <c:choose>
        <c:when test="${empty sessionScope.session_user}">
            <a href="<c:url value='/jsps/user/login.jsp'/>" target=body><span>登录  </span></a>&nbsp;
            <a href="<c:url value='/jsps/user/regist.jsp'/>" target=body><span >注册</span></a>
        </c:when>
        <c:otherwise>
            您好：${sessionScope.session_user.username}&nbsp;&nbsp;|&nbsp;&nbsp;
            <a href="<c:url value='/jsps/cart/list.jsp'/>" target="body">我的购物车</a>&nbsp;&nbsp;|&nbsp;&nbsp;
            <a href="<c:url value='/OrderServlet?method=myOrders'/>" target="body">我的订单</a>&nbsp;&nbsp;|&nbsp;&nbsp;
            <a href="<c:url value="/UserServlet?method=quit"/>" target="_parent">退出</a>
            <br/>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
