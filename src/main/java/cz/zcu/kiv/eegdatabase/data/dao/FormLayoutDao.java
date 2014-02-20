/***********************************************************************************************************************
 *
 * This file is part of the EEG-database project
 *
 * =============================================
 *
 * Copyright (C) 2014 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * FormLayoutDao.java, 18. 2. 2014 17:59:45, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.FormLayout;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;


/**
 * DAO interface for fetching and saving form-layouts data.
 * 
 * @author Jakub Krauz
 */
public interface FormLayoutDao extends GenericDao<FormLayout, Integer> {
	
	/**
	 * Updates the layout or creates a new one.
	 * @param layout the updated/saved layout
	 */
	void createOrUpdateByName(FormLayout layout);
	
	/**
	 * Gets count of all forms with available layouts.
	 * @return count of forms
	 */
	int getAllFormsCount();
	
	/**
	 * Gets count of forms with available layouts owned by the given person.
	 * @param owner the owner of layouts
	 * @return count of forms
	 */
	int getFormsCount(Person owner);
	
	/**
	 * Gets names of all forms with available layouts.
	 * @return names of forms
	 */
	List<String> getAllFormNames();
	
	/**
	 * Gets names of all forms with available layouts owned by the given person.
	 * @param owner the owner
	 * @return names of forms
	 */
	List<String> getFormNames(Person owner);
	
	/**
	 * Gets count of all layouts available.
	 * @return count of layouts
	 */
	int getAllLayoutsCount();
	
	/**
	 * Gets count of all layouts owned by the given person.
	 * @param owner the owner
	 * @return count of layouts
	 */
	int getLayoutsCount(Person owner);
	
	/**
	 * Gets count of all layouts for the given form.
	 * @param formName the name of the form
	 * @return count of layouts
	 */
	int getLayoutsCount(String formName);
	
	/**
	 * Gets count of all layouts for the given form and owned by the given person.
	 * @param owner the owner
	 * @param formName the name of the form
	 * @return count of layouts
	 */
	int getLayoutsCount(Person owner, String formName);
	
	/**
	 * Gets the layout for the given form and layout name.
	 * @param formName the name of the form
	 * @param layoutName the name of the layout
	 * @return the layout or null
	 */
	FormLayout getLayout(String formName, String layoutName);
	
	/**
	 * Gets all layouts available.
	 * @return list of layouts
	 */
	List<FormLayout> getAllLayouts();
	
	/**
	 * Gets all layouts owned by the given person.
	 * @param owner the owner
	 * @return list of layouts
	 */
	List<FormLayout> getLayouts(Person owner);
	
	/**
	 * Gets all layouts for the given form.
	 * @param formName the name of the form
	 * @return list of layouts
	 */
	List<FormLayout> getLayouts(String formName);
	
	/**
	 * Gets all layouts for the given form and owned by the given person.
	 * @param owner the owner
	 * @param formName the name of the form
	 * @return list of layouts
	 */
	List<FormLayout> getLayouts(Person owner, String formName);
	

}
