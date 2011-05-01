package cz.zcu.kiv.eegdatabase.logic.controller.service;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.signal.ChannelInfo;
import cz.zcu.kiv.eegdatabase.logic.signal.DataTransformer;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class AbstractProcessingController extends SimpleFormController {

    protected GenericDao<Experiment, Integer> experimentDao;
    protected DataTransformer transformer;

    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map map = new HashMap<String, Object>();
        int id = Integer.parseInt(request.getParameter("experimentId"));
        Experiment experiment = experimentDao.read(id);
        DataFile vhdr = null;
        for (DataFile d : experiment.getDataFiles()) {
            if (d.getFilename().endsWith(".vhdr")) {
                vhdr = d;
                break;
            }
        }
        byte[] bytes = vhdr.getFileContent().getBytes(1, (int) vhdr.getFileContent().length());
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
