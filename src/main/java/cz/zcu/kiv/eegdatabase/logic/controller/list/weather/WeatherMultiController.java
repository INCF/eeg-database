/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   WeatherMultiController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
    public String showSelectForm(WebRequest webRequest,ModelMap model){
        log.debug("Processing weather list controller");
        SelectGroupCommand selectGroupCommand= new SelectGroupCommand();
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
        return "lists/weather/list";
    }

    @RequestMapping(value="lists/weather-definitions/list.html",method=RequestMethod.POST)
    public String onSubmit(@ModelAttribute("selectGroupCommand") SelectGroupCommand selectGroupCommand, ModelMap model, HttpServletRequest request){
        if(!researchGroupList.isEmpty()){
            fillWeatherList(selectGroupCommand.getResearchGroupId());
        }
        boolean canEdit = auth.isAdmin() || auth.userIsExperimenter();
        model.addAttribute("userIsExperimenter", canEdit);
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

    @ModelAttribute("researchGroupList")
    private List<ResearchGroup> fillAuthResearchGroupList(HttpServletRequest request){
        String defaultName = messageSource.getMessage("label.defaultWeather", null, RequestContextUtils.getLocale(request));
        Person loggedUser = personDao.getLoggedPerson();
        if(loggedUser.getAuthority().equals("ROLE_ADMIN")){
            ResearchGroup defaultGroup = new ResearchGroup(DEFAULT_ID,loggedUser,defaultName,"-");
            researchGroupList = researchGroupDao.getAllRecords();
            researchGroupList.add(0,defaultGroup);
        }else{
            researchGroupList = researchGroupDao.getResearchGroupsWhereMember(loggedUser);
        }
        return researchGroupList;
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
