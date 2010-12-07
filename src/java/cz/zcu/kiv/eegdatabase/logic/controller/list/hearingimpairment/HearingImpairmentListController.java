package cz.zcu.kiv.eegdatabase.logic.controller.list.hearingimpairment;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
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
    private GenericDao<HearingImpairment, Integer> hearingImpairmentDao;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Processing hearing defect list controller");
        ModelAndView mav = new ModelAndView("lists/hearingDefect/list");

        mav.addObject("userIsExperimenter", auth.userIsExperimenter());

        List<HearingImpairment> list = hearingImpairmentDao.getAllRecords();
        mav.addObject("hearingDefectList", list);
        return mav;
    }

    public GenericDao<HearingImpairment, Integer> getHearingImpairmentDao() {
        return hearingImpairmentDao;
    }

    public void setHearingImpairmentDao(GenericDao<HearingImpairment, Integer> hearingImpairmentDao) {
        this.hearingImpairmentDao = hearingImpairmentDao;
    }
}
