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
		//初始化左侧菜单（作物管理）
		window.onload = function(){
			$("#initCropManager").attr("class","sub-menu opened");
			$("#initCropManager2").attr("class","current");
		}
		
		//恢复单个作物信息
        function recoveryOneCrop(id){
            layer.confirm('确认要恢复吗？',function(index){
            	$.post("${ctx}/admin/crop/recoveryOneCrop",{"id":id},function(data){
	    			if(data == "succeed"){
	    				window.location.href="${ctx}/admin/crop/findCropPage?name=${param.name}&&pageNumber=${cropPage.currentPageNum}&&pageSize=${cropPage.pageSize}&&exist=0";
	    			}else if(data == "fail"){
	    				layer.msg('恢复失败');
	    			}
	    		})    
            });
        }
		
        //恢复批量作物信息
        function recoveryMultiCrop() {
            var arrRecovery = document.getElementsByName("checkBox");
            var recoveryStr="";
		    for(i in arrRecovery){
			   if(arrRecovery[i].checked){
				   recoveryStr = recoveryStr + arrRecovery[i].value + ",";
			   }
		    }
            layer.confirm('确认要批量恢复吗？',function(index){
            	if(recoveryStr != ""){
	        	    $.post("${ctx}/admin/crop/recoveryMultiCrop",{"recoveryStr":recoveryStr},function(data){
			    	 	 if(data == "succeed"){
			    			 window.location.href="${ctx}/admin/crop/findCropPage?name=${param.name}&&pageNumber=${cropPage.currentPageNum}&&pageSize=${cropPage.pageSize}&&exist=0";
			    		 }else if(data == "fail"){
			    			 layer.msg('恢复失败');
			    		 }
		    	    }) 
            	}else{
            		layer.msg('恢复不能为空');
            	}
            });
         }
        
      	//彻底删除作物信息
        function deleteThoroughCrop(id){
            layer.confirm('彻底删除无法恢复，确认要删除数据吗？',function(index){
            	$.post("${ctx}/admin/crop/deleteThoroughCrop",{"id":id},function(data){
	    			if(data == "succeed"){
	    				window.location.href="${ctx}/admin/crop/findCropPage?name=${param.name}&&pageNumber=${cropPage.currentPageNum}&&pageSize=${cropPage.pageSize}&&exist=0";
	    			}else if(data == "fail"){
	    				layer.msg('删除失败');
	    			}
	    		}) 
            });
        }   
    </script>

</head>
<body>
    <!-- 顶部开始 -->
<%--    	<%@ include file="/layout/header.jsp"%>--%>
	<div class="container">
		<div class="logo"><a href="${ctx}/admin/toIndex">知识农场后台管理系统</a></div>
		<div class="open-nav"><i class="iconfont">&#xe699;</i></div>
		<ul class="layui-nav right" lay-filter="">
			<li class="layui-nav-item">
				<a href="javascript:;">${admin.account}</a>
				<dl class="layui-nav-child">
					<dd><a href="${ctx}/admin/logout">退出</a></dd>
				</dl>
			</li>
			<li class="layui-nav-item"><a href="/"></a></li>
		</ul>
	</div>
    <!-- 顶部结束 -->
    <!-- 中部开始 -->
    <div class="wrapper">
        <!-- 左侧菜单开始 -->
