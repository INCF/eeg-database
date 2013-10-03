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
 *   Indexer.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.indexing;

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
    }

    /**
     * Performs indexing of a list of objects.
     * @param instanceList The list of all objects to be indexed.
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

    /**
     * Removes an index document that corresponds to a given object.
     * @param instance the instance whose document representation should be removed from index.
     * @throws IOException
     * @throws SolrServerException
     */
    public abstract void unindex(T instance) throws IOException, SolrServerException;

    /**
     * Unindexes all documents from a specific subset of the whole index.
     * The document subset is determined by the type of the indexer
     * and its actual implementation of this method.
     * @throws IOException
     * @throws SolrServerException
     */
    public abstract void unindexAll() throws IOException, SolrServerException;

    /**
     * Transforms the input instance (the method parameter) to a Solr document.
     * @param instance The input object.
     * @return The Solr document representing the input object.
     * @throws IllegalAccessException
     * @throws IOException
     * @throws SolrServerException
     */
    public abstract SolrInputDocument prepareForIndexing(T instance) throws IllegalAccessException, IOException, SolrServerException;

    /**
     * Logs information from the response obtained from the Solr server.
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

    public SolrServer getSolrServer() {
        return solrServer;
    }
}
