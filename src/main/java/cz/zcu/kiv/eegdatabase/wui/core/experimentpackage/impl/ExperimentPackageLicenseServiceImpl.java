/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.impl;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageLicenseService;

/**
 *
 * @author veveri
 */
public class ExperimentPackageLicenseServiceImpl extends GenericServiceImpl<ExperimentPackageLicense, Integer> implements ExperimentPackageLicenseService{

    public ExperimentPackageLicenseServiceImpl() {
    }

    public ExperimentPackageLicenseServiceImpl(GenericDao<ExperimentPackageLicense, Integer> dao) {
	super(dao);
    }

    
}
