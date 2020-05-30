<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/17 0017
  Time: 14:06
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
        //修改题目
        function updateQuestion(){
            var form = document.getElementById("form1");
            var formData = new FormData(form);
            $.ajax({
                url: editUrl,
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
            <form id="form1" class="layui-form" enctype="multipart/form-data" action="javascript:updateQuestion()" method="post">
                <input type="hidden" name="id" value="${question.id}"/>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>题目
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="questionTitle" name="questionTitle" required="" lay-verify="pass"
                               autocomplete="off" class="layui-input" value="${question.questionTitle.title}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>年级
                    </label>
                    <div class="layui-input-inline">
                        <select name="grade">
                            <c:forEach var="grade" items="${grades}">
                                <c:if test="${grade.key == question.grade}">
                                    <option value="${grade.key}" selected>${grade.value}</option>
                                </c:if>
                                <c:if test="${grade.key != question.grade}">
                                    <option value="${grade.key}">${grade.value}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div id="btnSave" class="layui-form-item">
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
<script type="text/javascript">
    var form1 = document.getElementById("form1");
    var btnSave = document.getElementById("btnSave");
    var div = document.createElement("div");
    var editUrl = "${ctx}/admin/question/";
    if(${question.questionType.id == 1}){
        editUrl += "editSingleChoiceQuestion";
        div.innerHTML = "<div id=\"singleChoice\" class=\"layui-form-item\">\n" +
            "                    <div class=\"layui-form-item\">\n" +
            "                        <label class=\"layui-form-label\">\n" +
            "                            <font color=\"red\">*</font>答案\n" +
            "                        </label>\n" +
            "                        <div class=\"layui-input-inline\">\n" +
            "                            <input type=\"text\" name=\"answer\" required=\"\" lay-verify=\"pass\"\n" +
            "                                   autocomplete=\"off\" class=\"layui-input\" value=\"${singleChoice.answer}\">\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                    <div class=\"layui-form-item\">\n" +
            "                        <label class=\"layui-form-label\">\n" +
            "                            <font color=\"red\">*</font>选项1\n" +
            "                        </label>\n" +
            "                        <div class=\"layui-input-inline\">\n" +
            "                            <input type=\"text\" name=\"choice1\" lay-verify=\"pass\"\n" +
            "                                   autocomplete=\"off\" class=\"layui-input\" value=\"${singleChoice.choice1}\">\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                    <div class=\"layui-form-item\">\n" +
            "                        <label class=\"layui-form-label\">\n" +
            "                            <font color=\"red\">*</font>选项2\n" +
            "                        </label>\n" +
            "                        <div class=\"layui-input-inline\">\n" +
            "                            <input type=\"text\" name=\"choice2\" lay-verify=\"pass\"\n" +
            "                                   autocomplete=\"off\" class=\"layui-input\" value=\"${singleChoice.choice2}\">\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                    <div class=\"layui-form-item\">\n" +
            "                        <label class=\"layui-form-label\">\n" +
            "                            <font color=\"red\">*</font>选项3\n" +
            "                        </label>\n" +
            "                        <div class=\"layui-input-inline\">\n" +
            "                            <input type=\"text\" name=\"choice3\" lay-verify=\"pass\"\n" +
            "                                   autocomplete=\"off\" class=\"layui-input\" value=\"${singleChoice.choice3}\">\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                </div>";
    }
    if(${question.questionType.id == 2}){
        editUrl += "editCompletionQuestion";
        div.innerHTML = "                <div id=\"completion\" class=\"layui-form-item\">\n" +
            "                    <div class=\"layui-form-item\">\n" +
            "                        <label class=\"layui-form-label\">\n" +
            "                            <font color=\"red\">*</font>答案\n" +
            "                        </label>\n" +
            "                        <div class=\"layui-input-inline\">\n" +
            "                            <input type=\"text\" name=\"answer\" required=\"\" lay-verify=\"pass\"\n" +
            "                                   autocomplete=\"off\" class=\"layui-input\" value=\"${completion.answer}\">\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                </div>";
    }
    if(${question.questionType.id == 3}){
        editUrl += "editJudgmentQuestion";
        div.innerHTML = "<div id=\"judgment\" class=\"layui-form-item\">\n" +
            "                    <label class=\"layui-form-label\">\n" +
            "                        <font color=\"red\">*</font>答案\n" +
            "                    </label>\n" +
            "                    <div class=\"layui-input-inline\">\n" +
            "                        <select id=\"answerSelect\" name=\"answer\" lay-filter=\"answer\">\n" +
            "                            <option value=\"1\" ${(judgment.answer == 1) ? "selected" : ""}>正确</option>\n" +
            "                            <option value=\"0\" ${(judgment.answer == 0) ? "selected" : ""}>错误</option>\n" +
            "                        </select>\n" +
            "                    </div>\n" +
            "                </div>";
    }
    form1.insertBefore(div, btnSave);

    // layui.use(['form'], function(){
    //     var form = layui.form();
    //     form.on('select(answer)',function(data){
    //         if(data.value == 0){
    //             $("#choiceSelect").find("option[value="+1+"]").prop("selected",true);
    //         }else if(data.value == 1){
    //             $("#choiceSelect").find("option[value="+0+"]").prop("selected",true);
    //         }
    //         form.render();
    //     });
    //     form.on('select(choice)',function(data){
    //         if(data.value == 0){
    //             $("#answerSelect").find("option[value="+1+"]").prop("selected",true);
    //         }else if(data.value == 1){
    //             $("#answerSelect").find("option[value="+0+"]").prop("selected",true);
    //         }
    //         form.render();
    //     });
    // });

</script>
</body>
</html>
