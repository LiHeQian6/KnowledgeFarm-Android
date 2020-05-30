<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/4/28 0028
  Time: 16:42
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
        //初始化左侧菜单（管理员管理）
        window.onload = function(){
            $("#sendNotificationManager").attr("class","sub-menu opened");
            $("#sendCustomMessageManager").attr("class","current");
        }
        function sendCustomMessage() {
            var title = $("#title").val();
            var content = $("#content").val();
            var extra = MapToJson();
            var alias = $("#aliasInput").val();
            var aliasRadioValue = $('input[name="alias"]:checked').val();
            if(aliasRadioValue == 0){
                alias = "";
            }
            $.post("${ctx}/admin/notification/sendCustomMessage",{"title":title,"content":content,"extra":extra,"alias":alias},function(data){
                switch (data) {
                    case "succeed":
                        layer.msg('发送成功');
                        break;
                    case "fail":
                        layer.msg('发送失败');
                    case "notExist":
                        layer.msg('存在不能识别的设备别名');
                        break;
                    case "false":
                        layer.msg('极光推送连接错误');
                        break;
                }
            })
        }
        function MapToJson() {
            var map = new Map();
            for(var i = 0,j = 0;i < extraKeys.length && j < extraValues.length;i++,j++){
                var key = $("#" + extraKeys[i]).val();
                var value = $("#" + extraValues[i]).val();
                if(key == "" && value == ""){
                    continue;
                }else{
                    map.set(key, value);
                }
            }

            var str = '{';
            var n = 1;
            map.forEach(function (item, key, mapObj) {
                if(mapObj.size == n){
                    str += '"'+ key+'":"'+ item + '"';
                }else{
                    str += '"'+ key+'":"'+ item + '",';
                }
                n++;
            });
            str +='}';
            return str;
        }
    </script>
</head>
<body>
<!-- 顶部开始 -->
<%--<%@ include file="/layout/header.jsp"%>--%>
<div class="container">
    <div class="logo"><a href="${ctx}/admin/toIndex">知识农场后台管理系统</a></div>
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
            <form class="layui-form" action="javascript:sendCustomMessage()">
                <div class="layui-form-item">
                    <label class="layui-form-label">通知标题</label>
                    <div class="layui-input-block">
                        <input id="title" type="text" name="title" required lay-verify="title" autocomplete="off" placeholder="请输入通知标题" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">通知内容</label>
                    <div class="layui-input-block">
                        <textarea id="content" placeholder="请输入通知内容" required class="layui-textarea"></textarea>
                    </div>
                </div>
                <div id="extraAll" class="layui-form-item">
                    <div id="extraDiv0" class="layui-form-item">
                        <label class="layui-form-label">附加字段</label>
                        <div class="layui-inline">
                            <div class="layui-input-inline" style="width: 150px;">
                                <input id="extraKey0" type="text" name="price_min" placeholder="键" autocomplete="off" class="layui-input">
                            </div>
                            <div class="layui-form-mid">：</div>
                            <div class="layui-input-inline" style="width: 150px;">
                                <input id="extraValue0" type="text" name="price_max" placeholder="值" autocomplete="off" class="layui-input">
                            </div>
                            <div class="layui-input-inline" style="margin-top: 10px">
                                <a href="javascript:addKeyValue()"><i class="layui-icon">&#xe608;</i></a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">目标人群</label>
                    <div class="layui-input-block">
                        <input type="radio" name="alias" value="0" lay-filter="aliasEvent" title="广播给所有人" checked="">
                        <input type="radio" name="alias" value="1" lay-filter="aliasEvent" title="设备别名(Alias)">
                    </div>
                </div>
                <div id="aliasDiv" class="layui-form-item" hidden>
                    <label class="layui-form-label"></label>
                    <div class="layui-input-block">
                        <input id="aliasInput" type="text" name="title" lay-verify="title" autocomplete="off" placeholder="添加设备别名，用逗号分隔开" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label"></label>
                    <button class="layui-btn" lay-filter="add" lay-submit="">添加</button>
                </div>
            </form>
            <!-- 右侧内容框架，更改从这里结束 -->
        </div>
    </div>
    <!-- 右侧主体结束 -->
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

<!-- 键值对 -->
<script type="text/javascript">
    var count = 0;
    var extraKeys = new Array();
    var extraValues = new Array();
    extraKeys[count] = "extraKey0";
    extraValues[count] = "extraValue0";

    function addKeyValue() {
        var extraAll = document.getElementById("extraAll");
        var div = document.createElement("div");
        var id = count + 1;
        var extraDivId = "extraDiv" + id;
        var extraKeyId = "extraKey" + id;
        var extraValueId = "extraValue" + id;

        div.setAttribute("class", "layui-form-item");
        div.setAttribute("id", extraDivId);
        div.innerHTML = "<label class='layui-form-label'></label><div class='layui-inline'><div class='layui-input-inline' style='width: 150px;'><input id='"+extraKeyId+"' type='text' placeholder='键' autocomplete='off' class='layui-input'></div><div class='layui-form-mid'>：</div><div class='layui-input-inline' style='width: 150px;'><input id='"+extraValueId+"' type='text' name='price_max' placeholder='值' autocomplete='off' class='layui-input'></div><div class='layui-input-inline' style='margin-top: 10px;margin-left:-2px'><a href='javascript:removeKeyValue(&quot;"+extraDivId+"&quot;,&quot;"+extraKeyId+"&quot;,&quot;"+extraValueId+"&quot;)'><img src='${ctx}/images/decrease.png' style='width:22px;height:22px'/></a></div></div>";
        extraAll.appendChild(div);

        count++;
        extraKeys[count] = extraKeyId;
        extraValues[count] = extraValueId;
    }

    Array.prototype.remove = function(val) {
        var index = this.indexOf(val);
        if (index > -1) {
            this.splice(index, 1);
        }
    };

    function removeKeyValue(divId, keyId, valueId){
        var extraAll = document.getElementById("extraAll");
        var extraDiv = document.getElementById(divId);
        extraAll.removeChild(extraDiv);
        extraKeys.remove(keyId);
        extraValues.remove(valueId);
    }
</script>

<!-- 别名 -->
<script type="text/javascript">
    layui.use(['form'], function(){
        var form = layui.form();
        form.on('radio(aliasEvent)', function(data){
            var value = data.value;
            if(value == 0){
                $("#aliasDiv").hide();
            }
            if(value == 1){
                $("#aliasDiv").show();
            }
        });
    });
</script>
</body>
</html>