<%--        	<%@ include file="/layout/menuLeft.jsp"%>--%>
		<div class="left-nav">
			<div id="side-nav">
				<ul id="nav">
					<li class="list" current>
						<a href="${ctx}/admin/toIndex">
							<i class="iconfont">&#xe761;</i>
							欢迎页面
							<i class="iconfont nav_right">&#xe697;</i>
						</a>
					</li>
					<li class="list">
						<a href="javascript:;">
							<i class="iconfont">&#xe70b;</i>
							用户管理
							<i class="iconfont nav_right">&#xe697;</i>
						</a>
						<ul id="initUserManager" class="sub-menu">
							<li id="initUserManager1">
								<a href="${ctx}/admin/user/findUserPage?exist=1">
									<i class="iconfont">&#xe6a7;</i>
									用户列表
								</a>
							</li>
							<li id="initUserManager2">
								<a href="${ctx}/admin/user/findUserPage?exist=0">
									<i class="iconfont">&#xe6a7;</i>
									用户删除
								</a>
							</li>
						</ul>
					</li>
					<li class="list" >
						<a href="javascript:;">
							<i class="iconfont">&#xe6a3;</i>
							作物管理
							<i class="iconfont nav_right">&#xe697;</i>
						</a>
						<ul id="initCropManager" class="sub-menu">
							<li id="initCropManager1">
								<a href="${ctx}/admin/crop/findCropPage?exist=1">
									<i class="iconfont">&#xe6a7;</i>
									作物列表
								</a>
							</li>
							<li id="initCropManager2">
								<a href="${ctx}/admin/crop/findCropPage?exist=0">
									<i class="iconfont">&#xe6a7;</i>
									作物删除
								</a>
							</li>
						</ul>
					</li>
					<li class="list" >
						<a href="javascript:;">
							<i class="iconfont">&#xe6a3;</i>
							宠物管理
							<i class="iconfont nav_right">&#xe697;</i>
						</a>
						<ul id="initPetManager" class="sub-menu">
							<li id="initPetManager1">
								<a href="${ctx}/admin/pet/findPetPage?exist=1">
									<i class="iconfont">&#xe6a7;</i>
									宠物列表
								</a>
							</li>
							<li id="initPetManager2">
								<a href="${ctx}/admin/pet/findPetPage?exist=0">
									<i class="iconfont">&#xe6a7;</i>
									宠物删除
								</a>
							</li>
						</ul>
					</li>
					<li class="list" >
						<a href="javascript:;">
							<i class="iconfont">&#xe6a3;</i>
							宠物道具管理
							<i class="iconfont nav_right">&#xe697;</i>
						</a>
						<ul id="initPetUtilManager" class="sub-menu">
							<li id="initPetUtilManager1">
								<a href="${ctx}/admin/petUtil/findPetUtilPage?exist=1">
									<i class="iconfont">&#xe6a7;</i>
									宠物道具列表
								</a>
							</li>
							<li id="initPetUtilManager2">
								<a href="${ctx}/admin/petUtil/findPetUtilPage?exist=0">
									<i class="iconfont">&#xe6a7;</i>
									宠物道具删除
								</a>
							</li>
						</ul>
					</li>
					<li class="list">
						<a href="javascript:;">
							<i class="iconfont">&#xe6a3;</i>
							土地管理
							<i class="iconfont nav_right">&#xe697;</i>
						</a>
						<ul id="initUserLandManager" class="sub-menu">
							<li id="initUserLandManager1">
								<a href="${ctx}/admin/land/findPageLand">
									<i class="iconfont">&#xe6a7;</i>
									土地列表
								</a>
							</li>
						</ul>
					</li>
					<li class="list" >
						<a href="javascript:;">
							<i class="iconfont">&#xe6a3;</i>
							通知管理
							<i class="iconfont nav_right">&#xe697;</i>
						</a>
						<ul id="sendNotificationManager" class="sub-menu">
							<li id="sendPushManager">
								<a href="${ctx}/admin/notification/toNotification">
									<i class="iconfont">&#xe6a7;</i>
									发送通知
								</a>
							</li>
							<li id="sendCustomMessageManager">
								<a href="${ctx}/admin/notification/toNotificationCustomMessage">
									<i class="iconfont">&#xe6a7;</i>
									自定义消息
								</a>
							</li>
						</ul>
					</li>
					<li class="list" >
						<a href="javascript:;">
							<i class="iconfont">&#xe6a3;</i>
							管理员管理
							<i class="iconfont nav_right">&#xe697;</i>
						</a>
						<ul id="initAdminManager" class="sub-menu">
							<li id="initAdminManager1">
								<a href="${ctx}/admin/findAdminPage?exist=1">
									<i class="iconfont">&#xe6a7;</i>
									管理员列表
								</a>
							</li>
							<li id="initAdminManager2">
								<a href="${ctx}/admin/findAdminPage?exist=0">
									<i class="iconfont">&#xe6a7;</i>
									管理员删除
								</a>
							</li>
						</ul>
					</li>
				</ul>
			</div>
		</div>
        <!-- 左侧菜单结束 -->
        <!-- 右侧主体开始 -->
        <div class="page-content">
          <div class="content">
            <!-- 右侧内容框架，更改从这里开始 -->
            <form class="layui-form xbs" action="${ctx}/admin/crop/findCropPage">
                <div class="layui-form-pane" style="text-align: center;">
                  <div class="layui-form-item" style="display: inline-block;">
                    <div class="layui-input-inline">
                      <input type="text" name="name" placeholder="请输入作物名称" autocomplete="off" class="layui-input" value="${param.name}">
                      <input type="hidden" name="exist" value="0"/>
                    </div>
                    <div class="layui-input-inline" style="width:80px">
                        <button class="layui-btn"  lay-submit="" lay-filter="sreach"><i class="layui-icon">&#xe615;</i></button>
                    </div>
                  </div>
                </div> 
            </form>
            <xblock>
	            <button class="layui-btn layui-btn-danger" onclick="recoveryMultiCrop()">
	          		<i class="layui-icon">&#xe640;</i>批量恢复
	            </button>
	            <span class="x-right" style="line-height:40px">共有数据：${cropPage.totalCount} 条</span>
	        </xblock>
            <table class="layui-table">
                <thead >
                    <tr>
                        <th></th>
                        <th style="text-align:center;">作物ID</th>
                        <th style="text-align:center;">名称</th>
                        <th style="text-align:center;">价格</th>
                        <th style="text-align:center;">img1</th>
                        <th style="text-align:center;">img2</th>
                        <th style="text-align:center;">img3</th>
                        <th style="text-align:center;">img4</th>
                        <th style="text-align:center;">成熟时间</th>
                        <th style="text-align:center;">价值</th>
                        <th style="text-align:center;">提供经验</th>
                        <th style="text-align:center;">状态</th>
                        <th style="text-align:center;">操作</th>
                    </tr>
                </thead>
                <tbody align="center">
                    <c:forEach var="cropPage" items="${cropPage.list}">
	                    <tr>
	                        <td><input type="checkbox" value="${cropPage.id}" name="checkBox"></td>
	                        <td>${cropPage.id}</td>
	                        <td>${cropPage.name}</td>
	                        <td>${cropPage.price}</td>
	                        <td><img style="width:50px;height:50px;" src="${ctx}/photo/${cropPage.img1}"/></td>
	                        <td><img style="width:50px;height:50px;" src="${ctx}/photo/${cropPage.img2}"/></td>
							<td><img style="width:50px;height:50px;" src="${ctx}/photo/${cropPage.img3}"/></td>
							<td><img style="width:50px;height:50px;" src="${ctx}/photo/${cropPage.img4}"/></td>
	                        <td>${cropPage.matureTime}天</td>
	                        <td>${cropPage.value}金币</td>
	                        <td>${cropPage.experience}</td>
	                        <td class="td-status">
	                        	<span class="layui-btn layui-btn-danger layui-btn-mini">已删除</span>
	                        </td>
	                        <td class="td-manage" align="center">
	                            <a style="text-decoration:none" onclick="recoveryOneCrop(${cropPage.id})" href="javascript:;" title="恢复">
	                                <i class="layui-icon">&#xe618;</i>
	                            </a>
	                            <a title="彻底删除" href="javascript:;" onclick="deleteThoroughCrop(${cropPage.id})" style="text-decoration:none">
	                                <i class="layui-icon">&#xe640;</i>
	                            </a>
	                        </td>
	                    </tr>
	            	</c:forEach>
            </table>
            <!-- 右侧内容框架，更改从这里结束 -->
          </div>
          <!-- 分页处理开始 -->
		  <div align="center">
			<a  class="page" style="margin-left:25px;" href="${ctx}/admin/crop/findCropPage?name=${param.name}&&pageNumber=1&&pageSize=${cropPage.pageSize}&&exist=0">首页</a>
			<a  class="page" href="${ctx}/admin/crop/findCropPage?name=${param.name}&&pageNumber=${cropPage.prePageNum}&&pageSize=${cropPage.pageSize}&&exist=0">上一页</a>
			<a  class="page" href="${ctx}/admin/crop/findCropPage?name=${param.name}&&pageNumber=${cropPage.nextPageNum}&&pageSize=${cropPage.pageSize}&&exist=0">下一页</a>
			<a  class="page" href="${ctx}/admin/crop/findCropPage?name=${param.name}&&pageNumber=${cropPage.totalPageNum}&&pageSize=${cropPage.pageSize}&&exist=0">末页</a>
		  </div>
		  <div align="center" style="margin-top:20px;">
			  <span style="margin-right:10px;">${cropPage.currentPageNum}</span>
			  <span>/</span>
			  <span style="margin-left:10px;">${cropPage.totalPageNum}</span>
		  </div>
		  <!-- 分页处理结束 -->
        </div>
        <!-- 右侧主体结束 -->
    </div>
    <!-- 中部结束 -->
    <!-- 底部开始 -->
    <!-- 底部结束 -->
    <!-- 背景切换开始 -->
<%--    	<%@ include file="/layout/background.jsp"%>--%>
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

        //批量恢复提交
             function recoverAll () {
                layer.confirm('确认要批量恢复吗？',function(index){
                    //捉到所有被选中的，发异步进行恢复
                    layer.msg('恢复成功', {icon: 1});
                });
             }

            /*用户-彻底删除*/
            function member_unset(obj,id){
                layer.confirm('彻底删除无法恢复，确认要删除数据吗？',function(index){
                    //发异步删除数据
                    $(obj).parents("tr").remove();
                    layer.msg('已彻底删除',{icon:1,time:1000});
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