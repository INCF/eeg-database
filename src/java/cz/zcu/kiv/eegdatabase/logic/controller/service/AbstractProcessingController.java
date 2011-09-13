package cz.zcu.kiv.eegdatabase.logic.controller.service;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.signal.ChannelInfo;
import cz.zcu.kiv.eegdatabase.logic.signal.DataTransformer;
import cz.zcu.kiv.eegdatabase.logic.util.SignalProcessingUtils;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.sql.Blob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public abstract class AbstractProcessingController extends SimpleFormController {

    protected GenericDao<Experiment, Integer> experimentDao;
    protected DataTransformer transformer;

    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map map = new HashMap<String, Object>();
        int id = Integer.parseInt(request.getParameter("experimentId"));
        Experiment experiment = experimentDao.read(id);
        byte[] bytes = null;
        for (DataFile d : experiment.getDataFiles()) {
            if (d.getFilename().endsWith(".zip")) {
                Blob zipFile = d.getFileContent();
                ZipInputStream zis = new ZipInputStream
                        (new ByteArrayInputStream(zipFile.getBytes(1, (int) zipFile.length())));
                ZipEntry entry = zis.getNextEntry();
                while (entry != null) {
                    if (entry.getName().endsWith(".vhdr")) {
                        bytes = SignalProcessingUtils.extractZipEntry(zis);
                        break;
                    }
                    entry = zis.getNextEntry();
                }
                break;
            }
            if (d.getFilename().endsWith(".vhdr")) {
                bytes = d.getFileContent().getBytes(1, (int) d.getFileContent().length());

                break;
            }
        }
        transformer.readVhdr(bytes);
        List<ChannelInfo> channels = transformer.getChannelInfo();
        map.put("channels", channels);
        map.put("experimentId", id);

        return map;
    }

    public GenericDao<Experiment, Integer> getExperimentDao() {
        return experimentDao;
    }

    public void setExperimentDao(GenericDao<Experiment, Integer> experimentDao) {
        this.experimentDao = experimentDao;
    }

    public DataTransformer getTransformer() {
        return transformer;
    }

    public void setTransformer(DataTransformer transformer) {
        this.transformer = transformer;
    }
}
