package com.lzr.utils.videoparsing;

import com.lzr.vo.toolmodule.VideoInfoVO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 爬取360影视视频
 * @author lzr
 * @date 2019/9/17 0017 18:00
 */
public class Video360Parsing {

    public static void main(String[] args) throws Exception {

        video360InfoVOPage("3",1);

    }

    /**
     * 360影视解析 (1：电视剧 2：电影 3：综艺 4：动漫)
     * @param type
     * @param page
     * @return
     */
    public static List<VideoInfoVO> video360InfoVOPage(String type, Integer page) throws Exception{
        if (type.equals("1")) {// 电视剧
            type = "dianshi";
            String main = "https://www.360kan.com";
            String url = "https://www.360kan.com/" + type + "/list.php?rank=rankhot&cat=all&area=all&year=all&pageno=" + page;//?rank=rankhot&cat=all&area=all&year=all&pageno=2
            return getDianshiju(main,url);
        } else if (type.equals("2")) {// 电影
            type = "dianying";
            String main = "https://www.360kan.com";
            String url = "https://www.360kan.com/" + type + "/list.php?rank=rankhot&cat=all&area=all&year=all&pageno=" + page;//?rank=rankhot&cat=all&area=all&year=all&pageno=2
            return getDianying(main,url);
        } else if (type.equals("3")) {// 综艺
            type = "zongyi";
            String main = "https://www.360kan.com";
            String url = "https://www.360kan.com/" + type + "/list.php?rank=rankhot&cat=all&area=all&year=all&pageno=" + page;//?rank=rankhot&cat=all&area=all&year=all&pageno=2
            return getZongyi(main,url);
        } else if (type.equals("4")) {// 动画
            type = "dongman";
            String main = "https://www.360kan.com";
            String url = "https://www.360kan.com/" + type + "/list.php?rank=rankhot&cat=all&area=all&year=all&pageno=" + page;//?rank=rankhot&cat=all&area=all&year=all&pageno=2
            return getDongman(main,url);
        }
        return new ArrayList<>();
    }

    /**
     * TODO 电视剧
     * @param main
     * @param url
     * @return
     */
    private static List<VideoInfoVO> getDianshiju(String main,String url)throws Exception{
        Document doc = Jsoup.connect(url).get();//?rank=rankhot&cat=all&area=all&act=all&year=all&pageno=4
        Elements plays = doc.getElementsByClass("js-tongjic");
        List<VideoInfoVO> videoInfoVOS = new ArrayList<>();
        VideoInfoVO videoInfoVO = null;
        //遍历完imgs后，srcL链表已包含所有图片地址
        for (Element play : plays) {
            videoInfoVO = new VideoInfoVO();
            String p = play.toString();
            // 取出href里面的内容，src里面的内容，以及span里的年份，视频名和评分，还有演员
            String[] s = p.split(">");
            String hr = s[0] + ">";
            String hre = main + hr.substring(hr.indexOf("href=") + 6, hr.indexOf("\">"));
            String sr = s[2] + ">";
            // 获取图片路径
            String coverImageUrl = sr.substring(sr.indexOf("src=") + 5, sr.indexOf("\">"));
            String year, titleName, starring;
            year = s[5].split("<",2)[0];
            titleName = s[11].split("<")[0];
            starring = s[14].split("<")[0];
            if (starring!=null&&starring.trim().equals("")) {
                starring = "主演：暂无";
            }
            //从href网页源码中获取其他信息，如视频的详情，剧集，等等，然后传入播放界面并显示

            // 存进类里面
            videoInfoVO.setTitleName("片名："+titleName);
            videoInfoVO.setCoverImageUrl(coverImageUrl);
            videoInfoVO.setScore("评分：暂无");
            videoInfoVO.setStarring(starring);
            videoInfoVO.setVideoUrl(hre);// beac解析(通用接口,最好使用专属接口)
            videoInfoVO.setYear(year);
            videoInfoVOS.add(videoInfoVO);
        }
        return videoInfoVOS;
    }

