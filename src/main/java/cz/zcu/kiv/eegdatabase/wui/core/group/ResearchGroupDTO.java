package cz.zcu.kiv.eegdatabase.wui.core.group;

import cz.zcu.kiv.eegdatabase.wui.core.dto.FullPersonDTO;
import cz.zcu.kiv.eegdatabase.wui.core.dto.IdentifiDTO;

public class ResearchGroupDTO extends IdentifiDTO {

    private static final long serialVersionUID = -8441180060711874289L;

    private FullPersonDTO owner;
    private String title;
    private String description;

    public ResearchGroupDTO() {

    }

    public FullPersonDTO getOwner() {
        return owner;
    }

    public void setOwner(FullPersonDTO owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
