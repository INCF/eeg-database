package cz.zcu.kiv.eegdatabase.logic.signal;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 10.4.2011
 * Time: 17:37:21
 * To change this template use File | Settings | File Templates.
 */
public class ChannelInfo {

    protected String name;
    protected float resolution;
    protected String units;

    public final String getName() {
        return name;
    }

    public final double getResolution() {
        return resolution;
    }

    public final String getUnits() {
        return units;
    }

    /**
    Create new info from defined string

    @param strData Ch<Channel number>=<Name>,<Reference channel name>,
    <Resolution in "Unit">,<Unit>
     */
    public ChannelInfo(String strData) {

        String[] arr = strData.split("[,]", -1);
        name = arr[0];
        resolution = Float.parseFloat(arr[2]);
        units = arr[3];
    }
}
