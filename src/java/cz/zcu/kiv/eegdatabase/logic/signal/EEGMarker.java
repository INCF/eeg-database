package cz.zcu.kiv.eegdatabase.logic.signal;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 10.4.2011
 * Time: 17:39:17
 * To change this template use File | Settings | File Templates.
 */
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
