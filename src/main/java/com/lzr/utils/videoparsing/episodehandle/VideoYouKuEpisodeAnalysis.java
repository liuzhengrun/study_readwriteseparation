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
 * 优酷剧集解析
 * @author lzr
 * @date 2019/9/23 0023 18:02
 */
public class VideoYouKuEpisodeAnalysis{

    private static final String VIDEOYOUKU_ANALYSIS = "http://beaacc.com/api.php?url=";

    public static void main(String[] args) throws Exception{
        getVideoYouKuDetail("https://v.youku.com/v_show/id_XNDEyMzQ0MjUxNg==.html",4,"");
        //getVideoYouKuDetail("https://v.youku.com/v_show/id_XNDMyNjQxOTM5Ng==.html?spm=a2ha1.12701310.app.5~5!2~5!3~5~5~5~5~5~5~A",1,"");
        String m = "label-icon  ";

    }

    /**
     * 获取信息
     * @param url
     * @param type
     * @return
     */
    public static VideoEpisodeAnalysisVO getVideoYouKuDetail(String url, Integer type, String preCoverImageUrl) throws Exception{
        if(type == 1){// 电视剧
            return getVideoYouKuDianShiJuDetail(url,type,preCoverImageUrl);
        }else if(type == 4){// 动漫
            return getVideoYouKuDongManDetail(url,type,preCoverImageUrl);
        }else {
            return new VideoEpisodeAnalysisVO();
        }
    }

