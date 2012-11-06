package cz.zcu.kiv.eegdatabase.wui.core.educationlevel;

import java.io.Serializable;

public class EducationLevelDTO implements Serializable {

    private static final long serialVersionUID = 6468614462901866299L;
    
    private int educationLevelId;
    private String title;
    private int defaultNumber;

    public EducationLevelDTO() {

    }

    public int getEducationLevelId() {
        return educationLevelId;
    }

    public void setEducationLevelId(int educationLevelId) {
        this.educationLevelId = educationLevelId;
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
