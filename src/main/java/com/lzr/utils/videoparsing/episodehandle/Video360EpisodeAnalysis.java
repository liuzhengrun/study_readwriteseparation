package com.lzr.utils.videoparsing.episodehandle;

import com.lzr.vo.toolmodule.VideoEpisodeAnalysisVO;
import com.lzr.vo.toolmodule.VideoEpisodePlayInfoVO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 360剧集解析
 * @author lzr
 * @date 2019/9/20 0020 16:00
 */
public class Video360EpisodeAnalysis {

    private static final String VIDEO360_ANALYSIS = "http://beaacc.com/api.php?url=";

    public static void main(String[] args) throws Exception{
        getVideo360Detail("https://www.360kan.com/ct/QU8qap7lLYGxCz.html",4,"");
    }

    /**
     * 获取信息
     * @param url
     * @param type
     * @return
     */
    public static VideoEpisodeAnalysisVO getVideo360Detail(String url,Integer type,String preCoverImageUrl) throws Exception{
        if(type == 1){// 电视剧
            return getVideo360DianShiJuDetail(url,type,preCoverImageUrl);
        }else if(type == 4){// 动漫
            return getVideo360DongManDetail(url,type,preCoverImageUrl);
        }else {
            return new VideoEpisodeAnalysisVO();
        }

    }

    /**
     * 电视剧剧集解析
     */
    private static VideoEpisodeAnalysisVO getVideo360DianShiJuDetail(String url,Integer type,String preCoverImageUrl) throws Exception{
        Document document = Jsoup.connect(url).get();
        VideoEpisodeAnalysisVO videoEpisodeAnalysisVO = new VideoEpisodeAnalysisVO();
        // 获取图片
        Elements imageElements = document.getElementsByClass("s-top-left");
        if(imageElements!=null && imageElements.size()>0){
            Element element = imageElements.get(0);
            Elements src = element.getElementsByAttributeStarting("src");
            if(src!=null && src.size()>0){
                videoEpisodeAnalysisVO.setCoverImageUrl(src.get(0).attr("src"));
            }else {
                videoEpisodeAnalysisVO.setCoverImageUrl(preCoverImageUrl);
            }
        }else {
            videoEpisodeAnalysisVO.setCoverImageUrl(preCoverImageUrl);
        }
        // 获取详细信息
        Elements detailsInfo = document.getElementsByClass("s-top-info");
        if(detailsInfo!=null && detailsInfo.size()>0){
            Element element = detailsInfo.get(0);
            Elements h1s = element.getElementsByTag("h1");
            if (h1s!=null && h1s.size()>0){// 题目
                videoEpisodeAnalysisVO.setTitleName(h1s.get(0).text());
            }
            Elements tag = element.getElementsByClass("tag");
            if (tag!=null && tag.size()>0){// 剧集信息
                videoEpisodeAnalysisVO.setEpisodeInfo(tag.get(0).text());
            }
            Elements elementsByClass = element.getElementsByClass("g-clear item-wrap");
            if (elementsByClass!=null && elementsByClass.size()>0){
                Element element1 = elementsByClass.get(0);
                Elements items = element1.getElementsByClass("item");
                if(items!=null){
                    for (int i=0;i<items.size();i++){
                        if (i==0){// 视频类型
                            videoEpisodeAnalysisVO.setVideoTypeInfo(items.get(i).getElementsByTag("p").text());
                        }else if(i==1){// 年代
                            videoEpisodeAnalysisVO.setYear(items.get(i).getElementsByTag("p").text());
                        }else if(i==2){// 地区
                            videoEpisodeAnalysisVO.setArea(items.get(i).getElementsByTag("p").text());
                        }else if(i==3){// 导演
                            videoEpisodeAnalysisVO.setDirector(items.get(i).getElementsByTag("p").text());
                        }else if(i==4){// 演员
                            videoEpisodeAnalysisVO.setActors(items.get(i).getElementsByTag("p").text());
                        }else {
                            break;
                        }
                    }
                }

            }
            Elements elementsByClass1 = element.getElementsByClass("item-desc");
            if (elementsByClass1!=null && elementsByClass1.size()>0){// 简介
                videoEpisodeAnalysisVO.setIntroduction("简介："+elementsByClass1.get(0).text());
            }

        }
        // 剧集信息
        Elements elementsByClass = document.getElementsByClass("num-tab-main g-clear js-tab");
        List<VideoEpisodePlayInfoVO> videoEpisodePlayInfoVOS = new ArrayList<>();
        if (elementsByClass!=null&&elementsByClass.size()>0){
            Elements elementsByAttributeStarting = elementsByClass.get(0).getElementsByAttributeStarting("data-num");
            VideoEpisodePlayInfoVO videoEpisodePlayInfoVO = null;
            for (Element element : elementsByAttributeStarting) {
                videoEpisodePlayInfoVO = new VideoEpisodePlayInfoVO();
                String episode = element.attr("data-num");
                String videoUrl = VIDEO360_ANALYSIS+element.attr("href");
                videoEpisodePlayInfoVO.setEpisode(episode);
                videoEpisodePlayInfoVO.setVideoUrl(videoUrl);
                videoEpisodePlayInfoVOS.add(videoEpisodePlayInfoVO);
            }
        }
        videoEpisodeAnalysisVO.setVideoEpisodePlayInfoVOS(videoEpisodePlayInfoVOS);
        return videoEpisodeAnalysisVO;
    }

