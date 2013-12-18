package cz.zcu.kiv.eegdatabase.wui.core.signalProcessing;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.util.SignalProcessingUtils;
import cz.zcu.kiv.eegdatabase.webservices.EDPClient.ProcessService;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stebjan
 * Date: 11.12.13
 * Time: 14:11
 * To change this template use File | Settings | File Templates.
 */
public class SignalProcessingServiceImpl extends GenericServiceImpl<Experiment, Integer>
        implements SignalProcessingService {
    @Autowired
    private ExperimentDao<Experiment, Integer> experimentDao;
    private ProcessService eegService;


    @Override
    public List<String> getAvailableMethods() {

        return eegService.getAvailableMethods();
    }

    @Transactional
    @Override
    public List<String> getSuitableHeaders(int experimentId) {
        Experiment e = experimentDao.read(experimentId);
        return SignalProcessingUtils.getHeaders(e);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ProcessService getEegService() {
        return eegService;
    }

    public void setEegService(ProcessService eegService) {
        this.eegService = eegService;
    }

}
