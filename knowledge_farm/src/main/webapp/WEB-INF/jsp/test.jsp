<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/14 0014
  Time: 16:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    ===============<br>

    <a href="/admin/question/exportExcelModel">导出Excel表头</a><br>

    ===============<br>

    <a href="/admin/question/exportExcelData">导出Excel数据</a><br>

    ===============<br>

    <form action="/admin/question/importExcelToEdit" method="post" enctype="multipart/form-data">
        <p>Excel批量修改</p>
        <input type="file" name="file">
        <p><input type="submit" value="提交"></p>
    </form>

    ===============<br>

    <form action="/admin/question/importExcelToAdd" method="post" enctype="multipart/form-data">
        <p>Excel批量添加</p>
        <input type="file" name="file">
        <p><input type="submit" value="提交"></p>
    </form>
</body>
</html>
