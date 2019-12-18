package com.lzr.vo.toolmodule;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author lzr
 * @date 2019/9/19 0019 18:03
 */
@Data
@ToString
public class VideoTypeAndInfoVO implements Serializable {

    private Integer videoType;// 1：电视剧 2：电影 3：综艺 4：动漫

    private List<VideoInfoVO>  videoInfoVOS;// 视频获取信息

}
