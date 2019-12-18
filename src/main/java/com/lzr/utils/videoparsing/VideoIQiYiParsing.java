package com.lzr.utils.videoparsing;

import com.lzr.vo.toolmodule.VideoInfoVO;
import com.lzr.utils.JsonUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * 爬取爱奇艺影视
 * @author lzr
 * @date 2019/9/17 0017 18:03
 */
public class VideoIQiYiParsing {

    /**
     * 爱奇艺解析路径
     */
    private static final String IQIYI_ANALYSIS = "http://jx.598110.com/?url=";

    /**
     * 图片缓存解析地址
     */
    private static final String IQIYI_IMAGE_ANALYSIS="https://images.weserv.nl/?url=";

    public static void main(String[] args) throws Exception{
        videoIQiYiInfoVOPage("2",1);
    }

    /**
     * 获取爱奇艺视频数据 (1：电视剧 2：电影 3：综艺 4：动漫)
     * @param type
     * @param page
     * @return
     */
    public static List<VideoInfoVO> videoIQiYiInfoVOPage(String type,Integer page)throws Exception{
        if ("1".equals(type)){// 电视剧
            type = "2";
            String main = "https://www.iqiyi.com/";
            String url = "https://list.iqiyi.com/www/" + type + "/-------------24-"+page+"-1-iqiyi--.html";
            return getDianShiJu(main,url);
        }else if("2".equals(type)){// 电影
            type = "1";
            String main = "https://www.iqiyi.com/";
            String url = "https://list.iqiyi.com/www/" + type + "/-------------24-"+page+"-1-iqiyi--.html";
            return getDianYing(main,url);
        }else if("3".equals(type)){// 综艺
            type = "6";
            String main = "https://www.iqiyi.com/";
            String url = "https://list.iqiyi.com/www/" + type + "/-------------24-"+page+"-1-iqiyi--.html";
            return getZongYi(main,url);
        }else if("4".equals(type)){// 动漫
            type = "4";
            String main = "https://www.iqiyi.com/";
            String url = "https://list.iqiyi.com/www/" + type + "/-------------24-"+page+"-1-iqiyi--.html";
            return getDongMan(main,url);
        }
        return new ArrayList<>();
    }

    /**
     * 获取电视剧信息
     */
    private static List<VideoInfoVO> getDianShiJu(String main,String url)throws Exception{
        Document doc = Jsoup.connect(url).get();
        Element elementById = doc.getElementById("block-D");
        String s1 = elementById.toString().split("\">")[0];
        String substring = s1.substring(s1.indexOf("first-search-list=\"") + 19);
        String data = substring.replaceAll("&quot;", "\"");
        // 将data转化成类
        IQiYiDianYingTotalData iQiYiDianYingTotalData = JsonUtils.json2Object(data, IQiYiDianYingTotalData.class);
        List<VideoInfoVO> videoInfoVOS = new ArrayList<>();
        VideoInfoVO videoInfoVO = null;
        for (IQiYiDianYingListData iQiYiDianYingListData : iQiYiDianYingTotalData.getList()) {
            videoInfoVO = new VideoInfoVO();
            if(iQiYiDianYingListData.getLatestOrder()!=null&&iQiYiDianYingListData.getVideoCount()!=null){
                videoInfoVO.setYear(iQiYiDianYingListData.getLatestOrder()<iQiYiDianYingListData.getVideoCount()?"更新至"+iQiYiDianYingListData.getLatestOrder()+"集":iQiYiDianYingListData.getLatestOrder()+"集完");
            }else {
                if(iQiYiDianYingListData.getLatestOrder()!=null){
                    videoInfoVO.setYear("更新至"+iQiYiDianYingListData.getLatestOrder()+"集");
                }
            }
            videoInfoVO.setScore("评分："+(iQiYiDianYingListData.getScore()==null?"":iQiYiDianYingListData.getScore()));
            videoInfoVO.setCoverImageUrl(IQIYI_IMAGE_ANALYSIS+iQiYiDianYingListData.getImageUrl());
            videoInfoVO.setTitleName("片名："+iQiYiDianYingListData.getName());
            videoInfoVO.setStarring(iQiYiDianYingListData.getSecondInfo());
            videoInfoVO.setVideoUrl(iQiYiDianYingListData.getPlayUrl());
            videoInfoVOS.add(videoInfoVO);
        }
        return videoInfoVOS;
    }

    /**
     * 获取电影信息
     */
    private static List<VideoInfoVO> getDianYing(String main,String url)throws Exception{
        Document doc = Jsoup.connect(url).get();
        Element elementById = doc.getElementById("block-D");
        String s1 = elementById.toString().split("\">")[0];
        String substring = s1.substring(s1.indexOf("first-search-list=\"") + 19);
        String data = substring.replaceAll("&quot;", "\"");
        // 将data转化成类
        IQiYiDianYingTotalData iQiYiDianYingTotalData = JsonUtils.json2Object(data, IQiYiDianYingTotalData.class);
        List<VideoInfoVO> videoInfoVOS = new ArrayList<>();
        VideoInfoVO videoInfoVO = null;
        for (IQiYiDianYingListData iQiYiDianYingListData : iQiYiDianYingTotalData.getList()) {
            videoInfoVO = new VideoInfoVO();
            videoInfoVO.setYear("上映时间："+iQiYiDianYingListData.getFormatPeriod());
            videoInfoVO.setScore("评分："+(iQiYiDianYingListData.getScore()==null?"":iQiYiDianYingListData.getScore()));
            videoInfoVO.setCoverImageUrl(IQIYI_IMAGE_ANALYSIS+iQiYiDianYingListData.getImageUrl());
            videoInfoVO.setTitleName("片名："+iQiYiDianYingListData.getName());
            videoInfoVO.setStarring(iQiYiDianYingListData.getSecondInfo());
            videoInfoVO.setVideoUrl(IQIYI_ANALYSIS+iQiYiDianYingListData.getPlayUrl());
            videoInfoVOS.add(videoInfoVO);
        }
        return videoInfoVOS;
    }

