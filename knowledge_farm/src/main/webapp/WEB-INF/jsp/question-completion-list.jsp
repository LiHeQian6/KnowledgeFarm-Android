<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/17 0017
  Time: 13:17
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

    <style>
        .page{
            margin-right:25px;
        }
    </style>

    <script>
        //初始化左侧菜单（作物管理）
        window.onload = function(){
            $("#initQuestionManager").attr("class","sub-menu opened");
            $("#initQuestionManager2").attr("class","current");
        }

        //删除单个题目
        function deleteOneQuestion(id){
            layer.confirm('删除后无法恢复，确认要删除数据吗？',function(index){
                $.post("${ctx}/admin/question/deleteOneQuestion",{"id":id},function(data){
                    if(data == "succeed"){
                        window.location.href="${ctx}/admin/question/findAllQuestion?questionTitle=${param.questionTitle}&&questionTypeId=${param.questionTypeId}&&subject=${param.subject}&&grade=${param.grade}&&pageNumber=${questionPage.currentPageNum}&&pageUtilSize=${questionPage.pageSize}";
                    }else if(data == "fail"){
                        layer.msg('删除失败');
                    }
                })
            });
        }

        //删除批量题目
        function deleteMultiQuestion() {
            var arrDelete = document.getElementsByName("checkBox");
            var deleteStr="";
            for(i in arrDelete){
                if(arrDelete[i].checked){
                    deleteStr = deleteStr + arrDelete[i].value + ",";
                }
            }
            layer.confirm('删除后无法恢复，确认要批量删除吗？',function(index){
                if(deleteStr != ""){
                    $.post("${ctx}/admin/question/deleteMultiQuestion",{"deleteStr":deleteStr},function(data){
                        if(data == "succeed"){
                            window.location.href="${ctx}/admin/question/findAllQuestion?questionTitle=${param.questionTitle}&&questionTypeId=${param.questionTypeId}&&subject=${param.subject}&&grade=${param.grade}&&pageNumber=${questionPage.currentPageNum}&&pageUtilSize=${questionPage.pageSize}";
                        }else if(data == "fail"){
                            layer.msg('删除失败');
                        }
                    })
                }else{
                    layer.msg('删除不能为空');
                }
            });
        }

        //添加题目
        function addQuestion(title,url,w,h){
            x_admin_show(title,url,w,h);
        }

        //添加批量题目
        function addMultiQuestion(){
            $.post("${ctx}/admin/question/deleteOneQuestion",{"id":id},function(data){
                if(data == "succeed"){

                }else if(data == "fail"){
                    layer.msg('删除失败');
                }
            })
        }

        //修改题目
        function updateQuestion(title,url,w,h) {
            x_admin_show(title,url,w,h);
        }

    </script>

