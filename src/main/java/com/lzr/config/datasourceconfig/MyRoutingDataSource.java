package com.lzr.config.datasourceconfig;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

/**
 * 获取database对应的key值
 * DataSourceConfig中配置的key
 */
public class MyRoutingDataSource extends AbstractRoutingDataSource {
    /**
     * 在DBContextHolder中获取当前请求对应的database的key值
     * @return
     */
    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        return DBContextHolder.get();
    }

}