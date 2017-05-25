package com.mymasturbation.common;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wa on 17-5-25.
 * @link http://flt95.blog.163.com/blog/static/127361289201361845820132/
 */
public class MyDateHelper {

    public  String getTheDate(){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sd.format(new Date());
        return dateStr;
    }



    public  Map getMonthDate(){
        Map<String,String> map = new HashMap<String,String>();
        // 获取Calendar
        Calendar calendar = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 设置时间,当前时间不用设置
        // calendar.setTime(new Date());
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
        map.put("monthF", format.format(calendar.getTime()));
        // 设置日期为本月最大日期
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        // 打印
        map.put("monthL", format.format(calendar.getTime()));
        return map;
    }

    public  Map getWeekDay() {
        Map<String,String> map = new HashMap<String,String>();
        Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek()); //获取本周一的日期
        map.put("mon", df.format(cal.getTime()));
        //这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        //增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 0);
        map.put("sun", df.format(cal.getTime()));
        return map;
    }


}
