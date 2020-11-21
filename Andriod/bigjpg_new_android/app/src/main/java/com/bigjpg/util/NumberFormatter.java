package com.bigjpg.util;

import java.text.DecimalFormat;

/**
 * NumberFormatter
 *
 * @author Momo
 * @date 2016-04-13 17:24
 */
public class NumberFormatter {

    /**
     * 千分制
     *
     * @param value
     * @return
     */
    public static String formatWithThousandSeparator(double value) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            value = 0;
        }
        DecimalFormat df = new DecimalFormat("#,###");
        String s = df.format(value);
        return s;
    }


    /**
     * 千分制,保留两位小数
     *
     * @param value
     * @return
     */
    public static String formatWithThousandSeparator2(double value) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            value = 0;
        }
        String pattern = "#,##0.00";
        DecimalFormat df = new DecimalFormat(pattern);
        String s = df.format(value);
        return s;
    }

    public static String formatWithMoneyThousandSeparator(double value) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            value = 0;
        }
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String s = df.format(value);
        return "¥" + s;
    }

    public static String formatWithThousandSeparatorSigned(double value) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            value = 0;
        }
        DecimalFormat df = new DecimalFormat("#,###");
        String s = df.format(value);
        if(value > 0){
            s = "+" + s;
        }
        return s;
    }

    public static String formatWithMoneyThousandSeparatorSigned(double value) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            value = 0;
        }
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String s = df.format(value);
        if(value > 0){
            return "+¥" + s;
        }else if(value < 0){
            return "-¥" + s.substring(1);
        }else{
            return "¥" + s;
        }
    }



    /**
     * 大于万则显示万，大于百万显示带"百万"符号。小于则用千分制显示
     *
     * @param value
     * @return
     */
    public static String formatWithMillionOrThousandSeparator(long value) {
        double temp = Math.abs(value);
        temp = temp / 10000.0D;
        if (temp >= 1.0D) {
            if(value > 0){
                return scale(temp, 2) + "万";
            }else{
                return scale(-temp, 2) + "万";
            }
        }
        temp = temp / 100.0D;
        if (temp >= 1.0D) {
            if(value > 0){
                return scale(temp, 2) + "百万";
            }else{
                return scale(-temp, 2) + "百万";
            }
        } else {
            return formatWithThousandSeparator(value);
        }
    }

    /**
     * 精确到整数
     *
     * @param value
     * @return
     */
    public static String scale(double value) {
        return scale(value, 0);
    }

    /**
     * 精确到小数点后1位
     *
     * @param value
     * @return
     */
    public static String scale1(double value) {
        return scale(value, 1);
    }

    /**
     * 精确到小数点后2位
     *
     * @param value
     * @return
     */
    public static String scale2(double value) {
        return scale(value, 2);
    }

    /**
     * 精确到小数点后3位
     *
     * @param value
     * @return
     */
    public static String scale3(double value) {
        return scale(value, 3);
    }

    /**
     * 精确显示
     *
     * @param value
     * @param scale
     * @return
     */
    public static String scale(double value, int scale) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            value = 0;
        }
        String format = "%." + scale + "f";
        return String.format(format, value);
    }

    /**
     * 带正负号的精确显示
     *
     * @param value
     * @param scale
     * @return
     */
    public static String scaleSigned(double value, int scale) {
        String format;
        if (value > 0) {
            format = "+%." + scale + "f";
        } else {
            format = "%." + scale + "f";
        }
        return String.format(format, value);
    }

    /**
     * 数值转化成整数形式的百分值
     *
     * @param value
     * @return
     */
    public static String percent(double value) {
        return percent(value, 0);
    }

    /**
     * 数值转化成1位小数的百分值
     *
     * @param value
     * @return
     */
    public static String percent1(double value) {
        return percent(value, 1);
    }

    /**
     * 数值转化成2位小数的百分值
     *
     * @param value
     * @return
     */
    public static String percent2(double value) {
        return percent(value, 2);
    }

    /**
     * 数值转化成百分值
     *
     * @param value
     * @param scale 小数位数
     * @return
     */
    public static String percent(double value, int scale) {
        value = value * 100;
        String format = "%." + scale + "f%%";
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            value = 0;
        }
        return String.format(format, value);
    }

    /**
     * 数值转化成带正负号整数的百分值
     *
     * @param value
     * @return
     */
    public static String percentSigned(double value) {
        return percentSigned(value, 0);
    }

    /**
     * 数值转化成带正负号2位小数形式的百分值
     *
     * @param value
     * @return
     */
    public static String percentSigned2(double value) {
        return percentSigned(value, 2);
    }

    /**
     * 数值转化成带正负号形式的百分值
     *
     * @param value
     * @return
     */
    public static String percentSigned(double value, int scale) {
        String format;
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            value = 0;
        }
        if (value > 0) {
            format = "+%." + scale + "f%%";
        } else {
            format = "%." + scale + "f%%";
        }
        return String.format(format, value * 100);
    }

    /**
     * 数值转化成特定形式带正负号形式
     *
     * @param value
     * @return
     */
    public static String formatWithMillionOrThousandSeparatorSigned(long value) {
        String result = formatWithMillionOrThousandSeparator(value);
        String format;
        if (value > 0) {
            format = "+%s";
        } else {
            format = "%s";
        }
        return String.format(format, result);
    }

    public static String sign(double value) {
        if (value > 0) {
            return "+" + value;
        } else {
            return String.valueOf(value);
        }
    }

    public static String sign(long value) {
        if (value > 0) {
            return "+" + value;
        } else {
            return String.valueOf(value);
        }
    }

    public static String sign(int value) {
        if (value > 0) {
            return "+" + value;
        } else {
            return String.valueOf(value);
        }
    }

    public static String sign(float value) {
        if (value > 0) {
            return "+" + value;
        } else {
            return String.valueOf(value);
        }
    }
}
