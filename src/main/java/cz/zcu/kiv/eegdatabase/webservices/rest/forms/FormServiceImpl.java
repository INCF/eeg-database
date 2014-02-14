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
 * FormServiceImpl.java, 8. 1. 2014 12:11:20, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.forms;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import cz.zcu.kiv.eegdatabase.webservices.rest.forms.wrappers.AvailableLayoutsData;
import cz.zcu.kiv.eegdatabase.webservices.rest.forms.wrappers.AvailableLayoutsDataList;
import cz.zcu.kiv.formgen.Form;
import cz.zcu.kiv.formgen.LayoutGenerator;
import cz.zcu.kiv.formgen.core.SimpleLayoutGenerator;
import cz.zcu.kiv.formgen.odml.OdmlFormProvider;

/**
 * Service implementation for form layouts. Used in REST.
 * 
 * @author Jakub Krauz
 */
@Service
public class FormServiceImpl implements FormService, InitializingBean {
	
	
	// TODO databaze
	
	
	/** Base package of POJO classes. */
	private static final String POJO_BASE = "cz.zcu.kiv.eegdatabase.data.pojo";
	
	/** Tool for transforming POJO classes to form layouts. */
	private LayoutGenerator generator;
	
	
	/**
	 * Initializes the FormGenerator object.
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		generator = new SimpleLayoutGenerator(new OdmlFormProvider());
		generator.loadPackage(POJO_BASE);
	}
	
	
	/**
     * {@inheritDoc}
	 */
	@Override
	public int availableFormsCount() {
		return generator.getForms().size();
	}
	
	
	/**
     * {@inheritDoc}
	 */
	@Override
	public List<String> availableForms() {
		List<String> list = new LinkedList<String>();				
		for (Form form : generator.getForms())
			list.add(form.getName());
		return list;
	}
	
	
	/**
     * {@inheritDoc}
	 */
	@Override
	public int availableLayoutsCount() {
		return generator.getForms().size();
	}
	
	
	/**
     * {@inheritDoc}
	 */
	@Override
	public int availableLayoutsCount(String formName) {
		return 1;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AvailableLayoutsDataList availableLayouts() {
		List<AvailableLayoutsData> list = new LinkedList<AvailableLayoutsData>();				
		for (Form form : generator.getForms())
			list.add(new AvailableLayoutsData(form.getName(), form.getLayoutName()));
		return new AvailableLayoutsDataList(list);
	}
	
	
	/**
     * {@inheritDoc}
	 */
	@Override
	public AvailableLayoutsDataList availableLayouts(String formName) {
		List<AvailableLayoutsData> list = new LinkedList<AvailableLayoutsData>();				
		Form form = generator.getForm(formName);
		list.add(new AvailableLayoutsData(form.getName(), form.getLayoutName()));
		return new AvailableLayoutsDataList(list);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Form getLayout(String formName, String layoutName) {
		return generator.getForm(formName);
	}


}
