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
		<%@ include file="/layout/background.jsp"%>
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