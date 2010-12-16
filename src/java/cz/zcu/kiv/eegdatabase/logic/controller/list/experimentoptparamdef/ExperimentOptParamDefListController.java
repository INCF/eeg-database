package cz.zcu.kiv.eegdatabase.logic.controller.list.experimentoptparamdef;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author JiPER
 */
public class ExperimentOptParamDefListController extends AbstractController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private ExperimentOptParamDefDao experimentOptParamDefDao;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Processing measuration additional parameters list controller");
        ModelAndView mav = new ModelAndView("lists/measurationAdditionalParams/list");

        mav.addObject("userIsExperimenter", auth.userIsExperimenter());


        List<ExperimentOptParamDef> list = experimentOptParamDefDao.getItemsForList();
        mav.addObject("measurationAdditionalParamsList", list);
        return mav;
    }
}
