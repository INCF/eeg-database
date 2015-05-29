package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.impl;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacadeImpl;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageLicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageLicenseService;
import org.springframework.beans.factory.annotation.Required;

/**
 * Created by Lichous on 11.5.15.
 */
public class ExperimentPackageLicenseFacadeImpl extends GenericFacadeImpl<ExperimentPackageLicense, Integer> implements ExperimentPackageLicenseFacade {

    private ExperimentPackageLicenseService service;

    public ExperimentPackageLicenseFacadeImpl(ExperimentPackageLicenseService expPacLicService) {
        super(expPacLicService);
        setService(expPacLicService);
    }

    @Required
    public void setService( ExperimentPackageLicenseService service) {
        this.service = service;
    }


}
