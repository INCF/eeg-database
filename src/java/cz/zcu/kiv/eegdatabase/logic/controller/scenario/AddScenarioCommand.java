package cz.zcu.kiv.eegdatabase.logic.controller.scenario;

import org.springframework.web.multipart.MultipartFile;

public class AddScenarioCommand {

    private int id;
    private int researchGroup;
    private String title;
    private String length;
    private String description;
    private MultipartFile dataFile;
    private boolean privateNote;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResearchGroup() {
        return researchGroup;
    }

    public void setResearchGroup(int researchGroup) {
        this.researchGroup = researchGroup;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getDataFile() {
        return dataFile;
    }

    public void setDataFile(MultipartFile dataFile) {
        this.dataFile = dataFile;
    }

    public boolean getPrivateNote() {
        return this.privateNote;
    }

    public void setPrivateNote(boolean privateNote) {
        this.privateNote = privateNote;
    }

}
