package cz.zcu.kiv.eegdatabase.logic.signal;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 10.4.2011
 * Time: 17:46:31
 * To change this template use File | Settings | File Templates.
 */
public class EegReader {

    private VhdrReader info;
    private int channelCnt;
    private int dataShift;
    private Class binaryType;
    private byte[] eegData;

    public EegReader(VhdrReader info) {
        this.info = info;
        dataShift = Integer.parseInt(info.getProperties().get("CI").get("Prestimulus"));
        channelCnt = Integer.parseInt(info.getProperties().get("CI").get("NumberOfChannels"));
        if (info.getProperties().get("BI").get("BinaryFormat").equals("INT_16")) {
            binaryType = Integer.class;
        }
        if (info.getProperties().get("BI").get("BinaryFormat").equals("IEEE_FLOAT_32")) {
            binaryType = Float.class;
        }
    }

    public double[] readFile(DataFile binaryFile) throws SQLException {
        eegData = binaryFile.getFileContent().getBytes(1, (int) binaryFile.getFileContent().length());
        int channel = 5; // electrode
        
        return readOneChannel(-Integer.parseInt(info.getProperties().get("CI").get("Prestimulus")), channel);
    }

    private double[] readOneChannel(int time, int channel) {

        int len = eegData.length / (getBinarySize() * channelCnt);
        double[] ret;
        double[] channelSet = new double[channelCnt];
        double resolution = info.getChannels().get(channel).getResolution();
        ret = new double[len];
        for (int i = 0; i < len; i++) {
            channelSet = readChannelSet(time + dataShift + i, channelSet);
            ret[i] = (channelSet[channel - 1] * resolution);
        }
        
        return ret;
    }

    private double[] readChannelSet(int dataPos, double[] channelSet) {
        int index = dataPos * getBinarySize() * channelCnt;
        for (int i = 0; i < channelCnt; i++) {
            ByteBuffer bb = ByteBuffer.allocate(2);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.put(eegData[index]);
            bb.put(eegData[index+1]);
            channelSet[i] = bb.getShort(0);
            index = index+2;

        }
        return channelSet;
    }

    private int getBinarySize() {
        if (binaryType.equals(Float.class)) {
            return 4;
        }

        return 2; //default int16
    }
    
}
