package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.WeatherDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import net.sf.json.JSONObject;

/**
 * Adding new weather, person atd. to database
 * User: pbruha
 * Date: 3.4.11
 * Time: 11:00
 * To change this template use File | Settings | File Templates.
 */
public class WizardAjaxMultiController extends MultiActionController{
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private WeatherDao weatherDao;

    /**
     * Added new weather to database
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ModelAndView addNewWeather(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView("experiments/JSonView");
        log.debug("WizardAjaxMultiController - Add new weather.");
        String weatherTitle;
        String weatherDescription;
        Weather weather = null;
        weatherTitle = request.getParameter("title");
        weatherDescription = request.getParameter("description");
        JSONObject jo = new JSONObject();
        if (!weatherTitle.equals("") && !weatherDescription.equals("") && weatherDao.canSaveNewDescription(weatherDescription)) {
            log.debug("Creating new weather object.");
            weather = new Weather();
            weather.setTitle(weatherTitle);
            weather.setDescription(weatherDescription);
            weatherDao.create(weather);
            log.debug("Saving attribute - success: true");
            jo.put("success", true);
            log.debug("Saving attribute - weatherId: "+weather.getWeatherId());
            jo.put("weatherId", weather.getWeatherId());
        } else {
             log.debug("Saving attribute - success: false");
             jo.put("success", false);
        }
        log.debug("Saving  JSONObject: "+jo);
        mav.addObject("result",jo);
        return mav;
    }
}
