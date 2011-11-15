package cz.zcu.kiv.eegdatabase.logic.controller.service;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ServiceResultDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.signal.ChannelInfo;
import cz.zcu.kiv.eegdatabase.logic.signal.DataTransformer;
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
    protected ServiceResultDao resultDao;
    protected PersonDao personDao;
    protected DataTransformer transformer;
    protected String fileName;

    protected Map referenceData(HttpServletRequest request) throws Exception {
        String header = request.getParameter("headerName");
        Map map = new HashMap<String, Object>();
        int id = Integer.parseInt(request.getParameter("experimentId"));
        Experiment experiment = experimentDao.read(id);
        byte[] bytes = null;
        for (DataFile d : experiment.getDataFiles()) {
            if ((d.getFilename().endsWith(".vhdr"))&&(d.getFilename().startsWith(header))) {
                bytes = d.getFileContent().getBytes(1, (int) d.getFileContent().length());
                int index = d.getFilename().lastIndexOf(".");
                fileName = d.getFilename().substring(0, index);
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

    public ServiceResultDao getResultDao() {
        return resultDao;
    }

    public void setResultDao(ServiceResultDao resultDao) {
        this.resultDao = resultDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
