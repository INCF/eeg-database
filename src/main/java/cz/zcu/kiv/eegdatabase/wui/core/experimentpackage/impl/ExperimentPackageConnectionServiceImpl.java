package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.impl;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentPackageConnectionDao;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageConnection;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageConnectionService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jakub Danek
 */
public class ExperimentPackageConnectionServiceImpl extends GenericServiceImpl<ExperimentPackageConnection, Integer>
								    implements ExperimentPackageConnectionService {

    private ExperimentPackageConnectionDao experimentPackageConnectionDao;

    public ExperimentPackageConnectionServiceImpl() {
    }

    public ExperimentPackageConnectionServiceImpl(GenericDao<ExperimentPackageConnection, Integer> dao) {
	super(dao);
    }

    @Required
    public void setExperimentPackageConnectionDao(ExperimentPackageConnectionDao experimentPackageConnectionDao) {
	this.experimentPackageConnectionDao = experimentPackageConnectionDao;
    }

    @Override
    @Transactional
    public boolean addExperimentToPackage(Experiment exp, ExperimentPackage pckg) {
        ExperimentPackageConnection experimentPackageConnection = new ExperimentPackageConnection();
        experimentPackageConnection.setExperiment(exp);
        experimentPackageConnection.setExperimentPackage(pckg);
        int id = experimentPackageConnectionDao.create(experimentPackageConnection);

	return id > 0;
    }

    @Override
    @Transactional
    public boolean removeExperimentFromPackage(Experiment exp, ExperimentPackage pckg) {
	return experimentPackageConnectionDao.removeExperimentFromPackage(exp.getExperimentId(), pckg.getExperimentPackageId());
    }
}
