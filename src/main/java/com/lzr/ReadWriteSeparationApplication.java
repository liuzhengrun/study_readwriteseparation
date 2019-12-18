package com.lzr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author lzr
 * @date 2019/12/11 0011 17:43
 */
@SpringBootApplication
@MapperScan(basePackages = "com.lzr.dao")
@EnableTransactionManagement(order = 10)// 配置事务优先级为10 (不设置order，则默认最大integer数值)
public class ReadWriteSeparationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReadWriteSeparationApplication.class,args);
    }

}
