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
 *   AddExperimentOptParamDefController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.list.experimentoptparamdef;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDefGroupRelId;
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
@SessionAttributes("addExperimentOptParamDef")
public class AddExperimentOptParamDefController{
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private ExperimentOptParamDefDao experimentOptParamDefDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private HierarchicalMessageSource messageSource;

    private final int DEFAULT_ID = -1;
    AddExperimentOptParamDefValidator addExperimentOptParamDefValidator;

    @Autowired
	public AddExperimentOptParamDefController(AddExperimentOptParamDefValidator addExperimentOptParamDefValidator){
		this.addExperimentOptParamDefValidator = addExperimentOptParamDefValidator;
	}

    @RequestMapping(value="lists/experiment-optional-parameters/edit.html", method= RequestMethod.GET)
    protected String showEditForm(@RequestParam("id") String idString, @RequestParam("groupid") String idString2, ModelMap model, HttpServletRequest request){
        AddExperimentOptParamDefCommand data = new AddExperimentOptParamDefCommand();
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            model.addAttribute("userIsExperimenter", true);
            if (idString2 != null) {
                int id = Integer.parseInt(idString2);
                data.setResearchGroupId(id);
                 if(id!=DEFAULT_ID){
                     String title = researchGroupDao.getResearchGroupTitle(id);
                     data.setResearchGroupTitle(title);
                 }else{
                     String defaultExperimentOptParamDef = messageSource.getMessage("label.defaultExperimentOptParamDef", null, RequestContextUtils.getLocale(request));
                     data.setResearchGroupTitle(defaultExperimentOptParamDef);
                 }
            }
            if (idString != null) {
                // Editation of existing experimentOptParamDef
                int id = Integer.parseInt(idString);

                log.debug("Loading experimentOptParamDef to the command object for editing.");
                ExperimentOptParamDef experimentOptParamDef = experimentOptParamDefDao.read(id);

                data.setId(id);
                data.setParamName(experimentOptParamDef.getParamName());
                data.setParamDataType(experimentOptParamDef.getParamDataType());
            }
            model.addAttribute("addExperimentOptParamDef",data);

            return "lists/measurationAdditionalParams/addItemForm";
        }else{
           return "lists/userNotExperimenter";
        }
    }

    @RequestMapping(value="lists/experiment-optional-parameters/add.html",method=RequestMethod.GET)
    protected String showAddForm(@RequestParam("groupid") String idString, ModelMap model, HttpServletRequest request) throws Exception {
        AddExperimentOptParamDefCommand data = new AddExperimentOptParamDefCommand();
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            if (idString != null) {
                int id = Integer.parseInt(idString);
                 data.setResearchGroupId(id);
                 if(id!=DEFAULT_ID){
                     String title = researchGroupDao.getResearchGroupTitle(id);
                     data.setResearchGroupTitle(title);
                 }else{
                     String defaultExperimentOptParamDef = messageSource.getMessage("label.defaultExperimentOptParamDef", null, RequestContextUtils.getLocale(request));
                     data.setResearchGroupTitle(defaultExperimentOptParamDef);
                 }
            }
            model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
            model.addAttribute("addExperimentOptParamDef",data);
            return "lists/measurationAdditionalParams/addItemForm";
        }else{
            return "lists/userNotExperimenter";
        }
    }


    @RequestMapping(method=RequestMethod.POST)
    protected String onSubmit(@ModelAttribute("addExperimentOptParamDef") AddExperimentOptParamDefCommand data, BindingResult result,ModelMap model) {
        log.debug("Processing form data.");
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            addExperimentOptParamDefValidator.validate(data,result);
            if (result.hasErrors()) {
                return "lists/measurationAdditionalParams/addItemForm";
            }
            int experimentOptParamDefId = data.getId();
            ExperimentOptParamDef experimentOptParamDef;
            if (experimentOptParamDefId > 0) {
                // Editing
                log.debug("Editing existing experimentOptParamDef object.");
                experimentOptParamDef = experimentOptParamDefDao.read(experimentOptParamDefId);
                if(experimentOptParamDefDao.isDefault(experimentOptParamDefId)){
                     if(data.getResearchGroupId()!=DEFAULT_ID){
                         // new experimentOptParamDef
                         ExperimentOptParamDef newExperimentOptParamDef = new ExperimentOptParamDef();
                         newExperimentOptParamDef.setDefaultNumber(0);
                         newExperimentOptParamDef.setParamName(data.getParamName());
                         newExperimentOptParamDef.setParamDataType(data.getParamDataType());
                         int newId = experimentOptParamDefDao.create(newExperimentOptParamDef);
                         ExperimentOptParamDefGroupRel rel = experimentOptParamDefDao.getGroupRel(experimentOptParamDefId,data.getResearchGroupId());
                         // delete old rel, create new one
                         ExperimentOptParamDefGroupRelId newRelId = new ExperimentOptParamDefGroupRelId();
                         ExperimentOptParamDefGroupRel newRel = new ExperimentOptParamDefGroupRel();
                         newRelId.setExperimentOptParamDefId(newId);
                         newRelId.setResearchGroupId(data.getResearchGroupId());
                         newRel.setId(newRelId);
                         newRel.setExperimentOptParamDef(newExperimentOptParamDef);
                         ResearchGroup r = researchGroupDao.read(data.getResearchGroupId());
                         newRel.setResearchGroup(r);
                         experimentOptParamDefDao.deleteGroupRel(rel);
                         experimentOptParamDefDao.createGroupRel(newRel);
                     }else{
                        if(!experimentOptParamDefDao.hasGroupRel(experimentOptParamDefId) && experimentOptParamDefDao.canDelete(experimentOptParamDefId)){
                            experimentOptParamDef.setParamName(data.getParamName());
                            experimentOptParamDef.setParamDataType(data.getParamDataType());
                        }else{
                            return "lists/itemUsed";
                        }
                     }
                }else{
                     experimentOptParamDef.setParamName(data.getParamName());
                     experimentOptParamDef.setParamDataType(data.getParamDataType());
                }
            } else {
                // Creating new
                experimentOptParamDef = new ExperimentOptParamDef();
                experimentOptParamDef.setParamName(data.getParamName());
                experimentOptParamDef.setParamDataType(data.getParamDataType());
                int pkGroup = data.getResearchGroupId();
                if(pkGroup == DEFAULT_ID){
                    log.debug("Creating new default experimentOptParamDef object.");
                    experimentOptParamDefDao.createDefaultRecord(experimentOptParamDef);
                }else{
                    log.debug("Creating new group experimentOptParamDef object.");
                    int pkExperimentOptParamDef = experimentOptParamDefDao.create(experimentOptParamDef);

                    ExperimentOptParamDefGroupRelId experimentOptParamDefGroupRelId = new ExperimentOptParamDefGroupRelId(pkExperimentOptParamDef,pkGroup);
                    ResearchGroup researchGroup = researchGroupDao.read(pkGroup);
                    ExperimentOptParamDefGroupRel experimentOptParamDefGroupRel = new ExperimentOptParamDefGroupRel(experimentOptParamDefGroupRelId,researchGroup,experimentOptParamDef);
                    experimentOptParamDefDao.createGroupRel(experimentOptParamDefGroupRel);
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
