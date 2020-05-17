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
    	function updateUserLand(id) {
            var flag = $("#flag"+id).val();
            var waterLimit = $("#waterLimit"+id).val();
            var fertilizerLimit = $("#fertilizerLimit"+id).val();
            var progress = $("#progress"+id).val();
            var status = $("#status"+id).val();
            $.post("${ctx}/admin/land/editLand",{"userId":"${land.user.id}", "landNumber":"land"+id, "flag":flag, "waterLimit":waterLimit,
                                                "fertilizerLimit":fertilizerLimit, "progress":progress, "status":status},function(data){
                if(data == "succeed"){
                    x_admin_close();
                }else{
                    layer.msg('修改失败');
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
            <form class="layui-form" action="javascript:updateUserLand(1)">
                <div class="layui-form-item">
                    <label class="layui-form-label">
                        <font color="red">*</font>land1
                    </label>
                    <div class="layui-input-inline">
                        <select id="flag1" name="flag">
                            <c:choose>
                                <c:when test="${empty land.userCrop1}">
                                    <option value="0">开垦</option>
                                    <option value="-1" selected>未开垦</option>
                                    <c:forEach var="crop" items="${crops}">
                                        <option value="${crop.id}">${crop.name}</option>
                                    </c:forEach>
                                </c:when>
                                <c:when test="${not empty land.userCrop1}">
                                    <c:choose>
                                        <c:when test="${empty land.userCrop1.crop}">
                                            <option value="0" selected>开垦</option>
                                            <option value="-1">未开垦</option>
                                            <c:forEach var="crop" items="${crops}">
                                                <option value="${crop.id}">${crop.name}</option>
                                            </c:forEach>
                                        </c:when>
                                        <c:when test="${not empty land.userCrop1.crop}">
                                            <option value="0">开垦</option>
                                            <option value="-1">未开垦</option>
                                            <c:forEach var="crop" items="${crops}">
                                                <c:if test="${land.userCrop1.crop.id == crop.id}">
                                                    <option value="${crop.id}" selected>${crop.name}</option>
                                                </c:if>
                                                <c:if test="${land.userCrop1.crop.id != crop.id}">
                                                    <option value="${crop.id}">${crop.name}</option>
                                                </c:if>
                                            </c:forEach>
                                        </c:when>
                                    </c:choose>
                                </c:when>
                            </c:choose>
                        </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-form-item">
                        <label class="layui-form-label"></label>
                        <div class="layui-input-inline">
                            <input id="waterLimit1" type="text" name="waterLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                   value="${(empty land.userCrop1) ? '0' : land.userCrop1.waterLimit}">
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            *浇水的限制次数
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label"></label>
                        <div class="layui-input-inline">
                            <input id="fertilizerLimit1" type="text" name="fertilizerLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                   value="${(empty land.userCrop1) ? '0' : land.userCrop1.fertilizerLimit}">
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            *施肥的限制次数
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label"></label>
                        <div class="layui-input-inline">
                            <input id="progress1" type="text" name="progress" required lay-verify="required" autocomplete="off" class="layui-input"
                                   value="${(empty land.userCrop1) ? '0' : land.userCrop1.progress}">
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            *作物进度（该作物进度最多）
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label"></label>
                        <div class="layui-input-inline">
                            <select id="status1" name="status">
                                <c:choose>
                                    <c:when test="${empty land.userCrop1}">
                                        <option value="0" selected>干旱</option>
                                        <option value="1">湿润</option>
                                    </c:when>
                                    <c:when test="${not empty land.userCrop1}">
                                        <c:choose>
                                            <c:when test="${land.userCrop1.status == 0}">
                                                <option value="0" selected>干旱</option>
                                                <option value="1">湿润</option>
                                            </c:when>
                                            <c:when test="${land.userCrop1.status == 1}">
                                                <option value="0">干旱</option>
                                                <option value="1" selected>湿润</option>
                                            </c:when>
                                        </c:choose>
                                    </c:when>
                                </c:choose>
                            </select>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            *干湿状态（0表示干旱，1表示湿润）
                        </div>
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
            <form class="layui-form" action="javascript:updateUserLand(2)">
                  <div class="layui-form-item">
                      <label class="layui-form-label">
                          <font color="red">*</font>land2
                      </label>
                      <div class="layui-input-inline">
                          <select id="flag2" name="flag">
                              <c:choose>
                                  <c:when test="${empty land.userCrop2}">
                                      <option value="0">开垦</option>
                                      <option value="-1" selected>未开垦</option>
                                      <c:forEach var="crop" items="${crops}">
                                          <option value="${crop.id}">${crop.name}</option>
                                      </c:forEach>
                                  </c:when>
                                  <c:when test="${not empty land.userCrop2}">
                                      <c:choose>
                                          <c:when test="${empty land.userCrop2.crop}">
                                              <option value="0" selected>开垦</option>
                                              <option value="-1">未开垦</option>
                                              <c:forEach var="crop" items="${crops}">
                                                  <option value="${crop.id}">${crop.name}</option>
                                              </c:forEach>
                                          </c:when>
                                          <c:when test="${not empty land.userCrop2.crop}">
                                              <option value="0">开垦</option>
                                              <option value="-1">未开垦</option>
                                              <c:forEach var="crop" items="${crops}">
                                                  <c:if test="${land.userCrop2.crop.id == crop.id}">
                                                      <option value="${crop.id}" selected>${crop.name}</option>
                                                  </c:if>
                                                  <c:if test="${land.userCrop2.crop.id != crop.id}">
                                                      <option value="${crop.id}">${crop.name}</option>
                                                  </c:if>
                                              </c:forEach>
                                          </c:when>
                                      </c:choose>
                                  </c:when>
                              </c:choose>
                          </select>
                      </div>
                  </div>
                  <div class="layui-form-item">
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="waterLimit2" type="text" name="waterLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop2) ? '0' : land.userCrop2.waterLimit}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *浇水的限制次数
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="fertilizerLimit2" type="text" name="fertilizerLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop2) ? '0' : land.userCrop2.fertilizerLimit}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *施肥的限制次数
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="progress2" type="text" name="progress" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop2) ? '0' : land.userCrop2.progress}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *作物进度（该作物进度最多）
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <select id="status2" name="status">
                                  <c:choose>
                                      <c:when test="${empty land.userCrop2}">
                                          <option value="0" selected>干旱</option>
                                          <option value="1">湿润</option>
                                      </c:when>
                                      <c:when test="${not empty land.userCrop2}">
                                          <c:choose>
                                              <c:when test="${land.userCrop2.status == 0}">
                                                  <option value="0" selected>干旱</option>
                                                  <option value="1">湿润</option>
                                              </c:when>
                                              <c:when test="${land.userCrop2.status == 1}">
                                                  <option value="0">干旱</option>
                                                  <option value="1" selected>湿润</option>
                                              </c:when>
                                          </c:choose>
                                      </c:when>
                                  </c:choose>
                              </select>
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *干湿状态（0表示干旱，1表示湿润）
                          </div>
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
            <form class="layui-form" action="javascript:updateUserLand(3)">
                  <div class="layui-form-item">
                      <label class="layui-form-label">
                          <font color="red">*</font>land3
                      </label>
                      <div class="layui-input-inline">
                          <select id="flag3" name="flag">
                              <c:choose>
                                  <c:when test="${empty land.userCrop3}">
                                      <option value="0">开垦</option>
                                      <option value="-1" selected>未开垦</option>
                                      <c:forEach var="crop" items="${crops}">
                                          <option value="${crop.id}">${crop.name}</option>
                                      </c:forEach>
                                  </c:when>
                                  <c:when test="${not empty land.userCrop3}">
                                      <c:choose>
                                          <c:when test="${empty land.userCrop3.crop}">
                                              <option value="0" selected>开垦</option>
                                              <option value="-1">未开垦</option>
                                              <c:forEach var="crop" items="${crops}">
                                                  <option value="${crop.id}">${crop.name}</option>
                                              </c:forEach>
                                          </c:when>
                                          <c:when test="${not empty land.userCrop3.crop}">
                                              <option value="0">开垦</option>
                                              <option value="-1">未开垦</option>
                                              <c:forEach var="crop" items="${crops}">
                                                  <c:if test="${land.userCrop3.crop.id == crop.id}">
                                                      <option value="${crop.id}" selected>${crop.name}</option>
                                                  </c:if>
                                                  <c:if test="${land.userCrop3.crop.id != crop.id}">
                                                      <option value="${crop.id}">${crop.name}</option>
                                                  </c:if>
                                              </c:forEach>
                                          </c:when>
                                      </c:choose>
                                  </c:when>
                              </c:choose>
                          </select>
                      </div>
                  </div>
                  <div class="layui-form-item">
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="waterLimit3" type="text" name="waterLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop3) ? '0' : land.userCrop3.waterLimit}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *浇水的限制次数
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="fertilizerLimit3" type="text" name="fertilizerLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop3) ? '0' : land.userCrop3.fertilizerLimit}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *施肥的限制次数
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="progress3" type="text" name="progress" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop3) ? '0' : land.userCrop3.progress}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *作物进度（该作物进度最多）
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <select id="status3" name="status">
                                  <c:choose>
                                      <c:when test="${empty land.userCrop3}">
                                          <option value="0" selected>干旱</option>
                                          <option value="1">湿润</option>
                                      </c:when>
                                      <c:when test="${not empty land.userCrop3}">
                                          <c:choose>
                                              <c:when test="${land.userCrop3.status == 0}">
                                                  <option value="0" selected>干旱</option>
                                                  <option value="1">湿润</option>
                                              </c:when>
                                              <c:when test="${land.userCrop3.status == 1}">
                                                  <option value="0">干旱</option>
                                                  <option value="1" selected>湿润</option>
                                              </c:when>
                                          </c:choose>
                                      </c:when>
                                  </c:choose>
                              </select>
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *干湿状态（0表示干旱，1表示湿润）
                          </div>
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
            <form class="layui-form" action="javascript:updateUserLand(4)">
                  <div class="layui-form-item">
                      <label class="layui-form-label">
                          <font color="red">*</font>land4
                      </label>
                      <div class="layui-input-inline">
                          <select id="flag4" name="flag">
                              <c:choose>
                                  <c:when test="${empty land.userCrop4}">
                                      <option value="0">开垦</option>
                                      <option value="-1" selected>未开垦</option>
                                      <c:forEach var="crop" items="${crops}">
                                          <option value="${crop.id}">${crop.name}</option>
                                      </c:forEach>
                                  </c:when>
                                  <c:when test="${not empty land.userCrop4}">
                                      <c:choose>
                                          <c:when test="${empty land.userCrop4.crop}">
                                              <option value="0" selected>开垦</option>
                                              <option value="-1">未开垦</option>
                                              <c:forEach var="crop" items="${crops}">
                                                  <option value="${crop.id}">${crop.name}</option>
                                              </c:forEach>
                                          </c:when>
                                          <c:when test="${not empty land.userCrop4.crop}">
                                              <option value="0">开垦</option>
                                              <option value="-1">未开垦</option>
                                              <c:forEach var="crop" items="${crops}">
                                                  <c:if test="${land.userCrop4.crop.id == crop.id}">
                                                      <option value="${crop.id}" selected>${crop.name}</option>
                                                  </c:if>
                                                  <c:if test="${land.userCrop4.crop.id != crop.id}">
                                                      <option value="${crop.id}">${crop.name}</option>
                                                  </c:if>
                                              </c:forEach>
                                          </c:when>
                                      </c:choose>
                                  </c:when>
                              </c:choose>
                          </select>
                      </div>
                  </div>
                  <div class="layui-form-item">
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="waterLimit4" type="text" name="waterLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop4) ? '0' : land.userCrop4.waterLimit}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *浇水的限制次数
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="fertilizerLimit4" type="text" name="fertilizerLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop4) ? '0' : land.userCrop4.fertilizerLimit}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *施肥的限制次数
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="progress4" type="text" name="progress" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop4) ? '0' : land.userCrop4.progress}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *作物进度（该作物进度最多）
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <select id="status4" name="status">
                                  <c:choose>
                                      <c:when test="${empty land.userCrop4}">
                                          <option value="0" selected>干旱</option>
                                          <option value="1">湿润</option>
                                      </c:when>
                                      <c:when test="${not empty land.userCrop4}">
                                          <c:choose>
                                              <c:when test="${land.userCrop4.status == 0}">
                                                  <option value="0" selected>干旱</option>
                                                  <option value="1">湿润</option>
                                              </c:when>
                                              <c:when test="${land.userCrop4.status == 1}">
                                                  <option value="0">干旱</option>
                                                  <option value="1" selected>湿润</option>
                                              </c:when>
                                          </c:choose>
                                      </c:when>
                                  </c:choose>
                              </select>
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *干湿状态（0表示干旱，1表示湿润）
                          </div>
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
            <form class="layui-form" action="javascript:updateUserLand(5)">
                  <div class="layui-form-item">
                      <label class="layui-form-label">
                          <font color="red">*</font>land5
                      </label>
                      <div class="layui-input-inline">
                          <select id="flag5" name="flag">
                              <c:choose>
                                  <c:when test="${empty land.userCrop5}">
                                      <option value="0">开垦</option>
                                      <option value="-1" selected>未开垦</option>
                                      <c:forEach var="crop" items="${crops}">
                                          <option value="${crop.id}">${crop.name}</option>
                                      </c:forEach>
                                  </c:when>
                                  <c:when test="${not empty land.userCrop5}">
                                      <c:choose>
                                          <c:when test="${empty land.userCrop5.crop}">
                                              <option value="0" selected>开垦</option>
                                              <option value="-1">未开垦</option>
                                              <c:forEach var="crop" items="${crops}">
                                                  <option value="${crop.id}">${crop.name}</option>
                                              </c:forEach>
                                          </c:when>
                                          <c:when test="${not empty land.userCrop5.crop}">
                                              <option value="0">开垦</option>
                                              <option value="-1">未开垦</option>
                                              <c:forEach var="crop" items="${crops}">
                                                  <c:if test="${land.userCrop5.crop.id == crop.id}">
                                                      <option value="${crop.id}" selected>${crop.name}</option>
                                                  </c:if>
                                                  <c:if test="${land.userCrop5.crop.id != crop.id}">
                                                      <option value="${crop.id}">${crop.name}</option>
                                                  </c:if>
                                              </c:forEach>
                                          </c:when>
                                      </c:choose>
                                  </c:when>
                              </c:choose>
                          </select>
                      </div>
                  </div>
                  <div class="layui-form-item">
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="waterLimit5" type="text" name="waterLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop5) ? '0' : land.userCrop5.waterLimit}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *浇水的限制次数
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="fertilizerLimit5" type="text" name="fertilizerLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop5) ? '0' : land.userCrop5.fertilizerLimit}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *施肥的限制次数
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="progress5" type="text" name="progress" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop5) ? '0' : land.userCrop5.progress}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *作物进度（该作物进度最多）
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <select id="status5" name="status">
                                  <c:choose>
                                      <c:when test="${empty land.userCrop5}">
                                          <option value="0" selected>干旱</option>
                                          <option value="1">湿润</option>
                                      </c:when>
                                      <c:when test="${not empty land.userCrop5}">
                                          <c:choose>
                                              <c:when test="${land.userCrop5.status == 0}">
                                                  <option value="0" selected>干旱</option>
                                                  <option value="1">湿润</option>
                                              </c:when>
                                              <c:when test="${land.userCrop5.status == 1}">
                                                  <option value="0">干旱</option>
                                                  <option value="1" selected>湿润</option>
                                              </c:when>
                                          </c:choose>
                                      </c:when>
                                  </c:choose>
                              </select>
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *干湿状态（0表示干旱，1表示湿润）
                          </div>
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
            <form class="layui-form" action="javascript:updateUserLand(6)">
                  <div class="layui-form-item">
                      <label class="layui-form-label">
                          <font color="red">*</font>land6
                      </label>
                      <div class="layui-input-inline">
                          <select id="flag6" name="flag">
                              <c:choose>
                                  <c:when test="${empty land.userCrop6}">
                                      <option value="0">开垦</option>
                                      <option value="-1" selected>未开垦</option>
                                      <c:forEach var="crop" items="${crops}">
                                          <option value="${crop.id}">${crop.name}</option>
                                      </c:forEach>
                                  </c:when>
                                  <c:when test="${not empty land.userCrop6}">
                                      <c:choose>
                                          <c:when test="${empty land.userCrop6.crop}">
                                              <option value="0" selected>开垦</option>
                                              <option value="-1">未开垦</option>
                                              <c:forEach var="crop" items="${crops}">
                                                  <option value="${crop.id}">${crop.name}</option>
                                              </c:forEach>
                                          </c:when>
                                          <c:when test="${not empty land.userCrop6.crop}">
                                              <option value="0">开垦</option>
                                              <option value="-1">未开垦</option>
                                              <c:forEach var="crop" items="${crops}">
                                                  <c:if test="${land.userCrop6.crop.id == crop.id}">
                                                      <option value="${crop.id}" selected>${crop.name}</option>
                                                  </c:if>
                                                  <c:if test="${land.userCrop6.crop.id != crop.id}">
                                                      <option value="${crop.id}">${crop.name}</option>
                                                  </c:if>
                                              </c:forEach>
                                          </c:when>
                                      </c:choose>
                                  </c:when>
                              </c:choose>
                          </select>
                      </div>
                  </div>
                  <div class="layui-form-item">
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="waterLimit6" type="text" name="waterLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop6) ? '0' : land.userCrop6.waterLimit}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *浇水的限制次数
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="fertilizerLimit6" type="text" name="fertilizerLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop6) ? '0' : land.userCrop6.fertilizerLimit}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *施肥的限制次数
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="progress6" type="text" name="progress" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop6) ? '0' : land.userCrop6.progress}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *作物进度（该作物进度最多）
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <select id="status6" name="status">
                                  <c:choose>
                                      <c:when test="${empty land.userCrop6}">
                                          <option value="0" selected>干旱</option>
                                          <option value="1">湿润</option>
                                      </c:when>
                                      <c:when test="${not empty land.userCrop6}">
                                          <c:choose>
                                              <c:when test="${land.userCrop6.status == 0}">
                                                  <option value="0" selected>干旱</option>
                                                  <option value="1">湿润</option>
                                              </c:when>
                                              <c:when test="${land.userCrop6.status == 1}">
                                                  <option value="0">干旱</option>
                                                  <option value="1" selected>湿润</option>
                                              </c:when>
                                          </c:choose>
                                      </c:when>
                                  </c:choose>
                              </select>
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *干湿状态（0表示干旱，1表示湿润）
                          </div>
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
            <form class="layui-form" action="javascript:updateUserLand(7)">
                  <div class="layui-form-item">
                      <label class="layui-form-label">
                          <font color="red">*</font>land7
                      </label>
                      <div class="layui-input-inline">
                          <select id="flag7" name="flag">
                              <c:choose>
                                  <c:when test="${empty land.userCrop7}">
                                      <option value="0">开垦</option>
                                      <option value="-1" selected>未开垦</option>
                                      <c:forEach var="crop" items="${crops}">
                                          <option value="${crop.id}">${crop.name}</option>
                                      </c:forEach>
                                  </c:when>
                                  <c:when test="${not empty land.userCrop7}">
                                      <c:choose>
                                          <c:when test="${empty land.userCrop7.crop}">
                                              <option value="0" selected>开垦</option>
                                              <option value="-1">未开垦</option>
                                              <c:forEach var="crop" items="${crops}">
                                                  <option value="${crop.id}">${crop.name}</option>
                                              </c:forEach>
                                          </c:when>
                                          <c:when test="${not empty land.userCrop7.crop}">
                                              <option value="0">开垦</option>
                                              <option value="-1">未开垦</option>
                                              <c:forEach var="crop" items="${crops}">
                                                  <c:if test="${land.userCrop7.crop.id == crop.id}">
                                                      <option value="${crop.id}" selected>${crop.name}</option>
                                                  </c:if>
                                                  <c:if test="${land.userCrop7.crop.id != crop.id}">
                                                      <option value="${crop.id}">${crop.name}</option>
                                                  </c:if>
                                              </c:forEach>
                                          </c:when>
                                      </c:choose>
                                  </c:when>
                              </c:choose>
                          </select>
                      </div>
                  </div>
                  <div class="layui-form-item">
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="waterLimit7" type="text" name="waterLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop7) ? '0' : land.userCrop7.waterLimit}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *浇水的限制次数
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="fertilizerLimit7" type="text" name="fertilizerLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop7) ? '0' : land.userCrop7.fertilizerLimit}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *施肥的限制次数
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="progress7" type="text" name="progress" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop7) ? '0' : land.userCrop7.progress}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *作物进度（该作物进度最多）
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <select id="status7" name="status">
                                  <c:choose>
                                      <c:when test="${empty land.userCrop7}">
                                          <option value="0" selected>干旱</option>
                                          <option value="1">湿润</option>
                                      </c:when>
                                      <c:when test="${not empty land.userCrop7}">
                                          <c:choose>
                                              <c:when test="${land.userCrop7.status == 0}">
                                                  <option value="0" selected>干旱</option>
                                                  <option value="1">湿润</option>
                                              </c:when>
                                              <c:when test="${land.userCrop7.status == 1}">
                                                  <option value="0">干旱</option>
                                                  <option value="1" selected>湿润</option>
                                              </c:when>
                                          </c:choose>
                                      </c:when>
                                  </c:choose>
                              </select>
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *干湿状态（0表示干旱，1表示湿润）
                          </div>
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
            <form class="layui-form" action="javascript:updateUserLand(8)">
                  <div class="layui-form-item">
                      <label class="layui-form-label">
                          <font color="red">*</font>land8
                      </label>
                      <div class="layui-input-inline">
                          <select id="flag8" name="flag">
                              <c:choose>
                                  <c:when test="${empty land.userCrop8}">
                                      <option value="0">开垦</option>
                                      <option value="-1" selected>未开垦</option>
                                      <c:forEach var="crop" items="${crops}">
                                          <option value="${crop.id}">${crop.name}</option>
                                      </c:forEach>
                                  </c:when>
                                  <c:when test="${not empty land.userCrop8}">
                                      <c:choose>
                                          <c:when test="${empty land.userCrop8.crop}">
                                              <option value="0" selected>开垦</option>
                                              <option value="-1">未开垦</option>
                                              <c:forEach var="crop" items="${crops}">
                                                  <option value="${crop.id}">${crop.name}</option>
                                              </c:forEach>
                                          </c:when>
                                          <c:when test="${not empty land.userCrop8.crop}">
                                              <option value="0">开垦</option>
                                              <option value="-1">未开垦</option>
                                              <c:forEach var="crop" items="${crops}">
                                                  <c:if test="${land.userCrop8.crop.id == crop.id}">
                                                      <option value="${crop.id}" selected>${crop.name}</option>
                                                  </c:if>
                                                  <c:if test="${land.userCrop8.crop.id != crop.id}">
                                                      <option value="${crop.id}">${crop.name}</option>
                                                  </c:if>
                                              </c:forEach>
                                          </c:when>
                                      </c:choose>
                                  </c:when>
                              </c:choose>
                          </select>
                      </div>
                  </div>
                  <div class="layui-form-item">
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="waterLimit8" type="text" name="waterLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop8) ? '0' : land.userCrop8.waterLimit}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *浇水的限制次数
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="fertilizerLimit8" type="text" name="fertilizerLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop8) ? '0' : land.userCrop8.fertilizerLimit}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *施肥的限制次数
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="progress8" type="text" name="progress" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop8) ? '0' : land.userCrop8.progress}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *作物进度（该作物进度最多）
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <select id="status8" name="status">
                                  <c:choose>
                                      <c:when test="${empty land.userCrop8}">
                                          <option value="0" selected>干旱</option>
                                          <option value="1">湿润</option>
                                      </c:when>
                                      <c:when test="${not empty land.userCrop8}">
                                          <c:choose>
                                              <c:when test="${land.userCrop8.status == 0}">
                                                  <option value="0" selected>干旱</option>
                                                  <option value="1">湿润</option>
                                              </c:when>
                                              <c:when test="${land.userCrop8.status == 1}">
                                                  <option value="0">干旱</option>
                                                  <option value="1" selected>湿润</option>
                                              </c:when>
                                          </c:choose>
                                      </c:when>
                                  </c:choose>
                              </select>
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *干湿状态（0表示干旱，1表示湿润）
                          </div>
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
            <form class="layui-form" action="javascript:updateUserLand(9)">
                  <div class="layui-form-item">
                      <label class="layui-form-label">
                          <font color="red">*</font>land9
                      </label>
                      <div class="layui-input-inline">
                          <select id="flag9" name="flag">
                              <c:choose>
                                  <c:when test="${empty land.userCrop9}">
                                      <option value="0">开垦</option>
                                      <option value="-1" selected>未开垦</option>
                                      <c:forEach var="crop" items="${crops}">
                                          <option value="${crop.id}">${crop.name}</option>
                                      </c:forEach>
                                  </c:when>
                                  <c:when test="${not empty land.userCrop9}">
                                      <c:choose>
                                          <c:when test="${empty land.userCrop9.crop}">
                                              <option value="0" selected>开垦</option>
                                              <option value="-1">未开垦</option>
                                              <c:forEach var="crop" items="${crops}">
                                                  <option value="${crop.id}">${crop.name}</option>
                                              </c:forEach>
                                          </c:when>
                                          <c:when test="${not empty land.userCrop9.crop}">
                                              <option value="0">开垦</option>
                                              <option value="-1">未开垦</option>
                                              <c:forEach var="crop" items="${crops}">
                                                  <c:if test="${land.userCrop9.crop.id == crop.id}">
                                                      <option value="${crop.id}" selected>${crop.name}</option>
                                                  </c:if>
                                                  <c:if test="${land.userCrop9.crop.id != crop.id}">
                                                      <option value="${crop.id}">${crop.name}</option>
                                                  </c:if>
                                              </c:forEach>
                                          </c:when>
                                      </c:choose>
                                  </c:when>
                              </c:choose>
                          </select>
                      </div>
                  </div>
                  <div class="layui-form-item">
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="waterLimit9" type="text" name="waterLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop9) ? '0' : land.userCrop9.waterLimit}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *浇水的限制次数
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="fertilizerLimit9" type="text" name="fertilizerLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop9) ? '0' : land.userCrop9.fertilizerLimit}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *施肥的限制次数
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="progress9" type="text" name="progress" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop9) ? '0' : land.userCrop9.progress}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *作物进度（该作物进度最多）
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <select id="status9" name="status">
                                  <c:choose>
                                      <c:when test="${empty land.userCrop9}">
                                          <option value="0" selected>干旱</option>
                                          <option value="1">湿润</option>
                                      </c:when>
                                      <c:when test="${not empty land.userCrop9}">
                                          <c:choose>
                                              <c:when test="${land.userCrop9.status == 0}">
                                                  <option value="0" selected>干旱</option>
                                                  <option value="1">湿润</option>
                                              </c:when>
                                              <c:when test="${land.userCrop9.status == 1}">
                                                  <option value="0">干旱</option>
                                                  <option value="1" selected>湿润</option>
                                              </c:when>
                                          </c:choose>
                                      </c:when>
                                  </c:choose>
                              </select>
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *干湿状态（0表示干旱，1表示湿润）
                          </div>
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
            <form class="layui-form" action="javascript:updateUserLand(10)">
                  <div class="layui-form-item">
                      <label class="layui-form-label">
                          <font color="red">*</font>land10
                      </label>
                      <div class="layui-input-inline">
                          <select id="flag10" name="flag">
                              <c:choose>
                                  <c:when test="${empty land.userCrop10}">
                                      <option value="0">开垦</option>
                                      <option value="-1" selected>未开垦</option>
                                      <c:forEach var="crop" items="${crops}">
                                          <option value="${crop.id}">${crop.name}</option>
                                      </c:forEach>
                                  </c:when>
                                  <c:when test="${not empty land.userCrop10}">
                                      <c:choose>
                                          <c:when test="${empty land.userCrop10.crop}">
                                              <option value="0" selected>开垦</option>
                                              <option value="-1">未开垦</option>
                                              <c:forEach var="crop" items="${crops}">
                                                  <option value="${crop.id}">${crop.name}</option>
                                              </c:forEach>
                                          </c:when>
                                          <c:when test="${not empty land.userCrop10.crop}">
                                              <option value="0">开垦</option>
                                              <option value="-1">未开垦</option>
                                              <c:forEach var="crop" items="${crops}">
                                                  <c:if test="${land.userCrop10.crop.id == crop.id}">
                                                      <option value="${crop.id}" selected>${crop.name}</option>
                                                  </c:if>
                                                  <c:if test="${land.userCrop10.crop.id != crop.id}">
                                                      <option value="${crop.id}">${crop.name}</option>
                                                  </c:if>
                                              </c:forEach>
                                          </c:when>
                                      </c:choose>
                                  </c:when>
                              </c:choose>
                          </select>
                      </div>
                  </div>
                  <div class="layui-form-item">
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="waterLimit10" type="text" name="waterLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop10) ? '0' : land.userCrop10.waterLimit}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *浇水的限制次数
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="fertilizerLimit10" type="text" name="fertilizerLimit" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop10) ? '0' : land.userCrop10.fertilizerLimit}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *施肥的限制次数
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <input id="progress10" type="text" name="progress" required lay-verify="required" autocomplete="off" class="layui-input"
                                     value="${(empty land.userCrop10) ? '0' : land.userCrop10.progress}">
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *作物进度（该作物进度最多）
                          </div>
                      </div>
                      <div class="layui-form-item">
                          <label class="layui-form-label"></label>
                          <div class="layui-input-inline">
                              <select id="status10" name="status">
                                  <c:choose>
                                      <c:when test="${empty land.userCrop10}">
                                          <option value="0" selected>干旱</option>
                                          <option value="1">湿润</option>
                                      </c:when>
                                      <c:when test="${not empty land.userCrop10}">
                                          <c:choose>
                                              <c:when test="${land.userCrop10.status == 0}">
                                                  <option value="0" selected>干旱</option>
                                                  <option value="1">湿润</option>
                                              </c:when>
                                              <c:when test="${land.userCrop10.status == 1}">
                                                  <option value="0">干旱</option>
                                                  <option value="1" selected>湿润</option>
                                              </c:when>
                                          </c:choose>
                                      </c:when>
                                  </c:choose>
                              </select>
                          </div>
                          <div class="layui-form-mid layui-word-aux">
                              *干湿状态（0表示干旱，1表示湿润）
                          </div>
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