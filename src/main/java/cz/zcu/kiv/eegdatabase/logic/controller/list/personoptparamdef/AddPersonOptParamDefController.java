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
 *   AddPersonOptParamDefController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.list.personoptparamdef;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.PersonOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDefGroupRelId;
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
@SessionAttributes("addPersonOptParamDef")
public class AddPersonOptParamDefController{
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private PersonOptParamDefDao personOptParamDefDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private HierarchicalMessageSource messageSource;

    private final int DEFAULT_ID = -1;
    AddPersonOptParamDefValidator addPersonOptParamDefValidator;

    @Autowired
	public AddPersonOptParamDefController(AddPersonOptParamDefValidator addPersonOptParamDefValidator){
		this.addPersonOptParamDefValidator = addPersonOptParamDefValidator;
	}

    @RequestMapping(value="lists/person-optional-parameters/edit.html", method= RequestMethod.GET)
    protected String showEditForm(@RequestParam("id") String idString, @RequestParam("groupid") String idString2, ModelMap model, HttpServletRequest request){
        AddPersonOptParamDefCommand data = new AddPersonOptParamDefCommand();
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            model.addAttribute("userIsExperimenter", true);
            if (idString2 != null) {
                int id = Integer.parseInt(idString2);
                data.setResearchGroupId(id);
                 if(id!=DEFAULT_ID){
                     String title = researchGroupDao.getResearchGroupTitle(id);
                     data.setResearchGroupTitle(title);
                 }else{
                     String defaultPersonOptParamDef = messageSource.getMessage("label.defaultPersonOptParamDef", null, RequestContextUtils.getLocale(request));
                     data.setResearchGroupTitle(defaultPersonOptParamDef);
                 }
            }
            if (idString != null) {
                // Editation of existing personOptParamDef
                int id = Integer.parseInt(idString);

                log.debug("Loading personOptParamDef to the command object for editing.");
                PersonOptParamDef personOptParamDef = personOptParamDefDao.read(id);

                data.setId(id);
                data.setParamName(personOptParamDef.getParamName());
                data.setParamDataType(personOptParamDef.getParamDataType());
            }
            model.addAttribute("addPersonOptParamDef",data);

            return "lists/personAdditionalParams/addItemForm";
        }else{
           return "lists/userNotExperimenter";
        }
    }

    @RequestMapping(value="lists/person-optional-parameters/add.html",method=RequestMethod.GET)
    protected String showAddForm(@RequestParam("groupid") String idString, ModelMap model, HttpServletRequest request) throws Exception {
        AddPersonOptParamDefCommand data = new AddPersonOptParamDefCommand();
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            if (idString != null) {
                int id = Integer.parseInt(idString);
                 data.setResearchGroupId(id);
                 if(id!=DEFAULT_ID){
                     String title = researchGroupDao.getResearchGroupTitle(id);
                     data.setResearchGroupTitle(title);
                 }else{
                     String defaultPersonOptParamDef = messageSource.getMessage("label.defaultPersonOptParamDef", null, RequestContextUtils.getLocale(request));
                     data.setResearchGroupTitle(defaultPersonOptParamDef);
                 }
            }
            model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
            model.addAttribute("addPersonOptParamDef",data);
            return "lists/personAdditionalParams/addItemForm";
        }else{
            return "lists/userNotExperimenter";
        }
    }


    @RequestMapping(method=RequestMethod.POST)
    protected String onSubmit(@ModelAttribute("addPersonOptParamDef") AddPersonOptParamDefCommand data, BindingResult result,ModelMap model) {
        log.debug("Processing form data.");
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            addPersonOptParamDefValidator.validate(data,result);
            if (result.hasErrors()) {
                return "lists/personAdditionalParams/addItemForm";
            }
            int personOptParamDefId = data.getId();
            PersonOptParamDef personOptParamDef;
            if (personOptParamDefId > 0) {
                // Editing
                log.debug("Editing existing personOptParamDef object.");
                personOptParamDef = personOptParamDefDao.read(personOptParamDefId);
                if(personOptParamDefDao.isDefault(personOptParamDefId)){
                     if(data.getResearchGroupId()!=DEFAULT_ID){
                         // new personOptParamDef
                         PersonOptParamDef newPersonOptParamDef = new PersonOptParamDef();
                         newPersonOptParamDef.setDefaultNumber(0);
                         newPersonOptParamDef.setParamName(data.getParamName());
                         newPersonOptParamDef.setParamDataType(data.getParamDataType());
                         int newId = personOptParamDefDao.create(newPersonOptParamDef);
                         PersonOptParamDefGroupRel rel = personOptParamDefDao.getGroupRel(personOptParamDefId,data.getResearchGroupId());
                         // delete old rel, create new one
                         PersonOptParamDefGroupRelId newRelId = new PersonOptParamDefGroupRelId();
                         PersonOptParamDefGroupRel newRel = new PersonOptParamDefGroupRel();
                         newRelId.setPersonOptParamDefId(newId);
                         newRelId.setResearchGroupId(data.getResearchGroupId());
                         newRel.setId(newRelId);
                         newRel.setPersonOptParamDef(newPersonOptParamDef);
                         ResearchGroup r = researchGroupDao.read(data.getResearchGroupId());
                         newRel.setResearchGroup(r);
                         personOptParamDefDao.deleteGroupRel(rel);
                         personOptParamDefDao.createGroupRel(newRel);
                     }else{
                        if(!personOptParamDefDao.hasGroupRel(personOptParamDefId) && personOptParamDefDao.canDelete(personOptParamDefId)){
                            personOptParamDef.setParamName(data.getParamName());
                            personOptParamDef.setParamDataType(data.getParamDataType());
                        }else{
                            return "lists/itemUsed";
                        }
                     }
                }else{
                     personOptParamDef.setParamName(data.getParamName());
                     personOptParamDef.setParamDataType(data.getParamDataType());
                }
            } else {
                // Creating new
                personOptParamDef = new PersonOptParamDef();
                personOptParamDef.setParamName(data.getParamName());
                personOptParamDef.setParamDataType(data.getParamDataType());
                int pkGroup = data.getResearchGroupId();
                if(pkGroup == DEFAULT_ID){
                    log.debug("Creating new default personOptParamDef object.");
                    personOptParamDefDao.createDefaultRecord(personOptParamDef);
                }else{
                    log.debug("Creating new group personOptParamDef object.");
                    int pkPersonOptParamDef = personOptParamDefDao.create(personOptParamDef);

                    PersonOptParamDefGroupRelId personOptParamDefGroupRelId = new PersonOptParamDefGroupRelId(pkPersonOptParamDef,pkGroup);
                    ResearchGroup researchGroup = researchGroupDao.read(pkGroup);
                    PersonOptParamDefGroupRel personOptParamDefGroupRel = new PersonOptParamDefGroupRel(personOptParamDefGroupRelId,researchGroup,personOptParamDef);
                    personOptParamDefDao.createGroupRel(personOptParamDefGroupRel);
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
