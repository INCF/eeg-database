package cz.zcu.kiv.eegdatabase.logic.controller.list.weather;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.WeatherDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.data.pojo.WeatherGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.WeatherGroupRelId;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;
import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes("addWeather")
public class AddWeatherController{
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private WeatherDao weatherDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private HierarchicalMessageSource messageSource;

    private final int DEFAULT_ID = -1;
    AddWeatherValidator addWeatherValidator;

    @Autowired
	public AddWeatherController(AddWeatherValidator addWeatherValidator){
		this.addWeatherValidator = addWeatherValidator;
	}

    @RequestMapping(value="lists/weather-definitions/edit.html", method= RequestMethod.GET)
    protected String showEditForm(@RequestParam("id") String idString, @RequestParam("groupid") String idString2, ModelMap model, HttpServletRequest request){
        AddWeatherCommand data = new AddWeatherCommand();
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            model.addAttribute("userIsExperimenter", true);
            if (idString2 != null) {
                int id = Integer.parseInt(idString2);
                data.setResearchGroupId(id);
                 if(id!=DEFAULT_ID){
                     String title = researchGroupDao.getResearchGroupTitle(id);
                     data.setResearchGroupTitle(title);
                 }else{
                     String defaultWeather = messageSource.getMessage("label.defaultWeather", null, RequestContextUtils.getLocale(request));
                     data.setResearchGroupTitle(defaultWeather);
                 }
            }
            if (idString != null) {
                // Editation of existing weather
                int id = Integer.parseInt(idString);

                log.debug("Loading weather to the command object for editing.");
                Weather weather = weatherDao.read(id);
    
                data.setId(id);
                data.setTitle(weather.getTitle());
                data.setDescription(weather.getDescription());
            }
            model.addAttribute("addWeather",data);

            return "lists/weather/addItemForm";
        }else{
           return "lists/userNotExperimenter";
        }
    }

    @RequestMapping(value="lists/weather-definitions/add.html",method=RequestMethod.GET)
    protected String showAddForm(@RequestParam("groupid") String idString, ModelMap model, HttpServletRequest request) throws Exception {
        AddWeatherCommand data = new AddWeatherCommand();
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            if (idString != null) {
                int id = Integer.parseInt(idString);
                 data.setResearchGroupId(id);
                 if(id!=DEFAULT_ID){
                     String title = researchGroupDao.getResearchGroupTitle(id);
                     data.setResearchGroupTitle(title);
                 }else{
                     String defaultWeather = messageSource.getMessage("label.defaultWeather", null, RequestContextUtils.getLocale(request));
                     data.setResearchGroupTitle(defaultWeather);
                 }
            }
            model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
            model.addAttribute("addWeather",data);
            return "lists/weather/addItemForm";
        }else{
            return "lists/userNotExperimenter";
        }
    }


    @RequestMapping(method=RequestMethod.POST)
    protected String onSubmit(@ModelAttribute("addWeather") AddWeatherCommand data, BindingResult result,ModelMap model) {
        log.debug("Processing form data.");

        if (auth.userIsExperimenter() || auth.isAdmin()) {
            addWeatherValidator.validate(data,result);
            if (result.hasErrors()) {
                return "lists/weather/addItemForm";
            }
            int weatherId = data.getId();
            Weather weather;
            if (weatherId > 0) {
                // Editing
                log.debug("Editing existing weather object.");
                weather = weatherDao.read(weatherId);

                if(weatherDao.isDefault(weatherId)){
                     if(data.getResearchGroupId()!=DEFAULT_ID){
                         // new weather
                         Weather newWeather = new Weather();
                         newWeather.setDefaultNumber(0);
                         newWeather.setTitle(data.getTitle());
                         newWeather.setDescription(data.getDescription());
                         int newId = weatherDao.create(newWeather);
                         WeatherGroupRel rel = weatherDao.getGroupRel(weatherId,data.getResearchGroupId());
                         // delete old rel, create new one
                         WeatherGroupRelId newRelId = new WeatherGroupRelId();
                         WeatherGroupRel newRel = new WeatherGroupRel();
                         newRelId.setWeatherId(newId);
                         newRelId.setResearchGroupId(data.getResearchGroupId());
                         newRel.setId(newRelId);
                         newRel.setWeather(newWeather);
                         ResearchGroup r = researchGroupDao.read(data.getResearchGroupId());
                         newRel.setResearchGroup(r);
                         weatherDao.deleteGroupRel(rel);
                         weatherDao.createGroupRel(newRel);
                     }else{
                        if(!weatherDao.hasGroupRel(weatherId) && weatherDao.canDelete(weatherId)){
                            weather.setTitle(data.getTitle());
                            weather.setDescription(data.getDescription());
                        }else{
                            return "lists/itemUsed";
                        }
                     }
                }else{
                     weather.setTitle(data.getTitle());
                     weather.setDescription(data.getDescription());
                }
            } else {
                // Creating new
                weather = new Weather();
                weather.setTitle(data.getTitle());
                weather.setDescription(data.getDescription());
                int pkGroup = data.getResearchGroupId();
                if(pkGroup == DEFAULT_ID){
                    log.debug("Creating new default weather object.");
                    weatherDao.createDefaultRecord(weather);
                }else{
                    log.debug("Creating new group weather object.");
                    int pkWeather = weatherDao.create(weather);

                    WeatherGroupRelId weatherGroupRelId = new WeatherGroupRelId(pkWeather,pkGroup);
                    ResearchGroup researchGroup = researchGroupDao.read(pkGroup);
                    WeatherGroupRel weatherGroupRel = new WeatherGroupRel(weatherGroupRelId,researchGroup,weather);
                    weatherDao.createGroupRel(weatherGroupRel);
                }

            }
            log.debug("Returning MAV");
            String redirect = "redirect:list.html?groupid="+data.getResearchGroupId();
            return redirect;
        }else{
            return "lists/userNotExperimenter";
        }
    }
}
