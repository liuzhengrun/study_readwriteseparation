package com.lzr.controller.common;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lzr
 * @date 2019/12/17 0017 10:56
 */
@Log4j2
@RestController
public class MainController {

    @GetMapping({"/main","/"})
    public ModelAndView home(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("main");
        return mav;
    }

}
