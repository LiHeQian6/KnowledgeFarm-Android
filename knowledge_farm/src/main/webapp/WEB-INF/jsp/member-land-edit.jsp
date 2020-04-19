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
    	//修改用户土地信息
    	function updateUserLand(){
    		var grade = $("#grade option:selected").val();
    		var land1 = $("#land1").val();
    		var land2 = $("#land2").val();
    		var land3 = $("#land3").val();
    		var land4 = $("#land4").val();
    		var land5 = $("#land5").val();
    		var land6 = $("#land6").val();
    		var land7 = $("#land7").val();
    		var land8 = $("#land8").val();
    		var land9 = $("#land9").val();
    		var land10 = $("#land10").val();
    		var land11 = $("#land11").val();
    		var land12 = $("#land12").val();
    		var land13 = $("#land13").val();
    		var land14 = $("#land14").val();
    		var land15 = $("#land15").val();
    		var land16 = $("#land16").val();
    		var land17 = $("#land17").val();
    		var land18 = $("#land18").val();
    		
    		$.post("${ctx}/admin/user/updateUserLand",{"account":"${user.account}","land1":land1,"land2":land2,"land3":land3,"land4":land4,"land5":land5
    			,"land6":land6,"land7":land7,"land8":land8,"land9":land9,"land10":land10,"land11":land11,"land12":land12,"land13":land13
    			,"land14":land14,"land15":land15,"land16":land16,"land17":land17,"land18":land18},function(data){
    			if(data == "succeed"){
					x_admin_close();
    			}else if(data == "fail"){
    				layer.msg('修改失败');
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
            <form class="layui-form" action="javascript:updateUserLand()">
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>land1
                    </label>
                    <div class="layui-input-inline">
                    	 <select name="interest" lay-filter="aihao">
                             <c:if test="${empty land.userCrop1}">

                             </c:if>
                             <option value="0">开垦</option>
                             <option value="-1">未开垦</option>
					     </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label"></label>
                    <div class="layui-input-inline">
                        <input type="text" name="water" required lay-verify="required"
                               autocomplete="off" class="layui-input" value="">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        *浇水的限制次数
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label"></label>
                    <div class="layui-input-inline">
                        <input type="text" name="water" required lay-verify="required"
                               autocomplete="off" class="layui-input" value="">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        *施肥的限制次数
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label"></label>
                    <div class="layui-input-inline">
                        <input type="text" name="water" required lay-verify="required"
                               autocomplete="off" class="layui-input" value="">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        *作物进度（该作物进度最多${}）
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label"></label>
                    <div class="layui-input-inline">
                        <input type="text" name="water" required lay-verify="required"
                               autocomplete="off" class="layui-input" value="">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        *干湿状态（0表示干旱，1表示湿润）
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
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