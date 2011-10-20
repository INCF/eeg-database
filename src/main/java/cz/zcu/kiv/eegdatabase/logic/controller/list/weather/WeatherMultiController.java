package cz.zcu.kiv.eegdatabase.logic.controller.list.weather;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.WeatherDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class WeatherMultiController extends MultiActionController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private WeatherDao weatherDao;

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Processing weather list controller");
        ModelAndView mav = new ModelAndView("lists/weather/list");

        mav.addObject("userIsExperimenter", auth.userIsExperimenter());

        List<Weather> list = weatherDao.getItemsForList();
        mav.addObject("weatherList", list);
        return mav;
    }

    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Deleting weather.");
        ModelAndView mav = new ModelAndView("lists/itemDeleted");

        String idString = request.getParameter("id");
        if (idString != null) {
            int id = Integer.parseInt(idString);

            if (weatherDao.canDelete(id)) {
                weatherDao.delete(weatherDao.read(id));
            } else {
                mav.setViewName("lists/itemUsed");
            }
        }

        return mav;
    }
}
