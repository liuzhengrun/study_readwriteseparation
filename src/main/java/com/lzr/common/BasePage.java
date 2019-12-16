package com.lzr.common;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@ToString
public class BasePage implements Serializable {
    
    /**
     * 每页条数
     */
    private Integer pageSize = 10;
    /**
     * 当前页数
     */
    private Integer pageNum = 1;
    /**
     * 排序字段
     */
    private String orderField;
    /**
     * 正序还是倒序
     */
    private String order;

    public String createOrderSql() {
        if (this.orderField != null) {
            return camelToUnderline(new StringBuffer(this.orderField)).append(" " + order).toString();
        }
        return "";
    }

    /**
     * 下划线转驼峰
     *
     * @param str
     * @return
     */
    public static StringBuffer underlineToCamel(StringBuffer str) {
        //利用正则删除下划线，把下划线后一位改成大写
        Pattern pattern = Pattern.compile("_(\\w)");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if (matcher.find()) {
            sb = new StringBuffer();
            //将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
            //正则之前的字符和被替换的字符
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
            //把之后的也添加到StringBuffer对象里
            matcher.appendTail(sb);
        } else {
            return sb;
        }
        return underlineToCamel(sb);
    }


    /**
     * 驼峰转下划线
     *
     * @param str
     * @return
     */
    private static StringBuffer camelToUnderline(StringBuffer str) {
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if (matcher.find()) {
            sb = new StringBuffer();
            //将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
            //正则之前的字符和被替换的字符
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
            //把之后的也添加到StringBuffer对象里
            matcher.appendTail(sb);
        } else {
            return sb;
        }
        return camelToUnderline(sb);
    }
}
