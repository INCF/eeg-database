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
 *   IndexingService.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
