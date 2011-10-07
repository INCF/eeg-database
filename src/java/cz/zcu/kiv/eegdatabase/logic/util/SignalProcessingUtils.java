package cz.zcu.kiv.eegdatabase.logic.util;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
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
        byte[] buff = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int data = 0;
        while ((data = zis.read(buff, 0, 1024)) != -1) {
            out.write(buff, 0, data);
        }
        byte[] bytes = out.toByteArray();
        out.close();
        return bytes;
    }
}
