package com.lzr.utils.videoparsing.episodehandle;

import com.lzr.vo.toolmodule.VideoEpisodeAnalysisVO;
import com.lzr.vo.toolmodule.VideoEpisodePlayInfoVO;
import com.lzr.utils.JsonUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 腾讯剧集解析
 * @author lzr
 * @date 2019/9/24 0024 12:26
 */
public class VideoTencentEpisodeAnalysis {

    private static final String TENCENT_ANALYSIS = "http://beaacc.com/api.php?url=";

    public static void main(String[] args) throws Exception {

        getVideoTencentDetail("https://v.qq.com/x/cover/m441e3rjq9kwpsc.html",4,"");

    }

    /**
     * 获取信息
     */
    public static VideoEpisodeAnalysisVO getVideoTencentDetail(String url, Integer type, String preCoverImageUrl) throws Exception{
        if(type == 1){// 电视剧
            return getVideoTencentDianShiJuDetail(url,type,preCoverImageUrl);
        }else if(type == 4){// 动漫
            return getVideoTencentDongManDetail(url,type,preCoverImageUrl);
        }else {
            return new VideoEpisodeAnalysisVO();
        }
    }

    /**
     * 电视剧剧集解析
     */
    private static VideoEpisodeAnalysisVO getVideoTencentDianShiJuDetail(String url,Integer type,String preCoverImageUrl) throws Exception{
        Document document = Jsoup.connect(url).get();
        VideoEpisodeAnalysisVO videoEpisodeAnalysisVO = new VideoEpisodeAnalysisVO();
        String s = document.toString();
        String[] split = s.split("COVER_INFO = ", 2);
        if(split.length>=2){
            String[] split1 = split[1].split("var COLUMN_INFO", 2);
            if(split1.length>=2){
                VideoTencentEpisodeAnalysisTotalData data = JsonUtils.json2Object(split1[0].trim(), VideoTencentEpisodeAnalysisTotalData.class);
                videoEpisodeAnalysisVO.setTitleName(data.getTitle());
                videoEpisodeAnalysisVO.setIntroduction(data.getDescription());
                videoEpisodeAnalysisVO.setEpisodeInfo(data.getEpisode_updated()+"/"+data.getEpisode_all()+"集");
                videoEpisodeAnalysisVO.setArea("地区："+data.getArea_name());
                videoEpisodeAnalysisVO.setCoverImageUrl(preCoverImageUrl);
                videoEpisodeAnalysisVO.setYear("年代："+data.getYear());
                String director = "导演：";
                if(data.getDirector()!=null&&data.getDirector().size()>0){
                    for (String s1 : data.getDirector()) {
                        director = director + s1 +"  ";
                    }
                }else{
                    director = director + "暂无";
                }
                videoEpisodeAnalysisVO.setDirector(director);
                String actors = "主演：";
                if(data.getLeading_actor()!=null&&data.getLeading_actor().size()>0){
                    for (String s1 : data.getLeading_actor()) {
                        actors = actors + s1 +"  ";
                    }
                }else{
                    actors = actors + "暂无";
                }
                videoEpisodeAnalysisVO.setActors(actors);
                String videoTypeInfo = "类型：";
                if(data.getSub_genre()!=null&&data.getSub_genre().size()>0){
                    for (String s1 : data.getSub_genre()) {
                        videoTypeInfo = videoTypeInfo + s1 +"  ";
                    }
                }else{
                    videoTypeInfo = videoTypeInfo + "暂无";
                }
                videoEpisodeAnalysisVO.setVideoTypeInfo(videoTypeInfo);
                List<VideoEpisodePlayInfoVO> videoEpisodePlayInfoVOS = new ArrayList<>();
                VideoEpisodePlayInfoVO videoEpisodePlayInfoVO = null;
                List<String> video_ids = data.getVideo_ids();
                for (int i = 1; i <= video_ids.size(); i++) {
                    videoEpisodePlayInfoVO = new VideoEpisodePlayInfoVO();
                    videoEpisodePlayInfoVO.setEpisode(""+i);
                    videoEpisodePlayInfoVO.setVideoUrl(TENCENT_ANALYSIS+"https://v.qq.com/x/cover/"+data.getId()+"/"+video_ids.get(i-1)+".html");// 拼接成我们需要的路径
                    videoEpisodePlayInfoVOS.add(videoEpisodePlayInfoVO);
                }
                videoEpisodeAnalysisVO.setVideoEpisodePlayInfoVOS(videoEpisodePlayInfoVOS);
            }
        }
        return videoEpisodeAnalysisVO;
    }

