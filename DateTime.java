package com.vince;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
//import java.util.concurrent.TimeUnit;

public class DateTime extends Root {
    
    public static LocalDateTime time;
    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM:dd:mm:ss");
    
    public static String time(LocalDateTime end){        
        LocalDateTime currentDate = LocalDateTime.now(); 
        long Month = ChronoUnit.MONTHS.between(currentDate, end);
        long Day = ChronoUnit.DAYS.between(currentDate, end);
        long Hour = ChronoUnit.HOURS.between(currentDate, end)%24;
        long Minute = ChronoUnit.MINUTES.between(currentDate, end)%60;
        long Second = ChronoUnit.SECONDS.between(currentDate, end)%60;

        String clockString = String.format("%1$d, %2$d | %3$d : %4$d : %5$d (M:D:H:M:S)", Month, Day, Hour, Minute, Second);
            return clockString;
    }     
}