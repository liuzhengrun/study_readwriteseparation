package com.lzr.vo.toolmodule;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class VideoEpisodePlayInfoVO implements Serializable {

    private String episode;// 剧集

    private String status;// 状态

    private String videoUrl;// 视频路径
}