package cz.zcu.kiv.eegdatabase.logic.controller.group;

/**
 *
 * @author Miroslav Brozek
 */
public class EditGroupRoleCommand {
    private int editedGroup;
    private boolean admin;
    private String role;
    private String group;
    private String user;

    public String getGroup() {
      return group;
    }


  public void setGroup(String group) {
    this.group = group;
  }



  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

  public int getEditedGroup() {
    return editedGroup;
  }

  public void setEditedGroup(int editedGroup) {
    this.editedGroup = editedGroup;
  }

}
