package com.lzr.utils;

import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: CHENJUNPENG648
 * @Date: 2018-8-29 15:00
 * @Description:
 */
public class MyExcelHandler<T> extends ExcelDataHandlerDefaultImpl<T> {
    private Map<String, Integer> rule = new ConcurrentHashMap<>();


    public MyExcelHandler() {
    }

    public MyExcelHandler(Map<String, Integer> rule) {
        if (rule != null && rule.size() > 0) {
            String[] strings = new String[rule.size()];
            setNeedHandlerFields(rule.keySet().toArray(strings));
        }
        this.rule = rule;
    }


    @Override
    public Object exportHandler(T obj, String name, Object value) {
        if (rule.containsKey(name) && value != null) {
            DecimalFormat df = new DecimalFormat("#.00");
            return df.format(Double.parseDouble(value + "") / rule.get(name));
        } else {
            return value;
        }
    }


    @Override
    public Object importHandler(T obj, String name, Object value) {
        if (rule.containsKey(name) && value != null) {
            double x = Double.parseDouble(value + "") * rule.get(name);
            return Long.parseLong(new DecimalFormat("0").format(x));
        } else {
            return value;
        }
    }

}
