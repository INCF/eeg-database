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
 *   AddPersonAdditionalParamValueController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.person;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author František Liška
 */
@Controller
@SessionAttributes("addPersonAdditionalParameter")
public class AddPersonAdditionalParamValueController{

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private PersonDao personDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    @Qualifier("personOptParamValDao")
    private GenericDao<PersonOptParamVal, PersonOptParamValId> personOptParamValDao;
    @Autowired
    private PersonOptParamDefDao personOptParamDefDao;
    private AddPersonAdditionalParamValueValidator addPersonAdditionalParamValueValidator;

    @Autowired
    public AddPersonAdditionalParamValueController(AddPersonAdditionalParamValueValidator addPersonAdditionalParamValueValidator){
        this.addPersonAdditionalParamValueValidator = addPersonAdditionalParamValueValidator;
    }


    @RequestMapping(value="people/add-optional-parameter.html",method= RequestMethod.GET)
    protected String showForm(ModelMap model){
        log.debug("Preparing data for form");
        AddPersonAdditionalParamValueCommand data = new AddPersonAdditionalParamValueCommand();
        model.addAttribute("addPersonAdditionalParameter", data);
        return "people/optionalParams/addItemForm";

    }

    @RequestMapping(method=RequestMethod.POST)
    protected String onSubmit(@ModelAttribute("addPersonAdditionalParameter") AddPersonAdditionalParamValueCommand data, BindingResult result){
        addPersonAdditionalParamValueValidator.validate(data, result);
        if (result.hasErrors()) {
            return "people/optionalParams/addItemForm";
        }

        log.debug("Processing form data.");

        log.debug("Creating new object");
        PersonOptParamVal val = new PersonOptParamVal();
        val.setId(new PersonOptParamValId(data.getPersonFormId(), data.getParamId()));
        val.setParamValue(data.getParamValue());

        log.debug("Saving object to database");
        personOptParamValDao.create(val);

       String redirect =  "redirect:/people/detail.html?personId="+data.getPersonFormId();
        return redirect;
    }

    @ModelAttribute("personAdditionalParams")
    protected List<PersonOptParamDef> populateOptParamList(){
       log.debug("Loading parameter list for select box");
        List<PersonOptParamDef> list = new ArrayList<PersonOptParamDef>();
        Person loggedUser = personDao.getLoggedPerson();
        if(loggedUser.getAuthority().equals("ROLE_ADMIN")){
           list.addAll(personOptParamDefDao.getAllRecords());
        }else{
            List<ResearchGroup> researchGroups = researchGroupDao.getResearchGroupsWhereMember(loggedUser);
            for(int i =0; i<researchGroups.size();i++){
                list.addAll(personOptParamDefDao.getRecordsByGroup(researchGroups.get(i).getResearchGroupId()));
            }
        }

       return list;
    }

    @ModelAttribute("personDetail")
    private Person populatePersonDetail(@RequestParam("personId") String idString){
        log.debug("Loading person info");
        int personId = Integer.parseInt(idString);
        Person person = personDao.read(personId);
        return person;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }

    public GenericDao<PersonOptParamVal, PersonOptParamValId> getPersonOptParamValDao() {
        return personOptParamValDao;
    }

    public void setPersonOptParamValDao(GenericDao<PersonOptParamVal, PersonOptParamValId> personOptParamValDao) {
        this.personOptParamValDao = personOptParamValDao;
    }

    public PersonOptParamDefDao getPersonOptParamDefDao() {
        return personOptParamDefDao;
    }

    public void setPersonOptParamDefDao(PersonOptParamDefDao personOptParamDefDao) {
        this.personOptParamDefDao = personOptParamDefDao;
    }

    public AddPersonAdditionalParamValueValidator getAddPersonAdditionalParamValueValidator() {
        return addPersonAdditionalParamValueValidator;
    }

    public void setAddPersonAdditionalParamValueValidator(AddPersonAdditionalParamValueValidator addPersonAdditionalParamValueValidator) {
        this.addPersonAdditionalParamValueValidator = addPersonAdditionalParamValueValidator;
    }
}
