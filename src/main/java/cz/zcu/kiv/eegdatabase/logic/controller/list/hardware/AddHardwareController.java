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
 *   AddHardwareController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.list.hardware;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.HardwareGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.HardwareGroupRelId;
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
@SessionAttributes("addHardware")
public class AddHardwareController{
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private HardwareDao hardwareDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private HierarchicalMessageSource messageSource;

    private final int DEFAULT_ID = -1;
    AddHardwareValidator addHardwareValidator;

    @Autowired
	public AddHardwareController(AddHardwareValidator addHardwareValidator){
		this.addHardwareValidator = addHardwareValidator;
	}

    @RequestMapping(value="lists/hardware-definitions/edit.html", method=RequestMethod.GET)
    protected String showEditForm(@RequestParam("id") String idString, @RequestParam("groupid") String idString2, ModelMap model,HttpServletRequest request){
        AddHardwareCommand data = new AddHardwareCommand();
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            model.addAttribute("userIsExperimenter", true);
            if (idString2 != null) {
                int id = Integer.parseInt(idString2);
                data.setResearchGroupId(id);
                 if(id!=DEFAULT_ID){
                     String title = researchGroupDao.getResearchGroupTitle(id);
                     data.setResearchGroupTitle(title);
                 }else{
                     String defaultHardware = messageSource.getMessage("label.defaultHardware", null, RequestContextUtils.getLocale(request));
                     data.setResearchGroupTitle(defaultHardware);
                 }
            }
            if (idString != null) {
                // Editation of existing hardware
                int id = Integer.parseInt(idString);

                log.debug("Loading hardware to the command object for editing.");
                Hardware hardware = hardwareDao.read(id);
                data.setId(id);
                data.setDescription(hardware.getDescription());
                data.setTitle(hardware.getTitle());
                data.setType(hardware.getType());
            }
            model.addAttribute("addHardware",data);

            return "lists/hardware/addItemForm";
        }else{
           return "lists/userNotExperimenter";
        }
    }

    @RequestMapping(value="lists/hardware-definitions/add.html",method=RequestMethod.GET)
    protected String showAddForm(@RequestParam("groupid") String idString, ModelMap model, HttpServletRequest request) throws Exception {
        AddHardwareCommand data = new AddHardwareCommand();
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            if (idString != null) {
                int id = Integer.parseInt(idString);
                 data.setResearchGroupId(id);
                 if(id!=DEFAULT_ID){
                     String title = researchGroupDao.getResearchGroupTitle(id);
                     data.setResearchGroupTitle(title);
                 }else{
                     String defaultHardware = messageSource.getMessage("label.defaultHardware", null, RequestContextUtils.getLocale(request));
                     data.setResearchGroupTitle(defaultHardware);
                 }
            }
            model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
            model.addAttribute("addHardware",data);
            return "lists/hardware/addItemForm";
        }else{
            return "lists/userNotExperimenter";
        }
    }


    @RequestMapping(method=RequestMethod.POST)
    protected String onSubmit(@ModelAttribute("addHardware") AddHardwareCommand data, BindingResult result,ModelMap model) {
        log.debug("Processing form data.");

        if (auth.userIsExperimenter() || auth.isAdmin()) {
            addHardwareValidator.validate(data,result);
            if (result.hasErrors()) {
                return "lists/hardware/addItemForm";
            }
            int hwId = data.getId();
            Hardware hardware;
            if (hwId > 0) {
                // Editing
                log.debug("Editing existing hardware object.");
                hardware = hardwareDao.read(hwId);

                if(hardwareDao.isDefault(hwId)){
                     if(data.getResearchGroupId()!=DEFAULT_ID){
                         // new hw
                         Hardware newHw = new Hardware();
                         newHw.setDefaultNumber(0);
                         newHw.setDescription(data.getDescription());
                         newHw.setTitle(data.getTitle());
                         newHw.setType(data.getType());
                         int newId = hardwareDao.create(newHw);
                         HardwareGroupRel rel = hardwareDao.getGroupRel(hwId,data.getResearchGroupId());
                         // delete old rel, create new one
                         HardwareGroupRelId newRelId = new HardwareGroupRelId();
                         HardwareGroupRel newRel = new HardwareGroupRel();
                         newRelId.setHardwareId(newId);
                         newRelId.setResearchGroupId(data.getResearchGroupId());
                         newRel.setId(newRelId);
                         newRel.setHardware(newHw);
                         ResearchGroup r = researchGroupDao.read(data.getResearchGroupId());
                         newRel.setResearchGroup(r);
                         hardwareDao.deleteGroupRel(rel);
                         hardwareDao.createGroupRel(newRel);
                     }else{
                        if(!hardwareDao.hasGroupRel(hwId) && hardwareDao.canDelete(hwId)){
                            hardware.setDescription(data.getDescription());
                            hardware.setTitle(data.getTitle());
                            hardware.setType(data.getType());
                        }else{
                            return "lists/itemUsed";
                        }
                     }
                }else{
                    hardware.setDescription(data.getDescription());
                    hardware.setTitle(data.getTitle());
                    hardware.setType(data.getType());
                }
            } else {
                // Creating new
                hardware = new Hardware();
                hardware.setTitle(data.getTitle());
                hardware.setType(data.getType());
                hardware.setDescription(data.getDescription());
                int pkGroup = data.getResearchGroupId();
                if(pkGroup == DEFAULT_ID){
                    log.debug("Creating new default hardware object.");
                    hardwareDao.createDefaultRecord(hardware);
                }else{
                    log.debug("Creating new group hardware object.");
                    int pkHardware = hardwareDao.create(hardware);

                    HardwareGroupRelId hardwareGroupRelId = new HardwareGroupRelId(pkHardware,pkGroup);
                    ResearchGroup researchGroup = researchGroupDao.read(pkGroup);
                    HardwareGroupRel hardwareGroupRel = new HardwareGroupRel(hardwareGroupRelId,researchGroup,hardware);
                    hardwareDao.createGroupRel(hardwareGroupRel);
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
