package org.carroll.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.carroll.utils.Files;
import org.carroll.data.Paths;

/**
 * Class that handles logging. When {@link Paths#getLog()} changes, logging
 * automatically switches to the new file.
 *
 * @author Joel Gallant
 */
public class Logging {

    static File log;

    /**
     * Logs a message in the file file.
     *
     * @param x message to log
     */
    public static void log(String x) {
        if (log == null || !log.getPath().equals(Paths.getLog())) {
            log = new File(Paths.getLog());
            if (!log.exists()) {
                try {
                    Files.createNewFileWithParents(log);
                } catch (IOException ex) {
                    System.out.println("COULD NOT CREATE LOG FILE.");
                }
            }
            try {
                PrintStream logStream = new PrintStream(log);
                System.setOut(logStream);
                System.setErr(logStream);
            } catch (FileNotFoundException ex) {
                System.out.println("COULD NOT SET LOG FILE AS OUT");
            }
        }
        System.out.println("["+getTime()+"] "+x);
    }

    static String getTime() {
        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        String am_pm;
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        String parsedMinute;
        if (minute < 10) {
            parsedMinute = "0" + minute;
        } else {
            parsedMinute = Integer.toString(minute);
        }
        int second = calendar.get(Calendar.SECOND);
        String parsedSecond;
        if (second < 10) {
            parsedSecond = "0" + second;
        } else {
            parsedSecond = Integer.toString(second);
        }
        if (calendar.get(Calendar.AM_PM) == 0) {
            am_pm = "AM";
        } else {
            am_pm = "PM";
        }
        return hour + ":" + parsedMinute + ":" + parsedSecond + am_pm;
    }
}
