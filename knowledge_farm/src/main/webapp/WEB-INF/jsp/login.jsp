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
    		var account = $("#account").val();
    		var password = $("#password").val();
            var testCode = $("#testCode").val();
    		if(account == "" || password == ""){
    			layer.msg('输入框不能为空');
    		}else{
	    		$.post("${ctx}/admin/login",{"account":account,"password":password,"testCode":testCode},function(data){
	    			if(data == "succeed"){
	    				window.location.href="${ctx}/admin/toIndex";
	    			}else if(data == "fail"){
	    				layer.msg('账号或密码错误');
	    			}else if(data == "notUser"){
	    				layer.msg('该管理员账号不可用');
	    			}else if(data == "false"){
                        layer.msg('验证码输入错误');
                    }
	    		}) 
    		}
    	}

    	//点击更换验证码
        function changeTestCode(){
            $.post("${ctx}/admin/changeTestCode",{"test":"1"},function(data){
                if(data == "fail"){
                    layer.msg('验证码更换失败');
                }else{
                    console.log('data' + data);
                    $("#testCodeImage").attr("src","${ctx}/photo/" + data);
                }
            });
        }
    </script>
    
</head>
<body>
    <div class="login-logo"><h1>知识农场后台管理系统</h1></div>
    <div class="login-box">
        <form class="layui-form layui-form-pane" action="javascript:adminLogin()">
            <h3>管理员登陆</h3>
            <label class="login-title">账号</label>
            <div class="layui-form-item">
                <label class="layui-form-label login-form"><i class="iconfont">&#xe6b8;</i></label>
                <div class="layui-input-inline login-inline">
                  <input id="account" type="text" name="username" lay-verify="required" placeholder="请输入管理员账号" autocomplete="off" class="layui-input">
                </div>
            </div>
            <label class="login-title" for="password">密码</label>
            <div class="layui-form-item">
                <label class="layui-form-label login-form"><i class="iconfont">&#xe82b;</i></label>
                <div class="layui-input-inline login-inline">
                	<input id="password" type="password" name="password" lay-verify="required" placeholder="请输入密码" autocomplete="off" class="layui-input">
                </div>
            </div>
            <label class="login-title" for="password">验证码</label>
            <div class="layui-form-item">
                <div class="layui-form-inline" style="width: 100%;">
                    <label class="layui-form-label login-form"><i class="iconfont">&#xe82b;</i></label>
                    <div class="layui-input-inline">
                        <input id="testCode" type="text" name="testCode" lay-verify="required" placeholder="请输入验证码" autocomplete="off" class="layui-input">
                    </div>
                    <a href="javascript:changeTestCode()"><img id="testCodeImage" src="${ctx}/photo/${testCodeImage}" style="width: 50px;height: 35px;"></a>
                </div>
            </div>
            <div class="form-actions" style="margin-top:45px;">
                <button class="btn btn-warning pull-right" lay-submit lay-filter="login" type="submit">登陆</button>
            </div>
        </form>
    </div>
<%--    <%@ include file="/layout/background.jsp"%>--%>
    <div class="bg-changer">
        <div class="swiper-container changer-list">
            <div class="swiper-wrapper">
                <<div class="swiper-slide"><img class="item" src="${ctx}/images/a.jpg" alt=""></div>
                <div class="swiper-slide"><img class="item" src="${ctx}/images/b.jpg" alt=""></div>
                <div class="swiper-slide"><img class="item" src="${ctx}/images/c.jpg" alt=""></div>
                <div class="swiper-slide"><img class="item" src="${ctx}/images/d.jpg" alt=""></div>
                <div class="swiper-slide"><img class="item" src="${ctx}/images/e.jpg" alt=""></div>
                <div class="swiper-slide"><img class="item" src="${ctx}/images/f.jpg" alt=""></div>
                <div class="swiper-slide"><img class="item" src="${ctx}/images/g.jpg" alt=""></div>
                <div class="swiper-slide"><img class="item" src="${ctx}/images/h.jpg" alt=""></div>
                <div class="swiper-slide"><img class="item" src="${ctx}/images/i.jpg" alt=""></div>
                <div class="swiper-slide"><img class="item" src="${ctx}/images/j.jpg" alt=""></div>
                <div class="swiper-slide"><img class="item" src="${ctx}/images/k.jpg" alt=""></div>
                <div class="swiper-slide"><span class="reset">恢复默认</span></div>
            </div>
        </div>
        <div class="bg-out"></div>
        <div id="changer-set"><i class="iconfont">&#xe696;</i></div>
    </div>
</body>
</html>