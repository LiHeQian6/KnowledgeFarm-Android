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
	
		//添加作物信息
		function addCrop(){
			var form = document.getElementById("form1");
			var formData = new FormData(form);
			 $.ajax({
				    url: "${ctx}/admin/crop/addCrop",
				    type: "POST",
				    processData: false,  // 告诉jQuery不要去处理发送的数据
				    contentType: false,  // 告诉jQuery不要去设置Content-Type请求头
				    data: formData,
				    success:function (data) {
				      if(data == "succeed"){
				    	  x_admin_close();
				      }else if(data == "fail"){
		    			  layer.msg('添加失败');
		    		  }else if(data == "null"){
		    			  layer.msg('图片不能为空');
		    		  }
				    }
			 })
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
            <form id="form1" class="layui-form" enctype="multipart/form-data" action="javascript:addCrop()">
            	<div class="layui-form-item">
                    <label class="layui-form-label">
                        	<font color="red">*</font>名称
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="name" name="name" required="" lay-verify="pass"
                        autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>价格
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="price" name="price" required="" lay-verify="nikename"
                        autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>成熟时间
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="matureTime" name="matureTime" required="" lay-verify="pass"
                        autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>价值
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="value" name="value" required="" lay-verify="pass"
                        autocomplete="off" class="layui-input">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                    	*金币
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>提供经验
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="experience" name="experience" required="" lay-verify="pass"
                        autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>img1
                    </label>
                    <div class="layui-input-inline">
                       <input type="file" id="img1" name="upload" accept="image/*" style="margin-top:5px;"/>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>img2
                    </label>
                    <div class="layui-input-inline">
                       <input type="file" id="img2" name="upload" accept="image/*" style="margin-top:5px;"/>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>img3
                    </label>
                    <div class="layui-input-inline">
                        <input type="file" id="img3" name="upload" accept="image/*" style="margin-top:5px;"/>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>img4
                    </label>
                    <div class="layui-input-inline">
                        <input type="file" id="img4" name="upload" accept="image/*" style="margin-top:5px;"/>
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