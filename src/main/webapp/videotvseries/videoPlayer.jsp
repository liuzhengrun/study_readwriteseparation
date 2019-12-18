<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path;
%>
<html>
<head>
    <!-- href 属性规定页面中所有相对链接的基准 URL -->
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <!-- 必须先引入vue  后使用element-ui -->
    <script src="https://unpkg.com/vue@2.5.2/dist/vue.js"></script>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="https://unpkg.com/element-ui/lib/index.js"></script>
    <!-- 引入样式 -->
    <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
    <!-- 处理 IE或360浏览器“Promise”未定义 错误 -->
    <script src="https://cdn.jsdelivr.net/npm/es6-promise@4/dist/es6-promise.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/es6-promise@4/dist/es6-promise.auto.min.js"></script>
    <title>播放页面</title>
    <style>
        html,body{
            width: 100%;
            height: 100%;
        }
        #videoPlayer {
            width: 100%;
            height: 100%;
            text-align: center;
        }
        .videoPlayerDiv {
            margin: auto;
            width: 80%;
            height: 80%;
        }
    </style>
</head>
<body>
    <div id="videoPlayer">
        <p><span style="font-size:30px;color:red;">{{videoName}}</span>&nbsp;&nbsp;<span v-if="curVideoEpisode" style="font-size:10px;">第{{curVideoEpisode}}集</span></p>
        <div class="videoPlayerDiv">
            <iframe scrolling="no" allowfullscreen="true" allowtransparency="true" width="100%" height="100%" :src="videoUrl"></iframe>
        </div>
        <br>
        <p>该视频来源于网络,若侵害了您的权利，请及时联系我们，发送邮箱至<span style="color:red;">1640071746@qq.com</span>，我们将尽快处理</p>
    </div>
</body>
<script>
    new Vue({
        el: '#videoPlayer',
        data: {
            videoUrl: '',
            videoName: '',
            curVideoEpisode: ''
        },
        methods: {

        },
        computed:function() {

        },
        created:function() {
            this.videoUrl = '<%=request.getParameter("videoUrl")%>'; // video真实地址
            this.videoName = '<%=request.getParameter("videoName")%>'; // video名称
            this.curVideoEpisode = '<%=request.getParameter("videoName")%>'; // video当前集数
        }
    });
</script>
</html>