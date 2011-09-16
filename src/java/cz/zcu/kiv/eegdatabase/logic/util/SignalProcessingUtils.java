package cz.zcu.kiv.eegdatabase.logic.util;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.crypto.Data;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 31.5.2011
 * Time: 13:42:41
 * To change this template use File | Settings | File Templates.
 */
public class SignalProcessingUtils {

    public static List<String> getHeaders(Experiment e) throws Exception {
        List<String> headers = new ArrayList<String>();
        for (DataFile file: e.getDataFiles()) {
            if (file.getFilename().endsWith(".zip")) {
                Blob zipFile = file.getFileContent();
                ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream
                        (zipFile.getBytes(1, (int) zipFile.length())));
                ZipEntry entry = zis.getNextEntry();
                List<String> entryNames = new ArrayList<String>();
                while (entry != null) {
                    entryNames.add(entry.getName());
                    entry = zis.getNextEntry();
                }
                for (String entryName: entryNames) {
                    if (entryName.endsWith(".vhdr")) {
                        int index = entryName.lastIndexOf(".");
                        String name = entryName.substring(0, index);
                        for (String entryName2: entryNames) {
                            if ((entryName2.endsWith(".eeg")) || (entryName2.endsWith(".avg"))) {
                                if (entryName2.startsWith(name)) {
                                    headers.add(name);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (file.getFilename().endsWith(".vhdr")) {
                int index = file.getFilename().lastIndexOf(".");
                String name = file.getFilename().substring(0, index);
                for (DataFile file2: e.getDataFiles()) {
                    if ((file2.getFilename().endsWith(".eeg")) || (file2.getFilename().endsWith(".avg"))) {
                        if (file2.getFilename().startsWith(name)) {
                            headers.add(name);
                            break;
                        }
                    }
                }
            }
        }
        return headers;
    }

    public static void splitExperimentToView(ModelAndView mav, List<Experiment> list) throws Exception {
        List<Experiment> suitable = new ArrayList<Experiment>();
        List<Experiment> notSuitable = new ArrayList<Experiment>();
        for (Experiment e : list) {
            if (getHeaders(e).size() > 0) {
                suitable.add(e);
            } else {
                notSuitable.add(e);
            }
        }
        mav.addObject("suitable", suitable);
        mav.addObject("notSuitable", notSuitable);
    }

    public static byte[] extractZipEntry(ZipInputStream zis) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int data = 0;
        while ((data = zis.read()) != -1) {
            out.write(data);
        }
        byte[] bytes = out.toByteArray();
        out.close();
        return bytes;
    }
}
