package cz.zcu.kiv.eegdatabase.logic.controller.list.hearingimpairment;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.HearingImpairmentDao;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;
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
public class HearingImpairmentListController extends AbstractController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private HearingImpairmentDao hearingImpairmentDao;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Processing hearing defect list controller");
        ModelAndView mav = new ModelAndView("lists/hearingDefect/list");

        mav.addObject("userIsExperimenter", auth.userIsExperimenter());

        List<HearingImpairment> list = hearingImpairmentDao.getItemsForList();
        mav.addObject("hearingDefectList", list);
        return mav;
    }
}
