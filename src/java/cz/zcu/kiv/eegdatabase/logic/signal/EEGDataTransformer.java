package cz.zcu.kiv.eegdatabase.logic.signal;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;

import java.sql.SQLException;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 10.4.2011
 * Time: 17:25:17
 * To change this template use File | Settings | File Templates.
 */
public class EEGDataTransformer implements DataTransformer {

    public boolean isSuitableExperiment(Experiment e) {
        boolean vhdr = false;
        boolean eeg = false;
        Set<DataFile> files = e.getDataFiles();
        for (DataFile file: files) {
            if (file.getFilename().endsWith(".vhdr")) {
                vhdr = true;
            }
            if (file.getFilename().endsWith(".eeg")) {
                eeg = true;
            }
        }
        return (vhdr && eeg);
    }

    public double[] transformExperimentalData(Experiment e) {
        VhdrReader r = new VhdrReader();
        EegReader eeg;
        DataFile vhdr = null;
        DataFile vmrk = null;
        DataFile binaryFile = null;
        double[] data = null;
        for (DataFile file: e.getDataFiles()) {
            if (file.getFilename().endsWith(".vhdr")) {
                vhdr = file;
            }
            if (file.getFilename().endsWith(".vmrk")) {
                vmrk = file;
            }
        }
        try {
            r.readVhdr(vhdr);
            for (DataFile file: e.getDataFiles()) {
                if (file.getFilename().equals(r.getProperties().get("CI").get("DataFile"))) {
                    binaryFile = file;
                    break;
                }
            }
            if (vmrk != null) {
                r.readVmrk(vmrk);
            }
            eeg = new EegReader(r);
            data = eeg.readFile(binaryFile);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        return data; 
    }
}
