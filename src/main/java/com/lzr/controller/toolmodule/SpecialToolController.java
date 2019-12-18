package com.lzr.controller.toolmodule;

import com.lzr.response.HttpResponse;
import com.lzr.service.toolmudule.SpecialToolService;
import com.lzr.dto.toolmodule.Video360InfoVOPageDTO;
import com.lzr.dto.toolmodule.VideoEpisodeAnalysisInfoDTO;
import com.lzr.dto.toolmodule.VideoSearchingFeatureDTO;
import com.lzr.vo.toolmodule.VideoTypeAndInfoVO;
import com.lzr.utils.videoparsing.Video360Parsing;
import com.lzr.utils.videoparsing.VideoIQiYiParsing;
import com.lzr.utils.videoparsing.VideoTencentParsing;
import com.lzr.utils.videoparsing.VideoYoukuParsing;
import com.lzr.utils.videoparsing.episodehandle.Video360EpisodeAnalysis;
import com.lzr.utils.videoparsing.episodehandle.VideoIQiYiEpisodeAnalysis;
import com.lzr.utils.videoparsing.episodehandle.VideoTencentEpisodeAnalysis;
import com.lzr.utils.videoparsing.episodehandle.VideoYouKuEpisodeAnalysis;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 只是写着玩的，别太认真哟
 * @author lzr
 * @date 2019/5/8 0008 09:56
 */
@Log4j2
@Validated
@RestController
@RequestMapping("/specialTool")
public class SpecialToolController {

    @Resource
    private SpecialToolService specialToolService;

    /**
     * 获取影视数据
     * @param video360InfoVOPageDTO
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/video360InfoVOPage")
    public HttpResponse video360InfoVOPage(@Valid @RequestBody Video360InfoVOPageDTO video360InfoVOPageDTO) throws Exception{
        VideoTypeAndInfoVO videoTypeAndInfoVO = new VideoTypeAndInfoVO();
        videoTypeAndInfoVO.setVideoType(video360InfoVOPageDTO.getVideoType());
        if(video360InfoVOPageDTO.getVideoResource().equals(1)){// 360影视
            videoTypeAndInfoVO.setVideoInfoVOS(Video360Parsing.video360InfoVOPage(String.valueOf(video360InfoVOPageDTO.getVideoType()),video360InfoVOPageDTO.getPageNum()));
            return HttpResponse.success(videoTypeAndInfoVO);
        }else if(video360InfoVOPageDTO.getVideoResource().equals(2)){// 爱奇艺
            videoTypeAndInfoVO.setVideoInfoVOS(VideoIQiYiParsing.videoIQiYiInfoVOPage(String.valueOf(video360InfoVOPageDTO.getVideoType()),video360InfoVOPageDTO.getPageNum()));
            return HttpResponse.success(videoTypeAndInfoVO);
        }else if(video360InfoVOPageDTO.getVideoResource().equals(3)){// 优酷
            videoTypeAndInfoVO.setVideoInfoVOS(VideoYoukuParsing.videoYouKuInfoVOPage(String.valueOf(video360InfoVOPageDTO.getVideoType()),video360InfoVOPageDTO.getPageNum()));
            return HttpResponse.success(videoTypeAndInfoVO);
        }else if(video360InfoVOPageDTO.getVideoResource().equals(4)){// 腾讯
            videoTypeAndInfoVO.setVideoInfoVOS(VideoTencentParsing.videoTencentInfoVOPage(String.valueOf(video360InfoVOPageDTO.getVideoType()),video360InfoVOPageDTO.getPageNum()));
            return HttpResponse.success(videoTypeAndInfoVO);
        }else {
            return HttpResponse.fail("数据来源类型错误");
        }

    }

    /**
     * 获取电视剧剧集及详细信息数据
     * @param videoEpisodeAnalysisInfoDTO
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/videoEpisodeAnalysisDetails")
    public HttpResponse videoEpisodeAnalysisDetails(@Valid @RequestBody VideoEpisodeAnalysisInfoDTO videoEpisodeAnalysisInfoDTO) throws Exception{
        if (videoEpisodeAnalysisInfoDTO.getVideoResource()==1){// 360影视
            return HttpResponse.success(Video360EpisodeAnalysis.getVideo360Detail(videoEpisodeAnalysisInfoDTO.getVideoUrl(),videoEpisodeAnalysisInfoDTO.getVideoType(),videoEpisodeAnalysisInfoDTO.getPreCoverImageUrl()));
        }else if(videoEpisodeAnalysisInfoDTO.getVideoResource()==2){// 爱奇艺
            return HttpResponse.success(VideoIQiYiEpisodeAnalysis.getVideoIQiYiDetail(videoEpisodeAnalysisInfoDTO.getVideoUrl(),videoEpisodeAnalysisInfoDTO.getVideoType(),videoEpisodeAnalysisInfoDTO.getPreCoverImageUrl()));
        }else if(videoEpisodeAnalysisInfoDTO.getVideoResource()==3){// 优酷
            return HttpResponse.success(VideoYouKuEpisodeAnalysis.getVideoYouKuDetail(videoEpisodeAnalysisInfoDTO.getVideoUrl(),videoEpisodeAnalysisInfoDTO.getVideoType(),videoEpisodeAnalysisInfoDTO.getPreCoverImageUrl()));
        }else if(videoEpisodeAnalysisInfoDTO.getVideoResource()==4){// 腾讯
            return HttpResponse.success(VideoTencentEpisodeAnalysis.getVideoTencentDetail(videoEpisodeAnalysisInfoDTO.getVideoUrl(),videoEpisodeAnalysisInfoDTO.getVideoType(),videoEpisodeAnalysisInfoDTO.getPreCoverImageUrl()));
        }else {
            return HttpResponse.fail("数据来源类型错误");
        }
    }

    /**
     * 获取影视数据(搜索功能)
     * @param videoSearchingFeatureDTO
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/videoSearchingFeature")
    public HttpResponse videoSearchingFeature(@Valid @RequestBody VideoSearchingFeatureDTO videoSearchingFeatureDTO) throws Exception{
        return HttpResponse.success();
    }

}
