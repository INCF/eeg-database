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
 *   HibernateFilter.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.persistent;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.CleanupFailureDataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.OpenSessionInViewFilter;

/**
 * @author Jindra
 */
public class HibernateFilter extends OpenSessionInViewFilter {

    @Override
    protected Session getSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
        Session session = SessionFactoryUtils.getSession(sessionFactory, true);

        // set FlushMode to AUTO in order to save objects.
        session.setFlushMode(FlushMode.AUTO);

        return session;
    }

    @Override
    protected void closeSession(Session session, SessionFactory sessionFactory) throws DataAccessResourceFailureException {
        if (session != null && session.isOpen() && session.isConnected()) {
            try {
                session.flush();
            } catch (HibernateException e) {
                throw new CleanupFailureDataAccessException("Failed to flush session before close: " + e.getMessage(), e);
            } catch (Exception e) {
            } finally {
                super.closeSession(session, sessionFactory);
            }
        }
    }
}
