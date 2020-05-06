<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/6 0006
  Time: 22:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <jsp:forward page="${ctx}/admin/toLogin"></jsp:forward>
</body>
</html>
