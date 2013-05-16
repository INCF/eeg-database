package cz.zcu.kiv.eegdatabase.logic.indexing;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.social.linkedin.api.Post;

import java.io.IOException;
import java.util.List;

/**
 * Defines methods for the indexing service
 * User: Jan Koren
 * Date: 7.4.13
 * Time: 15:02
 */
public interface IndexingService {

    /**
     * Performs indexing of the whole database.
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws SolrServerException
     */
    public void indexDatabase() throws IllegalAccessException, SolrServerException, IOException, NoSuchMethodException, InstantiationException;

    /**
     * Does indexing of all LinkedIn posts.
     * @throws IllegalAccessException
     * @throws SolrServerException
     * @throws IOException
     */
    public void indexLinkedIn() throws IllegalAccessException, SolrServerException, IOException;

    /**
     * Does indexing of specific LinkedIn posts.
     * @param posts Posts that are to be indexed.
     * @throws IllegalAccessException
     * @throws SolrServerException
     * @throws IOException
     */
    public void indexLinkedIn(List<Post> posts) throws IllegalAccessException, SolrServerException, IOException;

    /**
     * Does indexing of both database and LinkedIn posts.
     * @throws IllegalAccessException
     * @throws SolrServerException
     * @throws InstantiationException
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws ClassNotFoundException
     */
    public void indexAll();

    /**
     * Adds the searched phrase to the index to obtain the phrase in the autocomplete field in future searches.
     * @param phrase
     */
    public void addToAutocomplete(String phrase);
}
