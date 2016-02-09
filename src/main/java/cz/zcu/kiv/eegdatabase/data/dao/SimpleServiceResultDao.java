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
 *   SimpleServiceResultDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ServiceResult;
import org.hibernate.Hibernate;

import java.sql.Blob;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 9.11.11
 * Time: 14:13
 * To change this template use File | Settings | File Templates.
 */
public class SimpleServiceResultDao extends SimpleGenericDao<ServiceResult, Integer> implements ServiceResultDao {
    public SimpleServiceResultDao() {
        super(ServiceResult.class);
    }

    @Override
    public List<ServiceResult> getResultByPerson(int personId) {
        String hqlQuery = "from ServiceResult s where s.owner.personId = :personId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("personId", personId).list();
    }

    @Override
    public Blob createBlob(byte[] content) {
        return Hibernate.getLobCreator(this.getSessionFactory().getCurrentSession()).createBlob(content);
    }
}
