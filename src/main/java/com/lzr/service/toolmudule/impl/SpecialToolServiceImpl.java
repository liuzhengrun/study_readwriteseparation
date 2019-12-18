package com.lzr.service.toolmudule.impl;

import com.lzr.service.toolmudule.SpecialToolService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lzr
 * @date 2019/5/8 0008 10:33
 */
@Log4j2
@Service
@Transactional(rollbackFor = Exception.class)
public class SpecialToolServiceImpl implements SpecialToolService {


}
