package cz.zcu.kiv.eegdatabase.wui.core.educationlevel;

import java.io.Serializable;

import cz.zcu.kiv.eegdatabase.wui.core.dto.IdentifiDTO;

public class EducationLevelDTO extends IdentifiDTO implements Serializable {

    private static final long serialVersionUID = 6468614462901866299L;
    
    private String title;
    private int defaultNumber;

    public EducationLevelDTO() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
    }
    
}