    /**
     * 获取综艺信息
     */
    private static List<VideoInfoVO> getZongYi(String main,String url)throws Exception{
        Document doc = Jsoup.connect(url).get();
        Element elementById = doc.getElementById("block-D");
        String s1 = elementById.toString().split("\">")[0];
        String substring = s1.substring(s1.indexOf("first-search-list=\"") + 19);
        String data = substring.replaceAll("&quot;", "\"");
        // 将data转化成类
        IQiYiDianYingTotalData iQiYiDianYingTotalData = JsonUtils.json2Object(data, IQiYiDianYingTotalData.class);
        List<VideoInfoVO> videoInfoVOS = new ArrayList<>();
        VideoInfoVO videoInfoVO = null;
        for (IQiYiDianYingListData iQiYiDianYingListData : iQiYiDianYingTotalData.getList()) {
            videoInfoVO = new VideoInfoVO();
            videoInfoVO.setYear("上映时间："+iQiYiDianYingListData.getFormatPeriod());
            videoInfoVO.setScore("评分："+(iQiYiDianYingListData.getScore()==null?"":iQiYiDianYingListData.getScore()));
            videoInfoVO.setCoverImageUrl(IQIYI_IMAGE_ANALYSIS+iQiYiDianYingListData.getImageUrl());
            videoInfoVO.setTitleName("片名："+iQiYiDianYingListData.getName());
            videoInfoVO.setStarring("简介："+iQiYiDianYingListData.getSecondInfo());
            videoInfoVO.setVideoUrl(IQIYI_ANALYSIS+iQiYiDianYingListData.getPlayUrl());
            videoInfoVOS.add(videoInfoVO);
        }
        return videoInfoVOS;
    }

    /**
     * 获取动漫信息
     */
    private static List<VideoInfoVO> getDongMan(String main,String url)throws Exception{
        Document doc = Jsoup.connect(url).get();
        Element elementById = doc.getElementById("block-D");
        String s1 = elementById.toString().split("\">")[0];
        String substring = s1.substring(s1.indexOf("first-search-list=\"") + 19);
        String data = substring.replaceAll("&quot;", "\"");
        // 将data转化成类
        IQiYiDianYingTotalData iQiYiDianYingTotalData = JsonUtils.json2Object(data, IQiYiDianYingTotalData.class);
        List<VideoInfoVO> videoInfoVOS = new ArrayList<>();
        VideoInfoVO videoInfoVO = null;
        if(iQiYiDianYingTotalData.getList()!=null){
            for (IQiYiDianYingListData iQiYiDianYingListData : iQiYiDianYingTotalData.getList()) {
                videoInfoVO = new VideoInfoVO();
                if(iQiYiDianYingListData.getLatestOrder()!=null&&iQiYiDianYingListData.getVideoCount()!=null){
                    videoInfoVO.setYear(iQiYiDianYingListData.getLatestOrder()<iQiYiDianYingListData.getVideoCount()?"更新至"+iQiYiDianYingListData.getLatestOrder()+"集":iQiYiDianYingListData.getLatestOrder()+"集完");
                }else {
                    if(iQiYiDianYingListData.getLatestOrder()!=null){
                        videoInfoVO.setYear("更新至"+iQiYiDianYingListData.getLatestOrder()+"集");
                    }
                }
                videoInfoVO.setScore("评分："+(iQiYiDianYingListData.getScore()==null?"":iQiYiDianYingListData.getScore()));
                videoInfoVO.setCoverImageUrl(IQIYI_IMAGE_ANALYSIS+iQiYiDianYingListData.getImageUrl());
                videoInfoVO.setTitleName("片名："+iQiYiDianYingListData.getName());
                videoInfoVO.setStarring(iQiYiDianYingListData.getSecondInfo());
                videoInfoVO.setVideoUrl(iQiYiDianYingListData.getPlayUrl());
                videoInfoVOS.add(videoInfoVO);
            }
        }
        return videoInfoVOS;
    }

}

// 爱奇艺电影总数据类
@Data
@JsonIgnoreProperties(ignoreUnknown = true) //Jackson解析JSON数据时，忽略未知的字段。
class IQiYiDianYingTotalData{

    private Integer total;

    private String event_id;

    private Integer pageTotal;

    private String bkt;

    private Integer result_num;

    private List<IQiYiDianYingListData> list;

    private Integer search_time;


}

// 爱奇艺电影list数据类
@Data
@JsonIgnoreProperties(ignoreUnknown = true) //Jackson解析JSON数据时，忽略未知的字段。
class IQiYiDianYingListData{
    private Long issueTime;

    private String docId;

    private String payMarkUrl;

    private String description;

    private Boolean qiyiProduced;

    private String focus;

    private String tvId;

    private String formatPeriod;// 上映时间

    private String playUrl;// 播放视频地址

    private String duration;

    private Integer videoCount;// 总集数

    private String videoInfoType;

    private Double score;

    private Integer latestOrder;// 当前集数

    private String imageUrl;// 图片路径

    private String name;// 电影名称

    private Integer payMark;

    private Boolean exclusive;

    private String siteId;

    private Boolean is1080p;

    private String secondInfo;// 主演

    private Integer channelId;

}
