package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

import java.util.LinkedList;
import java.util.List;

/**
 * @author František Liška
 */
public class HardwareInfo {
    private int hardwareId;
    private String title;
    private String type;
    private String description;
    private int defaultNumber;
    private List<Integer> researchGroupIdList = new LinkedList<Integer>();

    public int getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(int hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
    }

    public List<Integer> getResearchGroupIdList() {
        return researchGroupIdList;
    }

    public void setResearchGroupIdList(List<Integer> researchGroupIdList) {
        this.researchGroupIdList = researchGroupIdList;
    }
}
