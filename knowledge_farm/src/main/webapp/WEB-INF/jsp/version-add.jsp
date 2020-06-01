<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/31 0031
  Time: 9:05
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
    <link rel="stylesheet" href="${ctx}/css/layui1.0.9.css">
    <link rel="stylesheet" href="https://cdn.bootcss.com/Swiper/3.4.2/css/swiper.min.css">
    <script type="text/javascript" src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdn.bootcss.com/Swiper/3.4.2/js/swiper.jquery.min.js"></script>
    <script src="${ctx}/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${ctx}/js/xadmin.js"></script>

    <style>
        .mask{
            width: 100%;
            height: 1300px;
            background-color: #000000; /*background-color: #dedcd8;*/
            opacity: 0.5;
            position: fixed;
            left: 0px;
            top:0px;
        }
        .progressDialog {
            width: 50%;
            position: fixed;
            left: 28%;
            top: 28%;
        }
    </style>

    <script>
        //关闭弹出框口
        function x_admin_close(){
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
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
            <form class="layui-form" enctype="multipart/form-data" method="post">
                <div class="layui-form-pane">
                    <div class="layui-form-item">
                        <div class="layui-input-inline">
                            <input type="text" id="description" name="description" required="" lay-verify="pass" placeholder="请输入描述"
                                   autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-input-inline">
                            <input type="file" id="file" name="file" style="margin-top:5px;"/>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <button id="submit" class="layui-btn" lay-filter="add" lay-submit="">
                            上传
                        </button>
                    </div>
                </div>
            </form>
            <!-- 右侧内容框架，更改从这里结束 -->
        </div>
    </div>
    <!-- 右侧主体结束 -->
</div>
<div id="mask" class="mask" hidden></div>
<div id="progressDialog" class="progressDialog" hidden>
    <div align="right">
        <a id="closeMask"><i class="layui-icon" style="font-size: 30px; color: #fff;">&#x1006;</i></a>
    </div>
    <fieldset class="layui-elem-field layui-field-title">
        <legend>文件上传进度</legend>
        <div class="layui-field-box" style><font color="#00CD00"><h4 id="uploadTime"></h4></font></div>
    </fieldset>
    <div id="progressModal" class="layui-progress layui-progress-big">
        <div id="progress_rate" class="layui-progress-bar" style="width: 0%">
            <span id="percent">0%</span>
        </div>
    </div>
</div>
<!-- 中部结束 -->
<script>
    var xhr = new XMLHttpRequest();
    $("#submit").attr("disabled",false);
    $("#submit").click(function() {
        //判断输入框内容是否为空
        var description = $("#description").val();
        if(description == null || description == ""){
            layer.msg("必填项不能为空");
            return false;
        }

        //判断文件是否为空
        var fileValue = $("#file").val();
        if (fileValue == null || fileValue == "") {
            layer.msg("请先选择文件");
            return false;
        }

        // 判断文件类型
        var obj = $("#file");
        var photoExt = obj.val().substr(obj.val().lastIndexOf(".")).toLowerCase();//获得文件后缀名
        if (photoExt != ".apk") {
            layer.msg("请选择apk格式的文件，不支持其他格式文件");
            return false;
        }

        // 判断文件大小
        var fileSize = 0;
        var isIE = /msie/i.test(navigator.userAgent) && !window.opera;
        if (isIE && !obj.files) {
            var filePath = obj.val();
            var fileSystem = new ActiveXObject("Scripting.FileSystemObject");
            var file = fileSystem.GetFile(filePath);
            fileSize = file.Size;
        } else {
            fileSize = obj.get(0).files[0].size;
        }
        fileSize = fileSize / 1024;
        fileSize = fileSize / 1024;
        console.log(fileSize);
        if (fileSize > 100) {
            layer.msg("文件不能大于100M");
            return false;
        }

        $(this).attr("disabled", true);
        $("#progress_rate").width("0%");
        $("#mask").show();
        $("#progressDialog").show();
        uploadFile();
    });

    function uploadFile() {
        var date1 = new Date();
        var fileObj = $("#file").get(0).files[0]; // js 获取文件对象
        // FormData 对象
        var form = new FormData();
        form.append("description", $("#description").val()); // 可以增加表单数据
        form.append("file", fileObj); // 文件对象
        // XMLHttpRequest 对象
        xhr.open("post", "${ctx}/admin/version/upload", true);
        xhr.onload = function() {
            var data = this.response;
            $("#submit").attr('disabled', false);
            switch (data) {
                case "succeed":
                    var date2 = new Date();
                    var interval = (date2.getTime() - date1.getTime()) / 1000;
                    $("#uploadTime").text("文件上传成功，共用时" + interval + "s");
                    break;
                case "null":
                    layer.msg("上传文件为空");
                    break;
                default:
                    layer.msg("文件上传失败");
            }
        };
        xhr.upload.addEventListener("progress", progressFunction, false);
        xhr.send(form);
    }

    function progressFunction(evt) {
        var progressBar = $("#progress_rate");
        if (evt.lengthComputable) {
            var completePercent = Math.round(evt.loaded / evt.total * 100)+ "%";
            progressBar.width(completePercent);
            $("#percent").text(completePercent);
        }
    }

    $("#closeMask").click(function () {
        xhr.abort();
        $("#uploadTime").text("");
        $("#percent").text("");
        $("#mask").hide();
        $("#progressDialog").hide();
        $("#submit").attr('disabled', false);
    });
</script>
</body>
</html>
