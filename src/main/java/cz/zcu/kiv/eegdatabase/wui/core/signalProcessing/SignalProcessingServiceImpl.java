package cz.zcu.kiv.eegdatabase.wui.core.signalProcessing;

import cz.zcu.kiv.eegdatabase.webservices.EDPClient.ProcessService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stebjan
 * Date: 11.12.13
 * Time: 14:11
 * To change this template use File | Settings | File Templates.
 */
public class SignalProcessingServiceImpl implements SignalProcessingService {


    private ProcessService eegService;

    @Override
    public List<String> getAvailableMethods() {

        return eegService.getAvailableMethods();
    }

    public ProcessService getEegService() {
        return eegService;
    }

    public void setEegService(ProcessService eegService) {
        this.eegService = eegService;
    }

}
