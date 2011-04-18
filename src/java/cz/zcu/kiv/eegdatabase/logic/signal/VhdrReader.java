package cz.zcu.kiv.eegdatabase.logic.signal;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 10.4.2011
 * Time: 17:45:52
 * To change this template use File | Settings | File Templates.
 */
public class VhdrReader {

    protected HashMap<String, HashMap<String, String>> properties;
    protected HashMap<Integer, ChannelInfo> channels;
    protected HashMap<String, EEGMarker> markers;

    public VhdrReader() {
        properties = new HashMap<String, HashMap<String, String>>();
        channels = new HashMap<Integer, ChannelInfo>();
        markers = new HashMap<String, EEGMarker>();
    }


    public void readVhdr(DataFile vhdr) throws SQLException {
        byte[] bytes = vhdr.getFileContent().getBytes(1, (int) vhdr.getFileContent().length());
        String vhdrIn = "";
        for (int i = 0; i < bytes.length; i++) {
            vhdrIn = vhdrIn + "" + ((char) (bytes[i] & 0xFF));
        }
        String[] lines = vhdrIn.split("\n");

        readCommonInfos(lines);
        loadChannelInfo();
    }

    public void readVmrk(DataFile vmrk) throws SQLException {
        byte[] bytes = vmrk.getFileContent().getBytes(1, (int) vmrk.getFileContent().length());
        String vmrkIn = "";
        for (int i = 0; i < bytes.length; i++) {
            vmrkIn = vmrkIn + "" + ((char) (bytes[i] & 0xFF));
        }
        String[] lines = vmrkIn.split("\n");
        
        readMarkersInfos(lines);
    }


    private void readMarkersInfos(String[] lines)  {
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

    private void readCommonInfos(String[] lines)  {
        properties.put("CI", readInfo(lines, "Common Infos"));
        properties.put("BI", readInfo(lines, "Binary Infos"));
        properties.put("CH", readInfo(lines, "Channel Infos"));
    }

    private void loadChannelInfo()  {
        int channelCnt = Integer.parseInt(properties.get("CI").get("NumberOfChannels"));
        for (int i = 1; i <= channelCnt; i++) {
              channels.put(i, new ChannelInfo(properties.get("CH").get("Ch"+i)));
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
        for (int i = index+1; i < lines.length; i++) {
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

    public HashMap<Integer, ChannelInfo> getChannels() {
        return channels;
    }

    public HashMap<String, EEGMarker> getMarkers() {
        return markers;
    }
}
