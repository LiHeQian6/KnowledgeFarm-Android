<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
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
	
		//添加用户信息
		function addUser(){
    		var nickName = $("#nickName").val();
    		var password = $("#password").val();
    		var testPassword = $("#testPassword").val();
    		var email = $("#email").val();
    		var grade = $("#grade option:selected").val();
   			if(password == testPassword){
   				$.post("${ctx}/admin/user/addUser",{"nickName":nickName,"password":password,"email":email,"grade":grade},function(data){
   					if(data == "succeed"){
   						x_admin_close();
   	    			}else if(data == "fail"){
   	    				layer.msg('添加失败');
   	    			}
   	    		}) 
   			}else{
   				layer.msg('两次密码输入不一致');
   			}
		}
		
		//关闭弹出框口
		function x_admin_close(){
		    var index = parent.layer.getFrameIndex(window.name);
		    parent.layer.close(index);
		}
		
	</script>

</head>
<body>
    <!-- 中部开始 -->
    <div class="wrapper">
        <!-- 右侧主体开始 -->
        <div class="page-content">
          <div class="content">
            <!-- 右侧内容框架，更改从这里开始 -->
            <form id="form1" class="layui-form" enctype="multipart/form-data" action="javascript:addUser()">
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>名称
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="nickName" name="nickName" required="" lay-verify="nikename"
                        autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>密码
                    </label>
                    <div class="layui-input-inline">
                        <input type="password" id="password" name="username" required="" lay-verify="nikename"
                        autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>确认密码
                    </label>
                    <div class="layui-input-inline">
                        <input type="password" id="testPassword" name="pass" required="" lay-verify="pass"
                        autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>邮箱
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="email" name="nickName" lay-verify=""
                        autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>年级
                    </label>
                    <div class="layui-input-inline">
                        <select id="grade" name="grade">
                            <option value="0" selected="">请选择年级</option>
                            <c:forEach var="grade" items="${grades}">
                                <option value="${grade.key}">${grade.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="layui-form-item layui-form-text">
                    <label for="L_sign" class="layui-form-label">
                    	签名
                    </label>
                    <div class="layui-input-block">
                        <textarea placeholder="随便写些什么刷下存在感" id="L_sign" name="sign" autocomplete="off"
                        class="layui-textarea" style="height: 80px;"></textarea>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                    </label>
                    <button  class="layui-btn" lay-filter="add" lay-submit="">
                    	添加
                    </button>
                </div>
            </form>
            <!-- 右侧内容框架，更改从这里结束 -->
          </div>
        </div>
        <!-- 右侧主体结束 -->
    </div>
    <!-- 中部结束 -->
</body>
</html>