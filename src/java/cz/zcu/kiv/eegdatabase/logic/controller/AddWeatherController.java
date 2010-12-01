package cz.zcu.kiv.eegdatabase.logic.controller;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.AddWeatherCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddWeatherController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    private AuthorizationManager auth;
    private GenericDao<Weather, Integer> weatherDao;

    public AddWeatherController() {
        setCommandClass(AddWeatherCommand.class);
        setCommandName("addWeather");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        ModelAndView mav = new ModelAndView(getSuccessView());

        log.debug("Processing form data.");
        AddWeatherCommand data = (AddWeatherCommand) command;

        if (!auth.userIsExperimenter()) {
            mav.setViewName("lists/userNotExperimenter");
        }

        log.debug("Creating new object");
        Weather weather = new Weather();
        weather.setTitle(data.getTitle());
        weather.setDescription(data.getDescription());

        log.debug("Saving data to database");
        weatherDao.create(weather);

        log.debug("Returning MAV");
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
