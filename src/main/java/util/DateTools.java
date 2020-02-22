package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *    日期格式化工具类,使用ThreadLocal解决SimpleDateFormat非线程安全问题
 */
public class DateTools {

    private static ThreadLocal<SimpleDateFormat> t1 = new ThreadLocal<>();
    private static ThreadLocal<DateFormat> t2 = new ThreadLocal<>();

    public static SimpleDateFormat getSimpleDateFormat() {
        SimpleDateFormat sdf = null;
        sdf = t1.get();
        if(sdf == null) {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            t1.set(sdf);
        }
        return sdf;
    }

    public static DateFormat getDateFormat() {
        DateFormat df = null;
        df = t2.get();
        if(df == null) {
            df = DateFormat.getDateInstance(DateFormat.SHORT);
            t2.set(df);
        }
        return df;
    }
}