    /**
     * 电视剧剧集解析
     */
    private static VideoEpisodeAnalysisVO getVideoYouKuDianShiJuDetail(String url,Integer type,String preCoverImageUrl) throws Exception{
        Document document = Jsoup.connect(url).get();
        VideoEpisodeAnalysisVO videoEpisodeAnalysisVO = new VideoEpisodeAnalysisVO();
        // 图片
        videoEpisodeAnalysisVO.setCoverImageUrl(preCoverImageUrl);
        // 明星信息
        Element starCode = document.getElementById("module_basic_star-code");
        if(starCode!=null){
            Elements head_tab = starCode.getElementsByClass("head_tab");
            if(head_tab!=null && head_tab.size()>0){
                Element element = head_tab.get(0);
                Elements nameInfo = element.getElementsByClass("name");
                if(nameInfo!=null || nameInfo.size()>0){
                    StringBuilder actors = new StringBuilder("演员：");
                    for (Element name : nameInfo) {
                        actors.append(name.text()+"  ");
                    }
                    videoEpisodeAnalysisVO.setActors(actors.toString());
                }else {
                    videoEpisodeAnalysisVO.setActors("演员：暂无");
                }
            }
        }else {
            videoEpisodeAnalysisVO.setActors("演员：暂无");
        }
        // 详细信息
        Element leftTitleCode = document.getElementById("bpmodule-playpage-lefttitle-code");
        if(leftTitleCode!=null){
            Elements h1 = leftTitleCode.getElementsByTag("h1");
            if(h1!=null && h1.size()>0){// 名称
                Element element = h1.get(0);
                Elements a = element.getElementsByTag("a");
                if(a!=null && a.size()>0){
                    videoEpisodeAnalysisVO.setTitleName(a.get(0).text());
                }
            }
            Elements desc = leftTitleCode.getElementsByClass("desc");
            if(desc!=null && desc.size()>0){
                Element element = desc.get(0);
                Elements elementsByClass = element.getElementsByClass("video-status");
                if(elementsByClass!=null && elementsByClass.size()>0){// 剧集信息
                    videoEpisodeAnalysisVO.setEpisodeInfo(elementsByClass.get(0).text());
                }
                Elements elementsByClass1 = element.getElementsByClass("v-tag");
                String videoTypeInfo = "类型：";
                if(elementsByClass1!=null && elementsByClass1.size()>0){
                    for (Element element1 : elementsByClass1) {
                        videoTypeInfo = videoTypeInfo+element1.text()+"  ";
                    }
                    videoEpisodeAnalysisVO.setVideoTypeInfo(videoTypeInfo);
                }else {
                    videoEpisodeAnalysisVO.setVideoTypeInfo(videoTypeInfo+"暂无");
                }
            }
        }
        // 简介
        Element matetitle = document.getElementById("bpmodule-playpage-matetitle-code");
        if(matetitle!=null){
            Elements elementsByAttributeValue = matetitle.getElementsByAttributeValue("name", "description");
            if(elementsByAttributeValue!=null && elementsByAttributeValue.size()>0){
                videoEpisodeAnalysisVO.setIntroduction(elementsByAttributeValue.get(0).attr("content"));
            }
        }
        // 获取showid
        Element elementById1 = document.getElementById("pageconfig-code");
        String showid = "";
        if(elementById1!=null){
            String s = elementById1.toString();
            if(s.contains("showid:")){
                String[] split = s.split("showid: '",2);
                if(split.length>=2){
                    showid = split[1].split("'",2)[0];
                }
            }
        }
        // 处理剧集信息
        Integer page = 1;
        String str = "https://v.youku.com/page/playlist?showid="+showid+"&isSimple=false&page=";
        VideoYouKuEpisodeAnalysisTotalData videoYouKuEpisodeAnalysisTotalData = JsonUtils.json2Object(sendGetMsg(str+page), VideoYouKuEpisodeAnalysisTotalData.class);
        if(DataProcessingTool.isNotEmpty(videoYouKuEpisodeAnalysisTotalData.getHtml())){
            List<VideoEpisodePlayInfoVO> videoEpisodePlayInfoVOS = new ArrayList<>();
            handleVideoEpisodePlayInfoVO(videoYouKuEpisodeAnalysisTotalData.getHtml(),videoEpisodePlayInfoVOS,str,page);
            videoEpisodeAnalysisVO.setVideoEpisodePlayInfoVOS(videoEpisodePlayInfoVOS);
        }else {
            // 剧集信息
            Element elementById = document.getElementById("bpmodule-playpage-anthology-code");
            if(elementById!=null){
                Elements sn = elementById.getElementsByClass("sn");
                List<VideoEpisodePlayInfoVO> videoEpisodePlayInfoVOS = new ArrayList<>();
                VideoEpisodePlayInfoVO videoEpisodePlayInfoVO = null;
                for (Element element : sn) {
                    videoEpisodePlayInfoVO = new VideoEpisodePlayInfoVO();
                    videoEpisodePlayInfoVO.setVideoUrl(VIDEOYOUKU_ANALYSIS+"https:"+element.attr("href"));
                    Elements elementsByClass = element.getElementsByClass("label-icon label-preview ");
                    if(elementsByClass==null||elementsByClass.size()==0){
                        videoEpisodePlayInfoVO.setEpisode(element.text());
                    }else {
                        videoEpisodePlayInfoVO.setEpisode("预"+element.text());
                    }
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
    private static VideoEpisodeAnalysisVO getVideoYouKuDongManDetail(String url,Integer type,String preCoverImageUrl) throws Exception{
        Document document = Jsoup.connect(url).get();
        VideoEpisodeAnalysisVO videoEpisodeAnalysisVO = new VideoEpisodeAnalysisVO();
        // 图片
        videoEpisodeAnalysisVO.setCoverImageUrl(preCoverImageUrl);
        // 明星信息
        Element starCode = document.getElementById("module_basic_star-code");
        if(starCode!=null){
            Elements head_tab = starCode.getElementsByClass("head_tab");
            if(head_tab!=null && head_tab.size()>0){
                Element element = head_tab.get(0);
                Elements nameInfo = element.getElementsByClass("name");
                if(nameInfo!=null || nameInfo.size()>0){
                    StringBuilder actors = new StringBuilder("演员：");
                    for (Element name : nameInfo) {
                        actors.append(name.text()+"  ");
                    }
                    videoEpisodeAnalysisVO.setActors(actors.toString());
                }else {
                    videoEpisodeAnalysisVO.setActors("演员：暂无");
                }
            }
        }else {
            videoEpisodeAnalysisVO.setActors("演员：暂无");
        }
        // 详细信息
        Element leftTitleCode = document.getElementById("bpmodule-playpage-lefttitle-code");
        if(leftTitleCode!=null){
            Elements h1 = leftTitleCode.getElementsByTag("h1");
            if(h1!=null && h1.size()>0){// 名称
                Element element = h1.get(0);
                Elements a = element.getElementsByTag("a");
                if(a!=null && a.size()>0){
                    videoEpisodeAnalysisVO.setTitleName(a.get(0).text());
                }
            }
            Elements desc = leftTitleCode.getElementsByClass("desc");
            if(desc!=null && desc.size()>0){
                Element element = desc.get(0);
                Elements elementsByClass = element.getElementsByClass("video-status");
                if(elementsByClass!=null && elementsByClass.size()>0){// 剧集信息
                    videoEpisodeAnalysisVO.setEpisodeInfo(elementsByClass.get(0).text());
                }
                Elements elementsByClass1 = element.getElementsByClass("v-tag");
                String videoTypeInfo = "类型：";
                if(elementsByClass1!=null && elementsByClass1.size()>0){
                    for (Element element1 : elementsByClass1) {
                        videoTypeInfo = videoTypeInfo+element1.text()+"  ";
                    }
                    videoEpisodeAnalysisVO.setVideoTypeInfo(videoTypeInfo);
                }else {
                    videoEpisodeAnalysisVO.setVideoTypeInfo(videoTypeInfo+"暂无");
                }
            }
        }
        // 简介
        Element matetitle = document.getElementById("bpmodule-playpage-matetitle-code");
        if(matetitle!=null){
            Elements elementsByAttributeValue = matetitle.getElementsByAttributeValue("name", "description");
            if(elementsByAttributeValue!=null && elementsByAttributeValue.size()>0){
                videoEpisodeAnalysisVO.setIntroduction(elementsByAttributeValue.get(0).attr("content"));
            }
        }
        // 获取showid
        Element elementById1 = document.getElementById("pageconfig-code");
        String showid = "";
        if(elementById1!=null){
            String s = elementById1.toString();
            if(s.contains("showid:")){
                String[] split = s.split("showid: '",2);
                if(split.length>=2){
                    showid = split[1].split("'",2)[0];
                }
            }
        }
        // 处理剧集信息
        Integer page = 1;
        String str = "https://v.youku.com/page/playlist?showid="+showid+"&isSimple=false&page=";
        VideoYouKuEpisodeAnalysisTotalData videoYouKuEpisodeAnalysisTotalData = JsonUtils.json2Object(sendGetMsg(str+page), VideoYouKuEpisodeAnalysisTotalData.class);
        if(DataProcessingTool.isNotEmpty(videoYouKuEpisodeAnalysisTotalData.getHtml())){
            List<VideoEpisodePlayInfoVO> videoEpisodePlayInfoVOS = new ArrayList<>();
            handleVideoEpisodePlayInfoVO(videoYouKuEpisodeAnalysisTotalData.getHtml(),videoEpisodePlayInfoVOS,str,page);
            videoEpisodeAnalysisVO.setVideoEpisodePlayInfoVOS(videoEpisodePlayInfoVOS);
        }else {
            // 剧集信息
            Element elementById = document.getElementById("bpmodule-playpage-anthology-code");
            if(elementById!=null){
                Elements sn = elementById.getElementsByClass("sn");
                List<VideoEpisodePlayInfoVO> videoEpisodePlayInfoVOS = new ArrayList<>();
                VideoEpisodePlayInfoVO videoEpisodePlayInfoVO = null;
                for (Element element : sn) {
                    videoEpisodePlayInfoVO = new VideoEpisodePlayInfoVO();
                    videoEpisodePlayInfoVO.setVideoUrl(VIDEOYOUKU_ANALYSIS+"https:"+element.attr("href"));
                    Elements elementsByClass = element.getElementsByClass("label-icon label-preview ");
                    if(elementsByClass==null||elementsByClass.size()==0){
                        videoEpisodePlayInfoVO.setEpisode(element.text());
                    }else {
                        videoEpisodePlayInfoVO.setEpisode("预"+element.text());
                    }
                    videoEpisodePlayInfoVOS.add(videoEpisodePlayInfoVO);
                }
                videoEpisodeAnalysisVO.setVideoEpisodePlayInfoVOS(videoEpisodePlayInfoVOS);
            }
        }
        System.out.println(videoEpisodeAnalysisVO);
        return videoEpisodeAnalysisVO;
    }

    /**
     * 递归处理数据
     */
    private static void handleVideoEpisodePlayInfoVO(String html, List<VideoEpisodePlayInfoVO> videoEpisodePlayInfoVOS,String str,Integer page) throws Exception{
        Document doc = Jsoup.parse(html);
        Elements elementsByClass = doc.getElementsByClass("item item-cover");
        VideoEpisodePlayInfoVO videoEpisodePlayInfoVO = null;
        if(elementsByClass!=null){
            for (Element byClass : elementsByClass) {
                videoEpisodePlayInfoVO = new VideoEpisodePlayInfoVO();
                Elements elementsByClass1 = byClass.getElementsByClass("label-icon label-preview ");
                if(elementsByClass1==null||elementsByClass1.size()==0){// 正片
                    videoEpisodePlayInfoVO.setEpisode(byClass.attr("seq"));
                    Elements sn = byClass.getElementsByClass("sn");
                    if(sn!=null&&sn.size()>0){
                        videoEpisodePlayInfoVO.setVideoUrl(VIDEOYOUKU_ANALYSIS+"https:"+sn.get(0).attr("href"));
                    }
                }else {// 预告片
                    videoEpisodePlayInfoVO.setEpisode("预"+byClass.attr("seq"));
                    Elements sn = byClass.getElementsByClass("sn");
                    if(sn!=null&&sn.size()>0){
                        videoEpisodePlayInfoVO.setVideoUrl(VIDEOYOUKU_ANALYSIS+"https:"+sn.get(0).attr("href"));
                    }
                }
                videoEpisodePlayInfoVOS.add(videoEpisodePlayInfoVO);
            }
            if(elementsByClass.size()>=50&&page<40){// 继续 优酷默认50条 “page<40”防止无限循环
                page++;
                VideoYouKuEpisodeAnalysisTotalData videoYouKuEpisodeAnalysisTotalData = JsonUtils.json2Object(sendGetMsg(str+page), VideoYouKuEpisodeAnalysisTotalData.class);
                if(DataProcessingTool.isNotEmpty(videoYouKuEpisodeAnalysisTotalData.getHtml())){
                    handleVideoEpisodePlayInfoVO(videoYouKuEpisodeAnalysisTotalData.getHtml(),videoEpisodePlayInfoVOS,str,page);
                }
            }
        }
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
class VideoYouKuEpisodeAnalysisTotalData{

    private Integer error;

    private String message;

    private String html;// html
}
