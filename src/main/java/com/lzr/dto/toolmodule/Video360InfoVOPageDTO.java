package com.lzr.dto.toolmodule;

import com.lzr.common.BasePage;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 获取影视数据
 * @author lzr
 * @date 2019/9/17 0017 10:14
 */
@Data
@ToString
public class Video360InfoVOPageDTO extends BasePage implements Serializable {

    /**
     * 类型 1：电视剧 2：电影 3：综艺 4：动漫
     */
    @NotNull(message = "视频来源不能为空")
    private Integer videoType;

    /**
     * 视频来源 1：360视频 2：爱奇艺 3：优酷 4：腾讯
     */
    @NotNull(message = "视频来源不能为空")
    private Integer videoResource;

}
