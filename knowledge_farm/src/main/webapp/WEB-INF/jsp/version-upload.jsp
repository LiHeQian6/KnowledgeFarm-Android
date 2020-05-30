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
            top: 35%;
        }
    </style>
</head>
<script>
    //初始化左侧菜单
    window.onload = function(){
        $("#initVersionUploadManager").attr("class","sub-menu opened");
        $("#initVersionUploadManager1").attr("class","current");
    };
</script>
<body>
<!-- 顶部开始 -->
<%--<%@ include file="/layout/header.jsp"%>--%>
<div class="container">
    <div class="logo"><a href="${ctx}/admin/toIndex"><font color="#fff">知识农场后台管理系统</font></a></div>
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
                    <a href="${ctx}/admin/toIndex">
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
                        宠物管理
                        <i class="iconfont nav_right">&#xe697;</i>
                    </a>
                    <ul id="initPetManager" class="sub-menu">
                        <li id="initPetManager1">
                            <a href="${ctx}/admin/pet/findPetPage?exist=1">
                                <i class="iconfont">&#xe6a7;</i>
                                宠物列表
                            </a>
                        </li>
                        <li id="initPetManager2">
                            <a href="${ctx}/admin/pet/findPetPage?exist=0">
                                <i class="iconfont">&#xe6a7;</i>
                                宠物删除
                            </a>
                        </li>
                    </ul>
                </li>
                <li class="list" >
                    <a href="javascript:;">
                        <i class="iconfont">&#xe6a3;</i>
                        宠物道具管理
                        <i class="iconfont nav_right">&#xe697;</i>
                    </a>
                    <ul id="initPetUtilManager" class="sub-menu">
                        <li id="initPetUtilManager1">
                            <a href="${ctx}/admin/petUtil/findPetUtilPage?exist=1">
                                <i class="iconfont">&#xe6a7;</i>
                                宠物道具列表
                            </a>
                        </li>
                        <li id="initPetUtilManager2">
                            <a href="${ctx}/admin/petUtil/findPetUtilPage?exist=0">
                                <i class="iconfont">&#xe6a7;</i>
                                宠物道具删除
                            </a>
                        </li>
                    </ul>
                </li>
                <li class="list">
                    <a href="javascript:;">
                        <i class="iconfont">&#xe6a3;</i>
                        用户宠物管理
                        <i class="iconfont nav_right">&#xe697;</i>
                    </a>
                    <ul id="initUserPetHouseManager" class="sub-menu">
                        <li id="initUserPetHouseManager1">
                            <a href="${ctx}/admin/user_pet_house/findUserPetHousePage">
                                <i class="iconfont">&#xe6a7;</i>
                                用户宠物列表
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
                            <a href="${ctx}/admin/land/findPageLand">
                                <i class="iconfont">&#xe6a7;</i>
                                土地列表
                            </a>
                        </li>
                    </ul>
                </li>
                <li class="list" >
                    <a href="javascript:;">
                        <i class="iconfont">&#xe6a3;</i>
                        通知管理
                        <i class="iconfont nav_right">&#xe697;</i>
                    </a>
                    <ul id="sendNotificationManager" class="sub-menu">
                        <li id="sendPushManager">
                            <a href="${ctx}/admin/notification/toNotification">
                                <i class="iconfont">&#xe6a7;</i>
                                发送通知
                            </a>
                        </li>
                        <li id="sendCustomMessageManager">
                            <a href="${ctx}/admin/notification/toNotificationCustomMessage">
                                <i class="iconfont">&#xe6a7;</i>
                                自定义消息
                            </a>
                        </li>
                    </ul>
                </li>
                <li class="list" >
                    <a href="javascript:;">
                        <i class="iconfont">&#xe6a3;</i>
                        题目管理
                        <i class="iconfont nav_right">&#xe697;</i>
                    </a>
                    <ul id="initQuestionManager" class="sub-menu">
                        <li id="initQuestionManager1">
                            <a href="${ctx}/admin/question/findAllQuestion?questionTypeId=1">
                                <i class="iconfont">&#xe6a7;</i>
                                单选题
                            </a>
                        </li>
                        <li id="initQuestionManager2">
                            <a href="${ctx}/admin/question/findAllQuestion?questionTypeId=2">
                                <i class="iconfont">&#xe6a7;</i>
                                填空题
                            </a>
                        </li>
                        <li id="initQuestionManager3">
                            <a href="${ctx}/admin/question/findAllQuestion?questionTypeId=3">
                                <i class="iconfont">&#xe6a7;</i>
                                判断题
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
                        更新管理
                        <i class="iconfont nav_right">&#xe697;</i>
                    </a>
                    <ul id="initVersionUploadManager" class="sub-menu">
                        <li id="initVersionUploadManager1">
                            <a href="${ctx}/admin/versionUpload/toVersionUpload">
                                <i class="iconfont">&#xe6a7;</i>
                                APK更新
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
            <form id="form1" class="layui-form" enctype="multipart/form-data" method="post">
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>请上传文件
                    </label>
                    <div class="layui-input-inline">
                        <input id="file" type="file" name="file">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label"></label>
                    <button id="submit" class="layui-btn" key="set-mine" lay-filter="save" lay-submit>
                        保存
                    </button>
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
    var xhr = new XMLHttpRequest();
    $("#submit").attr("disabled",false);
    $("#submit").click(function() {
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
        var fileObj = $("#file").get(0).files[0]; // js 获取文件对象
        // FormData 对象
        var form = new FormData();
        // form.append("author", "hooyes"); // 可以增加表单数据
        form.append("file", fileObj); // 文件对象
        // XMLHttpRequest 对象
        xhr.open("post", "${ctx}/admin/versionUpload/upload", true);
        xhr.onload = function() {
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
                    $("#uploadTime").text("文件上传成功，共用时" + data);
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
        $("#uploadTime").text("");
        $("#percent").text("");
        $("#mask").hide();
        $("#progressDialog").hide();
        $("#submit").attr('disabled', false);
    });
</script>
</body>
</html>
