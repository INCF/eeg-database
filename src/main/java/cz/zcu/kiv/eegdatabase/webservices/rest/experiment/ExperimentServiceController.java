package cz.zcu.kiv.eegdatabase.webservices.rest.experiment;

import cz.zcu.kiv.eegdatabase.webservices.rest.common.wrappers.RecordCountData;
import cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers.ExperimentDataList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.PathParam;

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

    @RequestMapping(value = "/public/{fromId}/{count}")
    public ExperimentDataList getPublicExperiments(@PathVariable int fromId, @PathVariable int count) {
        return new ExperimentDataList(service.getPublicExperiments(fromId, count));
    }

    @RequestMapping(value = "/public/{count}")
    public ExperimentDataList getPublicExperiments(@PathVariable int count) {
        return new ExperimentDataList(service.getPublicExperiments(0, count));
    }

    @RequestMapping(value = "/count")
    public RecordCountData getCount() {
        RecordCountData countData = new RecordCountData();
        countData.setMyRecords(service.getMyExperiments().size());
        countData.setPublicRecords(service.getPublicExperimentsCount());
        return countData;
    }

    @RequestMapping(value = "/mine")
    public ExperimentDataList getMyExperiments() {
        return new ExperimentDataList(service.getMyExperiments());
    }
}
