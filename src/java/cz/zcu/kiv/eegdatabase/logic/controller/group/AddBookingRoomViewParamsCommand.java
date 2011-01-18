package cz.zcu.kiv.eegdatabase.logic.controller.group;

/**
 * @author Jan Kolena
 */
public class AddBookingRoomViewParamsCommand {

    private String paramStart;
    private String paramEnd;
    private String paramDate;
    private int paramGroup;

    public String getParamStart() {
        return paramStart;
    }

    public void setParamStart(String paramStart) {
        this.paramStart = paramStart;
    }

    public String getParamEnd() {
        return paramEnd;
    }

    public void setParamEnd(String paramEnd) {
        this.paramEnd = paramEnd;
    }

    public String getParamDate() {
        return paramDate;
    }

    public void setParamDate(String paramDate) {
        this.paramDate = paramDate;
    }

    public int getParamGroup() {
        return paramGroup;
    }

    public void setParamGroup(int paramGroup) {
        this.paramGroup = paramGroup;
    }

    public String toString() {
        return "group=" + paramGroup + "&date=" + paramDate + "&startTime=" + paramStart + "&endTime" + paramEnd;
    }
}
