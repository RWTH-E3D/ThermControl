/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package system.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Time {
    public static LocalDateTime baseTime;
    public static LocalDateTime transportStartTime;
    private static DateTimeFormatter dateTimeFormatter;
    
    public static synchronized void init() {
        Time.baseTime = LocalDateTime.now();
        Time.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
    }
    
    public static synchronized long secondsUntilNow(LocalDateTime t) {
        return ChronoUnit.SECONDS.between(t, LocalDateTime.now());
    }
    
    public static synchronized long millisUntilNow(LocalDateTime t) {
        return ChronoUnit.MILLIS.between(t, LocalDateTime.now());
    }
    
    public static synchronized String timestamp(LocalDateTime t) {
        return t.format(Time.dateTimeFormatter);
    }
    
    public static synchronized void resetBaseTime() {
        synchronized(Time.baseTime) {
            Time.baseTime = LocalDateTime.now();
        }
    }
}