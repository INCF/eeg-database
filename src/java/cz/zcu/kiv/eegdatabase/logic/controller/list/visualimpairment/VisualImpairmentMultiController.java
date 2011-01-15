package cz.zcu.kiv.eegdatabase.logic.controller.list.visualimpairment;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.VisualImpairmentDao;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author JiPER
 */
public class VisualImpairmentMultiController extends MultiActionController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private VisualImpairmentDao visualImpairmentDao;

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Processing eyes defect list controller");
        ModelAndView mav = new ModelAndView("lists/eyesDefect/list");

        mav.addObject("userIsExperimenter", auth.userIsExperimenter());

        List<VisualImpairment> list = visualImpairmentDao.getItemsForList();
        mav.addObject("eyesDefectList", list);
        return mav;
    }

    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Deleting visual impairment.");
        ModelAndView mav = new ModelAndView("/lists/itemDeleted");

        String idString = request.getParameter("id");
        if (idString != null) {
            int id = Integer.parseInt(idString);

            if (visualImpairmentDao.canDelete(id)) {
                visualImpairmentDao.delete(visualImpairmentDao.read(id));
            } else {
                mav.setViewName("/lists/itemUsed");
            }
        }

        return mav;
    }
}
