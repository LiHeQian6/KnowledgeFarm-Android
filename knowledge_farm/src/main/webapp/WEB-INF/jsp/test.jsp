<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/14 0014
  Time: 16:29
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
    <link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdn.bootcss.com/Swiper/3.4.2/js/swiper.jquery.min.js"></script>
    <script src="${ctx}/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${ctx}/js/xadmin.js"></script>

    <style>
        .mask{
            width: 100%;
            height: 1300px;
            background-color: #dedcd8;
            opacity: 0.5;
            position: fixed;
            left: 0px;
            top:0px;
        }
        .progressDialog {
            position: fixed;
            left: 28%;
            top: 25%;
        }
    </style>
</head>
<body>
<div class="container" align="center">
    <form id="form1" method="post" enctype="multipart/form-data">
        <table class="table" style="width: 50%">
            <tr id="msg" style="display: none;">
                <th colspan="2" style="text-align: center;">
                    <font color="#00CD00">文件上传成功，共用时${time}ms</font>
                </th>
            </tr>
            <tr>
                <th>上传文件1:</th>
                <td><input id="file" type="file" name="file"/></td>
            </tr>
            <tr>
                <th> </th>
                <td>
                    <button id="submit" type="submit" class="btn btn-default">上传</button>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="mask" class="mask" hidden></div>
<!-- 文件上传模态框 -->
<div id="progressModal" class="progressDialog" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false" hidden>
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <div id="closeMask" class="pull-right"><abbr title="关闭" class="initialism"><span class="glyphicon glyphicon-asterisk"></span></abbr></div>
                <h4 class="modal-title">文件上传进度</h4>
                <h5 id="uploadTime1" hidden><font id="uploadTime2" color="#00CD00">哈哈哈</font></h5>
            </div>
            <div class="modal-body">
                <div id="progress" class="progress">
                    <div id="progress_rate" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"
                         class="progress-bar progress-bar-success progress-bar-striped active"
                         role="progressbar" style="width: 0%">
                        <span id="percent">0%</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    var xhr = new XMLHttpRequest();
    $("#submit").attr("disabled",false);
    $("#submit").click(function() {
        //判断文件是否为空
        var fileValue = $("#file").val();
        if (fileValue == null || fileValue == "") {
            layer.msg("请先选择文件");
            return;
        }

        //判断文件类型
        // var obj = $("#file");
        // var photoExt = obj.val().substr(obj.val().lastIndexOf(".")).toLowerCase();//获得文件后缀名
        // if (photoExt != ".apk") {
        //     layer.msg("请选择apk格式的文件，不支持其他格式文件");
        //     return false;
        // }

        // 判断文件大小
        // var fileSize = 0;
        // var isIE = /msie/i.test(navigator.userAgent) && !window.opera;
        // if (isIE && !obj.files) {
        //     var filePath = obj.val();
        //     var fileSystem = new ActiveXObject("Scripting.FileSystemObject");
        //     var file = fileSystem.GetFile(filePath);
        //     fileSize = file.Size;
        // } else {
        //     fileSize = obj.get(0).files[0].size;
        // }
        // fileSize = Math.round(fileSize / 1024 * 100) / 100; //单位为KB
        // if (fileSize > 5000) {
        //     layer.msg("文件不能大于5M");
        //     return false;
        // }

        $("#progress_rate").width("0%");
        $(this).attr("disabled", true);
        $("#mask").show();
        $("#progressModal").show();
        uploadFile();
    });
    function uploadFile() {
        var fileObj = $("#file").get(0).files[0]; // js 获取文件对象
        console.info("上传的文件："+fileObj);
        var FileController = "${ctx}/admin/versionUpload/upload"; // 接收上传文件的后台地址
        // FormData 对象
        var form = new FormData();
        // form.append("author", "hooyes"); // 可以增加表单数据
        form.append("file", fileObj); // 文件对象
        // XMLHttpRequest 对象
        xhr.open("post", FileController, true);
        xhr.onload = function(data) {
            var data = this.response;
            $("#submit").attr('disabled', false);
            switch (data) {
                case "null":
                    layer.msg("上传文件为空")
                    break;
                case "fail":
                    layer.msg("文件上传失败");
                    break;
                default:
                    $("#uploadTime1").show();
                    $("#uploadTime2").text("文件上传成功，共用时" + data);
                    layer.msg("上传文件成功");
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
        $("#uploadTime2").text("");
        $("#percent").text("");
        $("#mask").hide();
        $("#progressModal").hide();
        $("#submit").attr('disabled', false);
    });
</script>
</body>
</html>
