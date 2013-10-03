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
 *   PersonOptParamDefMultiController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.list.personoptparamdef;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDefGroupRel;
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
public class PersonOptParamDefMultiController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonOptParamDefDao personOptParamDefDao;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private HierarchicalMessageSource messageSource;

    private List<ResearchGroup> researchGroupList;
    private List<PersonOptParamDef> personOptParamDefList;
    private final int DEFAULT_ID = -1;

    @RequestMapping(value="lists/person-optional-parameters/list.html",method= RequestMethod.GET)
    public String showSelectForm(WebRequest webRequest,ModelMap model, HttpServletRequest request){
        log.debug("Processing personOptParamDef list controller");
        SelectGroupCommand selectGroupCommand= new SelectGroupCommand();
        String defaultPersonOptParamDef = messageSource.getMessage("label.defaultPersonOptParamDef", null, RequestContextUtils.getLocale(request));
        fillAuthResearchGroupList(defaultPersonOptParamDef);
        String idString = webRequest.getParameter("groupid");
        if(auth.isAdmin()){
            if (idString != null) {
                int id = Integer.parseInt(idString);
                if(id!=DEFAULT_ID){
                    fillPersonOptParamDefList(id);
                    selectGroupCommand.setResearchGroupId(id);
                }else{
                   fillPersonOptParamDefList(DEFAULT_ID);
                    selectGroupCommand.setResearchGroupId(DEFAULT_ID);
                }

            }else{
                fillPersonOptParamDefList(DEFAULT_ID);
                selectGroupCommand.setResearchGroupId(DEFAULT_ID);
            }
            model.addAttribute("userIsExperimenter", true);
        }else{
            if(!researchGroupList.isEmpty()){
                if (idString != null) {
                    int id = Integer.parseInt(idString);
                    fillPersonOptParamDefList(id);
                    selectGroupCommand.setResearchGroupId(id);
                }else{
                    int myGroup = researchGroupList.get(0).getResearchGroupId();
                    fillPersonOptParamDefList(myGroup);
                    selectGroupCommand.setResearchGroupId(myGroup);
                }
                model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
            }else{
                model.addAttribute("userIsExperimenter", false);
            }
        }
        model.addAttribute("selectGroupCommand",selectGroupCommand);
        model.addAttribute("personOptParamDefList", personOptParamDefList);
        model.addAttribute("researchGroupList", researchGroupList);


        return "lists/personAdditionalParams/list";
    }

    @RequestMapping(value="lists/person-optional-parameters/list.html",method=RequestMethod.POST)
    public String onSubmit(@ModelAttribute("selectGroupCommand") SelectGroupCommand selectGroupCommand, ModelMap model, HttpServletRequest request){
        String defaultPersonOptParamDef = messageSource.getMessage("label.defaultPersonOptParamDef", null, RequestContextUtils.getLocale(request));
        fillAuthResearchGroupList(defaultPersonOptParamDef);
        if(!researchGroupList.isEmpty()){
            fillPersonOptParamDefList(selectGroupCommand.getResearchGroupId());
        }
        boolean canEdit = auth.isAdmin() || auth.userIsExperimenter();
        model.addAttribute("userIsExperimenter", canEdit);
        model.addAttribute("researchGroupList", researchGroupList);
        model.addAttribute("personOptParamDefList", personOptParamDefList);
        return "lists/personAdditionalParams/list";
    }

    @RequestMapping(value="lists/person-optional-parameters/delete.html")
    public String delete(@RequestParam("id") String idString, @RequestParam("groupid") String idString2) {
        log.debug("Deleting personOptParamDef.");
        if (idString != null) {
            int id = Integer.parseInt(idString);

            if (personOptParamDefDao.canDelete(id)) {
                if(idString2 !=null){
                    int groupId = Integer.parseInt(idString2);

                    if(groupId==DEFAULT_ID){ // delete default personOptParamDef if it's from default group
                        if(!personOptParamDefDao.hasGroupRel(id)){ // delete only if it doesn't have group relationship
                            personOptParamDefDao.delete(personOptParamDefDao.read(id));
                        }else{
                            return "lists/itemUsed";
                        }
                    }else{
                        PersonOptParamDefGroupRel h = personOptParamDefDao.getGroupRel(id,groupId);
                        if(!personOptParamDefDao.isDefault(id)){ // delete only non default personOptParamDef
                            personOptParamDefDao.delete(personOptParamDefDao.read(id));
                        }
                        personOptParamDefDao.deleteGroupRel(h);
                    }
                }

            } else {
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

    private void fillPersonOptParamDefList(int selectedGroupId){
        if(selectedGroupId == DEFAULT_ID){
            personOptParamDefList = personOptParamDefDao.getDefaultRecords();
        }else{
            if(!researchGroupList.isEmpty()){
                personOptParamDefList = personOptParamDefDao.getRecordsByGroup(selectedGroupId);
            }
        }
    }

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }

    public PersonOptParamDefDao getPersonOptParamDefDao() {
        return personOptParamDefDao;
    }

    public void setPersonOptParamDefDao(PersonOptParamDefDao personOptParamDefDao) {
        this.personOptParamDefDao = personOptParamDefDao;
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
