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
    
    <style>
    	.page{
    		margin-right:25px;
    	}
    </style>

	<script>
		//初始化左侧菜单（管理员管理）
		window.onload = function(){
			$("#initAdminManager").attr("class","sub-menu opened");
			$("#initAdminManager1").attr("class","current");
		}
		
		//删除单个管理员信息
		function deleteOneAdmin(id){
			layer.confirm('确认要删除吗？',function(index){
				if(id != "${admin.id}"){
					$.post("${ctx}/admin/deleteOneAdmin",{"id":id},function(data){
		    			if(data == "succeed"){
		    				window.location.href="${ctx}/admin/findAdminPage?exist=1";
		    			}else if(data == "fail"){
		    				layer.msg('删除失败');
		    			}
		    		})   
				}else{
					layer.msg('您正在登陆，不可删除');
				}
				             
            });   
        }
		
		//删除批量管理员信息
        function deleteMultiAdmin() {
        	var arrDelete = document.getElementsByName("checkBox");
        	var deleteStr="";
			for(i in arrDelete){
				if(arrDelete[i].checked){
					if(arrDelete[i].value == "${admin.id}"){
						layer.msg('您已经登录，不可删除');
						return;
					}
					deleteStr = deleteStr + arrDelete[i].value + ",";
				}
			}
            layer.confirm('确认要批量删除吗？',function(index){
            	if(deleteStr != ""){
	        	    $.post("${ctx}/admin/deleteMultiAdmin",{"deleteStr":deleteStr},function(data){
		    			if(data == "succeed"){
		    				window.location.href="${ctx}/admin/findAdminPage?exist=1";
		    			}else if(data == "fail"){
		    				layer.msg('删除失败');
		    			}
		    		}) 
            	}else{
            		layer.msg('删除不能为空');
            	}
            });
        }
		
        //添加管理员信息
        function addAdmin(title,url,w,h){
            x_admin_show(title,url,w,h);
        }
        
		//根据管理员id获取到要修改的管理员信息
		function getUpdateAdminInfo(id,path){
			 $.post("${ctx}/admin/getUpdateAdminInfo",{"id":id},function(data){
			 	updateAdmin('编辑',path,'600','400');
		     }) 
	 	}
   
     	//修改管理员账号信息
        function updateAdmin(title,url,w,h) {
            x_admin_show(title,url,w,h); 
        }
     	
    </script>

