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
 *   IndexingServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.indexing;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.logic.controller.social.LinkedInManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.social.linkedin.api.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the indexing service.
 * User: Jan Koren
 * Date: 12.3.13
 */
@Service
public class IndexingServiceImpl implements IndexingService, ApplicationContextAware {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private Indexer indexer;
    @Autowired
    private Indexer<Post> linkedInIndexer;
    @Autowired
    private Indexer<String> autocompleteIndexer;


    // if required, the tests would fail because the bean is not wired in
    // the test context due to its complex configuration
    @Autowired(required = false)
    private LinkedInManager linkedin;

    ApplicationContext applicationContext = null;

    private static final int LINKEDIN_POSTS_STEP = 50;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Performs indexing of the whole database.
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws SolrServerException
     */
    @Async
    @Transactional(propagation=Propagation.MANDATORY)
    public void indexDatabase() throws IllegalAccessException, SolrServerException, IOException, NoSuchMethodException, InstantiationException {

        // get required dao beans
        Set<Class<? extends GenericDao>> daoClasses = IndexingUtils.getDaosForIndexing();

        for (Class<? extends GenericDao> daoClass : daoClasses) {
            log.debug("class: " + daoClass.getName());

            List daoRecords = applicationContext.getBean(daoClass).getAllRecordsFull();

            indexer.indexAll(daoRecords);
        }
    }

    /**
     * Does indexing of all LinkedIn posts.
     * @throws IllegalAccessException
     * @throws SolrServerException
     * @throws IOException
     */
    @Async
    @Transactional(propagation=Propagation.MANDATORY)
    public void indexLinkedIn() throws IllegalAccessException, SolrServerException, IOException {

        int startIndex = 0;
        List<Post> posts = new ArrayList<Post>();
        List<Post> receivedPosts = linkedin.getGroupPostsWithMoreInfo(LINKEDIN_POSTS_STEP, startIndex);
        // additional iterations are necessary due to the limit of 50 returned posts at maximum (LinkedIn API restriciton)
        while (receivedPosts != null) {
            posts.addAll(receivedPosts);
            startIndex += LINKEDIN_POSTS_STEP;
            receivedPosts = linkedin.getGroupPostsWithMoreInfo(LINKEDIN_POSTS_STEP, startIndex);
        }

        linkedInIndexer.indexAll(posts);
    }

    /**
     * Indexer wrapper which indexes the input query string for autocomplete
     * suggestions.
     * @param phrase The searched phrase.
     */
    @Async
    public void addToAutocomplete(String phrase) {
        try {
            autocompleteIndexer.index(phrase);
        } catch (IllegalAccessException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        } catch (SolrServerException e) {
            log.error(e);
        }
    }

    /**
     * Does indexing of specific LinkedIn posts.
     * @param posts Posts that are to be indexed.
     * @throws IllegalAccessException
     * @throws SolrServerException
     * @throws IOException
     */
    public void indexLinkedIn(List<Post> posts) throws IllegalAccessException, SolrServerException, IOException {
        linkedInIndexer.indexAll(posts);
    }

    /**
     * Does indexing of both database and LinkedIn posts.
     * @throws IllegalAccessException
     * @throws SolrServerException
     * @throws InstantiationException
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */

    @Transactional(propagation=Propagation.REQUIRED)
    public void indexAll() {
//        log.info("Starting indexing data");
//        try {
//            indexDatabase();
//            indexLinkedIn();
//            log.info("Optimalization of index started");
//            long start = System.currentTimeMillis();
//            indexer.getSolrServer().optimize();
//            long end = System.currentTimeMillis();
//            log.info("Optimalization ended, duration " + (end - start) + "ms");
//        } catch (IllegalAccessException e) {
//            log.error(e);
//        } catch (SolrServerException e) {
//            log.error(e);
//        } catch (IOException e) {
//            log.error(e);
//        }  catch (NoSuchMethodException e) {
//            log.error(e);
//        } catch (InstantiationException e) {
//            log.error(e);
//        }
//        log.info("Indexing finished");
    }
}