    /**
     * TODO 360电影
     * @param main
     * @param url
     * @return
     */
    private static List<VideoInfoVO> getDianying(String main,String url)throws Exception{
        Document doc = Jsoup.connect(url).get();//?rank=rankhot&cat=all&area=all&act=all&year=all&pageno=4
        Elements plays = doc.getElementsByClass("js-tongjic");
        String regex = "http://([\\s\\S]+?)\"";
        Pattern pattern =Pattern.compile(regex);
        List<VideoInfoVO> videoInfoVOS = new ArrayList<>();
        VideoInfoVO videoInfoVO = null;
        //遍历完imgs后，srcL链表已包含所有图片地址
        for (Element play : plays) {
            videoInfoVO = new VideoInfoVO();
            String p = play.toString();
            // 取出href里面的内容，src里面的内容，以及span里的年份，视频名和评分，还有演员
            String[] s = p.split(">");
            String hr = s[0] + ">";
            String hre = main + hr.substring(hr.indexOf("href=") + 6, hr.indexOf("\">"));
            String sr = s[2] + ">";
            // 获取图片路径
            String coverImageUrl = sr.substring(sr.indexOf("src=") + 5, sr.indexOf("\">"));
            String year, titleName, score, starring,videoUrl;
            if (p.contains("付费")) {
                year = s[7].substring(0, 4);
                titleName = s[15].split("<")[0];
                score = s[9].split("<")[0];
                starring = s[18].split("<")[0];
                if (starring!=null&&starring.trim().equals("")) {
                    titleName = s[13].split("<")[0];
                    starring = s[16].split("<")[0];
                }
            } else {
                year = s[5].substring(0, 4);
                titleName = s[13].split("<")[0];
                score = s[7].split("<")[0];
                starring = s[16].split("<")[0];
            }
            if (score.equals("")) {
                score = "暂无";
            }
            if (year.length() != 4) {
                year = "暂无";
            }
            if (starring!=null&&starring.trim().equals("")) {
                starring = "主演：暂无";
            }
            //得到hre中的网页源码，以从中筛选出想要的信息
            Document docu = Jsoup.connect(hre).get();
            Elements btns = docu.getElementsByClass("s-cover");//播放链接在这个id中
            String sb = btns.toString();
            // 获取视频路径
            Matcher m =pattern.matcher(sb);
            if (m.find()){
                videoUrl = m.group(0);
                videoUrl = videoUrl.split("\"")[0];
            }else {
                videoUrl = "";
            }
            //从href网页源码中获取其他信息，如视频的详情，剧集，等等，然后传入播放界面并显示

            // 存进类里面
            videoInfoVO.setTitleName("片名："+titleName);
            videoInfoVO.setCoverImageUrl(coverImageUrl);
            videoInfoVO.setScore("评分："+score);
            videoInfoVO.setStarring(starring);
            videoInfoVO.setVideoUrl("http://beaacc.com/api.php?url="+videoUrl);// beac解析(通用接口,最好使用专属接口)
            videoInfoVO.setYear("年份："+year);
            videoInfoVOS.add(videoInfoVO);
        }
        return videoInfoVOS;
    }

