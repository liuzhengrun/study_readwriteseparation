package com.lzr.utils.videoparsing.episodehandle;

import com.lzr.vo.toolmodule.VideoEpisodeAnalysisVO;
import com.lzr.vo.toolmodule.VideoEpisodePlayInfoVO;
import com.lzr.utils.DataProcessingTool;
import com.lzr.utils.JsonUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 爱奇艺剧集解析
 * @author lzr
 * @date 2019/9/23 0023 14:36
 */
public class VideoIQiYiEpisodeAnalysis {

    private static final String VIDEOIQIYI_ANALYSIS = "http://jx.598110.com/?url=";

    public static void main(String[] args) throws Exception {

        getVideoIQiYiDetail("https://www.iqiyi.com/a_19rrh45l8h.html",4,"");

    }

    public static VideoEpisodeAnalysisVO getVideoIQiYiDetail(String url, Integer type,String preCoverImageUrl) throws Exception{
        if(type == 1){// 电视剧
            return getVideoIQiYiDianShiJuDetail(url,type,preCoverImageUrl);
        }else if(type == 4){// 动漫
            return getVideoIQiYiDongManDetail(url,type,preCoverImageUrl);
        }else {
            return new VideoEpisodeAnalysisVO();
        }

    }

    /**
     * 电视剧解析
     */
    public static VideoEpisodeAnalysisVO getVideoIQiYiDianShiJuDetail(String url,Integer type,String preCoverImageUrl) throws Exception{
        Document document = Jsoup.connect(url).get();
        VideoEpisodeAnalysisVO videoEpisodeAnalysisVO = new VideoEpisodeAnalysisVO();
        // 获取图片
        videoEpisodeAnalysisVO.setCoverImageUrl(preCoverImageUrl);
        // 获取详细信息
        Elements elementsByClass1 = document.getElementsByClass("info-intro");
        if(elementsByClass1!=null&&elementsByClass1.size()>0){
            Element element = elementsByClass1.get(0);
            Elements h1 = element.getElementsByTag("h1");
            if(h1!=null && h1.size()>0){// 名称
                videoEpisodeAnalysisVO.setTitleName(h1.get(0).attr("data-paopao-wallname"));
            }
            Elements updateInfo = element.getElementsByClass("intro-update");
            if(updateInfo!=null&&updateInfo.size()>0){// 获取更新情况
                Elements elementsByClass2 = updateInfo.get(0).getElementsByClass("update-progress");
                if(elementsByClass2!=null && elementsByClass2.size()>0){
                    videoEpisodeAnalysisVO.setEpisodeInfo(elementsByClass2.get(0).text());
                }
            }
            Elements area = element.getElementsByClass("episodeIntro-area");
            if(area!=null && area.size()>0){// 地区
                videoEpisodeAnalysisVO.setArea(area.get(0).text());
            }
            Elements director = element.getElementsByClass("episodeIntro-director");
            if(director!=null && director.size()>0){// 导演
                videoEpisodeAnalysisVO.setDirector(director.get(0).text());
            }
            Elements videoType = element.getElementsByClass("episodeIntro-type");
            if(videoType!=null && videoType.size()>0){// 类型
                videoEpisodeAnalysisVO.setVideoTypeInfo(videoType.get(0).text());
            }
            Elements year = element.getElementsByClass("episodeIntro-lang");
            if(year!=null && year.size()>0){// 年份
                videoEpisodeAnalysisVO.setYear(year.get(0).text());
            }
            Elements introduction = element.getElementsByClass("episodeIntro-brief");
            if(introduction!=null && introduction.size()>0){// 简介
                videoEpisodeAnalysisVO.setIntroduction(introduction.get(0).text());
            }
        }
        // 获取剧集
        Elements elementsByClass2 = document.getElementsByClass("site-piclist  site-piclist-12068  ");
        List<VideoEpisodePlayInfoVO> videoEpisodePlayInfoVOS = new ArrayList<>();
        if(elementsByClass2!=null && elementsByClass2.size()>0){
            Elements elementsByAttributeStarting = elementsByClass2.get(0).getElementsByAttributeStarting("data-order");
            VideoEpisodePlayInfoVO videoEpisodePlayInfoVO = null;
            for (Element element : elementsByAttributeStarting) {
                videoEpisodePlayInfoVO = new VideoEpisodePlayInfoVO();
                String episode = element.attr("data-order");
                Elements elementsByClass = element.getElementsByClass("site-piclist_pic_link");
                if(elementsByClass!=null && elementsByClass.size()>0){
                    String href = elementsByClass.get(0).attr("href");
                    String title = elementsByClass.get(0).attr("title");
                    if(DataProcessingTool.isNotEmpty(title)){
                        videoEpisodePlayInfoVO.setEpisode(episode);
                        videoEpisodePlayInfoVO.setVideoUrl(VIDEOIQIYI_ANALYSIS+"https:"+href);
                    }else {// 预告
                        videoEpisodePlayInfoVO.setEpisode("预"+episode);
                        videoEpisodePlayInfoVO.setVideoUrl(VIDEOIQIYI_ANALYSIS+"https:"+href);
                    }
                }else {
                    continue;
                }
                videoEpisodePlayInfoVOS.add(videoEpisodePlayInfoVO);
            }
        }
        videoEpisodeAnalysisVO.setVideoEpisodePlayInfoVOS(videoEpisodePlayInfoVOS);
        return videoEpisodeAnalysisVO;
    }

