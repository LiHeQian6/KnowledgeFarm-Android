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
			var form = document.getElementById("form1");
			var formData = new FormData(form);
			 $.ajax({
				    url: "${ctx}/admin/user/addUser",
				    type: "POST",
				    processData: false,  // 告诉jQuery不要去处理发送的数据
				    contentType: false,  // 告诉jQuery不要去设置Content-Type请求头
				    data: formData,
				    success:function (data) {
				      if(data == "succeed"){
				    	  x_admin_close();
				      }else if(data == "fail"){
		    			  layer.msg('添加失败');
		    		  }else if(data == "already"){
		    			  layer.msg('该授权Id已存在');
		    		  }else if(data == "null"){
		    			  layer.msg('图片不能为空');
		    		  }
				    }
			 })
		}
	
		//添加用户信息
		function a(){
			var openId = $("#openId").val();
    		var nickName = $("#nickName").val();
    		var photo = $("#photo").val();
    		var type = $("input[name='loginRadio']:checked").val();
    		if(openId == "" || nickName == "" || photo == "" || type == ""){
    			layer.msg('输入框不能为空');
    		}else{
				$.post("${ctx}/admin/user/addUser",{"openId":openId,"nickName":nickName,"photo":photo,"type":type},function(data){
					if(data == "succeed"){
						x_admin_close();
	    			}else if(data == "fail"){
	    				layer.msg('添加失败');
	    			}else if(data == "already"){
	    				layer.msg('该授权Id已存在');
	    			}
	    		}) 
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
                    <label for="L_pass" class="layui-form-label">
                        	<font color="red">*</font>授权Id
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="openId" name="openId" required="" lay-verify="pass"
                        autocomplete="off" class="layui-input">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                    	 *将会成为您授权登陆的唯一认证
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>名称
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="nickName" name="nickName" required="" lay-verify="nikename"
                        autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_repass" class="layui-form-label">
                        <font color="red">*</font>登陆类型
                    </label>
                    <div class="layui-inline">
                        <div class="layui-input-inline">
                            <input type="radio" name="type" value="QQ" checked title="QQ">
                            <input type="radio" name="type" value="微信" title="微信">
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_pass" class="layui-form-label">
                        <font color="red">*</font>头像
                    </label>
                    <div class="layui-input-inline">
                        <input type="file" id="photo" name="upfile" accept="image/*" style="margin-top:5px;"/>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_repass" class="layui-form-label">
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
    <script>
    //百度统计可去掉
    var _hmt = _hmt || [];
    (function() {
      var hm = document.createElement("script");
      hm.src = "https://hm.baidu.com/hm.js?b393d153aeb26b46e9431fabaf0f6190";
      var s = document.getElementsByTagName("script")[0]; 
      s.parentNode.insertBefore(hm, s);
    })();
    </script>
</body>
</html>