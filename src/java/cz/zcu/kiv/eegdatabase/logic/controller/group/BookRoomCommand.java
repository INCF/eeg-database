package cz.zcu.kiv.eegdatabase.logic.controller.group;

/**
 * @author Jenda Kolena
 */
public class BookRoomCommand {

    private int selectedGroup;
    private String date;
    private String startTime;
    private String endTime;
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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
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
