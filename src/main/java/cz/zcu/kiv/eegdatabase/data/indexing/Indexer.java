package cz.zcu.kiv.eegdatabase.data.indexing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
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
        prepareForIndexing(instance);
        System.out.println(document);
        addDocumentToIndex(document);
    } // TODO specify (own) exceptions

    public void indexAll(List<T> instanceList) throws IllegalAccessException, SolrServerException, IOException {
        for(T instance : instanceList) {
            index(instance);
        }
    }

    public abstract void unindex(T instance) throws Exception; // TODO specify (own) exceptions

    public abstract SolrInputDocument prepareForIndexing(T instance) throws IllegalAccessException, IOException, SolrServerException;

    protected void addDocumentToIndex(SolrInputDocument document)
            throws IOException, SolrServerException {
        if(document == null) {
            log.info("Document is null;");
            // TODO Throw new exception?
        }

        if(document.isEmpty()) {
            log.info("Nothing added to the solr index.");
            return;
        }

        solrServer.add(document); // if the document already exists, it is replaced by the new one

        UpdateResponse response = solrServer.commit();

        log.info("Document " + document.get("uuid").getValue().toString() + " added to the solr index.");
        long time = response.getElapsedTime();
        int status = response.getStatus();
        log.info("Time elapsed: " + time + ", status code: " + status);
    }


    public void setSolrServer(SolrServer solrServer) {
        this.solrServer = solrServer;
    }
}
