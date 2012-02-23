package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author JiPER
 */
public class AddDataFileCommand {

    private int measurationId;
    private String description;
    private MultipartFile dataFile;

    public int getMeasurationId() {
        return measurationId;
    }

    public void setMeasurationId(int measurationId) {
        this.measurationId = measurationId;
    }

    public MultipartFile getDataFile() {
        return dataFile;
    }

    public void setDataFile(MultipartFile dataFile) {
        this.dataFile = dataFile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}