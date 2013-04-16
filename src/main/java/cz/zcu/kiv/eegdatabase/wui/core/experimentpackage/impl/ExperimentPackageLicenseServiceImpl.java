package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.impl;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageLicenseService;

/**
 *
 * @author Jakub Danek
 */
public class ExperimentPackageLicenseServiceImpl extends GenericServiceImpl<ExperimentPackageLicense, Integer> implements ExperimentPackageLicenseService{

    public ExperimentPackageLicenseServiceImpl() {
    }

    public ExperimentPackageLicenseServiceImpl(GenericDao<ExperimentPackageLicense, Integer> dao) {
	super(dao);
    }

    
}
