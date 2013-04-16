package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.impl;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentPackageDao;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jakub Danek
 */
public class ExperimentPackageServiceImpl extends GenericServiceImpl<ExperimentPackage, Integer> implements ExperimentPackageService {

    private ExperimentPackageDao experimentPackageDao;

    public ExperimentPackageServiceImpl() {
    }

    public ExperimentPackageServiceImpl(GenericDao<ExperimentPackage, Integer> dao) {
	super(dao);
    }

    @Required
    public void setExperimentPackageDao(ExperimentPackageDao experimentPackageDao) {
	this.experimentPackageDao = experimentPackageDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentPackage> listExperimentPackagesByGroup(int researchGroupId) {
	return experimentPackageDao.readByParameter("researchGroup.researchGroupId", researchGroupId);
    }

}
