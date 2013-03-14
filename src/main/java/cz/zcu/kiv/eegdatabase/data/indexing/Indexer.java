package cz.zcu.kiv.eegdatabase.data.indexing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 28.2.13
 * Time: 1:50
 * To change this template use File | Settings | File Templates.
 */
public abstract class Indexer<T> {

    @Autowired
    protected SolrServer solrServer;

    protected Log log = LogFactory.getLog(getClass());

    private static final int BATCH_COMMIT_SIZE = 100;

    /**
     * Performs indexing of a POJO object.
     * @param instance The POJO to be indexed.
     * @throws IllegalAccessException
     * @throws IOException
     * @throws SolrServerException
     */
    public void index(T instance) throws IllegalAccessException,
            IOException, SolrServerException {
        SolrInputDocument document = prepareForIndexing(instance);
        log.debug(document);

        if (document == null || document.isEmpty()) {
            return;
        }
        solrServer.add(document); // if the document already exists, it is replaced by the new one
        UpdateResponse response = solrServer.commit();
        log.debug("Document " + document.get("uuid").getValue().toString() + " added to the solr index.");
        logCommitResponse(response);
    } // TODO specify (custom) exceptions

    /**
     * Performs indexing of a list of objects.
     * @param instanceList
     * @throws IllegalAccessException
     * @throws SolrServerException
     * @throws IOException
     */
    public void indexAll(List<T> instanceList) throws IllegalAccessException, SolrServerException, IOException {
        int documentsToBeAdded = 0;
        for(T instance : instanceList) {
            SolrInputDocument document = prepareForIndexing(instance);
            // skip empty or null documents
            if(document == null || document.isEmpty()) {
                continue;
            }

            solrServer.add(document);
            documentsToBeAdded++;
            // commit in a batch
            if(documentsToBeAdded % BATCH_COMMIT_SIZE == 0) {
                UpdateResponse response = solrServer.commit();
                logCommitResponse(response);
            }
        }

        // commit the rest if there are any uncommited documents
        if(documentsToBeAdded % BATCH_COMMIT_SIZE != 0) {
            UpdateResponse response = solrServer.commit();
            logCommitResponse(response);
        }
    }

    public abstract void unindex(T instance) throws IOException, SolrServerException; // TODO specify (custom) exceptions

    public abstract void unindexAll() throws IOException, SolrServerException;

    public abstract SolrInputDocument prepareForIndexing(T instance) throws IllegalAccessException, IOException, SolrServerException;

    /**
     *
     * @param response
     * @throws IOException
     * @throws SolrServerException
     */
    protected void logCommitResponse(UpdateResponse response)
            throws IOException, SolrServerException {

        long time = response.getElapsedTime();
        int status = response.getStatus();
        log.debug("Time elapsed: " + time + ", status code: " + status);
    }

    public void setSolrServer(SolrServer solrServer) {
        this.solrServer = solrServer;
    }
}
