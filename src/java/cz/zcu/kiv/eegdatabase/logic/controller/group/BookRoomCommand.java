package cz.zcu.kiv.eegdatabase.logic.controller.group;

import java.util.Date;
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

    public String getDateString() {
        return null;
    }

    public Date getDate() {
        return null;
    }

    public void setDate(String date) {
        //this.date = date;
    }

    public String getEndTimeString() {
        return null;
    }

    public GregorianCalendar getEndTime() {
        return null;
    }

    public void setEndTime(String endTime) {
        //this.endTime = endTime;
    }

    public String getStartTimeString() {
        return null;
    }

    public GregorianCalendar getStartTime() {
        return null;
    }

    public void setStartTime(String startTime) {
        String[] tmp = startTime.split(":");
        //this.startTime = new GregorianCalendar(year, month - 1, day, h, m, 0)
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
}