    /**
     * TODO 综艺
     * @param main
     * @param url
     * @return
     */
    private static List<VideoInfoVO> getZongyi(String main,String url)throws Exception{
        Document doc = Jsoup.connect(url).get();//?rank=rankhot&cat=all&area=all&act=all&year=all&pageno=4
        Elements plays = doc.getElementsByClass("js-tongjic");
        String regex = "http://([\\s\\S]+?)\"";
        Pattern pattern =Pattern.compile(regex);
        List<VideoInfoVO> videoInfoVOS = new ArrayList<>();
        VideoInfoVO videoInfoVO = null;
        //遍历完imgs后，srcL链表已包含所有图片地址
        for (Element play : plays) {
            videoInfoVO = new VideoInfoVO();
            String p = play.toString();
            // 取出href里面的内容，src里面的内容，以及span里的年份，视频名和评分，还有演员
            String[] s = p.split(">");
            String hr = s[0] + ">";
            String hre = main + hr.substring(hr.indexOf("href=") + 6, hr.indexOf("\">"));
            String sr = s[2] + ">";
            // 获取图片路径
            String coverImageUrl = sr.substring(sr.indexOf("src=") + 5, sr.indexOf("\">"));
            String year, titleName, score, starring,videoUrl;
            if (p.contains("付费")) {
                year = s[7].split("<")[0];
                titleName = s[13].split("<")[0];
                score = "";
                starring = s[16].split("<")[0];
            } else {
                year = s[5].split("<")[0];
                titleName = s[11].split("<")[0];
                score = "";
                starring = s[14].split("<")[0];
            }
            if (score.equals("")) {
                score = "暂无";
            }
            if (year.equals("")) {
                year = "暂无";
            }
            if (starring!=null&&starring.trim().equals("")) {
                starring = "描述：暂无";
            }
            //得到hre中的网页源码，以从中筛选出想要的信息
            Document docu = Jsoup.connect(hre).get();
            Elements btns = docu.getElementsByClass("s-cover");//播放链接在这个id中
            String sb = btns.toString();
            // 获取视频路径
            Matcher m =pattern.matcher(sb);
            if (m.find()){
                videoUrl = m.group(0);
                videoUrl = videoUrl.split("\"")[0];
            }else {
                videoUrl = "";
            }
            //从href网页源码中获取其他信息，如视频的详情，剧集，等等，然后传入播放界面并显示

            // 存进类里面
            videoInfoVO.setTitleName("片名："+titleName);
            videoInfoVO.setCoverImageUrl(coverImageUrl);
            videoInfoVO.setScore("评分："+score);
            videoInfoVO.setStarring(starring);
            videoInfoVO.setVideoUrl("http://beaacc.com/api.php?url="+videoUrl);// beac解析(通用接口,最好使用专属接口)
            videoInfoVO.setYear("年份："+year);
            videoInfoVOS.add(videoInfoVO);
        }
        return videoInfoVOS;
    }

    /**
     * TODO 动漫
     * @param main
     * @param url
     * @return
     */
    private static List<VideoInfoVO> getDongman(String main,String url)throws Exception{
        Document doc = Jsoup.connect(url).get();//?rank=rankhot&cat=all&area=all&act=all&year=all&pageno=4
        Elements plays = doc.getElementsByClass("js-tongjic");
        List<VideoInfoVO> videoInfoVOS = new ArrayList<>();
        VideoInfoVO videoInfoVO = null;
        //遍历完imgs后，srcL链表已包含所有图片地址
        for (Element play : plays) {
            videoInfoVO = new VideoInfoVO();
            String p = play.toString();
            // 取出href里面的内容，src里面的内容，以及span里的年份，视频名和评分，还有演员
            String[] s = p.split(">");
            String hr = s[0] + ">";
            String hre = main + hr.substring(hr.indexOf("href=") + 6, hr.indexOf("\">"));
            String sr = s[2] + ">";
            // 获取图片路径
            String coverImageUrl = sr.substring(sr.indexOf("src=") + 5, sr.indexOf("\">"));
            String year, titleName;
            year = s[5].split("<",2)[0];
            titleName = s[11].split("<")[0];
            //从href网页源码中获取其他信息，如视频的详情，剧集，等等，然后传入播放界面并显示

            // 存进类里面
            videoInfoVO.setTitleName("片名："+titleName);
            videoInfoVO.setCoverImageUrl(coverImageUrl);
            videoInfoVO.setScore("评分：暂无");
            videoInfoVO.setStarring("主演：暂无");
            videoInfoVO.setVideoUrl(hre);// beac解析(通用接口,最好使用专属接口)
            videoInfoVO.setYear(year);
            videoInfoVOS.add(videoInfoVO);
        }
        return videoInfoVOS;
    }

}
