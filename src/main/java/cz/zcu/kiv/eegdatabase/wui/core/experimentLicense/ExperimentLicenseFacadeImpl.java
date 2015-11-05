package cz.zcu.kiv.eegdatabase.wui.core.experimentLicense;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentLicence;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacadeImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Created by Lichous on 28.4.15.
 */
public class ExperimentLicenseFacadeImpl extends GenericFacadeImpl<ExperimentLicence, Integer> implements ExperimentLicenseFacade{

    protected Log log = LogFactory.getLog(getClass());
    private ExperimentLicenseService service;

    public ExperimentLicenseFacadeImpl(ExperimentLicenseService membershipPlanService) {
        super(membershipPlanService);
        setService(membershipPlanService);
    }

    @Required
    public void setService( ExperimentLicenseService service) {
        this.service = service;
    }

    @Override
    public void remove(Experiment experiment, License license) {
        service.remove(experiment,license);
    }

    @Override
    public List<ExperimentLicence> getExperimentLicensesForExperiment(Experiment experiment) {
        return service.getExperimentLicensesForExperiment(experiment);
    }

    @Override
    public ExperimentLicence getExperimentLicense(Experiment experiment, License license) {
        return service.getExperimentLicense(experiment, license);
    }


}
