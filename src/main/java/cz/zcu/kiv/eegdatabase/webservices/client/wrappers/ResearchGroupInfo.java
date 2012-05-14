package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

import java.util.LinkedList;
import java.util.List;

/**
 * @author František Liška
 */
public class ResearchGroupInfo {

    private int researchGroupId;
    private String ownerUsername;
    private String title;
    private String description;
    private List<HardwareInfo> hardwares = new LinkedList<HardwareInfo>();

    public int getResearchGroupId() {
        return researchGroupId;
    }

    public void setResearchGroupId(int researchGroupId) {
        this.researchGroupId = researchGroupId;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
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

    public List<HardwareInfo> getHardwares() {
        return hardwares;
    }

    public void setHardwares(List<HardwareInfo> hardwares) {
        this.hardwares = hardwares;
    }
}
