package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Data container of electrode location data for XML marshaling purposes.
 *
 * @author Petr Miko
 */
@XmlType(propOrder = {"id", "title", "abbr", "description", "defaultNumber", "electrodeFix", "electrodeType"})
@XmlRootElement(name = "electrodeLocation")
public class ElectrodeLocationData {

    private int id;
    private String title;
    private String abbr;
    private String description;
    private int defaultNumber;
    private ElectrodeFixData electrodeFix;
    private ElectrodeTypeData electrodeType;

    @XmlElement(required = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement(required = false)
    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    @XmlElement(required = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(required = false)
    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
    }

    public ElectrodeFixData getElectrodeFix() {
        return electrodeFix;
    }

    public void setElectrodeFix(ElectrodeFixData electrodeFix) {
        this.electrodeFix = electrodeFix;
    }

    public ElectrodeTypeData getElectrodeType() {
        return electrodeType;
    }

    public void setElectrodeType(ElectrodeTypeData electrodeType) {
        this.electrodeType = electrodeType;
    }
}
