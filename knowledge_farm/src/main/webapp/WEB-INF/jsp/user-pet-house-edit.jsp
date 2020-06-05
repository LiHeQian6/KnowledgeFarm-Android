<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/27 0027
  Time: 20:07
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
        function updateUserPetHouse(){
            var form = document.getElementById("form1");
            var formData = new FormData(form);
            $.ajax({
                url: "${ctx}/admin/user_pet_house/editUserPetHouse",
                type: "POST",
                processData: false,  // 告诉jQuery不要去处理发送的数据
                contentType: false,  // 告诉jQuery不要去设置Content-Type请求头
                data: formData,
                success:function (data) {
                    if(data == "succeed"){
                        x_admin_close();
                    }else if(data == "fail"){
                        layer.msg('修改失败');
                    }else{
                        layer.msg(data);
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
            <form id="form1" class="layui-form" enctype="multipart/form-data" action="javascript:updateUserPetHouse()" method="post">
                <input type="hidden" name="id" value="${userPetHouse.id}"/>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>生命值
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="life" name="life" required="" lay-verify="pass"
                               autocomplete="off" class="layui-input" value="${userPetHouse.life}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>智力值
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="intelligence" name="intelligence" required="" lay-verify="pass"
                               autocomplete="off" class="layui-input" value="${userPetHouse.intelligence}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>体力值
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="physical" name="physical" required="" lay-verify=""
                               autocomplete="off" class="layui-input" value="${userPetHouse.physical}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">是否使用</label>
                    <div class="layui-input-block">
                        <c:choose>
                            <c:when test="${userPetHouse.ifUsing == 0}">
                                <input type="radio" name="ifUsing" value="1" title="是">
                                <input type="radio" name="ifUsing" value="0" title="否" checked="">
                            </c:when>
                            <c:when test="${userPetHouse.ifUsing == 1}">
                                <input type="radio" name="ifUsing" value="1" title="是" checked="">
                                <input type="radio" name="ifUsing" value="0" title="否">
                            </c:when>
                        </c:choose>
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
</body>
</html>