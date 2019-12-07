<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div class="container">
        <div class="logo"><a href="${ctx}/admin/gotoIndex">知识农场后台管理系统</a></div>
        <div class="open-nav"><i class="iconfont">&#xe699;</i></div>
        <ul class="layui-nav right" lay-filter="">
          <li class="layui-nav-item">
            <a href="javascript:;">${admin.accout}</a>
            <dl class="layui-nav-child">
              <dd><a href="${ctx}/admin/registAdmin">退出</a></dd>
            </dl>
          </li>
          <li class="layui-nav-item"><a href="/"></a></li>
        </ul>
</div>
