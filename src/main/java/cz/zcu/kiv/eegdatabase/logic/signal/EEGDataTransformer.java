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
 *   EEGDataTransformer.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
