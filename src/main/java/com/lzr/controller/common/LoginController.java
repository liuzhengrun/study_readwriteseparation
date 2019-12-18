package com.lzr.controller.common;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @author lzr
 * @date 2019/12/17 0017 10:13
 */
@Log4j2
@RestController
@RequestMapping("/user")
public class LoginController {

    @PostMapping("/login")
    public ModelAndView login(){
        ModelAndView mav = new ModelAndView();
        return mav;
    }

}
