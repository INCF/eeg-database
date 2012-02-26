package cz.zcu.kiv.eegdatabase.webservices.dataDownload.wrappers;

/**
 * Wrapper for digitization information.
 *
 * @author Petr Miko
 */
public class DigitizationInfo {

    private float gain;
    private String filter;
    private float samplingRate;

    public float getGain() {
        return gain;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public float getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(float samplingRate) {
        this.samplingRate = samplingRate;
    }
}
