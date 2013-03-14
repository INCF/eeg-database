package cz.zcu.kiv.eegdatabase.data.indexing;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.logic.controller.social.LinkedInManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.social.linkedin.api.Post;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 12.3.13
 * Time: 21:32
 * To change this template use File | Settings | File Templates.
 */
@Service
public class IndexingService implements ApplicationContextAware {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private Indexer<Object> indexer;

    @Autowired
    private Indexer<Post> linkedInIndexer;

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
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws SolrServerException
     */
    public void indexDatabase() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException,
            InstantiationException, NoSuchMethodException, IOException, SolrServerException {
        // get all dao beans from the Spring application context
        Map<String, ? extends GenericDao> beanMap = applicationContext.getBeansOfType(GenericDao.class);
        beanMap.remove("genericDao"); // delete the Generic Dao reference which we don't want to query
        for (String key : beanMap.keySet()) {
            log.debug(key + ": " + beanMap.get(key));
            List<Object> daoRecords = beanMap.get(key).getAllRecords();
            log.debug("found records: " + daoRecords.size());
            indexer.indexAll(daoRecords);
        }
    }

    /**
     * Does indexing of all LinkedIn posts.
     * @throws IllegalAccessException
     * @throws SolrServerException
     * @throws IOException
     */
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
    public void indexAll() throws IllegalAccessException, SolrServerException, InstantiationException, IOException,
            NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
        indexDatabase();
        indexLinkedIn();
    }
}