</head>
<body>
    <!-- 顶部开始 -->
    	<%@ include file="/layout/header.jsp"%>
    <!-- 顶部结束 -->
    <!-- 中部开始 -->
    <div class="wrapper">
        <!-- 左侧菜单开始 -->
        	<%@ include file="/layout/menuLeft.jsp"%>
        <!-- 左侧菜单结束 -->
        <!-- 右侧主体开始 -->
        <div class="page-content">
          <div class="content">
            <!-- 右侧内容框架，更改从这里开始 -->
            <form class="layui-form xbs" action="${ctx}/admin/findAdminPage">
                <div class="layui-form-pane" style="text-align: center;">
                  <div class="layui-form-item" style="display: inline-block;">
                    <div class="layui-input-inline">
                      <input type="text" name="accout" placeholder="请输入管理员账号" autocomplete="off" class="layui-input" value="${param.accout}">
                      <input type="hidden" name="exist" value="1"/>
                    </div>
                    <div class="layui-input-inline" style="width:80px">
                        <button class="layui-btn"  lay-submit="" lay-filter="sreach"><i class="layui-icon">&#xe615;</i></button>
                    </div>
                  </div>
                </div> 
            </form>
            <xblock>
	            <button class="layui-btn layui-btn-danger" onclick="deleteMultiAdmin()">
	            	<i class="layui-icon">&#xe640;</i>批量删除
	            </button>
	            <button class="layui-btn" onclick="addAdmin('添加管理员','${ctx}/admin-add.jsp','600','500')">
	            	<i class="layui-icon">&#xe608;</i>添加
	            </button>
	            <a href="${ctx}/admin/findAdminPage?accout=${param.accout}&&pageNumber=${adminPage.pageNumber}&&pageSize=${adminPage.pageSize}&&exist=1">
            		<button class="layui-btn" style="margin-left:11px;">
            			<i class="layui-icon">
            				<img style="width:20px;height:20px;margin-top:5px" src="${ctx}/images/save.png"/>
            			</i>刷新
            		</button>
            	</a>
	            <span class="x-right" style="line-height:40px">共有数据：${adminPage.totalRow}条</span>
            </xblock>
            <table class="layui-table">
                <thead >
                    <tr>
                        <th></th>
                        <th style="text-align:center;">管理员ID</th>
                        <th style="text-align:center;">管理员账号</th>
                        <th style="text-align:center;">密码</th>
                        <th style="text-align:center;">状态</th>
                        <th style="text-align:center;">操作</th>
                    </tr>
                </thead>
                <tbody align="center">
                	<c:forEach var="adminPage" items="${adminPage.list}">
	                    <tr>
	                        <td><input type="checkbox" value="${adminPage.id}" name="checkBox"></td>
	                        <td>${adminPage.id}</td>
	                        <td>${adminPage.accout}</td>
	                        <td>${adminPage.password}</td>
	                        <td class="td-status">
	                        	<span class="layui-btn layui-btn-normal layui-btn-mini">存在</span>
	                        </td>
	                        <td class="td-manage" align="center">
	                            <a style="text-decoration:none"  onclick="getUpdateAdminInfo(${adminPage.id},'${ctx}/admin-edit.jsp')" href="javascript:;" title="修改">
	                                <i class="layui-icon">&#xe642;</i>
	                            </a>
	                            <a style="text-decoration:none"  onclick="getUpdateAdminInfo(${adminPage.id},'${ctx}/admin-password.jsp')" href="javascript:;" title="修改">
	                                <i class="layui-icon">&#xe631;</i>
	                            </a>
	                            <a title="删除" href="javascript:;" onclick="deleteOneAdmin(${adminPage.id})" style="text-decoration:none">
	                                <i class="layui-icon">&#xe640;</i>
	                            </a>
	                        </td>
	                    </tr>
                    </c:forEach>
                </tbody>
            </table>
            <!-- 右侧内容框架，更改从这里结束 -->
          </div>
          <!-- 分页处理开始 -->
          		<!-- 上一页 -->
	          	<c:choose>
	        		<c:when test="${adminPage.pageNumber-1 > 0}">
	        			<c:set var="prePage" value="${adminPage.pageNumber-1}"></c:set>
	        		</c:when>
	        		<c:when test="${adminPage.pageNumber-1 <= 0}">
	        			<c:set var="prePage" value="1"></c:set>
	        		</c:when>
	        	</c:choose>
	        	<!-- 查询结果不为空 -->
	          	<c:if test="${adminPage.totalPage != 0}">
	          		<!-- 下一页 -->
	          		<c:choose>
	          			<c:when test="${adminPage.pageNumber+1 <= adminPage.totalPage}">
	          				<c:set var="nextPage" value="${adminPage.pageNumber+1}"></c:set>
	          			</c:when>
	          			<c:when test="${adminPage.pageNumber+1 > adminPage.totalPage}">
	          				<c:set var="nextPage" value="${adminPage.totalPage}"></c:set>
	          			</c:when>
	          		</c:choose>
	          		<!-- 末页 -->
	          		<c:set var="lastPage" value="${adminPage.totalPage}"></c:set>
	          	</c:if>
	          	<!-- 查询结果为空 -->
	          	<c:if test="${adminPage.totalPage == 0}">
	          		<!-- 下一页 -->
	          		<c:set var="nextPage" value="1"></c:set>
	          		<!-- 末页 -->
	          		<c:set var="lastPage" value="1"></c:set>
	          	</c:if>
			  <div align="center">
				<a  class="page" style="margin-left:25px;" href="${ctx}/admin/findAdminPage?accout=${param.accout}&&pageNumber=1&&pageSize=${adminPage.pageSize}&&exist=1">首页</a>
				<a  class="page" href="${ctx}/admin/findAdminPage?accout=${param.accout}&&pageNumber=${prePage}&&pageSize=${adminPage.pageSize}&&exist=1">上一页</a>
				<a  class="page" href="${ctx}/admin/findAdminPage?accout=${param.accout}&&pageNumber=${nextPage}&&pageSize=${adminPage.pageSize}&&exist=1">下一页</a>
				<a  class="page" href="${ctx}/admin/findAdminPage?accout=${param.accout}&&pageNumber=${lastPage}&&pageSize=${adminPage.pageSize}&&exist=1">末页</a>			
			  </div>
			  <div align="center" style="margin-top:20px;">
				<span style="margin-right:10px;">
					<!-- 查询结果不为空 -->
					<c:if test="${adminPage.totalPage != 0}">
						${adminPage.pageNumber}
					</c:if>
					<!-- 查询结果为空 -->
					<c:if test="${adminPage.totalPage == 0}">
						0
					</c:if>
				</span>
				<span>/</span>
				<span style="margin-left:10px;">${adminPage.totalPage}</span>
			  </div>
		  <!-- 分页处理结束 -->
        <!-- 右侧主体结束 -->
    </div>
    <!-- 中部结束 -->
    <!-- 底部开始 -->
    <!-- 底部结束 -->
    <!-- 背景切换开始 -->
    	<%@ include file="/layout/background.jsp"%>
    <!-- 背景切换结束 -->
    <!-- 页面动态效果 -->
    <script>

        layui.use(['laydate'], function(){
          laydate = layui.laydate;//日期插件

          //以上模块根据需要引入
          //
          

          
          var start = {
            min: laydate.now()
            ,max: '2099-06-16 23:59:59'
            ,istoday: false
            ,choose: function(datas){
              end.min = datas; //开始日选好后，重置结束日的最小日期
              end.start = datas //将结束日的初始值设定为开始日
            }
          };
          
          var end = {
            min: laydate.now()
            ,max: '2099-06-16 23:59:59'
            ,istoday: false
            ,choose: function(datas){
              start.max = datas; //结束日选好后，重置开始日的最大日期
            }
          };
          
          document.getElementById('LAY_demorange_s').onclick = function(){
            start.elem = this;
            laydate(start);
          }
          document.getElementById('LAY_demorange_e').onclick = function(){
            end.elem = this
            laydate(end);
          }
          
        });

        /*用户-查看*/
        function member_show(title,url,id,w,h){
            x_admin_show(title,url,w,h);
        }

         /*用户-停用*/
        function member_stop(obj,id){
            layer.confirm('确认要停用吗？',function(index){
                //发异步把用户状态进行更改
                $(obj).parents("tr").find(".td-manage").prepend('<a style="text-decoration:none" onClick="member_start(this,id)" href="javascript:;" title="启用"><i class="layui-icon">&#xe62f;</i></a>');
                $(obj).parents("tr").find(".td-status").html('<span class="layui-btn layui-btn-disabled layui-btn-mini">已停用</span>');
                $(obj).remove();
                layer.msg('已停用!',{icon: 5,time:1000});
            });
        }

        /*用户-启用*/
        function member_start(obj,id){
            layer.confirm('确认要启用吗？',function(index){
                //发异步把用户状态进行更改
                $(obj).parents("tr").find(".td-manage").prepend('<a style="text-decoration:none" onClick="member_stop(this,id)" href="javascript:;" title="停用"><i class="layui-icon">&#xe601;</i></a>');
                $(obj).parents("tr").find(".td-status").html('<span class="layui-btn layui-btn-normal layui-btn-mini">已启用</span>');
                $(obj).remove();
                layer.msg('已启用!',{icon: 6,time:1000});
            });
        }
        
        </script>
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