</head>
<body>
<!-- 顶部开始 -->
<%--    	<%@ include file="/layout/header.jsp"%>--%>
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
    <%--        	<%@ include file="/layout/menuLeft.jsp"%>--%>
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
            <!-- 右侧内容框架，更改从这里开始 -->
            <form class="layui-form xbs" action="${ctx}/admin/question/findAllQuestion">
                <div class="layui-form-pane" style="text-align: center;">
                    <div class="layui-form-item" style="display: inline-block;">
                        <div class="layui-input-inline">
                            <input type="text" name="questionTitle" placeholder="请输入题目有关词" autocomplete="off" class="layui-input" value="${param.questionTitle}">
                        </div>
                        <div class="layui-input-inline">
                            <select name="subject">
                                <option value="" selected="">请选择学科</option>
                                <c:forEach var="subject" items="${subjects}">
                                    <c:if test="${subject == param.subject}">
                                        <option value="${subject}" selected>${subject}</option>
                                    </c:if>
                                    <c:if test="${subject != param.subject}">
                                        <option value="${subject}">${subject}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="layui-input-inline">
                            <select name="grade">
                                <option value="0" selected="">请选择年级</option>
                                <c:forEach var="grade" items="${grades}">
                                    <c:if test="${grade.key == param.grade}">
                                        <option value="${grade.key}" selected>${grade.value}</option>
                                    </c:if>
                                    <c:if test="${grade.key != param.grade}">
                                        <option value="${grade.key}">${grade.value}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                        <input type="hidden" name="questionTypeId" value="${param.questionTypeId}"/>
                        <div class="layui-input-inline" style="width:80px">
                            <button class="layui-btn"  lay-submit="" lay-filter="sreach"><i class="layui-icon">&#xe615;</i></button>
                        </div>
                    </div>
                </div>
            </form>

            <xblock>
                <a href="${ctx}/admin/question/exportExcelData?questionTypeId=${param.questionTypeId}">
                    <button class="layui-btn layui-btn-normal" id="exportData">
                        <i class="layui-icon">
                            <img style="width:20px;height:20px;margin-top:5px" src="${ctx}/images/download.png"/>
                        </i>导出Excel数据
                    </button>
                </a>
                <a href="${ctx}/admin/question/exportExcelModel?questionTypeId=${param.questionTypeId}">
                    <button class="layui-btn layui-btn-normal" id="exportHeader">
                        <i class="layui-icon">
                            <img style="width:20px;height:20px;margin-top:5px" src="${ctx}/images/download.png"/>
                        </i>导出Excel表头
                    </button>
                </a>
                <button class="layui-btn layui-btn-danger" onclick="deleteMultiQuestion()">
                    <i class="layui-icon">&#xe640;</i>批量删除
                </button>
                <input id="addMulti" type="file" lay-type="file" name="file" class="layui-upload-file">
                <input id="editMulti" type="file" lay-type="file" name="file" class="layui-upload-file">
                <button class="layui-btn" onclick="addQuestion('添加题目','${ctx}/admin/question/toAdd?questionTypeId=${param.questionTypeId}','600','500')">
                    <i class="layui-icon">&#xe608;</i>添加
                </button>
                <a href="${ctx}/admin/question/findAllQuestion?questionTitle=${param.questionTitle}&&questionTypeId=${param.questionTypeId}&&subject=${param.subject}&&grade=${param.grade}&&pageNumber=${questionPage.currentPageNum}&&pageSize=${questionPage.pageSize}">
                    <button class="layui-btn">
                        <i class="layui-icon">
                            <img style="width:20px;height:20px;margin-top:5px" src="${ctx}/images/save.png"/>
                        </i>刷新
                    </button>
                </a>
                <span class="x-right" style="line-height:40px">共有数据：${questionPage.totalCount} 条</span>
            </xblock>
            <table class="layui-table">
                <thead >
                <tr>
                    <th></th>
                    <th style="text-align:center;">ID</th>
                    <th style="text-align:center;">题目</th>
                    <th style="text-align:center;">学科</th>
                    <th style="text-align:center;">年级</th>
                    <th style="text-align:center;">答案</th>
                    <th style="text-align:center;">操作</th>
                </tr>
                </thead>
                <tbody align="center">
                <c:forEach var="questionPage" items="${questionPage.list}">
                    <tr>
                        <td><input type="checkbox" value="${questionPage.id}" name="checkBox"></td>
                        <td>${questionPage.id}</td>
                        <td>${questionPage.questionTitle.title}</td>
                        <td>${questionPage.subject}</td>
                        <c:forEach var="grade" items="${grades}">
                            <c:if test="${grade.key == questionPage.grade}">
                                <td>${grade.value}</td>
                            </c:if>
                        </c:forEach>
                        <td>${questionPage.answer}</td>
                        <td class="td-manage" align="center">
                            <a style="text-decoration:none"  onclick="updateQuestion('编辑','${ctx}/admin/question/toEdit?id=${questionPage.id}','600','400')" href="javascript:;" title="编辑">
                                <i class="layui-icon">&#xe642;</i>
                            </a>
                            <a title="删除" href="javascript:;" onclick="deleteOneQuestion(${questionPage.id})" style="text-decoration:none">
                                <i class="layui-icon">&#xe640;</i>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <!-- 右侧内容框架，更改从这里结束 -->
        </div>
        <!-- 分页处理开始 -->
        <div align="center">
            <a  class="page" style="margin-left:25px;" href="${ctx}/admin/question/findAllQuestion?questionTitle=${param.questionTitle}&&questionTypeId=${param.questionTypeId}&&subject=${param.subject}&&grade=${param.grade}&&pageNumber=1&&pageSize=${questionPage.pageSize}&&exist=1">首页</a>
            <a  class="page" href="${ctx}/admin/question/findAllQuestion?questionTitle=${param.questionTitle}&&questionTypeId=${param.questionTypeId}&&subject=${param.subject}&&grade=${param.grade}&&pageNumber=${questionPage.prePageNum}&&pageSize=${questionPage.pageSize}&&exist=1">上一页</a>
            <a  class="page" href="${ctx}/admin/question/findAllQuestion?questionTitle=${param.questionTitle}&&questionTypeId=${param.questionTypeId}&&subject=${param.subject}&&grade=${param.grade}&&pageNumber=${questionPage.nextPageNum}&&pageSize=${questionPage.pageSize}&&exist=1">下一页</a>
            <a  class="page" href="${ctx}/admin/question/findAllQuestion?questionTitle=${param.questionTitle}&&questionTypeId=${param.questionTypeId}&&subject=${param.subject}&&grade=${param.grade}&&pageNumber=${questionPage.totalPageNum}&&pageSize=${questionPage.pageSize}&&exist=1">末页</a>
        </div>
        <div align="center" style="margin-top:20px;">
            <span style="margin-right:10px;">${questionPage.currentPageNum}</span>
            <span>/</span>
            <span style="margin-left:10px;">${questionPage.totalPageNum}</span>
        </div>
        <!-- 分页处理结束 -->
    </div>
    <!-- 右侧主体结束 -->
</div>
<!-- 中部结束 -->
<!-- 底部开始 -->
<!-- 底部结束 -->
<!-- 背景切换开始 -->
<%--    	<%@ include file="/layout/background.jsp"%>--%>
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
<script type="text/javascript">
    layui.use('upload', function(){
        layui.upload({
            elem: '#addMulti' //绑定元素
            ,url: '${ctx}/admin/question/importExcelToAdd?questionTypeId=${param.questionTypeId}' //上传接口
            ,ext: 'xls|xlsx'
            ,method: 'post'
            ,title: '批量添加'
            ,success: function(res, input){
                if(res == "succeed"){
                    layer.msg("上传成功");
                }else{
                    layer.msg(res);
                }
            }
        });

        layui.upload({
            elem: '#editMulti' //绑定元素
            ,url: '${ctx}/admin/question/importExcelToEdit?questionTypeId=${param.questionTypeId}' //上传接口
            ,ext: 'xls|xlsx'
            ,method: 'post'
            ,title: '批量修改'
            ,success: function(res, input){
                if(res == "succeed"){
                    layer.msg("上传成功");
                }else{
                    layer.msg(res);
                }
            }
        });
    });
</script>
</body>
</html>


