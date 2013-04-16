package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.impl;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacadeImpl;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author Jakub Danek
 */
public class ExperimentPackageFacadeImpl extends GenericFacadeImpl<ExperimentPackage, Integer> implements ExperimentPackageFacade {

    private ExperimentPackageService experimentPackageService;

    public ExperimentPackageFacadeImpl() {
    }

    public ExperimentPackageFacadeImpl(GenericService<ExperimentPackage, Integer> service) {
	super(service);
    }

    @Required
    public void setExperimentPackageService(ExperimentPackageService experimentPackageService) {
	this.experimentPackageService = experimentPackageService;
    }

    @Override
    public List<ExperimentPackage> listExperimentPackagesByGroup(int researchGroupId) {
	return experimentPackageService.listExperimentPackagesByGroup(researchGroupId);
    }

}
