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
 *   PerformanceTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.perf.persistentLayer;

import org.hibernate.SessionFactory;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 16.5.11
 * Time: 17:34
 * To change this template use File | Settings | File Templates.
 * Abstract class includes setting for test.
 */

//@ContextConfiguration(locations = {"/test-context.xml"})
//setting perf. test
//@PerfTest(invocations = 10, threads = 1)
//@RunWith(JUnit4.class)
public abstract class PerformanceTest extends AbstractTransactionalDataSourceSpringContextTests {


//    GraphRenderer executionLogger = new GraphRenderer();
//    @Rule
//    public ContiPerfRule rule = new ContiPerfRule(executionLogger);
     private SessionFactory sessionFactory;

     public PerformanceTest() {
        setDependencyCheck(false);
        setAutowireMode(AUTOWIRE_BY_NAME);
        changeParserImplementationToXerces();//Important!
    }

    protected String[] getConfigLocations() {
        return new String[] {"test-context.xml"};
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

    public void setSessionFactory(SessionFactory factory) {
    this.sessionFactory = factory;
    }

//     @After
//     /**
//      * Method called after test, generate report.
//      */
//	 public void saveContiPerfResults(){
//    	executionLogger.save();
//	 }



}
