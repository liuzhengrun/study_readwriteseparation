package com.lzr.controller.common;

import com.lzr.response.HttpResponse;
import com.lzr.service.common.SysUserService;
import com.lzr.vo.common.SysUserVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lzr
 * @date 2019/12/12 0012 17:28
 */
@Log4j2
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/getAllUserInfo")
    public HttpResponse getAllUserInfo(){
        return sysUserService.getAllUserInfo();
    }

    @PostMapping("/insertUser")
    public HttpResponse insertUser(@RequestBody SysUserVO sysUserVO){
        return sysUserService.insertUser(sysUserVO);
    }

}
