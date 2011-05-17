package cz.zcu.kiv.eegdatabase.logic.controller.group;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * @author Jenda Kolena
 */
public class BookRoomCommand {

    private int selectedGroup;
    private String date;
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;
    private int repCount;
    private int repType;

    public int getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(int selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEndTimeString() {
        return getTime(endTime);
    }

    public GregorianCalendar getEndTime() {
        return endTime;
    }

    public Timestamp getEndTimeTimestamp() {
        return new Timestamp(endTime.getTimeInMillis());
    }

    public void setEndTime(String endTime) throws ParseException {
        this.endTime = getCalendar(endTime);
    }

    public String getStartTimeString() {
        return getTime(startTime);
    }

    public GregorianCalendar getStartTime() {
        return startTime;
    }

    public Timestamp getStartTimeTimestamp() {
        return new Timestamp(startTime.getTimeInMillis());
    }

    public void setStartTime(String startTime) throws ParseException {
        this.startTime = getCalendar(startTime);
    }

    public int getRepCount() {
        return repCount;
    }

    public void setRepCount(int repCount) {
        this.repCount = repCount;
    }

    public int getRepType() {
        return repType;
    }

    public void setRepType(int repType) {
        this.repType = repType;
    }

    /**
     * Converts String to GregorianCalendar.
     *
     * @param rawDate Date in dd/mm/yy hh:mm:ss format.
     * @return Created GregorianCalendar.
     * @throws ParseException If rawDate cannot be parsed.
     */
    public static GregorianCalendar getCalendar(String rawDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        GregorianCalendar tmp = new GregorianCalendar();
        tmp.setTime(sdf.parse(rawDate));
        return tmp;
    }

    /**
     * Gets time in hh:mm:ss format from GregorianCalendar.
     *
     * @param cal Input GregorianCalendar.
     * @return Retreived time.
     */
    public static String getTime(GregorianCalendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal);
    }

    /**
     * Gets time in dd/mm/yy format from GregorianCalendar.
     *
     * @param cal Input GregorianCalendar.
     * @return Retreived date.
     */
    public static String getDate(GregorianCalendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        return sdf.format(cal);
    }
}
