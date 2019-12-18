package com.lzr.utils.videoparsing;

import com.lzr.vo.toolmodule.VideoInfoVO;
import com.lzr.utils.DataProcessingTool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 爬取腾讯视频
 * @author lzr
 * @date 2019/9/19 0019 09:32
 */
public class VideoTencentParsing {

    private static final String TENCENT_ANALYSIS = "http://beaacc.com/api.php?url=";

    public static void main(String[] args) throws Exception {
        videoTencentInfoVOPage("1",1);
    }

    public static List<VideoInfoVO> videoTencentInfoVOPage(String type, Integer page) throws Exception{
        if("1".equals(type)){// 电视剧
            type = "tv";
            String main = "https://v.qq.com/";
            String url = "https://v.qq.com/x/bu/pagesheet/list?listpage="+page+"&channel="+type+"&sort=18&_all=1&offset="+((page-1)*30)+"&pagesize=30";// 热播排序
            return getDianShiJu(main,url);
        }else if("2".equals(type)){// 电影
            type = "movie";
            String main = "https://v.qq.com/";
            String url = "https://v.qq.com/x/bu/pagesheet/list?listpage="+page+"&channel="+type+"&sort=18&_all=1&offset="+((page-1)*30)+"&pagesize=30";// 热播排序
            return getDianYing(main,url);
        }else if("3".equals(type)){// 综艺
            type = "variety";
            String main = "https://v.qq.com/";
            String url = "https://v.qq.com/x/bu/pagesheet/list?listpage="+page+"&channel="+type+"&sort=4&_all=1&offset="+((page-1)*30)+"&pagesize=30";// 热播排序
            return getZongYi(main,url);
        }else if("4".equals(type)){
            type = "cartoon";
            String main = "https://v.qq.com/";
            String url = "https://v.qq.com/x/bu/pagesheet/list?listpage="+page+"&channel="+type+"&sort=18&_all=1&offset="+((page-1)*30)+"&pagesize=30";// 热播排序
            return getDongMan(main,url);
        }
        return new ArrayList<>();
    }

    /**
     * 获取电视剧资源
     */
    private static List<VideoInfoVO> getDianShiJu(String main, String url)throws Exception{
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.getElementsByClass("list_item");
        List<VideoInfoVO> videoInfoVOS = new ArrayList<>();
        VideoInfoVO videoInfoVO = null;
        for (Element element : elements) {
            videoInfoVO = new VideoInfoVO();
            String[] s = element.toString().split(">");
            String videoUrl = s[1].substring(s[1].indexOf("href=\"") + 6).split("\"",2)[0];
            String coverImageUrl = s[2].substring(s[2].indexOf("src=\"") + 5).split("\"",2)[0];
            String year = s[4].split("<",2)[0].trim();
            String titleName = s[9].split("<",2)[0].trim();
            if(!DataProcessingTool.isNotEmpty(titleName)){
                titleName = s[8].split("<",2)[0].trim();
            }
            String starring = s[11].split("<",2)[0].trim();
            if(!DataProcessingTool.isNotEmpty(starring)){
                starring = s[10].split("<",2)[0].trim();
            }
            videoInfoVO.setStarring(starring);
            videoInfoVO.setYear(year);
            videoInfoVO.setTitleName("片名："+titleName);
            videoInfoVO.setVideoUrl(videoUrl);
            videoInfoVO.setCoverImageUrl("https:"+coverImageUrl);
            videoInfoVO.setScore("评分：暂无");
            videoInfoVOS.add(videoInfoVO);
        }
        return videoInfoVOS;
    }

