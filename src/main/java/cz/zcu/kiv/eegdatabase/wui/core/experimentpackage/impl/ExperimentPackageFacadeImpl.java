package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.impl;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacadeImpl;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageConnectionService;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageService;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author Jakub Danek
 */
public class ExperimentPackageFacadeImpl extends GenericFacadeImpl<ExperimentPackage, Integer> implements ExperimentPackageFacade {

    private ExperimentPackageService experimentPackageService;
    private ExperimentPackageConnectionService experimentPackageConnectionService;
	private LicenseService licenseServise;

    public ExperimentPackageFacadeImpl() {
    }

    public ExperimentPackageFacadeImpl(GenericService<ExperimentPackage, Integer> service) {
	super(service);
    }

    @Required
    public void setExperimentPackageService(ExperimentPackageService experimentPackageService) {
	this.experimentPackageService = experimentPackageService;
    }

    @Required
    public void setExperimentPackageConnectionService(ExperimentPackageConnectionService experimentPackageConnectionService) {
	this.experimentPackageConnectionService = experimentPackageConnectionService;
    }
	
	@Required
    public void setLicenseService(LicenseService licenseServise) {
		this.licenseServise = licenseServise;
    }

    @Override
    public List<ExperimentPackage> listExperimentPackagesByGroup(int researchGroupId) {
	return experimentPackageService.listExperimentPackagesByGroup(researchGroupId);
    }

    @Override
    public boolean addExperimentToPackage(Experiment exp, ExperimentPackage pckg) {
	return this.experimentPackageConnectionService.addExperimentToPackage(exp, pckg);
    }

    @Override
    public int addExperimentsToPackage(List<Experiment> exp, ExperimentPackage pckg) {
	int i = 0;
	for(Experiment e : exp) {
	    if(this.addExperimentToPackage(e, pckg)) {
		i++;
	    }
	}
	return i;
    }

    @Override
    public boolean removeExperimentFromPackage(Experiment exp, ExperimentPackage pckg) {
	return experimentPackageConnectionService.removeExperimentFromPackage(exp, pckg);
    }

    @Override
    public int removeExperimentsFromPackage(List<Experiment> exp, ExperimentPackage pckg) {
	int i = 0;
	for(Experiment e : exp) {
	    if(this.removeExperimentFromPackage(e, pckg)) {
		i++;
	    }
	}
	return i;
    }

	@Override
	public void createExperimentPackage(ExperimentPackage pckg, License license) {
		int id = experimentPackageService.create(pckg);
		pckg.setExperimentPackageId(id);
		licenseServise.addLicenseForPackage(license, pckg);
	}

	@Override
	public void removeExperimentPackage(ExperimentPackage pckg) {
		//TODO implement this, #2096
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
