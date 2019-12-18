package com.lzr.config.datasourceconfig;


import com.lzr.response.enums.DBTypeEnum;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 上下文key值处理设置类
 */
@Log4j2
public class DBContextHolder {

    // 初始化数字
    private static final Integer initNum = new Integer(0);

    /**
     * 可以作为当前请求对应线程的上下问数据
     */
    private static final ThreadLocal<DBTypeEnum> threadLocal = new ThreadLocal<>();

    private static final AtomicInteger atomicInteger = new AtomicInteger(initNum);

    public static void set(DBTypeEnum dbType) {
        threadLocal.set(dbType);
    }

    public static DBTypeEnum get() {
        return threadLocal.get();
    }

    public static void master() {
        set(DBTypeEnum.MASTER);
        log.info("切换到master");
    }

    public static void slave() {
        //  处理两个从库切换方式
        int index = atomicInteger.getAndIncrement();
        if (index>>10 > 1) {// 最大请求2048次
            atomicInteger.set(initNum);
        }
        if (index>>1 == 0) {
            set(DBTypeEnum.SLAVE1);
            log.info("切换到"+DBTypeEnum.SLAVE1.name());
        }else {
            set(DBTypeEnum.SLAVE2);
            log.info("切换到"+DBTypeEnum.SLAVE2.name());
        }
    }

}