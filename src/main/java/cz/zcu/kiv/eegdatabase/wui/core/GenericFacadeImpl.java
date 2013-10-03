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
 *   GenericFacadeImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core;

import java.io.Serializable;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. Danek
 */
public class GenericFacadeImpl<T, PK extends Serializable> implements GenericFacade<T, PK> {

	private GenericService<T, PK> service;

	public GenericFacadeImpl() {
		this(null);
	}

	public GenericFacadeImpl(GenericService<T, PK> service) {
		this.service = service;
	}

	@Override
	@Transactional
	public PK create(T newInstance) {
		return service.create(newInstance);
	}

	@Override
	@Transactional(readOnly = true)
	public T read(PK id) {
		return service.read(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> readByParameter(String parameterName, Object parameterValue) {
		return service.readByParameter(parameterName, parameterValue);
	}

	@Override
	@Transactional
	public void update(T transientObject) {
		service.update(transientObject);
	}

	@Override
	@Transactional
	public void delete(T persistentObject) {
		service.delete(persistentObject);
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> getAllRecords() {
		return service.getAllRecords();
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> getRecordsAtSides(int first, int max) {
		return service.getRecordsAtSides(first, max);
	}

	@Override
	@Transactional(readOnly = true)
	public int getCountRecords() {
		return service.getCountRecords();
	}

	@Override
	public List<T> getUnique(T example) {
		return this.service.getUnique(example);
	}
}
