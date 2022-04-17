package com.singularityfold.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类，用于返回系统当前日期，
 * 格式为yyyy-MM-dd HH:mm:ss
 *
 * @author Mr_Hades
 * @date 2021-09-18 10:22
 */
public class DateUtil {

    public static String getLocalTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置日期格式
        return df.format(new Date()); // new Date()为获取当前系统时间
    }

}
