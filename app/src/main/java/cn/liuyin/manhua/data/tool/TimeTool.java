package cn.liuyin.manhua.data.tool;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeTool {
    public static String getTimeString(int time) {
        Date date = new Date((long) time * 1000);
        SimpleDateFormat ft =
                new SimpleDateFormat("更新于:yyyy.MM.dd 'at' HH:mm:ss", Locale.CHINESE);
        return ft.format(date);
    }
}
