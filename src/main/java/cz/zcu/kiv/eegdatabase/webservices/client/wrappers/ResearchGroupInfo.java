package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

import java.util.LinkedList;
import java.util.List;

/**
 * @author František Liška
 */
public class ResearchGroupInfo {
    private int researchGroupId;
    private int personOwner;
    private String title;
    private String description;
    private List<HardwareInfo> hardwares = new LinkedList<HardwareInfo>();
    private List<WeatherInfo> weathers = new LinkedList<WeatherInfo>();
    private List<PersonOptParamDefInfo> personOptParamDefInfos = new LinkedList<PersonOptParamDefInfo>();
    private List<ExperimentOptParamDefInfo> experimentOptParamDefInfos = new LinkedList<ExperimentOptParamDefInfo>();
    private List<FileMetadataParamDefInfo> fileMetadataParamDefInfos = new LinkedList<FileMetadataParamDefInfo>();
    private List<ResearchGroupMembershipInfo> researchGroupMembershipInfos = new LinkedList<ResearchGroupMembershipInfo>();

    public int getResearchGroupId() {
        return researchGroupId;
    }

    public void setResearchGroupId(int researchGroupId) {
        this.researchGroupId = researchGroupId;
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

    public List<WeatherInfo> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<WeatherInfo> weathers) {
        this.weathers = weathers;
    }

    public List<PersonOptParamDefInfo> getPersonOptParamDefInfos() {
        return personOptParamDefInfos;
    }

    public void setPersonOptParamDefInfos(List<PersonOptParamDefInfo> personOptParamDefInfos) {
        this.personOptParamDefInfos = personOptParamDefInfos;
    }

    public List<ExperimentOptParamDefInfo> getExperimentOptParamDefInfos() {
        return experimentOptParamDefInfos;
    }

    public void setExperimentOptParamDefInfos(List<ExperimentOptParamDefInfo> experimentOptParamDefInfos) {
        this.experimentOptParamDefInfos = experimentOptParamDefInfos;
    }

    public List<FileMetadataParamDefInfo> getFileMetadataParamDefInfos() {
        return fileMetadataParamDefInfos;
    }

    public void setFileMetadataParamDefInfos(List<FileMetadataParamDefInfo> fileMetadataParamDefInfos) {
        this.fileMetadataParamDefInfos = fileMetadataParamDefInfos;
    }

    public int getPersonOwner() {
        return personOwner;
    }

    public void setPersonOwner(int personOwner) {
        this.personOwner = personOwner;
    }

    public List<ResearchGroupMembershipInfo> getResearchGroupMembershipInfos() {
        return researchGroupMembershipInfos;
    }

    public void setResearchGroupMembershipInfos(List<ResearchGroupMembershipInfo> researchGroupMembershipInfos) {
        this.researchGroupMembershipInfos = researchGroupMembershipInfos;
    }
}
