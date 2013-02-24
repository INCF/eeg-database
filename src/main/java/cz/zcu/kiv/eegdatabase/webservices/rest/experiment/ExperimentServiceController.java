package cz.zcu.kiv.eegdatabase.webservices.rest.experiment;

import cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers.ExperimentDataList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
@Secured("IS_AUTHENTICATED_FULLY")
@Controller
@RequestMapping("/experiments")
public class ExperimentServiceController {

    @Autowired
    private ExperimentService service;

    @RequestMapping(value = "/all")
    public ExperimentDataList getAllExperiments() {
        return new ExperimentDataList(service.getAllExperiments());
    }

    @RequestMapping(value = "/mine")
    public ExperimentDataList getMyExperiments() {
        return new ExperimentDataList(service.getMyExperiments());
    }
}
