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
    
    <script type="text/javascript">
	  	//修改用户信息
		function updateUser(){
			var form = document.getElementById("form1");
			var formData = new FormData(form);
			 $.ajax({
				    url: "${ctx}/admin/user/updateUser",
				    type: "POST",
				    processData: false,  // 告诉jQuery不要去处理发送的数据
				    contentType: false,  // 告诉jQuery不要去设置Content-Type请求头
				    data: formData,
				    success:function (data) {
				    	if(data == "succeed"){
							x_admin_close();
		    			}else if(data == "fail"){
		    				layer.msg('修改失败');
		    			}else if(data == "already"){
		    				layer.msg('该账号已存在');
		    			}
				    }
			 })
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
            <form id="form1" class="layui-form" enctype="multipart/form-data" action="javascript:updateUser()">
            	<input type="hidden" name="oldAccout" value="${user.accout}"/>
            	<input type="hidden" name="photo" value="${user.photo}"/>
            	<input type="hidden" name="photoName" value="${user.photoName}"/>
                <div class="layui-form-item">
                   <label for="L_username" class="layui-form-label">
                    	<font color="red">*</font>账号
                    </label>
                    <div class="layui-input-inline">
                        <input id="accout" type="text" id="L_username" name="newAccout" required lay-verify="required"
                        autocomplete="off" class="layui-input" value="${user.accout}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                    	<font color="red">*</font>名称
                    </label>
                    <div class="layui-input-inline">
                        <input id="nickName" type="text" id="L_username" name="nickName" required lay-verify="required"
                        autocomplete="off" class="layui-input" value="${user.nickName}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>邮箱
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="email" name="email" required="" lay-verify="nikename"
                        autocomplete="off" class="layui-input" value="${user.email}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>年级
                    </label>
                    <div class="layui-input-inline">
                        <select id="grade" name="grade" lay-filter="aihao">
					        <option value="1" selected="">一年级上</option>
					        <option value="2">一年级下</option>
					        <option value="3">二年级上</option>
					        <option value="4">二年级下</option>
					        <option value="5">三年级上</option>
					        <option value="6">三年级下</option>
					     </select>
                    </div>
                </div>
                 <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                    	<font color="red">*</font>等级
                    </label>
                    <div class="layui-input-inline">
                        <input id="level" type="text" id="L_username" name="level" required lay-verify="required"
                        autocomplete="off" class="layui-input" value="${user.level}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                    	<font color="red">*</font>经验
                    </label>
                    <div class="layui-input-inline">
                        <input id="experience" type="text" id="L_username" name="experience" required lay-verify="required"
                        autocomplete="off" class="layui-input" value="${user.experience}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                    	<font color="red">*</font>金币
                    </label>
                    <div class="layui-input-inline">
                        <input id="money" type="text" id="L_username" name="money" required lay-verify="required"
                        autocomplete="off" class="layui-input" value="${user.money}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                    	<font color="red">*</font>数学
                    </label>
                    <div class="layui-input-inline">
                        <input id="mathRewardCount" type="text" id="L_username" name="mathRewardCount" required lay-verify="required"
                        autocomplete="off" class="layui-input" value="${user.mathRewardCount}">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                    	*剩余奖励次数
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                    	<font color="red">*</font>英语
                    </label>
                    <div class="layui-input-inline">
                        <input id="englishRewardCount" type="text" id="L_username" name="englishRewardCount" required lay-verify="required"
                        autocomplete="off" class="layui-input" value="${user.englishRewardCount}">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                    	*剩余奖励次数
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                    	<font color="red">*</font>语文
                    </label>
                    <div class="layui-input-inline">
                        <input id="chineseRewardCount" type="text" id="L_username" name="chineseRewardCount" required lay-verify="required"
                        autocomplete="off" class="layui-input" value="${user.chineseRewardCount}">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                    	*剩余奖励次数
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                    	<font color="red">*</font>浇水
                    </label>
                    <div class="layui-input-inline">
                        <input id="water" type="text" id="L_username" name="water" required lay-verify="required"
                        autocomplete="off" class="layui-input" value="${user.water}">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                    	*剩余次数
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                    	<font color="red">*</font>施肥
                    </label>
                    <div class="layui-input-inline">
                        <input id="fertilizer" type="text" id="L_username" name="fertilizer" required lay-verify="required"
                        autocomplete="off" class="layui-input" value="${user.fertilizer}">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                    	*剩余次数
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                    	<font color="red">*</font>在线
                    </label>
				    <div class="layui-input-block">
				    	<c:choose>
				    		<c:when test="${user.online == 0}">
				    			<input type="checkbox" name="online" lay-skin="switch" lay-filter="switchTest" title="开关">
				    		</c:when>
				    		<c:when test="${user.online == 1}">
				    			<input type="checkbox" checked="" name="online" lay-skin="switch" lay-filter="switchTest" title="开关">
				    		</c:when>
				    	</c:choose>
				    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_city" class="layui-form-label">
                    	<font color="red">*</font>头像
                    </label>
                    <div class="layui-input-inline">
                        <input type="file" id="photo" name="upfile" accept="image/*" style="margin-top:5px;"/>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_sign" class="layui-form-label">
                    </label>
                    <button class="layui-btn" key="set-mine" lay-filter="save" lay-submit>
                    	 保存          
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