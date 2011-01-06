package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author JiPER
 */
public class AddDataFileCommand {

    private int measurationId;
    private String samplingRate;
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

    public String getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(String samplingRate) {
        this.samplingRate = samplingRate;
    }
}