package cz.zcu.kiv.eegdatabase.logic.controller.list.hardware;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
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
public class HardwareMultiController extends MultiActionController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private HardwareDao hardwareDao;

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Processing hardware list controller");
        ModelAndView mav = new ModelAndView("lists/hardware/list");

        mav.addObject("userIsExperimenter", auth.userIsExperimenter());

        List<Hardware> list = hardwareDao.getItemsForList();
        mav.addObject("hardwareList", list);
        return mav;
    }

    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Deleting hardware.");
        ModelAndView mav = new ModelAndView("lists/itemDeleted");

        String idString = request.getParameter("id");
        if (idString != null) {
            int id = Integer.parseInt(idString);

            if (hardwareDao.canDelete(id)) {
                hardwareDao.delete(hardwareDao.read(id));
            } else {
                mav.setViewName("lists/itemUsed");
            }
        }

        return mav;
    }
}
