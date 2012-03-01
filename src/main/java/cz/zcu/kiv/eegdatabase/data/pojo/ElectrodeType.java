package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 1.3.12
 * Time: 14:04
 * To change this template use File | Settings | File Templates.
 */
public class ElectrodeType implements Serializable {

    private int electrodeTypeId;
    private String title;
    private String description;
    private int defaultNumber;
    //vazba

    public ElectrodeType() {
    }

    public int getElectrodeTypeId() {
        return electrodeTypeId;
    }

    public void setElectrodeTypeId(int electrodeTypeId) {
        this.electrodeTypeId = electrodeTypeId;
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

    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
    }
}
