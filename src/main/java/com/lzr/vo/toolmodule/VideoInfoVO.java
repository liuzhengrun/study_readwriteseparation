package com.lzr.vo.toolmodule;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author lzr
 * @date 2019/9/16 0016 17:40
 */
@Data
@ToString
public class VideoInfoVO implements Serializable {
    // 年份
    private String year;
    // 片名
    private String titleName;
    // 评分
    private String score;
    // 主演
    private String starring;
    // 封面图片路径
    private String coverImageUrl;
    // 视频路径
    private String videoUrl;
}
