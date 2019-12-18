package com.lzr.utils;

import java.util.*;

/**
 * 数据处理工具
 *
 * @author lzr
 * @date 2019/3/12 0012 11:38
 */
public class DataProcessingTool {

    /**
     * 大数据增长率计算
     */
    public static String getBigDataGrowthRate(String preData,String thisData){
        String growthRate = "";
        try {
            if(isNotEmpty(preData)&&isNotEmpty(thisData)){
                if (Double.parseDouble(preData)==0){
                    growthRate = "-";
                }
                growthRate = String.format("%.3f",(Double.parseDouble(thisData)-Double.parseDouble(preData))/Double.parseDouble(preData))+"%";
            }else {
                growthRate = "";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return growthRate;
    }

    /**
     * 数据不为空
     *
     * @param data
     * @return
     */
    public static boolean isNotEmpty(String data) {
        if (data == null || data.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 判断集合是否有数据
     *
     * @param collection
     * @return
     */
    public static boolean isNotEmpty(Collection collection) {
        if (collection == null || collection.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 所有数据不为空
     *
     * @param objs
     * @return
     */
    public static boolean allNotEmpty(Object... objs) {
        for (Object obj : objs) {
            if (obj == null || obj.equals("")) {
                return false;
            }
        }
        return true;
    }

    /**
     * 两个时间相减得到String（年月）格式
     *
     * @param Subtracted  被减数
     * @param Subtraction 减数
     * @return
     */
    public static String TimeSubtractionToString(Date Subtracted, Date Subtraction) {
        long time1 = Subtracted.getTime();
        long time2 = Subtraction.getTime();
        long time = (time1 - time2) / 1000;
        long year = time / (24 * 3600 * 365);
        long month = time % (24 * 3600 * 365) / (24 * 3600 * 30);
        long day = time % (24 * 3600 * 365) % (24 * 3600 * 30) / (24 * 3600);
        return (year == 0 ? "" : year + "年") + (month == 0 ? "" : month + "个月") + (day == 0 ? "" : day + "天");
    }

    public static int getNowAge(Date birthDay){
        long pre = birthDay.getTime();
        long now = new Date().getTime();
        long time = (now - pre) / 1000;
        long year = time / (24 * 3600 * 365);
        return new Long(year<=0?0:year).intValue();
    }

    /**
     * 两个时间相减得到Date格式
     *
     * @param Subtracted  被减数
     * @param Subtraction 减数
     * @return
     */
    public static Date TimeSubtractionToDate(Date Subtracted, Date Subtraction) {

        return null;
    }

}
