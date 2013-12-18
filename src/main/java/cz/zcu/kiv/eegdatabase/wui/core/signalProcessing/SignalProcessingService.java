package cz.zcu.kiv.eegdatabase.wui.core.signalProcessing;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stebjan
 * Date: 11.12.13
 * Time: 13:15
 * To change this template use File | Settings | File Templates.
 */
public interface SignalProcessingService extends GenericService<Experiment, Integer> {

    public List<String> getAvailableMethods();

    public List<String> getSuitableHeaders(int experimentId);
}
