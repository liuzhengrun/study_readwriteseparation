package com.lzr.vo.toolmodule;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author lzr
 * @date 2019/9/20 0020 17:22
 */
@Data
@ToString
public class VideoEpisodeAnalysisVO implements Serializable {

    private String coverImageUrl;// 视频图片

    private String titleName;// 电影名称

    private String episodeInfo;// 剧集信息

    private String videoTypeInfo;// 类型信息

    private String year;// 年代

    private String director;// 导演

    private String actors;// 演员

    private String area;// 地区

    private String introduction;// 简介

    private List<VideoEpisodePlayInfoVO> videoEpisodePlayInfoVOS;// 播放信息

}
