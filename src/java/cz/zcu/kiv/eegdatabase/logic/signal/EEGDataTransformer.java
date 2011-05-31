package cz.zcu.kiv.eegdatabase.logic.signal;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class EEGDataTransformer implements DataTransformer {

    private VhdrReader reader = new VhdrReader();

    public double[] readBinaryData(byte[] binaryFile, int channel) {
        EegReader eeg = new EegReader(reader);
       return eeg.readFile(binaryFile, channel);
    }

    public void readVhdr(byte[] file) {
        reader.readVhdr(file);
    }

    public HashMap<String, HashMap<String, String>> getProperties() {
        return reader.getProperties();
    }

    public List<ChannelInfo> getChannelInfo() {
        return reader.getChannels();
    }

}
