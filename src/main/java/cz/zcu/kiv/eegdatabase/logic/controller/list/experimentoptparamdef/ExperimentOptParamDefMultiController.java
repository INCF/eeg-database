package cz.zcu.kiv.eegdatabase.logic.controller.list.experimentoptparamdef;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author JiPER
 */
public class ExperimentOptParamDefMultiController extends MultiActionController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private ExperimentOptParamDefDao experimentOptParamDefDao;

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Processing measuration additional parameters list controller");
        ModelAndView mav = new ModelAndView("lists/measurationAdditionalParams/list");

        mav.addObject("userIsExperimenter", auth.userIsExperimenter());

        List<ExperimentOptParamDef> list = experimentOptParamDefDao.getItemsForList();
        mav.addObject("measurationAdditionalParamsList", list);
        return mav;
    }

    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Deleting experiment  optional parameter.");
        ModelAndView mav = new ModelAndView("lists/itemDeleted");

        String idString = request.getParameter("id");
        if (idString != null) {
            int id = Integer.parseInt(idString);

            if (experimentOptParamDefDao.canDelete(id)) {
                experimentOptParamDefDao.delete(experimentOptParamDefDao.read(id));
            } else {
                mav.setViewName("lists/itemUsed");
            }
        }

        return mav;
    }
}
