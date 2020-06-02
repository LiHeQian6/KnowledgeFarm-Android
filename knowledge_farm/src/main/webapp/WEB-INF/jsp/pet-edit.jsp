<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/2 0002
  Time: 19:14
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
        function updatePet(){
            var form = document.getElementById("form1");
            var formData = new FormData(form);
            $.ajax({
                url: "${ctx}/admin/pet/updatePet",
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
            <form id="form1" class="layui-form" enctype="multipart/form-data" action="javascript:updatePet()" method="post">
                <input type="hidden" name="id" value="${pet.id}"/>
                <input type="hidden" name="img1" value="${pet.img1}"/>
                <input type="hidden" name="img2" value="${pet.img2}"/>
                <input type="hidden" name="img3" value="${pet.img3}"/>
                <input type="hidden" name="gif1" value="${pet.gif1}"/>
                <input type="hidden" name="gif2" value="${pet.gif2}"/>
                <input type="hidden" name="gif3" value="${pet.gif3}"/>
                <input type="hidden" name="exist" value="${pet.exist}"/>
                <input type="hidden" name="petFunctionId" value="${pet.petFunction.petFunctionId}"/>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>名称
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="name" name="name" required="" lay-verify="pass"
                               autocomplete="off" class="layui-input" value="${pet.name}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>描述
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="description" name="description" required="" lay-verify="pass"
                               autocomplete="off" class="layui-input" value="${pet.description}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>生命值
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="life" name="life" required="" lay-verify="nikename"
                               autocomplete="off" class="layui-input" value="${pet.life}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label  class="layui-form-label">
                        <font color="red">*</font>智力值
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="intelligence" name="intelligence" required="" lay-verify="pass"
                               autocomplete="off" class="layui-input" value="${pet.intelligence}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label  class="layui-form-label">
                        <font color="red">*</font>体力值
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="physical" name="physical" required="" lay-verify="pass"
                               autocomplete="off" class="layui-input" value="${pet.physical}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>价格
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="price" name="price" required="" lay-verify="pass"
                               autocomplete="off" class="layui-input" value="${pet.price}">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        *金币
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>几小时收获
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" name="harvestHour1" required="" lay-verify="pass"
                               autocomplete="off" class="layui-input" value="${pet.petFunction.harvestHour1}">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        *第一阶段
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label"></label>
                    <div class="layui-input-inline">
                        <input type="text" name="harvestHour2" required="" lay-verify="pass"
                               autocomplete="off" class="layui-input" value="${pet.petFunction.harvestHour2}">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        *第二阶段
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label"></label>
                    <div class="layui-input-inline">
                        <input type="text" name="harvestHour3" required="" lay-verify="pass"
                               autocomplete="off" class="layui-input" value="${pet.petFunction.harvestHour3}">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        *第三阶段
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>几小时生长
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" name="growHour1" required="" lay-verify="pass"
                               autocomplete="off" class="layui-input" value="${pet.petFunction.growHour1}">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        *第一阶段
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label"> </label>
                    <div class="layui-input-inline">
                        <input type="text" name="growHour2" required="" lay-verify="pass"
                               autocomplete="off" class="layui-input" value="${pet.petFunction.growHour2}">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        *第二阶段
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label"></label>
                    <div class="layui-input-inline">
                        <input type="text" name="growHour3" required="" lay-verify="pass"
                               autocomplete="off" class="layui-input" value="${pet.petFunction.growHour3}">
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        *第三阶段
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>img1
                    </label>
                    <div class="layui-input-inline">
                        <input type="file" id="img1" name="upload" accept="image/*" style="margin-top:5px;"/>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>img2
                    </label>
                    <div class="layui-input-inline">
                        <input type="file" id="img2" name="upload" accept="image/*" style="margin-top:5px;"/>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>img3
                    </label>
                    <div class="layui-input-inline">
                        <input type="file" id="img3" name="upload" accept="image/*" style="margin-top:5px;"/>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>gif1
                    </label>
                    <div class="layui-input-inline">
                        <input type="file" id="gif1" name="upload" accept="image/gif" style="margin-top:5px;"/>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>gif2
                    </label>
                    <div class="layui-input-inline">
                        <input type="file" id="gif2" name="upload" accept="image/gif" style="margin-top:5px;"/>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>gif3
                    </label>
                    <div class="layui-input-inline">
                        <input type="file" id="gif3" name="upload" accept="image/gif" style="margin-top:5px;"/>
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
