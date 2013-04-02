/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.impl;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacadeImpl;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;

/**
 *
 * @author veveri
 */
public class ExperimentPackageFacadeImpl extends GenericFacadeImpl<ExperimentPackage, Integer> implements ExperimentPackageFacade {

    public ExperimentPackageFacadeImpl() {
    }

    public ExperimentPackageFacadeImpl(GenericService<ExperimentPackage, Integer> service) {
	super(service);
    }

    
}
