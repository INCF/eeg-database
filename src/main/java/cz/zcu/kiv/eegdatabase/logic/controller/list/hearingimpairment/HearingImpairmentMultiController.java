package cz.zcu.kiv.eegdatabase.logic.controller.list.hearingimpairment;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.HearingImpairmentDao;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class HearingImpairmentMultiController extends MultiActionController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private HearingImpairmentDao hearingImpairmentDao;

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Processing hearing defect list controller");
        ModelAndView mav = new ModelAndView("lists/hearingDefect/list");

        mav.addObject("userIsExperimenter", auth.userIsExperimenter());

        List<HearingImpairment> list = hearingImpairmentDao.getItemsForList();
        mav.addObject("hearingDefectList", list);
        return mav;
    }

    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Deleting hearing impairment.");
        ModelAndView mav = new ModelAndView("lists/itemDeleted");

        String idString = request.getParameter("id");
        if (idString != null) {
            int id = Integer.parseInt(idString);

            if (hearingImpairmentDao.canDelete(id)) {
                hearingImpairmentDao.delete(hearingImpairmentDao.read(id));
            } else {
                mav.setViewName("lists/itemUsed");
            }
        }

        return mav;
    }
}
