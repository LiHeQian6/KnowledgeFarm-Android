<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>知识农场后台管理系统</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
    <link rel="stylesheet" href="${ctx}/css/font.css">
	<link rel="stylesheet" href="${ctx}/css/xadmin.css">
    <link rel="stylesheet" href="https://cdn.bootcss.com/Swiper/3.4.2/css/swiper.min.css">
    <script type="text/javascript" src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdn.bootcss.com/Swiper/3.4.2/js/swiper.jquery.min.js"></script>
    <script src="${ctx}/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${ctx}/js/xadmin.js"></script>
    
    <script>
    	//管理员登陆
    	function adminLogin(){
    		var accout = $("#accout").val();
    		var password = $("#password").val();
    		if(accout == "" || password == ""){
    			layer.msg('输入框不能为空');
    		}else{
	    		$.post("${ctx}/admin/login",{"accout":accout,"password":password},function(data){
	    			if(data == "succeed"){
	    				window.location.href="${ctx}/index.jsp";
	    			}else if(data == "fail"){
	    				layer.msg('账号或密码错误');
	    			}else if(data == "notExist"){
	    				layer.msg('管理员账号不存在');
	    			}
	    		}) 
    		}
    	}
    </script>
    
</head>
<body>
    <div class="login-logo"><h1>知识农场后台管理系统</h1></div>
    <div class="login-box">
        <form class="layui-form layui-form-pane" action="javascript:adminLogin()">
            <h3>管理员登陆</h3>
            <label class="login-title" for="username">账号</label>
            <div class="layui-form-item">
                <label class="layui-form-label login-form"><i class="iconfont">&#xe6b8;</i></label>
                <div class="layui-input-inline login-inline">
                  <input id="accout" type="text" name="username" lay-verify="required" placeholder="请输入管理员账号" autocomplete="off" class="layui-input">
                </div>
            </div>
            <label class="login-title" for="password">密码</label>
            <div class="layui-form-item">
                <label class="layui-form-label login-form"><i class="iconfont">&#xe82b;</i></label>
                <div class="layui-input-inline login-inline">
                	<input id="password" type="password" name="password" lay-verify="required" placeholder="请输入密码" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="form-actions" style="margin-top:60px;">
                <button class="btn btn-warning pull-right" lay-submit lay-filter="login" type="submit">登陆</button> 
            </div>
        </form>
    </div>
	<%@ include file="/layout/background.jsp"%>
</body>
</html>