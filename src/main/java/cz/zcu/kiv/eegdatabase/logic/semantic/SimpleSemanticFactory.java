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
 *   SimpleSemanticFactory.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.semantic;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import tools.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.apache.commons.io.IOUtils.toByteArray;

/**
 * Factory for transforming POJO object to resources of semantic web
 * User: pbruha
 * Date: 24.2.11
 * Time: 13:36
 */
public class SimpleSemanticFactory implements InitializingBean, ApplicationContextAware, SemanticFactory {

    private Log log = LogFactory.getLog(getClass());
    private ApplicationContext context;
    private List<GenericDao> gDaoList = new ArrayList<GenericDao>();
    private List<Object> dataList = new ArrayList<Object>();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    private File ontologyFile;         // temporary ontology document
    private Resource ontologyHeader;   // owl document with ontology header statements
    
    @Value("${semantic.transformation.regularInterval}")
    private int interval;

    @Value("${semantic.transformation.delayFirstTransform}")
    private int delay;

    @Autowired
    private TransactionTemplate transactionTemplate;


    /**
     * Creates temporary files for ontology document storing and
     * sets the timer to run the transformation process regularly.
     */
    public void init() {
        try {
            ontologyFile = File.createTempFile("ontology_", ".rdf.tmp");
            ontologyFile.deleteOnExit();
        } catch (IOException e) {
            log.error("Could not create the temporary rdf/xml file to store the ontology!", e);
        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TransformTask(), delay, interval);
    }


    /**
     * Creates list of instances of DAO
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        String[] beanNamesForType = context.getBeanNamesForType(GenericDao.class);
        for(String name : beanNamesForType) {
            gDaoList.add((GenericDao) context.getBean(name));
           // break; // ???
        }
    }

    
    @Override
    public InputStream getOntology(String syntax) throws IOException {
        InputStream is;
        JenaBeanExtension jbe;

        // check the validity of user request on serialization syntax
        if (syntax == null || syntax.equalsIgnoreCase("owl")
                    || syntax.equalsIgnoreCase(Syntax.RDF_XML_ABBREV))
            syntax = Syntax.RDF_XML;
        syntax = syntax.toUpperCase();

        // get the ontology document in required syntax
        lock.readLock().lock();
        InputStream fileIn = new FileInputStream(ontologyFile);
        if ( !syntax.equals(Syntax.RDF_XML)) {
            jbe = new JenaBeanExtensionTool();
            jbe.loadStatements(fileIn, Syntax.RDF_XML);
            fileIn.close();
            lock.readLock().unlock();
            is = jbe.getOntologyDocument(syntax);
        } else {
            is = new ByteArrayInputStream(toByteArray(fileIn));
            fileIn.close();
            lock.readLock().unlock();
        }

        return is;
    }


    @Override
    public InputStream getOntologySchema(String syntax) throws IOException {
        InputStream is;
        JenaBeanExtension jbe;

        // create the ontology schema
        lock.readLock().lock();
        InputStream fileIn = new FileInputStream(ontologyFile);
        jbe = new JenaBeanExtensionTool();
        jbe.loadStatements(fileIn, Syntax.RDF_XML);
        fileIn.close();
        lock.readLock().unlock();
        is = jbe.getOntologySchema(Syntax.RDF_XML_ABBREV);

        return is;
    }


    @Override
    public InputStream getOntologyOwlApi(String syntax) throws IOException,
                                    OWLOntologyCreationException, OWLOntologyStorageException {
        InputStream is;
        OwlApi owlApi;

        if (syntax == null)
            syntax = Syntax.OWL_XML;

        // get the serialization from the OWL API
        lock.readLock().lock();
        InputStream fileIn = new FileInputStream(ontologyFile);
        owlApi = new OwlApiTool(fileIn);
        fileIn.close();
        lock.readLock().unlock();
        is = owlApi.getOntologyDocument(syntax);

        return is;
    }


    /**
     * Loads date for transforms POJO object to resouces of semantic web.
     */
    private void loadData() {
         for (GenericDao gDao : gDaoList) {
            dataList.addAll(gDao.getAllRecords());
         }
    }


    /**
     * Sets application context.
     * @param ac - application context
     * @throws BeansException
     */
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        context = ac;
    }


    /**
     * Sets resource with static ontology header statements.
     * @param ontologyHeader - resource in rdf/xml
     */
    public void setOntologyHeader(Resource ontologyHeader) {
        this.ontologyHeader = ontologyHeader;
    }


    /**
     * Transforms the object-oriented model into the OWL ontology.
     * Serialization of this ontology is stored in a temporary file.
     */
    public void transformModel() {
        JenaBeanExtension jbe;
        OutputStream out;

        try {
            loadData();
            jbe = creatingJenaBean();
            lock.writeLock().lock();
            out = new FileOutputStream(ontologyFile);
            jbe.writeOntologyDocument(out, Syntax.RDF_XML);
            out.close();
        } catch (FileNotFoundException e) {
            log.error("Could not find the temporary rdf/xml file to store the ontology!", e);
        } catch (IOException e) {
            log.error("Could not close the temporary rdf/xml file!", e);
        } catch (Exception e) {
            log.error("Could not create the ontology!", e);
        } finally {
            lock.writeLock().unlock();
        }

    }


    /**
     * Creates an instance of JenaBeanExtension with loaded model.
     * @return instance of JenaBeanExtension
     */
    private JenaBeanExtension creatingJenaBean() {
        JenaBeanExtension jbe = new JenaBeanExtensionTool();
        try {
            jbe.loadStatements(ontologyHeader.getInputStream(), Syntax.RDF_XML_ABBREV);
        } catch (IOException e) {
            log.error("Could not open the input stream associated with the ontology header " +
                    "configuration document: " + ontologyHeader.getFilename(), e);
        }
        jbe.loadOOM(dataList);
        jbe.declareAllClassesDisjoint();
        return jbe;
    }
    


    /**
     * Represents the transformation task for a timer.
     * This task should be run regularly to keep the ontology up-to-date.
     */
    private class TransformTask extends TimerTask {

        @Override
        public void run() {
            log.debug("Starting OOP to OWL transformation process.");
            transactionTemplate.execute(new TransactionCallback() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    transformModel();
                    return null;
                }
            });
            log.debug("OOP to OWL transformation process finished.");
        }

    }


}


