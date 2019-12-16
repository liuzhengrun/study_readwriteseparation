package com.lzr.common;


import com.github.pagehelper.Page;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class PageResult<T> {

    public PageResult(Page<T> page) {
        this.pageNum = page.getPageNum();
        this.pageSize = page.getPageSize();
        this.total = page.getTotal();
        this.pages = page.getPages();
        this.result = page.getResult();
    }

    /**
     * 当前页数
     */
    private Integer pageNum;
    /**
     * 每页条数
     */
    private Integer pageSize;
    /**
     * 总条数
     */
    private long total;
    /**
     * 总页数
     */
    private Integer pages;
    /**
     * 数据
     */
    private List<T> result;

}
