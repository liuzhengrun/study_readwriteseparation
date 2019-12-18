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
    <title>剧集页面</title>
    <style>
        html,body{
            width: 100%;
            height: 100%;
        }

        #tvDetails {
            width: 100%;
            height: 100%;
            margin: auto;
        }

        .tvDiv1 {
            position: absolute;
            width: 100%;
            height: 275px;
        }

        .tvDiv1_1 {
            top: 5px;
            position: relative;
            width: 1400px;
            height: 275px;
            margin: auto;
        }

        .image {
            position: absolute;
            left: 5px;
            width: 205px;
            height: 273px;
        }

        .detailsDiv {
            position: absolute;
            left: 215px;
            width: 1180px;
            height: 273px;
            overflow: auto;
        }

        .episodeButton {
            float: left;
            font-size: 12px;
            width: 54px;
            height: 36px;
            background-color: #EDEDED;
            margin-left: 5px;
            margin-top: 3px;
            display: flex;
            justify-content: center;
            align-items: Center;
            color: #666;
            text-align: center;
        }

        .episodeButton:hover {
            background-color: #1DB69A;
            color: white;
        }

        .advertisingSpace {
            top: 300px;
            position: absolute;
            width: 100%;
            height: 600px;
            border: 1px solid blue;
        }
    </style>
</head>
<body>
    <div id="tvDetails">
        <div v-if="videoEpisodeAnalysisVO!=null" class="tvDiv1">
            <div class="tvDiv1_1">
                <el-image class="image" :src="videoEpisodeAnalysisVO.coverImageUrl" :fit="videoEpisodeAnalysisVO.titleName"></el-image>
                <div class="detailsDiv">
                    <p>&nbsp;<span style="font-size:20px;color:red;">{{videoEpisodeAnalysisVO.titleName}}</span>&nbsp;&nbsp;<span style="font-size:8px;">{{videoEpisodeAnalysisVO.episodeInfo}}</span></p>
                    <p>{{videoEpisodeAnalysisVO.videoTypeInfo}}</p>
                    <p><span v-if="videoEpisodeAnalysisVO.director">{{videoEpisodeAnalysisVO.director}} &nbsp;&nbsp; </span>
                        <span v-if="videoEpisodeAnalysisVO.year">{{videoEpisodeAnalysisVO.year}} &nbsp;&nbsp; </span>{{videoEpisodeAnalysisVO.area}}</p>
                    <p>{{videoEpisodeAnalysisVO.actors}}</p>
                    <p>{{videoEpisodeAnalysisVO.introduction}}</p>
                    <p>&nbsp;</p>
                    <p>剧集信息:</p>
                    <label v-for="(videoEpisodePlayInfoVOS,index) in videoEpisodeAnalysisVO.videoEpisodePlayInfoVOS" :key="index">
                        <span @click="toVideoPlayer(videoEpisodePlayInfoVOS,videoEpisodeAnalysisVO.titleName)">
                            <div class="episodeButton">
                                {{videoEpisodePlayInfoVOS.episode}}
                            </div>
                        </span>
                    </label>
                </div>
            </div>
        </div>
        <div class="advertisingSpace">
            这里是个框框
        </div>
    </div>
</body>
<script>
    new Vue({
        el: '#tvDetails',
        data: {
            videoEpisodeAnalysisVO: null,
            listQuery: {
                videoUrl: '',
                videoType: null,
                videoResource: null,
                preCoverImageUrl: ''
            }
        },
        watch: {},
        methods: {
            getVideoDetails:function() { // 获取视频详细信息
                var _this = this;
                axios.post('/specialTool/videoEpisodeAnalysisDetails', this.listQuery, {
                    emulateJSON: true,
                    headers: {
                        'token': localStorage.getItem('user_token'),
                        'Authorization': localStorage.getItem('Authorization')
                    }
                }).then(function(data) {
                    if (!data.data.data) {
                        _this.$message.error(data.data.msg);
                    } else {
                        _this.videoEpisodeAnalysisVO = data.data.data;
                    }
                }).catch(function(error) {
                    console.error(error);
                });
            },
            toVideoPlayer:function(videoEpisodePlayInfoVOS, titleName) { // 跳转到播放页面
                var href = "/videotvseries/videoPlayer.jsp" +
                    "?videoUrl="+videoEpisodePlayInfoVOS.videoUrl+
                    "&videoName="+titleName+
                    "&curVideoEpisode="+videoEpisodePlayInfoVOS.episode;
                window.open(href, "_blank");
            }
        },
        created:function() {
            // 获取参数
            this.listQuery.videoUrl = '<%=request.getParameter("videoUrl")%>'; // 网页地址
            this.listQuery.videoType = '<%=request.getParameter("videoType")%>'; // 视频类型
            this.listQuery.videoResource = '<%=request.getParameter("videoResource")%>'; // 视频来源
            this.listQuery.preCoverImageUrl = '<%=request.getParameter("preCoverImageUrl")%>'; // 原封面图片
            this.getVideoDetails(); // 获取视频详细信息
        }
    });
</script>
</html>