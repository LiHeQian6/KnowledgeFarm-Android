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

</head>
<body>
<!-- 顶部开始 -->
<%--<%@ include file="/layout/header.jsp"%>--%>
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
<%--    <%@ include file="/layout/menuLeft.jsp"%>--%>
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
    <!-- 左侧菜单结束 -->
    <!-- 右侧主体开始 -->
    <div class="page-content">
        <div class="content">
            <!-- 右侧内容框架，更改从这里开始 -->
            <blockquote class="layui-elem-quote">
                欢迎登陆知识农场后台管理系统<span class="f-14"></span>
            </blockquote>
            <fieldset class="layui-elem-field layui-field-title site-title">
                <legend><a name="default">服务器</a></legend>
            </fieldset>
            <table class="layui-table">
                <thead>
                <tr>
                    <th colspan="2" scope="col">服务器信息</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <th width="30%">服务器计算机名</th>
                    <td><span id="lbServerName">ROOT</span></td>
                </tr>
                <tr>
                    <td>服务器IP地址</td>
                    <td>39.106.18.238</td>
                </tr>
                <tr>
                    <td>服务器域名</td>
                    <td>www.knowledgefarm.com</td>
                </tr>
                <tr>
                    <td>服务器端口 </td>
                    <td>8080</td>
                </tr>
                <tr>
                    <td>服务器版本 </td>
                    <td>apache-tomcat-8.5.39</td>
                </tr>
                <tr>
                    <td>本文件所在文件夹 </td>
                    <td>/home/admin/Tomcat/apache-tomcat-8.5.39/webapps/FarmKnowledge/</td>
                </tr>
                <tr>
                    <td>服务器操作系统 </td>
                    <td>Ubuntu16.04</td>
                </tr>
                <tr>
                    <td>服务器的语言种类 </td>
                    <td>Chinese (People's Republic of China)</td>
                </tr>
                <tr>
                    <td>CPU 总数 </td>
                    <td>1</td>
                </tr>
                <tr>
                    <td>虚拟内存 </td>
                    <td>2048M</td>
                </tr>
                <tr>
                    <td>当前系统用户名 </td>
                    <td>ROOT</td>
                </tr>
                </tbody>
            </table>
            <!-- 右侧内容框架，更改从这里结束 -->
        </div>
    </div>
    <!-- 右侧主体结束 -->
</div>
<!-- 中部结束 -->
<!-- 底部开始 -->
<!-- 底部结束 -->
<!-- 背景切换开始 -->
<%--<%@ include file="/layout/background.jsp"%>--%>
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