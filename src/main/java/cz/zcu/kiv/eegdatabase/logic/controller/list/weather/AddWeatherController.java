package cz.zcu.kiv.eegdatabase.logic.controller.list.weather;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.WeatherDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddWeatherController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private WeatherDao weatherDao;

    public AddWeatherController() {
        setCommandClass(AddWeatherCommand.class);
        setCommandName("addWeather");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        AddWeatherCommand data = (AddWeatherCommand) super.formBackingObject(request);

        String idString = request.getParameter("id");

        if (idString != null) {
            // Editation of existing hardware
            int id = Integer.parseInt(idString);

            log.debug("Loading weather to the command object for editing.");
            Weather weather = weatherDao.read(id);

            data.setId(id);
            data.setTitle(weather.getTitle());
            data.setDescription(weather.getDescription());
        }
        return data;
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        ModelAndView mav = super.showForm(request, response, errors);

        if (!auth.userIsExperimenter()) {
            mav.setViewName("lists/userNotExperimenter");
        }
        mav.addObject("userIsExperimenter", auth.userIsExperimenter());

        return mav;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        ModelAndView mav = new ModelAndView(getSuccessView());

        log.debug("Processing form data.");
        AddWeatherCommand data = (AddWeatherCommand) command;

        if (!auth.userIsExperimenter()) {
            mav.setViewName("lists/userNotExperimenter");
        }

        Weather weather;
        if (data.getId() > 0) {
            // Editing
            log.debug("Editing existing weather object.");
            weather = weatherDao.read(data.getId());
            weather.setTitle(data.getTitle());
            weather.setDescription(data.getDescription());
            weatherDao.update(weather);
        } else {
            // Creating new
            log.debug("Creating new weather object.");
            weather = new Weather();
            weather.setTitle(data.getTitle());
            weather.setDescription(data.getDescription());
            weatherDao.create(weather);
        }

        log.debug("Returning MAV");
        return mav;
    }
}
