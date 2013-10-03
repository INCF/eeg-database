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
 *   GenericServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import java.io.Serializable;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. Danek
 */
public class GenericServiceImpl <T, PK extends Serializable> implements GenericService<T, PK> {

    private GenericDao<T, PK> dao;

    public GenericServiceImpl() {
	this(null);
    }

    public GenericServiceImpl(GenericDao<T, PK> dao) {
	this.dao = dao;
    }

    @Override
    @Transactional
    public PK create(T newInstance) {
	return dao.create(newInstance);
    }

    @Override
    @Transactional(readOnly=true)
    public T read(PK id) {
	return dao.read(id);
    }

    @Override
    @Transactional(readOnly=true)
    public List<T> readByParameter(String parameterName, Object parameterValue) {
	return dao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(T transientObject) {
	dao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(T persistentObject) {
	dao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly=true)
    public List<T> getAllRecords() {
	return dao.getAllRecords();
    }

    @Override
    @Transactional(readOnly=true)
    public List<T> getRecordsAtSides(int first, int max) {
	return dao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly=true)
    public int getCountRecords() {
	return dao.getCountRecords();
    }

	@Override
	public List<T> getUnique(T example) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
