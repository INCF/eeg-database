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
 * FormService.java, 8. 1. 2014 12:05:51, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.forms;

import cz.zcu.kiv.eegdatabase.data.pojo.FormLayout;
import cz.zcu.kiv.eegdatabase.data.pojo.FormLayoutType;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.wrappers.RecordCountData;
import cz.zcu.kiv.eegdatabase.webservices.rest.forms.wrappers.AvailableFormsDataList;
import cz.zcu.kiv.eegdatabase.webservices.rest.forms.wrappers.AvailableLayoutsDataList;
import cz.zcu.kiv.eegdatabase.webservices.rest.forms.wrappers.RecordIdsDataList;

/**
 * Service interface providing form-layouts data and operations on REST service.
 * 
 * @author Jakub Krauz
 */
public interface FormService {
	
	
	/**
	 * Gets the count of forms with available layouts.
	 * @return count of forms
	 */
	RecordCountData availableFormsCount();
	
	
	/**
	 * Gets names of forms with available layouts.
	 * @param mineOnly - if true, processes only logged user's records, otherwise all records available
	 * @return names of forms
	 */
	AvailableFormsDataList availableForms(boolean mineOnly);
	
	
	/**
	 * Gets the count of all form-layouts available.
	 * @return count of form-layouts
	 */
	RecordCountData availableLayoutsCount();
	
	
	/**
	 * Gets the count of form-layouts available for the specified form.
	 * @param formName - name of the form
	 * @param type - required type of the template
	 * @return count of form-layouts
	 */
	RecordCountData availableLayoutsCount(String formName, FormLayoutType type);
	
	
	/**
	 * Gets names of all form layouts available.
	 * @return names of form layouts
	 */
	AvailableLayoutsDataList availableLayouts();
	
	
	/**
	 * Gets names of form layouts available for the specified form.
	 * @param mineOnly - if true, processes only logged user's records, otherwise all records available
	 * @param formName - name of the form
	 * @param type - required type of the template
	 * @return names of form layouts
	 */
	AvailableLayoutsDataList availableLayouts(boolean mineOnly, String formName, FormLayoutType type);
	
	
	/**
	 * Gets a form-layout with the specified name.
	 * @param formName - name of the form
	 * @param layoutName - name of the layout
	 * @return requested form-layout
	 * @throws FormServiceException if the specified layout cannot be found
	 */
	FormLayout getLayout(String formName, String layoutName) throws FormServiceException;
	
	
	/**
	 * Saves a new layout.
	 * @param formName - name of the form
	 * @param layoutName - name of the layout
	 * @param type - type of the template
	 * @param content - byte array with the layout document
	 * @throws FormServiceException if the specified layout already exists
	 */
	void createLayout(String formName, String layoutName, FormLayoutType type, byte[] content) throws FormServiceException;
	
	
	/**
	 * Updates an existing layout.
	 * @param formName - name of the form
	 * @param layoutName - name of the layout
	 * @param content - byte array with the layout document
	 * @throws FormServiceException if the specified layout cannot be found 
	 * 					or the logged user does not have permission to do the operation
	 */
	void updateLayout(String formName, String layoutName, byte[] content) throws FormServiceException;
	
	
	/**
	 * Deletes an existing layout.
	 * @param formName - name of the form
	 * @param layoutName - name of the layout
	 * @throws FormServiceException if the specified layout cannot be found 
	 * 					or the logged user does not have permission to do the operation
	 */
	void deleteLayout(String formName, String layoutName) throws FormServiceException;
	
	
	/**
	 * Gets all records of given type (entity) in odML format.
	 * @param entity - the entity's name
	 * @param type - the type of the odml template
	 * @return byte array with odML data
	 * @throws FormServiceException if the required data cannot be retrieved
	 */
	byte[] getOdmlData(String entity, FormLayoutType type) throws FormServiceException;
	
	
	/**
	 * Gets the record determined by the given ID in odML format.
	 * @param entity - the entity's name
	 * @param id - the record's ID
	 * @param type - the type of the odml template
	 * @return byte array with odML data
	 * @throws FormServiceException if the required data cannot be retrieved
	 */
	byte[] getOdmlData(String entity, Integer id, FormLayoutType type) throws FormServiceException;
	
	
	/**
	 * Gets count of records of the given type (entity).
	 * @param entity - the entity's name
	 * @return count of records
	 * @throws FormServiceException if the required data cannot be retrieved
	 */
	RecordCountData countDataRecords(String entity) throws FormServiceException;
	
	
	/**
	 * Gets list of IDs of all records of the given type (entity).
	 * @param entity - the entity's name
	 * @return list of IDs
	 * @throws FormServiceException if the required data cannot be retrieved
	 */
	RecordIdsDataList getRecordIds(String entity) throws FormServiceException;
	
	
	/**
	 * Creates a new persistent object from the odML data document.
	 * @param entity - the entity's name
	 * @param odml - the odML document
	 * @param type - the type of the odml template
	 * @return ID of the created record
	 * @throws FormServiceException if the record cannot be created
	 */
	Integer createRecord(String entity, byte[] odml, FormLayoutType type) throws FormServiceException;

}
