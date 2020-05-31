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
		//初始化左侧菜单（用户土地管理）
		window.onload = function(){
			$("#initUserLandManager").attr("class","sub-menu opened");
			$("#initUserLandManager1").attr("class","current");
		}
   
     	//修改用户土地信息
        function updateUserLand (title,url,w,h) {
            x_admin_show(title,url,w,h); 
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
							用户宠物管理
							<i class="iconfont nav_right">&#xe697;</i>
						</a>
						<ul id="initUserPetHouseManager" class="sub-menu">
							<li id="initUserPetHouseManager1">
								<a href="${ctx}/admin/user_pet_house/findUserPetHousePage">
									<i class="iconfont">&#xe6a7;</i>
									用户宠物列表
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
							题目管理
							<i class="iconfont nav_right">&#xe697;</i>
						</a>
						<ul id="initQuestionManager" class="sub-menu">
							<li id="initQuestionManager1">
								<a href="${ctx}/admin/question/findAllQuestion?questionTypeId=1">
									<i class="iconfont">&#xe6a7;</i>
									单选题
								</a>
							</li>
							<li id="initQuestionManager2">
								<a href="${ctx}/admin/question/findAllQuestion?questionTypeId=2">
									<i class="iconfont">&#xe6a7;</i>
									填空题
								</a>
							</li>
							<li id="initQuestionManager3">
								<a href="${ctx}/admin/question/findAllQuestion?questionTypeId=3">
									<i class="iconfont">&#xe6a7;</i>
									判断题
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
					<li class="list">
						<a href="javascript:;">
							<i class="iconfont">&#xe6a3;</i>
							版本更新管理
							<i class="iconfont nav_right">&#xe697;</i>
						</a>
						<ul id="initVersionManager" class="sub-menu">
							<li id="initVersionManager1">
								<a href="${ctx}/admin/version/findVersionPage">
									<i class="iconfont">&#xe6a7;</i>
									版本列表
								</a>
							</li>
						</ul>
					</li>
				</ul>
			</div>
		</div>
<%--        	<%@ include file="/layout/menuLeft.jsp"%>--%>
        <!-- 左侧菜单结束 -->
        <!-- 右侧主体开始 -->
        <div class="page-content">
          <div class="content">
            <!-- 右侧内容框架，更改从这里开始 -->
            <form class="layui-form xbs" action="${ctx}/admin/land/findPageLand">
                <div class="layui-form-pane" style="text-align: center;">
                  <div class="layui-form-item" style="display: inline-block;">
                    <div class="layui-input-inline">
                      <input type="text" name="account" placeholder="请输入账号" autocomplete="off" class="layui-input" value="${param.account}">
                    </div>
                    <div class="layui-input-inline" style="width:80px">
                        <button class="layui-btn"  lay-submit="" lay-filter="sreach"><i class="layui-icon">&#xe615;</i></button>
                    </div>
                  </div>
                </div> 
            </form>
            <xblock>
            	<a href="${ctx}/admin/land/findPageLand?account=${param.account}&&pageNumber=${landPage.currentPageNum}&&pageSize=${landPage.pageSize}">
            		<button class="layui-btn">
            			<i class="layui-icon">
            				<img style="width:20px;height:20px;margin-top:5px" src="${ctx}/images/save.png"/>
            			</i>刷新
            		</button>
            	</a>
            	<span>"0"表示土地已开垦，"-1"表示土地未开垦</span>
            	<span class="x-right" style="line-height:40px">共有数据：${landPage.totalCount} 条</span>
            </xblock>
            <table class="layui-table">
                <thead >
                    <tr>
                        <th style="text-align:center;">用户ID</th>
                        <th style="text-align:center;">用户账号</th>
                        <th style="text-align:center;">1</th>
                        <th style="text-align:center;">2</th>
                        <th style="text-align:center;">3</th>
                        <th style="text-align:center;">4</th>
                        <th style="text-align:center;">5</th>
                        <th style="text-align:center;">6</th>
                        <th style="text-align:center;">7</th>
                        <th style="text-align:center;">8</th>
                        <th style="text-align:center;">9</th>
                        <th style="text-align:center;">10</th>
                        <th style="text-align:center;">11</th>
                        <th style="text-align:center;">12</th>
                        <th style="text-align:center;">13</th>
                        <th style="text-align:center;">14</th>
                        <th style="text-align:center;">15</th>
                        <th style="text-align:center;">16</th>
                        <th style="text-align:center;">17</th>
                        <th style="text-align:center;">18</th>
                        <th style="text-align:center;">状态</th>
                        <th style="text-align:center;">操作</th>
                    </tr>
                </thead>
                <tbody align="center">
					<c:forEach var="landPage" items="${landPage.list}">
						<tr>
							<td>${landPage.user.id}</td>
							<td>${landPage.user.account}</td>
							<td>${(empty landPage.userCrop1) ? '未开垦' : ((empty landPage.userCrop1.crop) ? '已开垦' : landPage.userCrop1.crop.name)}</td>
							<td>${(empty landPage.userCrop2) ? '未开垦' : ((empty landPage.userCrop2.crop) ? '已开垦' : landPage.userCrop2.crop.name)}</td>
							<td>${(empty landPage.userCrop3) ? '未开垦' : ((empty landPage.userCrop3.crop) ? '已开垦' : landPage.userCrop3.crop.name)}</td>
							<td>${(empty landPage.userCrop4) ? '未开垦' : ((empty landPage.userCrop4.crop) ? '已开垦' : landPage.userCrop4.crop.name)}</td>
							<td>${(empty landPage.userCrop5) ? '未开垦' : ((empty landPage.userCrop5.crop) ? '已开垦' : landPage.userCrop5.crop.name)}</td>
							<td>${(empty landPage.userCrop6) ? '未开垦' : ((empty landPage.userCrop6.crop) ? '已开垦' : landPage.userCrop6.crop.name)}</td>
							<td>${(empty landPage.userCrop7) ? '未开垦' : ((empty landPage.userCrop7.crop) ? '已开垦' : landPage.userCrop7.crop.name)}</td>
							<td>${(empty landPage.userCrop8) ? '未开垦' : ((empty landPage.userCrop8.crop) ? '已开垦' : landPage.userCrop8.crop.name)}</td>
							<td>${(empty landPage.userCrop9) ? '未开垦' : ((empty landPage.userCrop9.crop) ? '已开垦' : landPage.userCrop9.crop.name)}</td>
							<td>${(empty landPage.userCrop10) ? '未开垦' : ((empty landPage.userCrop10.crop) ? '已开垦' : landPage.userCrop10.crop.name)}</td>
							<td>${(empty landPage.userCrop11) ? '未开垦' : ((empty landPage.userCrop11.crop) ? '已开垦' : landPage.userCrop11.crop.name)}</td>
							<td>${(empty landPage.userCrop12) ? '未开垦' : ((empty landPage.userCrop12.crop) ? '已开垦' : landPage.userCrop12.crop.name)}</td>
							<td>${(empty landPage.userCrop13) ? '未开垦' : ((empty landPage.userCrop13.crop) ? '已开垦' : landPage.userCrop13.crop.name)}</td>
							<td>${(empty landPage.userCrop14) ? '未开垦' : ((empty landPage.userCrop14.crop) ? '已开垦' : landPage.userCrop14.crop.name)}</td>
							<td>${(empty landPage.userCrop15) ? '未开垦' : ((empty landPage.userCrop15.crop) ? '已开垦' : landPage.userCrop15.crop.name)}</td>
							<td>${(empty landPage.userCrop16) ? '未开垦' : ((empty landPage.userCrop16.crop) ? '已开垦' : landPage.userCrop16.crop.name)}</td>
							<td>${(empty landPage.userCrop17) ? '未开垦' : ((empty landPage.userCrop17.crop) ? '已开垦' : landPage.userCrop17.crop.name)}</td>
							<td>${(empty landPage.userCrop18) ? '未开垦' : ((empty landPage.userCrop18.crop) ? '已开垦' : landPage.userCrop18.crop.name)}</td>
							<td class="td-status">
								<c:if test="${landPage.user.exist == 1}">
									<span class="layui-btn layui-btn-normal layui-btn-mini">存在</span>
								</c:if>
								<c:if test="${landPage.user.exist != 1}">
									<span class="layui-btn layui-btn-danger layui-btn-mini">已删除</span>
								</c:if>
							</td>
							<td class="td-manage" align="center">
								<a style="text-decoration:none" onclick="updateUserLand('编辑','${ctx}/admin/land/toEdit?id=${landPage.id}','600','400')" href="javascript:;" title="编辑">
									<i class="layui-icon">&#xe642;</i>
								</a>
							</td>
						</tr>
					</c:forEach>
                </tbody>
            </table>
            <!-- 右侧内容框架，更改从这里结束 -->
          </div>
          <!-- 分页处理开始 -->
		  <div align="center">
			<a  class="page" style="margin-left:25px;" href="${ctx}/admin/land/findPageLand?account=${param.account}&&pageNumber=1&&pageSize=${landPage.pageSize}">首页</a>
			<a  class="page" href="${ctx}/admin/land/findPageLand?account=${param.account}&&pageNumber=${landPage.prePageNum}&&pageSize=${landPage.pageSize}">上一页</a>
			<a  class="page" href="${ctx}/admin/land/findPageLand?account=${param.account}&&pageNumber=${landPage.nextPageNum}&&pageSize=${landPage.pageSize}">下一页</a>
			<a  class="page" href="${ctx}/admin/land/findPageLand?account=${param.account}&&pageNumber=${landPage.totalPageNum}&&pageSize=${landPage.pageSize}">末页</a>
		  </div>
		  <div align="center" style="margin-top:20px;">
			  <span style="margin-right:10px;">${landPage.currentPageNum}</span>
			  <span>/</span>
			  <span style="margin-left:10px;">${landPage.totalPageNum}</span>
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
</body>
</html>