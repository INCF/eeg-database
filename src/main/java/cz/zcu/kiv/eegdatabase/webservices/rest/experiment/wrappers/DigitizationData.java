package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import cz.zcu.kiv.eegdatabase.data.pojo.Digitization;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Petr Miko
 *         Date: 9.3.13
 */
@XmlRootElement(name = "digitization")
@XmlType(propOrder = {"digitizationId", "gain", "filter", "samplingRate"})
public class DigitizationData {

    private int digitizationId;
    private String filter;
    private float gain, samplingRate;

    public int getDigitizationId() {
        return digitizationId;
    }

    public void setDigitizationId(int digitizationId) {
        this.digitizationId = digitizationId;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public float getGain() {
        return gain;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    public float getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(float samplingRate) {
        this.samplingRate = samplingRate;
    }
}
