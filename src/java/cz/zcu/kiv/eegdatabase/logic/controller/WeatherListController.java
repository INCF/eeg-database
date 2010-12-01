package cz.zcu.kiv.eegdatabase.logic.controller;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class WeatherListController extends AbstractController {

    private Log log = LogFactory.getLog(getClass());
    private AuthorizationManager auth;
    private GenericDao<Weather, Integer> weatherDao;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Processing hardware list controller");
        ModelAndView mav = new ModelAndView("lists/weather/list");

        mav.addObject("userIsExperimenter", auth.userIsExperimenter());

        List<Weather> list = weatherDao.getAllRecords();
        mav.addObject("weatherList", list);
        return mav;
    }

    public GenericDao<Weather, Integer> getWeatherDao() {
        return weatherDao;
    }

    public void setWeatherDao(GenericDao<Weather, Integer> weatherDao) {
        this.weatherDao = weatherDao;
    }

    public AuthorizationManager getAuth() {
        return auth;
    }

    public void setAuth(AuthorizationManager auth) {
        this.auth = auth;
    }
}
