package cz.zcu.kiv.eegdatabase.logic.signal;

public class EEGMarker {

    protected String name;
    protected String stimul;
    protected int position;

    public EEGMarker clone() {
        EEGMarker varCopy = new EEGMarker();

        varCopy.name = this.name;
        varCopy.stimul = this.stimul;
        varCopy.position = this.position;

        return varCopy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStimul() {
        return stimul;
    }

    public void setStimul(String stimul) {
        this.stimul = stimul;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
