package cz.zcu.kiv.eegdatabase.logic.controller.list.hearingimpairment;

/**
 * @author Jindra
 */
public class AddHearingImpairmentCommand {

    private int hearingImpairmentId;
    private String description;

    public int getHearingImpairmentId() {
        return hearingImpairmentId;
    }

    public void setHearingImpairmentId(int hearingImpairmentId) {
        this.hearingImpairmentId = hearingImpairmentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
