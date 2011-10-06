package cz.zcu.kiv.eegdatabase.logic.signal;

import java.util.HashMap;
import java.util.List;


public interface DataTransformer {

    public double[] readBinaryData(byte[] binaryFile, int channel);

    public void readVhdr(byte[] file);

    public HashMap<String, HashMap<String, String>> getProperties();

    public List<ChannelInfo> getChannelInfo();
}
