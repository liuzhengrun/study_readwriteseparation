package com.lzr.config;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * 使用切面处理
 * 如果在service层设置数据源
 * 必须在事务AOP之前执行，所以实现PriorityOrdered,其中order的值越小，越先执行
 * 如果一旦开始切换到写库，则之后的读都会走写库
 * 注：AOP ，内部方法之间互相调用时，如果是this.xxx()这形式，不会触发AOP拦截
 */
@Aspect
@Component
public class DataSourceAop implements Ordered {

    @Before("@annotation(com.lzr.annotation.Slave)")
    public void read() {
        DBContextHolder.slave();
    }

    @Before("@annotation(com.lzr.annotation.Master)")
    public void write() {
        DBContextHolder.master();
    }

    /**
     * 值越小，越优先执行
     * 需要优于事务的执行
     * 在启动类中加上了@EnableTransactionManagement(order = 10)
     * @return
     */
    @Override
    public int getOrder() {
        return 1;
    }
}