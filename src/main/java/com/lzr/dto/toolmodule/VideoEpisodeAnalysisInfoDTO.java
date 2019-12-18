package com.lzr.dto.toolmodule;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 获取电视剧剧集及详细信息数据
 * @author lzr
 * @date 2019/9/20 0020 18:20
 */
@Data
@ToString
public class VideoEpisodeAnalysisInfoDTO implements Serializable {

    /**
     * video路径
     */
    @NotNull(message = "video路径不能为空")
    @NotBlank(message = "video路径不能为空")
    private String videoUrl;

    /**
     * video类型
     */
    @NotNull(message = "video类型不能为空")
    private Integer videoType;

    /**
     * video来源
     */
    @NotNull(message = "video来源不能为空")
    private Integer videoResource;

    /**
     * 原封面图片
     */
    private String preCoverImageUrl;

}
