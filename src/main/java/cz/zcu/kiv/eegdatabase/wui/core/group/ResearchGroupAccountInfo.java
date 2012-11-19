package cz.zcu.kiv.eegdatabase.wui.core.group;

import java.io.Serializable;

public class ResearchGroupAccountInfo implements Serializable {

    private static final long serialVersionUID = 8367524195703437004L;

    int groupId;
    String title;
    String authority;

    public ResearchGroupAccountInfo(int groupId, String title, String authority) {
        this.groupId = groupId;
        this.title = title;
        this.authority = authority;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
