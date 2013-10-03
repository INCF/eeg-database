/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   EegReader.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.signal;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.SQLException;

public class EegReader {

    private VhdrReader info;
    private int channelCnt;
    private Class binaryType;
    private byte[] eegData;

    public EegReader(VhdrReader info) {
        this.info = info;
        channelCnt = Integer.parseInt(info.getProperties().get("CI").get("NumberOfChannels"));
        if (info.getProperties().get("BI").get("BinaryFormat").equals("INT_16")) {
            binaryType = Integer.class;
        }
        if (info.getProperties().get("BI").get("BinaryFormat").equals("IEEE_FLOAT_32")) {
            binaryType = Float.class;
        }
    }

    public double[] readFile(byte[] binaryFile, int channel) {
        eegData = binaryFile;
        
        return readOneChannel(channel);
    }

    private double[] readOneChannel(int channel) {
        int len = eegData.length / (getBinarySize() * channelCnt);
        double[] ret;
        double[] channelSet = new double[channelCnt];
        double resolution = info.getChannels().get(channel-1).getResolution();
        ret = new double[len];
        for (int i = 0; i < len; i++) {
            channelSet = readChannelSet(i, channelSet);
            ret[i] = (channelSet[channel - 1] * resolution);
        }
        return ret;
    }

    private double[] readChannelSet(int dataPos, double[] channelSet) {
        int index = dataPos * getBinarySize() * channelCnt;
        for (int i = 0; i < channelCnt; i++) {
            if (getBinarySize() == 2) {
                ByteBuffer bb = ByteBuffer.allocate(2);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                bb.put(eegData[index]);
                bb.put(eegData[index + 1]);
                channelSet[i] = bb.getShort(0);
                index = index + 2;
            }
            else {
                 ByteBuffer bb = ByteBuffer.allocate(4);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                bb.put(eegData[index]);
                bb.put(eegData[index + 1]);
                bb.put(eegData[index + 2]);
                bb.put(eegData[index + 3]);
                channelSet[i] = bb.getFloat(0);
                index = index + 4;
            }
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
