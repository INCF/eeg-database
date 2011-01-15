package cz.zcu.kiv.eegdatabase.logic.controller.group;

/**
 * @author Jindrich Pergler
 */
public class CreateGroupCommand {

    private int id;
    private String researchGroupTitle;
    private String researchGroupDescription;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResearchGroupDescription() {
        return researchGroupDescription;
    }

    public void setResearchGroupDescription(String researchGroupDescription) {
        this.researchGroupDescription = researchGroupDescription;
    }

    public String getResearchGroupTitle() {
        return researchGroupTitle;
    }

    public void setResearchGroupTitle(String researchGroupTitle) {
        this.researchGroupTitle = researchGroupTitle;
    }
}