    /**
     * 动漫剧集解析
     */
    private static VideoEpisodeAnalysisVO getVideoTencentDongManDetail(String url,Integer type,String preCoverImageUrl) throws Exception{
        Document document = Jsoup.connect(url).get();
        VideoEpisodeAnalysisVO videoEpisodeAnalysisVO = new VideoEpisodeAnalysisVO();
        String s = document.toString();
        String[] split = s.split("COVER_INFO = ", 2);
        if(split.length>=2){
            String[] split1 = split[1].split("var COLUMN_INFO", 2);
            if(split1.length>=2){
                VideoTencentEpisodeAnalysisTotalData data = JsonUtils.json2Object(split1[0].trim(), VideoTencentEpisodeAnalysisTotalData.class);
                videoEpisodeAnalysisVO.setTitleName(data.getTitle());
                videoEpisodeAnalysisVO.setIntroduction(data.getDescription());
                videoEpisodeAnalysisVO.setEpisodeInfo(data.getEpisode_updated()+"/"+data.getEpisode_all()+"集");
                videoEpisodeAnalysisVO.setArea("地区："+data.getArea_name());
                videoEpisodeAnalysisVO.setCoverImageUrl(preCoverImageUrl);
                videoEpisodeAnalysisVO.setYear("年代："+data.getYear());
                String director = "导演：";
                if(data.getDirector_id()!=null&&data.getDirector_id().size()>0){
                    for (String s1 : data.getDirector_id()) {
                        director = director + s1 +"  ";
                    }
                }else{
                    director = director + "暂无";
                }
                videoEpisodeAnalysisVO.setDirector(director);
                String actors = "主演：";
                if(data.getLeading_actor()!=null&&data.getLeading_actor().size()>0){
                    for (String s1 : data.getLeading_actor()) {
                        actors = actors + s1 +"  ";
                    }
                }else{
                    actors = actors + "暂无";
                }
                videoEpisodeAnalysisVO.setActors(actors);
                String videoTypeInfo = "类型：";
                if(data.getSub_genre()!=null&&data.getSub_genre().size()>0){
                    for (String s1 : data.getSub_genre()) {
                        videoTypeInfo = videoTypeInfo + s1 +"  ";
                    }
                }else{
                    videoTypeInfo = videoTypeInfo + "暂无";
                }
                videoEpisodeAnalysisVO.setVideoTypeInfo(videoTypeInfo);
                List<VideoEpisodePlayInfoVO> videoEpisodePlayInfoVOS = new ArrayList<>();
                VideoEpisodePlayInfoVO videoEpisodePlayInfoVO = null;
                List<String> video_ids = data.getVideo_ids();
                for (int i = 1; i <= video_ids.size(); i++) {
                    videoEpisodePlayInfoVO = new VideoEpisodePlayInfoVO();
                    videoEpisodePlayInfoVO.setEpisode(""+i);
                    videoEpisodePlayInfoVO.setVideoUrl(TENCENT_ANALYSIS+"https://v.qq.com/x/cover/"+data.getId()+"/"+video_ids.get(i-1)+".html");// 拼接成我们需要的路径
                    videoEpisodePlayInfoVOS.add(videoEpisodePlayInfoVO);
                }
                videoEpisodeAnalysisVO.setVideoEpisodePlayInfoVOS(videoEpisodePlayInfoVOS);
            }
        }
        System.out.println(JsonUtils.Object2json(videoEpisodeAnalysisVO));
        return videoEpisodeAnalysisVO;
    }


}

// 腾讯电视剧总数据类
@Data
@JsonIgnoreProperties(ignoreUnknown = true) //Jackson解析JSON数据时，忽略未知的字段。
class VideoTencentEpisodeAnalysisTotalData{

    private String id;// 每个电视剧的标识

    private String langue;// 语言

    private String title;// 题目

    private List<String> leading_actor;// 领衔主演

    private String area_name;// 地区

    private Integer year;// 年代

    private String description;// 描述

    private String episode_updated;// 剧集更新信息

    private String episode_all;// 总剧集数

    private List<String> director;// 导演

    private List<String> director_id;// 导演(动漫)

    private List<String> sub_genre;// 视频类型

    private List<String> video_ids;// 视频id集合

}
