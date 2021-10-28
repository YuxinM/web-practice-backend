package com.example.webpractice.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转换工具类
 *
 * @Author MengYuxin
 * @Date 2021/10/26 16:08
 */
@Slf4j
public class DateUtil {


    /**
     * 将日期转化为时间戳
     *
     * @param dateString
     * @return
     */
    public static long dateToStamp(String dateString) {

        if (dateString.equals("")) {
            log.warn("有法律文书文件中日期一栏为空");
            return 0;
        }

        SimpleDateFormat simpleDateFormat = null;
        if (dateString.contains(",")) {
            dateString = dateString.replace(",", " ");
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        } else {
            dateString = dateString.replace("年", "-");
            dateString = dateString.replace("月", "-");
            dateString = dateString.replace("日", "-");
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        try {
            Date date = simpleDateFormat.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            log.error("日期格式转换出错");
        }
        return 0;
    }

    /**
     * 数据库时间戳转化为字符串格式
     *
     * @param timestamp
     * @return
     */
    public static String StampToDate(Timestamp timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
    }

}
