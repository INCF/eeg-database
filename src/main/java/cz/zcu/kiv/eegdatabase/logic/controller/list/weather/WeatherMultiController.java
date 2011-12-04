package cz.zcu.kiv.eegdatabase.logic.controller.list.weather;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.data.pojo.WeatherGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.controller.list.SelectGroupCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author František Liška
 */
@Controller
@SessionAttributes("selectGroupCommand")
public class WeatherMultiController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private WeatherDao weatherDao;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private HierarchicalMessageSource messageSource;

    private List<ResearchGroup> researchGroupList;
    private List<Weather> weatherList;
    private final int DEFAULT_ID = -1;

    @RequestMapping(value="lists/weather-definitions/list.html",method=RequestMethod.GET)
    public String showSelectForm(WebRequest webRequest,ModelMap model, HttpServletRequest request){
        log.debug("Processing weather list controller");
        SelectGroupCommand selectGroupCommand= new SelectGroupCommand();
        String defaultWeather = messageSource.getMessage("label.defaultWeather", null, RequestContextUtils.getLocale(request));
        fillAuthResearchGroupList(defaultWeather);
        String idString = webRequest.getParameter("groupid");
        if(auth.isAdmin()){
            if (idString != null) {
                int id = Integer.parseInt(idString);
                if(id!=DEFAULT_ID){
                    fillWeatherList(id);
                    selectGroupCommand.setResearchGroupId(id);
                }else{
                   fillWeatherList(DEFAULT_ID);
                    selectGroupCommand.setResearchGroupId(DEFAULT_ID);
                }

            }else{
                fillWeatherList(DEFAULT_ID);
                selectGroupCommand.setResearchGroupId(DEFAULT_ID);
            }
            model.addAttribute("userIsExperimenter", true);
        }else{
            if(!researchGroupList.isEmpty()){
                if (idString != null) {
                    int id = Integer.parseInt(idString);
                    fillWeatherList(id);
                    selectGroupCommand.setResearchGroupId(id);
                }else{
                    int myGroup = researchGroupList.get(0).getResearchGroupId();
                    fillWeatherList(myGroup);
                    selectGroupCommand.setResearchGroupId(myGroup);
                }
                model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
            }else{
                model.addAttribute("userIsExperimenter", false);
            }
        }
        model.addAttribute("selectGroupCommand",selectGroupCommand);
        model.addAttribute("weatherList", weatherList);
        model.addAttribute("researchGroupList", researchGroupList);


        return "lists/weather/list";
    }

    @RequestMapping(value="lists/weather-definitions/list.html",method=RequestMethod.POST)
    public String onSubmit(@ModelAttribute("selectGroupCommand") SelectGroupCommand selectGroupCommand, ModelMap model, HttpServletRequest request){
        String defaultWeather = messageSource.getMessage("label.defaultWeather", null, RequestContextUtils.getLocale(request));
        fillAuthResearchGroupList(defaultWeather);
        if(!researchGroupList.isEmpty()){
            fillWeatherList(selectGroupCommand.getResearchGroupId());
        }
        boolean canEdit = auth.isAdmin() || auth.userIsExperimenter();
        model.addAttribute("userIsExperimenter", canEdit);
        model.addAttribute("researchGroupList", researchGroupList);
        model.addAttribute("weatherList", weatherList);
        return "lists/weather/list";
    }

    @RequestMapping(value="lists/weather-definitions/delete.html")
    public String delete(@RequestParam("id") String idString, @RequestParam("groupid") String idString2) {
        log.debug("Deleting weather.");
        if (idString != null) {
            int id = Integer.parseInt(idString);

            if (weatherDao.canDelete(id)) {
                if(idString2 !=null){
                    int groupId = Integer.parseInt(idString2);

                    if(groupId==DEFAULT_ID){ // delete default weather if it's from default group
                        if(!weatherDao.hasGroupRel(id)){ // delete only if it doesn't have group relationship
                            weatherDao.delete(weatherDao.read(id));
                        }else{
                            System.out.println("hasGroupRel");
                            return "lists/itemUsed";
                        }
                    }else{
                        WeatherGroupRel h = weatherDao.getGroupRel(id,groupId);
                        if(!weatherDao.isDefault(id)){ // delete only non default weather
                            weatherDao.delete(weatherDao.read(id));
                        }
                        weatherDao.deleteGroupRel(h);
                    }
                }

            } else {
                System.out.println("cannotDelete");
                return "lists/itemUsed";
            }
        }

        return "lists/itemDeleted";
    }

    private void fillAuthResearchGroupList(String defaultName){
        Person loggedUser = personDao.getLoggedPerson();

        ResearchGroup defaultGroup = new ResearchGroup(DEFAULT_ID,loggedUser,defaultName,"-");

        if(loggedUser.getAuthority().equals("ROLE_ADMIN")){
            researchGroupList = researchGroupDao.getAllRecords();
            researchGroupList.add(0,defaultGroup);
        }else{
            researchGroupList = researchGroupDao.getResearchGroupsWhereMember(loggedUser);
        }
    }

    private void fillWeatherList(int selectedGroupId){
        if(selectedGroupId == DEFAULT_ID){
            weatherList = weatherDao.getDefaultRecords();
        }else{
            if(!researchGroupList.isEmpty()){
                weatherList = weatherDao.getRecordsByGroup(selectedGroupId);
            }
        }
    }

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }

    public WeatherDao getWeatherDao() {
        return weatherDao;
    }

    public void setWeatherDao(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }

    public HierarchicalMessageSource getMessageSource()
    {
        return messageSource;
    }

    public void setMessageSource(HierarchicalMessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

}