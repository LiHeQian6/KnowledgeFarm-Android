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
    		
    		$.post("${ctx}/admin/user/updateUserLand",{"accout":"${user.accout}","land1":land1,"land2":land2,"land3":land3,"land4":land4,"land5":land5
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
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>land1
                    </label>
                    <div class="layui-input-inline">
                    	 <select id="land1" name="interest" lay-filter="aihao">
					        <option value="${user.land1}" selected="">${user.land1}</option>
					        <option value="0">开垦</option>
					        <option value="-1">未开垦</option>
					     </select>
                        
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>land2
                    </label>
                    <div class="layui-input-inline">
                        <select id="land2" name="interest" lay-filter="aihao">
					        <option value="${user.land2}" selected="">${user.land2}</option>
					        <option value="0">开垦</option>
					        <option value="-1">未开垦</option>
					     </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>land3
                    </label>
                    <div class="layui-input-inline">
                        <select id="land3" name="interest" lay-filter="aihao">
					        <option value="${user.land3}" selected="">${user.land3}</option>
					        <option value="0">开垦</option>
					        <option value="-1">未开垦</option>
					     </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>land4
                    </label>
                    <div class="layui-input-inline">
                        <select id="land4" name="interest" lay-filter="aihao">
					        <option value="${user.land4}" selected="">${user.land4}</option>
					        <option value="0">开垦</option>
					        <option value="-1">未开垦</option>
					     </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>land5
                    </label>
                    <div class="layui-input-inline">
                        <select id="land5" name="interest" lay-filter="aihao">
					        <option value="${user.land5}" selected="">${user.land5}</option>
					        <option value="0">开垦</option>
					        <option value="-1">未开垦</option>
					     </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>land6
                    </label>
                    <div class="layui-input-inline">
                        <select id="land6" name="interest" lay-filter="aihao">
					        <option value="${user.land6}" selected="">${user.land6}</option>
					        <option value="0">开垦</option>
					        <option value="-1">未开垦</option>
					     </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>land7
                    </label>
                    <div class="layui-input-inline">
                        <select id="land7" name="interest" lay-filter="aihao">
					        <option value="${user.land7}" selected="">${user.land7}</option>
					        <option value="0">开垦</option>
					        <option value="-1">未开垦</option>
					     </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>land8
                    </label>
                    <div class="layui-input-inline">
                        <select id="land8" name="interest" lay-filter="aihao">
					        <option value="${user.land8}" selected="">${user.land8}</option>
					        <option value="0">开垦</option>
					        <option value="-1">未开垦</option>
					     </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>land9
                    </label>
                    <div class="layui-input-inline">
                        <select id="land9" name="interest" lay-filter="aihao">
					        <option value="${user.land9}" selected="">${user.land9}</option>
					        <option value="0">开垦</option>
					        <option value="-1">未开垦</option>
					     </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>land10
                    </label>
                    <div class="layui-input-inline">
                        <select id="land10" name="interest" lay-filter="aihao">
					        <option value="${user.land10}" selected="">${user.land10}</option>
					        <option value="0">开垦</option>
					        <option value="-1">未开垦</option>
					     </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>land11
                    </label>
                    <div class="layui-input-inline">
                        <select id="land11" name="interest" lay-filter="aihao">
					        <option value="${user.land11}" selected="">${user.land11}</option>
					        <option value="0">开垦</option>
					        <option value="-1">未开垦</option>
					     </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>land12
                    </label>
                    <div class="layui-input-inline">
                        <select id="land12" name="interest" lay-filter="aihao">
					        <option value="${user.land12}" selected="">${user.land12}</option>
					        <option value="0">开垦</option>
					        <option value="-1">未开垦</option>
					     </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>land13
                    </label>
                    <div class="layui-input-inline">
                        <select id="land13" name="interest" lay-filter="aihao">
					        <option value="${user.land13}" selected="">${user.land13}</option>
					        <option value="0">开垦</option>
					        <option value="-1">未开垦</option>
					     </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>land14
                    </label>
                    <div class="layui-input-inline">
                        <select id="land14" name="interest" lay-filter="aihao">
					        <option value="${user.land14}" selected="">${user.land14}</option>
					        <option value="0">开垦</option>
					        <option value="-1">未开垦</option>
					     </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>land15
                    </label>
                    <div class="layui-input-inline">
                        <select id="land15" name="interest" lay-filter="aihao">
					        <option value="${user.land15}" selected="">${user.land15}</option>
					        <option value="0">开垦</option>
					        <option value="-1">未开垦</option>
					     </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>land16
                    </label>
                    <div class="layui-input-inline">
                        <select id="land16" name="interest" lay-filter="aihao">
					        <option value="${user.land16}" selected="">${user.land16}</option>
					        <option value="0">开垦</option>
					        <option value="-1">未开垦</option>
					     </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>land17
                    </label>
                    <div class="layui-input-inline">
                        <select id="land17" name="interest" lay-filter="aihao">
					        <option value="${user.land17}" selected="">${user.land17}</option>
					        <option value="0">开垦</option>
					        <option value="-1">未开垦</option>
					     </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="L_username" class="layui-form-label">
                        <font color="red">*</font>land18
                    </label>
                    <div class="layui-input-inline">
                        <select id="land18" name="interest" lay-filter="aihao">
					        <option value="${user.land18}" selected="">${user.land18}</option>
					        <option value="0">开垦</option>
					        <option value="-1">未开垦</option>
					     </select>
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