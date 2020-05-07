<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/6 0006
  Time: 10:32
  To change this template use File | Settings | File Templates.
--%>
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

        //修改作物信息
        function updatePetUtil(){
            var form = document.getElementById("form1");
            var formData = new FormData(form);
            $.ajax({
                url: "${ctx}/admin/petUtil/updatePetUtil",
                type: "POST",
                processData: false,  // 告诉jQuery不要去处理发送的数据
                contentType: false,  // 告诉jQuery不要去设置Content-Type请求头
                data: formData,
                success:function (data) {
                    if(data == "succeed"){
                        x_admin_close();
                    }else if(data == "fail"){
                        layer.msg('修改失败');
                    }
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
            <form id="form1" class="layui-form" enctype="multipart/form-data" action="javascript:updatePetUtil()" method="post">
                <input type="hidden" name="id" value="${petUtil.id}"/>
                <input type="hidden" name="img" value="${petUtil.img}"/>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>名称
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="name" name="name" required="" lay-verify="pass"
                               autocomplete="off" class="layui-input" value="${petUtil.name}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>描述
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="description" name="description" required="" lay-verify="pass"
                               autocomplete="off" class="layui-input" value="${petUtil.description}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>提升值
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="value" name="value" required="" lay-verify=""
                               autocomplete="off" class="layui-input" value="${petUtil.value}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>价格
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="price" name="price" required="" lay-verify="pass"
                               autocomplete="off" class="layui-input" value="${petUtil.price}">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        *金币
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>道具类型
                    </label>
                    <div class="layui-input-inline">
                        <select name="petUtilTypeId">
                            <c:forEach var="petUtilType" items="${petUtilTypes}">
                                <c:if test="${petUtilType.id == petUtil.petUtilType.id}">
                                    <option value="${petUtilType.id}" selected>${petUtilType.name}</option>
                                </c:if>
                                <c:if test="${petUtilType.id != petUtil.petUtilType.id}">
                                    <option value="${petUtilType.id}">${petUtilType.name}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>img
                    </label>
                    <div class="layui-input-inline">
                        <input type="file" id="img" name="upload" accept="image/*" style="margin-top:5px;"/>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
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