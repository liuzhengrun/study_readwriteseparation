package com.lzr.service.common.impl;

import com.lzr.annotation.Master;
import com.lzr.annotation.Slave;
import com.lzr.dao.SysUserMapper;
import com.lzr.model.SysUser;
import com.lzr.response.HttpResponse;
import com.lzr.service.common.SysUserService;
import com.lzr.vo.common.SysUserVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author lzr
 * @date 2019/12/12 0012 17:31
 */
@Log4j2
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Slave
    @Override
    public HttpResponse getAllUserInfo() {
        return HttpResponse.success(sysUserMapper.selectAll());
    }

    @Master
    @Override
    public HttpResponse insertUser(SysUserVO sysUserVO) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserVO,sysUser);
        Date date = new Date();
        sysUser.setCreateTime(date);
        sysUser.setCreateBy("lzr");
        sysUser.setUpdateTime(date);
        sysUser.setUpdateBy("lzr");
        sysUser.setIsDelete((byte)0);// 未删除
        if(sysUserMapper.insertUseGeneratedKeys(sysUser)!=1){
            return HttpResponse.fail("新增失败");
        }
        return HttpResponse.success(sysUser);
    }

}
