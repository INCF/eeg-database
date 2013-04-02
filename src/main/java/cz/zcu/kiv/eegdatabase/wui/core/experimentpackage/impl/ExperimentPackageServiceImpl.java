/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.impl;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageService;

/**
 *
 * @author veveri
 */
public class ExperimentPackageServiceImpl extends GenericServiceImpl<ExperimentPackage, Integer> implements ExperimentPackageService {

    public ExperimentPackageServiceImpl() {
    }

    public ExperimentPackageServiceImpl(GenericDao<ExperimentPackage, Integer> dao) {
	super(dao);
    }

}
