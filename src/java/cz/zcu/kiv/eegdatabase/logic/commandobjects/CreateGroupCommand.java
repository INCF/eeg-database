package cz.zcu.kiv.eegdatabase.logic.commandobjects;

/**
 *
 * @author Jindrich Pergler
 */
public class CreateGroupCommand {

  private String researchGroupTitle;
  private String researchGroupDescription;

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
