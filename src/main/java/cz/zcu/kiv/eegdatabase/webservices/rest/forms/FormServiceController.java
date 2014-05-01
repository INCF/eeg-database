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
 * FormServiceController.java, 3. 1. 2014 10:08:47, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.forms;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cz.zcu.kiv.eegdatabase.data.pojo.FormLayout;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.wrappers.RecordCountData;
import cz.zcu.kiv.eegdatabase.webservices.rest.forms.wrappers.AvailableFormsDataList;
import cz.zcu.kiv.eegdatabase.webservices.rest.forms.wrappers.AvailableLayoutsDataList;
import cz.zcu.kiv.eegdatabase.webservices.rest.forms.wrappers.RecordIdsDataList;


/**
 * Controller class mapping the REST form-layout service.
 *
 * @author Jakub Krauz
 */
@Controller
@RequestMapping("/form-layouts")
@Secured("IS_AUTHENTICATED_FULLY")
public class FormServiceController {
	
	/** Logger. */
	private static final Logger logger = LoggerFactory.getLogger(FormServiceController.class);
	
	/** The service object providing data. */
	@Autowired
	private FormService service;
	
	
	/**
	 * Gets the count of forms available.
	 * @return count of forms available
	 */
	@RequestMapping(value = "/form/count", method = RequestMethod.GET)
	@ResponseBody
	public RecordCountData availableFormsCount() {
		return service.availableFormsCount();
	}
	
	
	/**
	 * Gets names of all forms with available layouts.
	 * @param mineOnly - if true, processes only logged user's records, otherwise all records available
	 * @return names of forms with available layouts
	 */
	@RequestMapping(value = "/form/available", method = RequestMethod.GET)
	@ResponseBody
	public AvailableFormsDataList availableForms (
					@RequestParam(value = "mineOnly", defaultValue = "false") boolean mineOnly) {
		
		return service.availableForms(mineOnly);
	}
	
	
	/**
	 * Gets the count of form-layouts available.
	 * @param formName - name of a specific form, or null (any form)
	 * @return count of form-layouts available
	 */
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	@ResponseBody
	public RecordCountData availableLayoutsCount (
					@RequestParam(value = "form", required = false) String formName) {
		
		if (formName == null)
			return service.availableLayoutsCount();
		else
			return service.availableLayoutsCount(formName);
	}
	
	
	/**
	 * Gets names of all form layouts available.
	 * @param mineOnly - if true, processes only logged user's records, otherwise all records available
	 * @param formName - name of a specific form, or null (any form)
	 * @return names of form layouts available
	 */
	@RequestMapping(value = "/available", method = RequestMethod.GET)
	@ResponseBody
	public AvailableLayoutsDataList availableLayouts (
					@RequestParam(value = "mineOnly", defaultValue = "false") boolean mineOnly,
					@RequestParam(value = "form", required = false) String formName) {
		
		if (formName == null)
			return service.availableLayouts(mineOnly);
		else
			return service.availableLayouts(formName, mineOnly);
	}
	
	
	/**
	 * Retrieves an existing layout with the specified name.
	 * @param formName - name of the form
	 * @param layoutName - name of the layout
	 * @param response - HTTP response object
	 * @throws IOException if an error occurs while writing to the response output stream
	 * @throws FormServiceException if the specified layout cannot be found
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public void getLayout(@RequestParam("form") String formName, @RequestParam("layout") String layoutName, 
					HttpServletResponse response) throws FormServiceException, IOException {

		FormLayout layout = service.getLayout(formName, layoutName);
		response.setContentType(MediaType.APPLICATION_XML_VALUE);
		response.setContentLength(layout.getContent().length);
		response.getOutputStream().write(layout.getContent());
		response.flushBuffer();
	}
	
	
	/**
	 * Saves a new layout with the specified name.
	 * @param formName - name of the form
	 * @param layoutName - name of the layout
	 * @param content - the layout in XML format
	 * @throws FormServiceException if the create operation cannot be executed
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public void createLayout(@RequestParam("form") String formName, 
			@RequestParam("layout") String layoutName, @RequestBody byte[] content) throws FormServiceException {

		service.createLayout(formName, layoutName, content);
	}
	
	
	/**
	 * Updates an existing layout.
	 * @param formName - name of the form
	 * @param layoutName - name of the layout
	 * @param content - the layout in XML format
	 * @throws FormServiceException if the update operation cannot be executed
	 */
	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_XML_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void updateLayout(@RequestParam("form") String formName, 
			@RequestParam("layout") String layoutName, @RequestBody byte[] content) throws FormServiceException {

		service.updateLayout(formName, layoutName, content);
	}
	
	
	/**
	 * Deletes an existing layout.
	 * @param formName - name of the form
	 * @param layoutName - name of the layout
	 * @throws FormServiceException if the delete operation cannot be executed
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteLayout(@RequestParam("form") String formName, 
			@RequestParam("layout") String layoutName) throws FormServiceException {

		service.deleteLayout(formName, layoutName);
	}
	
	
	/**
	 * Retrieves all records of given entity in odML data format.
	 * @param entity - the requested entity name
	 * @param response - HTTP response object
	 * @throws IOException if an error occurs when writing to response stream
	 * @throws FormServiceException if required odML data cannot be retrieved
	 */
    @RequestMapping(value = "/data", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public void getData(@RequestParam("entity") String entity,
                        @RequestParam(value = "id", required = false) Integer id,
			            HttpServletResponse response) throws IOException, FormServiceException {
    	byte[] odml;
        if (id == null)
        	odml = service.getOdmlData(entity);
		else
			odml = service.getOdmlData(entity, id);
		
        response.setContentType(MediaType.APPLICATION_XML_VALUE);
        response.setContentLength(odml.length);
        response.getOutputStream().write(odml);
        response.flushBuffer();
	}
    
    
    @RequestMapping(value = "/data/count", method = RequestMethod.GET)
    @ResponseBody
    public RecordCountData getDataCount(@RequestParam("entity") String entity) throws FormServiceException {
    	return service.countDataRecords(entity);
    }
    
    
    @RequestMapping(value = "/data/ids", method = RequestMethod.GET)
    @ResponseBody
    public RecordIdsDataList getDataIds(@RequestParam("entity") String entity) throws FormServiceException {
    	return service.getRecordIds(entity);
    }
	
	
	/**
	 * Handles the {@link FormServiceException} exception.
	 * @param exception - the exception
	 * @param response - HTTP response
	 * @throws IOException if an output exception occurs while writing to the response stream
	 */
	@ExceptionHandler(FormServiceException.class)
    public void handleException(FormServiceException exception, HttpServletResponse response) throws IOException {
		switch (exception.what()) {
			case PERMISSION:
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission for this operation.");
				break;
			case NOT_FOUND:
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "The requested resource was not found.");
				break;
			case CONFLICT:
				response.sendError(HttpServletResponse.SC_CONFLICT, "The specified name is in conflict with an existing resource.");
				break;
			default:
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		logger.debug("The requested operation was not successfull.", exception);
    }

	
}
