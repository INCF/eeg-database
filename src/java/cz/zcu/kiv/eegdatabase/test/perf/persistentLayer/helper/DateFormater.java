package cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.helper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 1.6.11
 * Time: 17:50
 * To change this template use File | Settings | File Templates.
 */
public class DateFormater {

     public static String prefix;
     public static String stringStamp;

/**
 * Method crate test date, this date will as prefix to test data.
 * @return prefix
 */
    public static String createTestDate(String name){
     Date today = Calendar.getInstance().getTime();
       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
       prefix = formatter.format(today);
        return prefix + "_"+name;
    }

    public static Timestamp getTimestamp(){
         Calendar cal = new GregorianCalendar();
         long millis = cal.getTime().getTime();
         Timestamp ts = new java.sql.Timestamp(millis);
         return ts;
    }
}
