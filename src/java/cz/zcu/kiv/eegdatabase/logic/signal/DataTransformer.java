package cz.zcu.kiv.eegdatabase.logic.signal;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 10.4.2011
 * Time: 17:22:59
 * To change this template use File | Settings | File Templates.
 */
public interface DataTransformer {

    public boolean isSuitableExperiment(Experiment e);

    public double[] transformExperimentalData(Experiment e);
}