    /**
     * 动漫解析
     */
    public static VideoEpisodeAnalysisVO getVideoIQiYiDongManDetail(String url,Integer type,String preCoverImageUrl) throws Exception{
        Document document = Jsoup.connect(url).get();
        VideoEpisodeAnalysisVO videoEpisodeAnalysisVO = new VideoEpisodeAnalysisVO();
        // 获取图片
        videoEpisodeAnalysisVO.setCoverImageUrl(preCoverImageUrl);
        // 获取更新情况
        Elements updateInfo = document.getElementsByClass("title-update-progress");
        if(updateInfo!=null && updateInfo.size()>0){
            videoEpisodeAnalysisVO.setEpisodeInfo(updateInfo.get(0).text());
        }
        // 获取详细信息
        Elements elementsByClass1 = document.getElementsByClass("info-intro");
        if(elementsByClass1!=null&&elementsByClass1.size()>0){
            Element element = elementsByClass1.get(0);
            Elements title = element.getElementsByClass("info-intro-title");
            if(title!=null && title.size()>0){// 名称
                videoEpisodeAnalysisVO.setTitleName(title.get(0).attr("title"));
            }else{
                title = element.getElementsByTag("h1");
                if(title!=null && title.size()>0){// 名称
                    videoEpisodeAnalysisVO.setTitleName(title.get(0).attr("data-paopao-wallname"));
                }
            }
            Elements area = element.getElementsByClass("episodeIntro-area");
            if(area!=null && area.size()>0){// 地区
                videoEpisodeAnalysisVO.setArea(area.get(0).text());
            }
            Elements director = element.getElementsByClass("episodeIntro-director");
            if(director!=null && director.size()>0){// 导演
                videoEpisodeAnalysisVO.setDirector(director.get(0).text());
            }
            Elements videoType = element.getElementsByClass("episodeIntro-type");
            if(videoType!=null && videoType.size()>0){// 类型
                videoEpisodeAnalysisVO.setVideoTypeInfo(videoType.get(0).text());
            }
            Elements year = element.getElementsByClass("episodeIntro-lang");
            if(year!=null && year.size()>0){// 年份
                videoEpisodeAnalysisVO.setYear(year.get(0).text());
            }
            Elements introduction = element.getElementsByClass("episodeIntro-brief");
            if(introduction!=null && introduction.size()>0){// 简介
                videoEpisodeAnalysisVO.setIntroduction(introduction.get(0).text());
            }
        }
        // 获取剧集
        List<VideoEpisodePlayInfoVO> videoEpisodePlayInfoVOS = new ArrayList<>();
        // 获取id
        Elements elementsByClass = document.getElementsByClass("effect-score");
        if(elementsByClass!=null && elementsByClass.size()>0){
            String albumId = elementsByClass.get(0).attr("data-score-tvid");
            if(DataProcessingTool.isNotEmpty(albumId)){
                String getUrl = "https://pcw-api.iqiyi.com/albums/album/avlistinfo?aid="+albumId+"&size=1500&page=1";
                VideoIQiYiEpisodeAnalysisTotalData videoIQiYiEpisodeAnalysisTotalData = JsonUtils.json2Object(sendGetMsg(getUrl), VideoIQiYiEpisodeAnalysisTotalData.class);
                if(videoIQiYiEpisodeAnalysisTotalData.getData()!=null){
                    if(videoIQiYiEpisodeAnalysisTotalData.getData().getEpsodelist()!=null){
                        VideoEpisodePlayInfoVO videoEpisodePlayInfoVO = null;
                        for (VideoIQiYiEpisodeAnalysisData videoIQiYiEpisodeAnalysisData : videoIQiYiEpisodeAnalysisTotalData.getData().getEpsodelist()) {
                            videoEpisodePlayInfoVO = new VideoEpisodePlayInfoVO();
                            videoEpisodePlayInfoVO.setEpisode(""+videoIQiYiEpisodeAnalysisData.getOrder());
                            videoEpisodePlayInfoVO.setVideoUrl(VIDEOIQIYI_ANALYSIS+videoIQiYiEpisodeAnalysisData.getPlayUrl());
                            videoEpisodePlayInfoVOS.add(videoEpisodePlayInfoVO);
                        }
                    }
                }
            }
        }
        videoEpisodeAnalysisVO.setVideoEpisodePlayInfoVOS(videoEpisodePlayInfoVOS);
        return videoEpisodeAnalysisVO;
    }

    /**
     * 发送get请求获取数据
     * @param url
     * @return
     */
    private static String sendGetMsg(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            HttpURLConnection conn=(HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");//请求方法
            conn.connect();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line=null;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "{}";
        }finally {
            try {
                if (in!=null){
                    in.close();
                }
            }catch (IOException ioe){
            }
        }
    }
}

/**
 * 总数据
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true) //Jackson解析JSON数据时，忽略未知的字段。
class VideoIQiYiEpisodeAnalysisTotalData{

    private String code;

    private VideoIQiYiEpisodeAnalysisDataList data;
}

/**
 * 剧集数据类集合
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true) //Jackson解析JSON数据时，忽略未知的字段。
class VideoIQiYiEpisodeAnalysisDataList{

    private List<VideoIQiYiEpisodeAnalysisData> epsodelist;

    private List vipprevuelist;

    private List updateprevuelist;

    private Integer page;

    private Integer size;

    private Integer total;

    private Integer part;

    private String albumId;

    private Integer latestOrder;

    private Integer videoCount;
}

/**
 * 剧集数据类
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true) //Jackson解析JSON数据时，忽略未知的字段。
class VideoIQiYiEpisodeAnalysisData{

    private Long tvId;

    private String description;

    private String subtitle;

    private String vid;

    private String name;

    private String playUrl;

    private Long issueTime;

    private Long publishTime;

    private Integer contentType;

    private Integer payMark;

    private String payMarkUrl;

    private String imageUrl;

    private String duration;

    private String period;

    private Boolean exclusive;

    private Integer order;

    private Boolean qiyiProduced;

    private String shortTitle;

    private Integer is1080p;

    private Integer is720p;

}
