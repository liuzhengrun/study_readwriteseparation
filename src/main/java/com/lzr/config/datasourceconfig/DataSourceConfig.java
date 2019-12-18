package com.lzr.config.datasourceconfig;

import com.lzr.response.enums.DBTypeEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源配置
 */
@Log4j2
@Configuration
public class DataSourceConfig {

    // 也可以new HikariDataSource(),但是DataSourceBuilder.create().build()会去寻找依赖
    // 当我们修改连接池的时候，就不需要修改本地的java代码了
    @Bean
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.slave1")
    public DataSource slave1DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.slave2")
    public DataSource slave2DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DataSource myRoutingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                          @Qualifier("slave1DataSource") DataSource slave1DataSource,
                                          @Qualifier("slave2DataSource") DataSource slave2DataSource) {
        log.info("开始配置多数据源");
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBTypeEnum.MASTER, masterDataSource);
        targetDataSources.put(DBTypeEnum.SLAVE1, slave1DataSource);
        targetDataSources.put(DBTypeEnum.SLAVE2, slave2DataSource);
        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        // 默认执行数据源
        myRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
        // 目标执行数据源集合
        myRoutingDataSource.setTargetDataSources(targetDataSources);
        log.info("多数据源配置完成");
        return myRoutingDataSource;
    }

}