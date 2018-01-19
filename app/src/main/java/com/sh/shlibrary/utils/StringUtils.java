package com.sh.shlibrary.utils;

import android.widget.TextView;

import java.util.Random;

/**
 * Created by <B>ChenYao</B> on <B>2016/9/2</B>.
 * <br/>字符串工具
 */
public class StringUtils {

    /**
     * 控件内容为空判断
     *
     * @param strs
     * @return
     */
    public static boolean isEmpty(TextView... strs) {
        for (TextView str : strs) {
            if (str == null || str.length() == 0 || str.getText().toString().trim().length() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 字符串为空判断
     *
     * @param strs
     * @return
     */
    public static boolean isEmpty(String... strs) {
        for (String str : strs) {
            if (str == null || str.length() == 0 || str.trim().length() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 格式化数据库返回的时间格式
     *
     * @param sqlDate 数据库返回的时间
     * @param part    标识需要时间的那一部分，1表示日期，2表示时间，其他表示日期加时间
     * @return
     */
    public static String getStrBySqlDate(String sqlDate, int part) {
        if (isEmpty(sqlDate)) {
            return null;
        }
        String[] date = sqlDate.split("T");
        if (date.length != 2) {
            return null;
        }
        date[1] = date[1].substring(0, date[1].lastIndexOf("."));
        switch (part) {
            case 1:
                return date[0];
            case 2:
                return date[1];
            default:
                return date[0] + " " + date[1];
        }
    }

    /**
     * 生成随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