    /**
     * 动漫剧集解析
     */
    private static VideoEpisodeAnalysisVO getVideo360DongManDetail(String url,Integer type,String preCoverImageUrl) throws Exception{
        Document document = Jsoup.connect(url).get();
        System.out.println(document.toString());
        VideoEpisodeAnalysisVO videoEpisodeAnalysisVO = new VideoEpisodeAnalysisVO();
        // 获取图片
        Elements imageElements = document.getElementsByClass("m-top-left");
        if(imageElements!=null && imageElements.size()>0){
            Element element = imageElements.get(0);
            Elements src = element.getElementsByAttributeStarting("src");
            if(src!=null && src.size()>0){
                videoEpisodeAnalysisVO.setCoverImageUrl(src.get(0).attr("src"));
            }else {
                videoEpisodeAnalysisVO.setCoverImageUrl(preCoverImageUrl);
            }
        }else {
            videoEpisodeAnalysisVO.setCoverImageUrl(preCoverImageUrl);
        }
        // 获取详细信息
        Elements detailsInfo = document.getElementsByClass("m-top-info");
        if(detailsInfo!=null && detailsInfo.size()>0){
            Element element = detailsInfo.get(0);
            Elements h1s = element.getElementsByTag("h1");
            if (h1s!=null && h1s.size()>0){// 题目
                videoEpisodeAnalysisVO.setTitleName(h1s.get(0).text());
            }
            Elements tag = element.getElementsByClass("tag");
            if (tag!=null && tag.size()>0){// 剧集信息
                videoEpisodeAnalysisVO.setEpisodeInfo(tag.get(0).text());
            }
            Elements elementsByClass = element.getElementsByClass("g-clear item-wrap");
            if (elementsByClass!=null && elementsByClass.size()>0){
                Element element1 = elementsByClass.get(0);
                Elements items = element1.getElementsByClass("item");
                if(items!=null){
                    for (int i=0;i<items.size();i++){
                        if (i==0){// 视频类型
                            videoEpisodeAnalysisVO.setVideoTypeInfo(items.get(i).getElementsByTag("p").text());
                        }else if(i==1){// 年代
                            videoEpisodeAnalysisVO.setYear(items.get(i).getElementsByTag("p").text());
                        }else if(i==2){// 地区
                            videoEpisodeAnalysisVO.setArea(items.get(i).getElementsByTag("p").text());
                        }else if(i==3){// 导演
                            videoEpisodeAnalysisVO.setDirector(items.get(i).getElementsByTag("p").text());
                        }else if(i==4){// 演员
                            videoEpisodeAnalysisVO.setActors(items.get(i).getElementsByTag("p").text());
                        }else {
                            break;
                        }
                    }
                }
            }
            Elements elementsByClass1 = element.getElementsByClass("item-desc");
            if (elementsByClass1!=null && elementsByClass1.size()>0){// 简介
                videoEpisodeAnalysisVO.setIntroduction("简介："+elementsByClass1.get(0).text());
            }

        }
        // 剧集信息
        Elements elementsByClass = document.getElementsByClass("js-series-all m-series-number-container");
        System.out.println(elementsByClass);
        List<VideoEpisodePlayInfoVO> videoEpisodePlayInfoVOS = new ArrayList<>();
        if (elementsByClass!=null&&elementsByClass.size()>0){
            Elements elementsByAttributeStarting = elementsByClass.get(0).getElementsByAttributeStarting("data-num");
            VideoEpisodePlayInfoVO videoEpisodePlayInfoVO = null;
            for (Element element : elementsByAttributeStarting) {
                videoEpisodePlayInfoVO = new VideoEpisodePlayInfoVO();
                String episode = element.attr("data-num");
                String videoUrl = VIDEO360_ANALYSIS+element.attr("href");
                videoEpisodePlayInfoVO.setEpisode(episode);
                videoEpisodePlayInfoVO.setVideoUrl(videoUrl);
                videoEpisodePlayInfoVOS.add(videoEpisodePlayInfoVO);
            }
        }else {
            elementsByClass = document.getElementsByClass("m-series-number-container g-clear");
            if (elementsByClass!=null&&elementsByClass.size()>0){
                Elements elementsByAttributeStarting = elementsByClass.get(0).getElementsByAttributeStarting("data-num");
                VideoEpisodePlayInfoVO videoEpisodePlayInfoVO = null;
                for (Element element : elementsByAttributeStarting) {
                    videoEpisodePlayInfoVO = new VideoEpisodePlayInfoVO();
                    String episode = element.attr("data-num");
                    String videoUrl = VIDEO360_ANALYSIS+element.attr("href");
                    videoEpisodePlayInfoVO.setEpisode(episode);
                    videoEpisodePlayInfoVO.setVideoUrl(videoUrl);
                    videoEpisodePlayInfoVOS.add(videoEpisodePlayInfoVO);
                }
            }
        }
        videoEpisodeAnalysisVO.setVideoEpisodePlayInfoVOS(videoEpisodePlayInfoVOS);
        return videoEpisodeAnalysisVO;
    }

}
