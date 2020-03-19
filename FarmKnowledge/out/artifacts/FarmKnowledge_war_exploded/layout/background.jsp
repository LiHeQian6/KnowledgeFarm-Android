<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
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