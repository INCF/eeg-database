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
 *   SimpleDataFileDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;

import java.io.InputStream;
import java.sql.Blob;

/**
 * @author František Liška
 */
public class SimpleDataFileDao extends SimpleGenericDao<DataFile,Integer> implements DataFileDao {

    public SimpleDataFileDao(){
        super(DataFile.class);
    }

    public Blob createBlob(byte[] input){
        return getHibernateTemplate().getSessionFactory().getCurrentSession().getLobHelper().createBlob(input);
    }

    public Blob createBlob(InputStream input, int length){
        return getHibernateTemplate().getSessionFactory().getCurrentSession().getLobHelper().createBlob(input,length);
    }
}
