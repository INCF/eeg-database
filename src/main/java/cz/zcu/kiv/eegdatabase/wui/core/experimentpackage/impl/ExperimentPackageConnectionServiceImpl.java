/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.impl;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageConnection;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageConnectionService;

/**
 *
 * @author veveri
 */
public class ExperimentPackageConnectionServiceImpl extends GenericServiceImpl<ExperimentPackageConnection, Integer>
								    implements ExperimentPackageConnectionService {

    public ExperimentPackageConnectionServiceImpl() {
    }

    public ExperimentPackageConnectionServiceImpl(GenericDao<ExperimentPackageConnection, Integer> dao) {
	super(dao);
    }


}
