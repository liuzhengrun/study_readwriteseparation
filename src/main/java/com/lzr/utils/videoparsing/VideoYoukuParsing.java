package com.lzr.utils.videoparsing;

import com.lzr.vo.toolmodule.VideoInfoVO;
import com.lzr.utils.JsonUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 爬取优酷视频
 * @author lzr
 * @date 2019/9/18 0018 16:27
 */
public class VideoYoukuParsing {

    private static final String YOUKU_ANALYSIS = "http://beaacc.com/api.php?url=";

    public static void main(String[] args) throws Exception{
        videoYouKuInfoVOPage("2",1);
    }

    public static List<VideoInfoVO> videoYouKuInfoVOPage(String type,Integer page)throws Exception{
        if ("1".equals(type)){// 电视剧
            type = "97";
            String main = "https://www.youku.com/";
            String url = "https://list.youku.com/category/page?c="+type+"&type=show&p="+page;
            return getDianShiJu(main,url);
        }else if("2".equals(type)){// 电影
            type = "96";
            String main = "https://www.youku.com/";
            String url = "https://list.youku.com/category/page?c="+type+"&type=show&p="+page;
            return getDianYing(main,url);
        }else if("3".equals(type)){// 综艺
            type = "85";
            String main = "https://www.youku.com/";
            String url = "https://list.youku.com/category/page?c="+type+"&type=show&p="+page;
            return getZongYi(main,url);
        }else if("4".equals(type)){// 动漫
            type = "100";
            String main = "https://www.youku.com/";
            String url = "https://list.youku.com/category/page?c="+type+"&type=show&p="+page;
            return getDongMan(main,url);
        }
        return new ArrayList<>();
    }

    /**
     * 获取电视剧信息
     */
    private static List<VideoInfoVO> getDianShiJu(String main,String url)throws Exception{
        String jsonData = sendGetMsg(url);
        // 将data转化成类
        YoukuDianYingTotalData youkuDianYingTotalData = JsonUtils.json2Object(jsonData, YoukuDianYingTotalData.class);
        List<VideoInfoVO> videoInfoVOS = new ArrayList<>();
        VideoInfoVO videoInfoVO = null;
        for (YoukuDianYingData youkuDianYingData : youkuDianYingTotalData.getData()) {
            videoInfoVO = new VideoInfoVO();
            videoInfoVO.setYear(youkuDianYingData.getSummary());
            videoInfoVO.setScore("评分：暂无");
            videoInfoVO.setCoverImageUrl("https:"+youkuDianYingData.getImg());
            videoInfoVO.setTitleName("片名："+youkuDianYingData.getTitle());
            videoInfoVO.setStarring("描述："+youkuDianYingData.getSubTitle());
            videoInfoVO.setVideoUrl("https:"+youkuDianYingData.getVideoLink());
            videoInfoVOS.add(videoInfoVO);
        }
        return videoInfoVOS;
    }

    /**
     * 获取优酷电影数据
     * @param main
     * @param url
     * @return
     */
    private static List<VideoInfoVO> getDianYing(String main,String url)throws Exception{
        String jsonData = sendGetMsg(url);
        // 将data转化成类
        YoukuDianYingTotalData youkuDianYingTotalData = JsonUtils.json2Object(jsonData, YoukuDianYingTotalData.class);
        List<VideoInfoVO> videoInfoVOS = new ArrayList<>();
        VideoInfoVO videoInfoVO = null;
        for (YoukuDianYingData youkuDianYingData : youkuDianYingTotalData.getData()) {
            videoInfoVO = new VideoInfoVO();
            videoInfoVO.setYear("类型："+youkuDianYingData.getSummary());
            videoInfoVO.setScore("评分：暂无");
            videoInfoVO.setCoverImageUrl("https:"+youkuDianYingData.getImg());
            videoInfoVO.setTitleName("片名："+youkuDianYingData.getTitle());
            videoInfoVO.setStarring("描述："+youkuDianYingData.getSubTitle());
            videoInfoVO.setVideoUrl(YOUKU_ANALYSIS+"https:"+youkuDianYingData.getVideoLink());
            videoInfoVOS.add(videoInfoVO);
        }
        return videoInfoVOS;
    }

    /**
     * 获取综艺信息
     */
    private static List<VideoInfoVO> getZongYi(String main,String url)throws Exception{
        String jsonData = sendGetMsg(url);
        // 将data转化成类
        YoukuDianYingTotalData youkuDianYingTotalData = JsonUtils.json2Object(jsonData, YoukuDianYingTotalData.class);
        List<VideoInfoVO> videoInfoVOS = new ArrayList<>();
        VideoInfoVO videoInfoVO = null;
        for (YoukuDianYingData youkuDianYingData : youkuDianYingTotalData.getData()) {
            videoInfoVO = new VideoInfoVO();
            videoInfoVO.setYear("更新时间："+youkuDianYingData.getSummary());
            videoInfoVO.setScore("评分：暂无");
            videoInfoVO.setCoverImageUrl("https:"+youkuDianYingData.getImg());
            videoInfoVO.setTitleName("片名："+youkuDianYingData.getTitle());
            videoInfoVO.setStarring("描述："+youkuDianYingData.getSubTitle());
            videoInfoVO.setVideoUrl(YOUKU_ANALYSIS+"https:"+youkuDianYingData.getVideoLink());
            videoInfoVOS.add(videoInfoVO);
        }
        return videoInfoVOS;
    }

    /**
     * 获取动漫信息
     */
    private static List<VideoInfoVO> getDongMan(String main,String url)throws Exception{
        String jsonData = sendGetMsg(url);
        // 将data转化成类
        YoukuDianYingTotalData youkuDianYingTotalData = JsonUtils.json2Object(jsonData, YoukuDianYingTotalData.class);
        List<VideoInfoVO> videoInfoVOS = new ArrayList<>();
        VideoInfoVO videoInfoVO = null;
        for (YoukuDianYingData youkuDianYingData : youkuDianYingTotalData.getData()) {
            videoInfoVO = new VideoInfoVO();
            videoInfoVO.setYear(youkuDianYingData.getSummary());
            videoInfoVO.setScore("评分：暂无");
            videoInfoVO.setCoverImageUrl("https:"+youkuDianYingData.getImg());
            videoInfoVO.setTitleName("片名："+youkuDianYingData.getTitle());
            videoInfoVO.setStarring("描述："+youkuDianYingData.getSubTitle());
            videoInfoVO.setVideoUrl("https:"+youkuDianYingData.getVideoLink());
            videoInfoVOS.add(videoInfoVO);
        }
        return videoInfoVOS;
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

// 优酷电影总数据类
@Data
@JsonIgnoreProperties(ignoreUnknown = true) //Jackson解析JSON数据时，忽略未知的字段。
class YoukuDianYingTotalData{

    private Boolean success;

    private String message;

    private List<YoukuDianYingData> data;

    private String code;

    private String jumpUrl;

}

// 优酷电影数据类
@Data
@JsonIgnoreProperties(ignoreUnknown = true) //Jackson解析JSON数据时，忽略未知的字段。
class YoukuDianYingData{

    private String summaryType;

    private String access;

    private String type;

    private String img;// 图片地址

    private String summary;// 摘要(unicode 转化为 utf-8)

    private String title;// 标题(unicode 转化为 utf-8)

    private String subTitle;// 字幕(unicode 转化为 utf-8)

    private String videoId;

    private String videoLink;// 视频地址

}
