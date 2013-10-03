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
 *   ChangeUserRoleController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.admin;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * Controller for changing the global role of user.
 *
 * @author Jindra
 */
public class ChangeUserRoleController extends SimpleFormController {

    private PersonDao personDao;

    public ChangeUserRoleController() {
        setCommandClass(ChangeUserRoleCommand.class);
        setCommandName("changeUserRole");
    }

    @Override
    protected ModelAndView onSubmit(Object command) throws Exception {
        ChangeUserRoleCommand data = (ChangeUserRoleCommand) command;

        Person person = personDao.getPerson(data.getUserName());
        person.setAuthority(data.getUserRole());
        personDao.update(person);

        return new ModelAndView(getSuccessView());
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
