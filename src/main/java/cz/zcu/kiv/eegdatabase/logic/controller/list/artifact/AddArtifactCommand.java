package cz.zcu.kiv.eegdatabase.logic.controller.list.artifact;

/**
 * Created with IntelliJ IDEA.
 * User: Honza
 * Date: 24.6.12
 * Time: 18:28
 * To change this template use File | Settings | File Templates.
 */
public class AddArtifactCommand {
    private int id;
    private String compensation;
    private String rejectCondition;
    private int researchGroupId;
    private String researchGroupTitle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompensation() {
        return compensation;
    }

    public void setCompensation(String compensation) {
        this.compensation = compensation;
    }

    public String getRejectCondition() {
        return rejectCondition;
    }

    public void setRejectCondition(String rejectCondition) {
        this.rejectCondition = rejectCondition;
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
