package cz.zcu.kiv.eegdatabase.logic.util;

import cz.zcu.kiv.eegdatabase.data.dao.ReservationDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Jenda Kolena
 */
public class BookingRoomUtils
{
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
    public static GregorianCalendar getCalendar(String rawDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
        GregorianCalendar tmp = new GregorianCalendar();
        try
        {
            tmp.setTime(sdf.parse(rawDate));
        } catch (ParseException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return tmp;
    }

    /**
     * Gets time in hh:mm:ss format from java.sql.Timestamp.
     *
     * @param timestamp Input Timestamp.
     * @return Retrieved time.
     * @see #timeFormat
     */
    public static String getTime(Timestamp timestamp)
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(timestamp);
        return getTime(cal);
    }

    /**
     * Gets time in hh:mm:ss format from GregorianCalendar.
     *
     * @param cal Input GregorianCalendar.
     * @return Retrieved time.
     * @see #timeFormat
     */
    public static String getTime(GregorianCalendar cal)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        Date date = cal.getTime();
        return sdf.format(date);
    }

    /**
     * Gets hours and minutes from complete time value.
     *
     * @param dateTime
     * @return Retrieved hours:minutes.
     */
    public static String getHoursAndMinutes(String dateTime)
    {
        String tmp = dateTime.split(" ")[1];
        return tmp.substring(0, tmp.lastIndexOf(":"));
    }

    /**
     * Gets hours and minutes from java.sql.Timestamp.
     *
     * @param timestamp Input Timestamp.
     * @return Retrieved hours:minutes.
     */
    public static String getHoursAndMinutes(Timestamp timestamp)
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(timestamp);
        return getHoursAndMinutes(cal);
    }

    /**
     * Gets hours and minutes from GregorianCalendar.
     *
     * @param cal Input GregorianCalendar.
     * @return Retrieved hours:minutes.
     */
    public static String getHoursAndMinutes(GregorianCalendar cal)
    {
        String tmp = getTime(cal);
        return getTime(cal).substring(0, tmp.lastIndexOf(":"));
    }

    /**
     * Gets time in dd/mm/yy format from java.sql.Timestamp.
     *
     * @param timestamp Input Timestamp.
     * @return Retrieved date.
     * @see #dateFormat
     */
    public static String getDate(Timestamp timestamp)
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(timestamp);
        return getDate(cal);
    }

    /**
     * Gets time in dd/mm/yy format from GregorianCalendar.
     *
     * @param cal Input GregorianCalendar.
     * @return Retrieved date.
     * @see #dateFormat
     */
    public static String getDate(GregorianCalendar cal)
    {
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
    public static int getWeeksAddCount(int repType, int repIndex)
    {
        int weekNum = new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);

        int add = 0;
        if (repType == 0) add = 1;
        if ((repType == 1 && weekNum % 2 == 1) || (repType == 2 && weekNum % 2 == 0))
        {
            add = 2;
        }
        if ((repType == 1 && weekNum % 2 == 0) || (repType == 2 && weekNum % 2 == 1))
        {
            if (repIndex == 0)
            {
                add = 1;
            }
            else
            {
                add = 2;
            }
        }
        return add;
    }

    public static ArrayList<Reservation> getCollisions(ReservationDao dao, int repCount, int repType, GregorianCalendar start, GregorianCalendar end)
    {
        ArrayList coll = new ArrayList();
        for (int i = 0; i < repCount; i++)
        {
            int add = BookingRoomUtils.getWeeksAddCount(repType, i);
            start.add(Calendar.WEEK_OF_YEAR, add);
            end.add(Calendar.WEEK_OF_YEAR, add);

            List nextRes = dao.getReservationsBetween(start, end);
            if (nextRes.size() > 0)
            {
                coll.addAll(nextRes);
            }
        }
        return coll;
    }

    /**
     * Gets formatted person name.
     *
     * @param person Person.
     * @return Persons name in format: Name SurName (UserName)
     */
    public static String formatPersonName(Person person)
    {
        return person.getGivenname() + " " + person.getSurname() + "(" + person.getUsername() + ")";
    }
}
