package cz.zcu.kiv.eegdatabase.logic.controller.list.hearingimpairment;

/**
 * @author Jindra
 */
public class AddHearingImpairmentCommand {

    private int hearingImpairmentId;
    private String description;
    private int researchGroupId;
    private String researchGroupTitle;

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

    public int getResearchGroupId() {
        return researchGroupId;
    }

    public void setResearchGroupId(int researchGroupId) {
        this.researchGroupId = researchGroupId;
    }

    public String getResearchGroupTitle() {
        return researchGroupTitle;
    }

    public void setResearchGroupTitle(String researchGroupTitle) {
        this.researchGroupTitle = researchGroupTitle;
    }
}
