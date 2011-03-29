package cz.zcu.kiv.eegdatabase.logic.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Jenda Kolena
 */
public class BookingRoomUtils {

    /**
     * Format of dateTime string.
     */
    public static final String dateTimeFormat = "dd/MM/yy HH:mm:ss";
    /**
     * Format of date string.
     */
    public static final String dateFormat = "dd/MM/yy";
    /**
     * Format of time string.
     */
    public static final String timeFormat = "HH:mm:ss";

    /**
     * Converts String to GregorianCalendar.
     *
     * @param rawDate Date in dd/mm/yy hh:mm:ss format.
     * @return Created GregorianCalendar.
     * @throws java.text.ParseException If rawDate cannot be parsed.
     * @see #dateTimeFormat
     */
    public static GregorianCalendar getCalendar(String rawDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
        GregorianCalendar tmp = new GregorianCalendar();
        try {
            tmp.setTime(sdf.parse(rawDate));
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return tmp;
    }

    /**
     * Gets time in hh:mm:ss format from GregorianCalendar.
     *
     * @param cal Input GregorianCalendar.
     * @return Retreived time.
     * @see #timeFormat
     */
    public static String getTime(GregorianCalendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        Date date = cal.getTime();
        return sdf.format(date);
    }

    /**
     * Gets hours and minutes from complete time value.
     *
     * @param dateTime
     * @return Retreived hours:minutes.
     */
    public static String getHoursAndMinutes(String dateTime) {
        String tmp = dateTime.split(" ")[1];
        return tmp.substring(0, tmp.lastIndexOf(":"));
    }

    /**
     * Gets hours and minutes from GregorianCalendar.
     *
     * @param cal Input GregorianCalendar.
     * @return Retreived hours:minutes.
     */
    public static String getHoursAndMinutes(GregorianCalendar cal) {
        String tmp = getTime(cal);
        return getTime(cal).substring(0, tmp.lastIndexOf(":"));
    }

    /**
     * Gets time in dd/mm/yy format from GregorianCalendar.
     *
     * @param cal Input GregorianCalendar.
     * @return Retreived date.
     * @see #dateFormat
     */
    public static String getDate(GregorianCalendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }

    /**
     * Gets count of weeks to add, when repetition was chosen.
     *
     * @param repType  Type of repetition.
     * @param repIndex Index of repetition (defacto FOR cycle).
     * @return int Count of weeks to add.
     */
    public static int getWeeksAddCount(int repType, int repIndex) {
        int weekNum = new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);

        int add = 0;
        if (repType == 0) add = 1;
        if ((repType == 1 && weekNum % 2 == 1) || (repType == 2 && weekNum % 2 == 0)) {
            add = 2;
        }
        if ((repType == 1 && weekNum % 2 == 0) || (repType == 2 && weekNum % 2 == 1)) {
            if (repIndex == 0) add = 1;
            else add = 2;
        }
        return add;
    }
}
