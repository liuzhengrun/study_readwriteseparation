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
    <title>主页面</title>
    <style>
        html,body{
            width: 100%;
            height: 100%;
        }
        #main {
            width: 100%;
            height: 100%;
        }
        .titleDiv {
            position: absolute;
            width: 100%;
            height: 20%;
        }
        a {
            text-decoration: none;
        }
        .titleDivVideoResource {
            height: 50%;
            width: 100%;
            display: flex;
            justify-content: center;
            align-items: Center;
            text-align: center;
        }
        .titleDivVideoType {
            height: 50%;
            width: 100%;
            display: flex;
            justify-content: center;
            align-items: Center;
            text-align: center;
        }
        .videoContent {
            border: 1px solid darkgray;
            overflow-y: auto;
            top: 20%;
            position: absolute;
            width: 100%;
            height: 80%;
        }
        .videoContentInfo {
            float: left;
            width: 210px;
            height: 380px;
            border: 1px solid blanchedalmond;
            overflow: hidden;
        }
        .videoContentInfo p {
            display: block;
            width: 100%;
        }
        .chooose {
            background: powderblue;
        }
    </style>
</head>
<body>
    <div id="main" v-loading="loading" element-loading-text="视频资源加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
        <div class="titleDiv">
            <div class="titleDivVideoResource">
                <el-select v-model="listQuery.videoResource" @change="selectVideoResource">
                    <el-option label="360视频" value="1">360视频</el-option>
                    <el-option label="爱奇艺视频" value="2">爱奇艺视频</el-option>
                    <el-option label="优酷视频" value="3">优酷视频</el-option>
                    <el-option label="腾讯视频" value="4">腾讯视频</el-option>
                </el-select>
            </div>
            <div class="titleDivVideoType">
                <el-popover placement="bottom" width="200" trigger="click">
                    <el-button :class="{chooose:listQuery.videoType==1}" slot="reference" @click="selectVideoType(1)">电视剧</el-button>
                </el-popover>
                <el-popover placement="bottom" width="200" trigger="click">
                    <el-button :class="{chooose:listQuery.videoType==2}" slot="reference" @click="selectVideoType(2)">电影</el-button>
                </el-popover>
                <el-popover placement="bottom" width="200" trigger="click">
                    <el-button :class="{chooose:listQuery.videoType==3}" slot="reference" @click="selectVideoType(3)">综艺</el-button>
                </el-popover>
                <el-popover placement="bottom" width="200" trigger="click">
                    <el-button :class="{chooose:listQuery.videoType==4}" slot="reference" @click="selectVideoType(4)">动漫</el-button>
                </el-popover>
            </div>
        </div>
        <div class="videoContent">
            <label v-show="listQuery.videoType==2||listQuery.videoType==3">
                <div class="videoContentInfo" v-for="(videoInfoVO,index) in videoInfoVOS" :key="index">
                    <span @click="toVideoPlayer(videoInfoVO)">
                        <el-image style="width: 205px; height: 273px" :src="videoInfoVO.coverImageUrl" :fit="videoInfoVO.titleName"></el-image>
                    </span>
                    <span @click="toVideoPlayer(videoInfoVO)">
                        <p>{{videoInfoVO.titleName}}</p>
                    </span>
                    <p>{{videoInfoVO.year}}</p>
                    <p>{{videoInfoVO.score}}</p>
                    <p>{{videoInfoVO.starring}}</p>
                </div>
            </label>
            <label v-show="listQuery.videoType==1||listQuery.videoType==4">
                <div class="videoContentInfo" v-for="(videoInfoVO,index) in videoInfoVOS" :key="index">
                    <span @click="toTvSeriesEpisode(videoInfoVO)">
                        <el-image style="width: 205px; height: 273px" :src="videoInfoVO.coverImageUrl" :fit="videoInfoVO.titleName"></el-image>
                    </span>
                    <span @click="toTvSeriesEpisode(videoInfoVO)">
                        <p>{{videoInfoVO.titleName}}</p>
                    </span>
                    <p>{{videoInfoVO.year}}</p>
                    <p>{{videoInfoVO.score}}</p>
                    <p>{{videoInfoVO.starring}}</p>
                </div>
            </label>
        </div>
        <div style="position:absolute;bottom: 0px;right:0px;">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum" :page-size=" listQuery.pageSize" layout="prev, pager, next, jumper" :pager-count="5">
            </el-pagination>
        </div>
    </div>
</body>
<script>

    new Vue({
        el: '#main',
        data: {
            loading: false,
            videoInfoVOS: [],
            total: 0,
            listQuery: {
                pageNum: 1,
                pageSize: 10,
                videoType: 1, // 默认1：电视剧 1：电视剧 2：电影 3：综艺 4：动漫
                videoResource: '1' // 视频来源 1：360视频 2：爱奇艺 3：优酷 4：腾讯
            }
        },
        methods: {
            handleSizeChange:function(val) { //pageSize 改变时会触发
                this.listQuery.pageSize = val;
                this.video360InfoVOPage();
            },
            handleCurrentChange:function(val) { //currentPage 改变时会触发
                this.listQuery.pageNum = val;
                this.video360InfoVOPage();
            },
            video360InfoVOPage:function() { // 获取视频信息
                this.loading = true;
                var _this = this;
                axios.post('/specialTool/video360InfoVOPage', this.listQuery, {
                    emulateJSON: true,
                    headers: {
                        'token': localStorage.getItem('user_token'),
                        'Authorization': localStorage.getItem('Authorization')
                    }
                }).then(function(data) {
                    if (!data.data.data) {} else {
                        _this.videoInfoVOS = data.data.data.videoInfoVOS;
                        _this.listQuery.pageSize = data.data.data.videoInfoVOS.length;
                    }
                    _this.loading = false;
                }).catch(function(error) {
                    console.error(error);
                    _this.loading = false;
                });
            },
            selectVideoResource:function() { // 设置视频来源，并获取数据
                this.video360InfoVOPage();
                this.listQuery.pageNum = 1;
            },
            selectVideoType:function(videoType) {
                this.listQuery.videoType = videoType;
                this.listQuery.pageNum = 1;
                this.video360InfoVOPage();
            },
            toTvSeriesEpisode:function(videoInfoVO) { // 跳转到剧集页面
                var href = "/videotvseries/tvSeriesEpisode.jsp" +
                    "?videoUrl="+videoInfoVO.videoUrl+
                    "&videoType="+this.listQuery.videoType+
                    "&videoResource="+this.listQuery.videoResource+
                    "&preCoverImageUrl="+videoInfoVO.coverImageUrl;
                window.open(href, "_blank");
            },
            toVideoPlayer:function(videoInfoVO) { // 跳转到播放页面
                var href = "/videotvseries/videoPlayer.jsp" +
                    "?videoUrl="+videoInfoVO.videoUrl+
                    "&videoName="+videoInfoVO.titleName+
                    "&curVideoEpisode=";
                window.open(href, "_blank");
            }
        },
        mounted:function() {
        },
        created:function() {
            //获取模块信息
            this.video360InfoVOPage();
        }
    });
</script>
</html>