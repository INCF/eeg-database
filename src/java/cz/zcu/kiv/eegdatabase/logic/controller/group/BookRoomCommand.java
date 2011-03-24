package cz.zcu.kiv.eegdatabase.logic.controller.group;

import cz.zcu.kiv.eegdatabase.logic.util.BookingRoomUtils;

import java.sql.Timestamp;
import java.util.GregorianCalendar;

/**
 * @author Jenda Kolena
 */
public class BookRoomCommand {

    private int selectedGroup;
    private String date;
    private String startTime;
    private GregorianCalendar startTimeCal;
    private String endTime;
    private GregorianCalendar endTimeCal;
    private int repCount;
    private int repType;

    public void setSelectedGroup(int selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public int getSelectedGroup() {
        return selectedGroup;
    }


    public void setDate(String date) throws Exception {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setRepCount(int repCount) {
        this.repCount = repCount;
    }

    public int getRepCount() {
        return repCount;
    }

    public void setRepType(int repType) {
        this.repType = repType;
    }

    public int getRepType() {
        return repType;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
        endTimeCal = BookingRoomUtils.getCalendar(endTime);
    }

    public String getEndTime() {
        return endTime;
    }

    public GregorianCalendar getEndTimeCal() {
        return endTimeCal;
    }


    public Timestamp getEndTimeTimestamp() {
        return new Timestamp(endTimeCal.getTimeInMillis());
    }


    public void setStartTime(String startTime) {
        this.startTime = startTime;
        startTimeCal = BookingRoomUtils.getCalendar(startTime);
    }

    public String getStartTime() {
        return startTime;
    }

    public GregorianCalendar getStartTimeCal() {
        return startTimeCal;
    }

    public Timestamp getStartTimeTimestamp() {
        return new Timestamp(getStartTimeCal().getTimeInMillis());
    }

}
