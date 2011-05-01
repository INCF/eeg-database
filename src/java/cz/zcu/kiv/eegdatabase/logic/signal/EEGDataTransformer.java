package cz.zcu.kiv.eegdatabase.logic.signal;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class EEGDataTransformer implements DataTransformer {

    private VhdrReader reader = new VhdrReader();

    public boolean isSuitableExperiment(Experiment e) {
        boolean vhdr = false;
        boolean eeg = false;
        Set<DataFile> files = e.getDataFiles();
        for (DataFile file : files) {
            if (file.getFilename().endsWith(".vhdr")) {
                vhdr = true;
            }
            if ((file.getFilename().endsWith(".eeg")) || (file.getFilename().endsWith(".avg"))) {
                eeg = true;
            }
        }
        return (vhdr && eeg);
    }

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
