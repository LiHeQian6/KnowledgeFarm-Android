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

    <script type="text/javascript" src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdn.bootcss.com/Swiper/3.4.2/js/swiper.jquery.min.js"></script>

    <script>
        function test(){
            var xhr = new XMLHttpRequest();
            xhr.open('GET', "/admin/test", true);
            xhr.responseType = "blob";
            xhr.onload = function() {
                if (this.status == 200) {
                    var blob = this.response;
                    var img = document.getElementById("testCodeImg");
                    img.onload = function(e) {
                        window.URL.revokeObjectURL(img.src);
                    };
                    img.src = window.URL.createObjectURL(blob);
                }
            }
            xhr.send();
        }
    </script>
</head>
<body>
<div id="testCodeImgDiv" onclick="javascript:test()"><img id="testCodeImg" src="/admin/test"></div>
</body>
</html>
