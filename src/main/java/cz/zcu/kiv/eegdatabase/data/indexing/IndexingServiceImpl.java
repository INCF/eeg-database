package cz.zcu.kiv.eegdatabase.data.indexing;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.logic.controller.social.LinkedInManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.social.linkedin.api.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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


    // if required, the tests would fail because the bean is not wired in the test context
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
    @Transactional
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
    @Transactional
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
    @Scheduled(cron = "${solr.indexingPeriod}")
    public void indexAll() {
        log.info("Starting indexing data");
        try {
            indexDatabase();
            indexLinkedIn();
            log.info("Optimalization of index started");
            long start = System.currentTimeMillis();
            indexer.getSolrServer().optimize();
            long end = System.currentTimeMillis();
            log.info("Optimalization ended, duration " + (end - start) + "ms");
        } catch (IllegalAccessException e) {
            log.error(e);
        } catch (SolrServerException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        }  catch (NoSuchMethodException e) {
            log.error(e);
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        log.info("Indexing finished");
    }
}
