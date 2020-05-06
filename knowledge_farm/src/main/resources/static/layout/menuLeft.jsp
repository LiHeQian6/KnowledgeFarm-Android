<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
		<div class="left-nav">
			<div id="side-nav">
				<ul id="nav">
					<li class="list" current>
						<a href="${ctx}/admin/gotoIndex">
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