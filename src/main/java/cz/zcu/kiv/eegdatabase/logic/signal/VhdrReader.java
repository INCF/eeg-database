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
 *   VhdrReader.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.signal;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class VhdrReader {

    protected HashMap<String, HashMap<String, String>> properties;
    protected List<ChannelInfo> channels;
    protected HashMap<String, EEGMarker> markers;

    public VhdrReader() {
        properties = new HashMap<String, HashMap<String, String>>();
        channels = new ArrayList<ChannelInfo>();
        markers = new HashMap<String, EEGMarker>();
    }


    public void readVhdr(byte[] bytes) {
        properties.clear();
        channels.clear();
        markers.clear();
        String vhdrIn = "";
        for (int i = 0; i < bytes.length; i++) {
            vhdrIn = vhdrIn + "" + ((char) (bytes[i] & 0xFF));
        }
        String[] lines = vhdrIn.split("\n");

        readCommonInfos(lines);
        loadChannelInfo();
    }

    public void readVmrk(byte[] bytes) throws SQLException {
        String vmrkIn = "";
        for (int i = 0; i < bytes.length; i++) {
            vmrkIn = vmrkIn + "" + ((char) (bytes[i] & 0xFF));
        }
        String[] lines = vmrkIn.split("\n");

        readMarkersInfos(lines);
    }


    private void readMarkersInfos(String[] lines) {
        int index;
        for (index = 0; index < lines.length; index++) {
            if (lines[index].contains("Marker Infos")) {
                break;
            }
        }
        for (int i = index + 1; i < lines.length; i++) {
            if (!(lines[i].startsWith(";"))) {
                String[] arr = lines[i].split("[=]");
                String[] par = arr[1].split("[,]");

                if (par[0].equals("Stimulus")) {
                    EEGMarker marker = new EEGMarker();
                    marker.setName(arr[0]);
                    marker.setStimul(par[1]);
                    marker.setPosition(Integer.parseInt(par[2]));

                    markers.put(arr[0], marker.clone());
                }
            }
        }

    }

    private void readCommonInfos(String[] lines) {
        properties.put("CI", readInfo(lines, "Common Infos"));
        properties.put("BI", readInfo(lines, "Binary Infos"));
        properties.put("CH", readInfo(lines, "Channel Infos"));
    }

    private void loadChannelInfo() {
        int channelCnt = Integer.parseInt(properties.get("CI").get("NumberOfChannels"));
        for (int i = 1; i <= channelCnt; i++) {
            channels.add(new ChannelInfo(i, properties.get("CH").get("Ch" + i)));
        }

    }

    private HashMap<String, String> readInfo(String[] lines, String beginLine) {
        HashMap<String, String> info = new HashMap<String, String>();
        int index;
        for (index = 0; index < lines.length; index++) {
            if (lines[index].contains(beginLine)) {
                break;
            }
        }
        for (int i = index + 1; i < lines.length; i++) {
            if (!(lines[i].startsWith(";"))) {
                if (lines[i].trim().equals("")) {
                    break;
                }
                String[] arr = lines[i].trim().split("[=]");
                info.put(arr[0], arr[1]);
            }

        }
        return info;
    }

    public HashMap<String, HashMap<String, String>> getProperties() {
        return properties;
    }

    public List<ChannelInfo> getChannels() {
        return channels;
    }

    public HashMap<String, EEGMarker> getMarkers() {
        return markers;
    }
}
