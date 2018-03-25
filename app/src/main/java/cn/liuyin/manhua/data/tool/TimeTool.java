package cn.liuyin.manhua.data.tool;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTool {
    public static String getTimeString(int time) {
        long lu = time;
        Date date = new Date(lu * 1000);
        SimpleDateFormat ft =
                new SimpleDateFormat("更新于:yyyy.MM.dd 'at' hh:mm:ss");
        return ft.format(date);
    }
}
