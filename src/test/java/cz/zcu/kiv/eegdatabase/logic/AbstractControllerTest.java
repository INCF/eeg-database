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
 *   AbstractControllerTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic;

import cz.zcu.kiv.eegdatabase.data.dao.ArticleDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ReservationDao;
import cz.zcu.kiv.eegdatabase.data.DaoCollection;
import org.hibernate.SessionFactory;
import org.junit.Ignore;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * Created: 5.5.11
 *
 * @version 0.3
 * @author: Jenda Kolena, jendakolena@gmail.com
 */
public abstract class AbstractControllerTest extends AbstractTransactionalDataSourceSpringContextTests
{
    protected SessionFactory sessionFactory;
    protected ReservationDao reservationDao;
    protected PersonDao personDao;
    protected ResearchGroupDao researchGroupDao;
    protected ArticleDao articleDao;
    protected HierarchicalMessageSource messageSource;

    public AbstractControllerTest()
    {
        setDependencyCheck(false);
        setAutowireMode(AUTOWIRE_BY_NAME);
        changeParserImplementationToXerces();//Important!
    }

    /**
     * If not changed, oracle parser would try to parse hibernate configurations
     * and fail with the following error:
     * ERROR ErrorLogger - Error parsing XML (31) : http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd<Line 31, Column 2>:
     * XML-20068: (Fatal Error) content model is not deterministic
     * org.hibernate.InvalidMappingException: Unable to read XML
     * Setting SAXParserFactory and DocumentBuilderFactory will change the parser
     * to xerces, enabling Hibernate-based tests
     */
    private void changeParserImplementationToXerces() {
        System.setProperty("javax.xml.parsers.SAXParserFactory","org.apache.xerces.jaxp.SAXParserFactoryImpl");
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory","org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
    }

    protected String[] getConfigLocations()
    {
        return new String[]{"test-context.xml"};
    }

    protected DaoCollection getDaoCollection()
    {
        return new DaoCollection(personDao, researchGroupDao, reservationDao, articleDao);
    }

    public void setSessionFactory(SessionFactory factory)
    {
        this.sessionFactory = factory;
    }

    public void setReservationDao(ReservationDao reservationDao)
    {
        this.reservationDao = reservationDao;
    }

    public void setPersonDao(PersonDao personDao)
    {
        this.personDao = personDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao)
    {
        this.researchGroupDao = researchGroupDao;
    }

    public void setMessageSource(HierarchicalMessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

    protected void debug(String message)
    {
        System.out.println("[DEBUG]: " + message);
    }
}
