package cz.zcu.kiv.eegdatabase.logic.controller.group;

/**
 * @author Jindra
 */
public class AddMemberToGroupCommand {

    private int groupFormId = -1;
    private String userName;
    private String userRole;

    public int getGroupFormId() {
        return groupFormId;
    }

    public void setGroupFormId(int groupFormId) {
        this.groupFormId = groupFormId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
