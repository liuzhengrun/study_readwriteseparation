package com.lzr.service;

import com.lzr.response.HttpResponse;
import com.lzr.vo.SysUserVO;

/**
 * @author lzr
 * @date 2019/12/12 0012 17:30
 */
public interface SysUserService {
    HttpResponse getAllUserInfo();

    HttpResponse insertUser(SysUserVO sysUserVO);
}
