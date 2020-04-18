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
		
		//根据用户id获取到要修改的用户信息
		function getUpdateUserInfo(id,path){
			 $.post("${ctx}/admin/user/getUpdateUserInfo",{"id":id},function(data){
			 	updateUserLand('编辑',path,'600','400');
		     }) 
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
		<div class="logo"><a href="${ctx}/admin/gotoIndex">知识农场后台管理系统</a></div>
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
							土地管理
							<i class="iconfont nav_right">&#xe697;</i>
						</a>
						<ul id="initUserLandManager" class="sub-menu">
							<li id="initUserLandManager1">
								<a href="${ctx}/admin/user/findUserPage">
									<i class="iconfont">&#xe6a7;</i>
									土地列表
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
            <form class="layui-form xbs" action="${ctx}/admin/user/findUserPage">
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
            	<a href="${ctx}/admin/user/findUserPage?account=${param.account}&&pageNumber=${userPage.currentPageNum}&&pageSize=${userPage.pageSize}">
            		<button class="layui-btn">
            			<i class="layui-icon">
            				<img style="width:20px;height:20px;margin-top:5px" src="${ctx}/images/save.png"/>
            			</i>刷新
            		</button>
            	</a>
            	<span>"0"表示土地已开垦，"-1"表示土地未开垦</span>
            	<span class="x-right" style="line-height:40px">共有数据：${userPage.totalCount} 条</span>
            </xblock>
            <table class="layui-table">
                <thead >
                    <tr>
                        <th style="text-align:center;">用户ID</th>
                        <th style="text-align:center;">用户账号</th>
                        <th style="text-align:center;">用户名称</th>
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
                	<c:forEach var="userPage" items="${userPage.list}">
	                    <tr>
	                        <td>${userPage.id}</td>
	                        <td>${userPage.account}</td>
	                        <td>${userPage.nickName}</td>
	                        <td>${userPage.land1}</td>
	                        <td>${userPage.land2}</td>
	                        <td>${userPage.land3}</td>
	                        <td>${userPage.land4}</td>
	                        <td>${userPage.land5}</td>
	                        <td>${userPage.land6}</td>
	                        <td>${userPage.land7}</td>
	                        <td>${userPage.land8}</td>
	                        <td>${userPage.land9}</td>
	                        <td>${userPage.land10}</td>
	                        <td>${userPage.land11}</td>
	                        <td>${userPage.land12}</td>
	                        <td>${userPage.land13}</td>
	                        <td>${userPage.land14}</td>
	                        <td>${userPage.land15}</td>
	                        <td>${userPage.land16}</td>
	                        <td>${userPage.land17}</td>
	                        <td>${userPage.land18}</td>
	                        <td class="td-status">
	                        	<c:if test="${userPage.exist == 1}">
	                        		<span class="layui-btn layui-btn-normal layui-btn-mini">存在</span>
	                        	</c:if>
		                        <c:if test="${userPage.exist != 1}">
		                        	<span class="layui-btn layui-btn-danger layui-btn-mini">已删除</span>
		                        </c:if>
	                        </td>
	                        <td class="td-manage" align="center">
	                            <a style="text-decoration:none" onclick="getUpdateUserInfo(${userPage.id},'${ctx}/member-land-edit.jsp')" href="javascript:;" title="编辑">
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
			<a  class="page" style="margin-left:25px;" href="${ctx}/admin/user/findUserPage?account=${param.account}&&pageNumber=1&&pageSize=${userPage.pageSize}">首页</a>
			<a  class="page" href="${ctx}/admin/user/findUserPage?account=${param.account}&&pageNumber=${userPage.prePageNum}&&pageSize=${userPage.pageSize}">上一页</a>
			<a  class="page" href="${ctx}/admin/user/findUserPage?account=${param.account}&&pageNumber=${userPage.nextPageNum}&&pageSize=${userPage.pageSize}">下一页</a>
			<a  class="page" href="${ctx}/admin/user/findUserPage?account=${param.account}&&pageNumber=${userPage.totalPageNum}&&pageSize=${userPage.pageSize}">末页</a>
		  </div>
		  <div align="center" style="margin-top:20px;">
			  <span style="margin-right:10px;">${userPage.currentPageNum}</span>
			  <span>/</span>
			  <span style="margin-left:10px;">${userPage.totalPageNum}</span>
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