    /**
     * 获取腾讯视频电影资源
     */
    private static List<VideoInfoVO> getDianYing(String main, String url) throws Exception{
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.getElementsByClass("list_item");
        List<VideoInfoVO> videoInfoVOS = new ArrayList<>();
        VideoInfoVO videoInfoVO = null;
        for (Element element : elements) {
            videoInfoVO = new VideoInfoVO();
            String[] s = element.toString().split(">");
            String videoUrl = s[1].substring(s[1].indexOf("href=\"") + 6).split("\"",2)[0];
            String coverImageUrl = s[2].substring(s[2].indexOf("src=\"") + 5).split("\"",2)[0];
            String titleName = s[7].split("<",2)[0].trim();
            if (!DataProcessingTool.isNotEmpty(titleName)){
                titleName = s[6].split("<",2)[0].trim();
            }
            String starring = s[9].split("<",2)[0].trim();
            if (!DataProcessingTool.isNotEmpty(starring)){
                starring = s[8].split("<",2)[0].trim();
            }
            String score = s[16].split("<",2)[0].trim();
            if (!DataProcessingTool.isNotEmpty(score)){
                score = s[15].split("<",2)[0].trim();
            }
            videoInfoVO.setStarring(starring);
            videoInfoVO.setYear("年份：暂无");
            videoInfoVO.setTitleName("片名："+titleName);
            videoInfoVO.setVideoUrl(TENCENT_ANALYSIS+videoUrl);
            videoInfoVO.setCoverImageUrl("https:"+coverImageUrl);
            videoInfoVO.setScore("播放量："+score);
            videoInfoVOS.add(videoInfoVO);
        }
        return videoInfoVOS;
    }

    /**
     * 获取综艺资源
     */
    private static List<VideoInfoVO> getZongYi(String main, String url) throws Exception{
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.getElementsByClass("list_item");
        List<VideoInfoVO> videoInfoVOS = new ArrayList<>();
        VideoInfoVO videoInfoVO = null;
        for (Element element : elements) {
            videoInfoVO = new VideoInfoVO();
            String[] s = element.toString().split(">");
            String videoUrl = s[1].substring(s[1].indexOf("href=\"") + 6).split("\"",2)[0];
            String coverImageUrl = s[2].substring(s[2].indexOf("src=\"") + 5).split("\"",2)[0];
            String titleName = s[6].split("<",2)[0].trim();
            if(!DataProcessingTool.isNotEmpty(titleName)){
                titleName = s[7].split("<",2)[0].trim();
            }
            String starring = s[8].split("<",2)[0].trim();
            if(!DataProcessingTool.isNotEmpty(starring)){
                starring = s[9].split("<",2)[0].trim();
            }
            videoInfoVO.setStarring(starring);
            videoInfoVO.setYear("年份：暂无");
            videoInfoVO.setTitleName("片名："+titleName);
            videoInfoVO.setVideoUrl(TENCENT_ANALYSIS+videoUrl);
            videoInfoVO.setCoverImageUrl("https:"+coverImageUrl);
            videoInfoVO.setScore("评分：暂无");
            videoInfoVOS.add(videoInfoVO);
        }
        return videoInfoVOS;
    }

    /**
     * 获取动漫资源
     */
    private static List<VideoInfoVO> getDongMan(String main, String url) throws Exception{
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.getElementsByClass("list_item");
        List<VideoInfoVO> videoInfoVOS = new ArrayList<>();
        VideoInfoVO videoInfoVO = null;
        for (Element element : elements) {
            videoInfoVO = new VideoInfoVO();
            String[] s = element.toString().split(">");
            String videoUrl = s[1].substring(s[1].indexOf("href=\"") + 6).split("\"",2)[0];
            String coverImageUrl = s[2].substring(s[2].indexOf("src=\"") + 5).split("\"",2)[0];
            String year = s[4].split("<",2)[0].trim();
            String titleName = s[9].split("<",2)[0].trim();
            if(!DataProcessingTool.isNotEmpty(titleName)){
                titleName = s[8].split("<",2)[0].trim();
            }
            String starring = s[11].split("<",2)[0].trim();
            if(!DataProcessingTool.isNotEmpty(starring)){
                starring = s[10].split("<",2)[0].trim();
            }
            videoInfoVO.setStarring(starring);
            videoInfoVO.setYear(year);
            videoInfoVO.setTitleName("片名："+titleName);
            videoInfoVO.setVideoUrl(videoUrl);
            videoInfoVO.setCoverImageUrl("https:"+coverImageUrl);
            videoInfoVO.setScore("评分：暂无");
            videoInfoVOS.add(videoInfoVO);
        }
        return videoInfoVOS;
    }

}
