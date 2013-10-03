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
 *   IndexingServiceTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.search;

import cz.zcu.kiv.eegdatabase.logic.indexing.IndexingService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.linkedin.api.Group;
import org.springframework.social.linkedin.api.LinkedInProfile;
import org.springframework.social.linkedin.api.Post;
import org.springframework.social.linkedin.api.impl.LinkedInTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests of the IndexingService class.
 * User: Jan Koren
 * Date: 13.3.13
 * Time: 19:05
 */
//@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
public class IndexingServiceTest {

    @Autowired
    private IndexingService indexingService;

    @Autowired
    private LinkedInTemplate linkedInTemplate;

    @Value("${linkedin.groupId}")
    private int groupId;

    private Log log = LogFactory.getLog("cz.zcu.kiv.eegdatabase.Tests");

    /**
     * Tests database indexing.
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws SolrServerException
     */
    @Ignore
    @Test
    public void testDatabaseIndexing() throws ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException, IOException, SolrServerException {
        indexingService.indexDatabase();

    }

    /**
     * Tests LinkedIn indexing.
     * @throws IllegalAccessException
     * @throws SolrServerException
     * @throws IOException
     */
    //@Ignore
    @Test
    public void testLinkedInIndexing() throws IllegalAccessException, SolrServerException, IOException {

        log.info("indexing all posts...");
        int count = 50;
        int start = 0;

        List<Post> posts = new ArrayList<Post>();

        List<Post> receivedPosts = getGroupPostsWithMoreInfo(count, start);
        while (receivedPosts != null) {
            posts.addAll(receivedPosts);
            start += 50;
            log.info("got next " + receivedPosts.size() + " posts...");
            receivedPosts = getGroupPostsWithMoreInfo(count, start);
        }

        log.info("indexing all " + posts.size() + " posts...");
        for(Post post : posts) {
            LinkedInProfile profile = post.getCreator();
            log.info("post id: " + post.getId());
            log.info("post created: " + post.getCreationTimestamp());
            log.info("post title: " + post.getTitle());
            log.info("post summary: " + post.getSummary());
            log.info("post creator: " + profile.getFirstName() + " " + profile.getLastName());
        }

        indexingService.indexLinkedIn(posts);
    }

    // = LinkedInManager.getGroupPostsWithMoreInfo
    private synchronized List<Post> getGroupPostsWithMoreInfo(int count, int start) {
        Group.GroupPosts groupPosts = linkedInTemplate.restOperations().getForObject(
                "http://api.linkedin.com/v1/groups/{group-id}/posts" +
                        ":(creation-timestamp,title,summary,id," +
                        "creator:(first-name,last-name))?" +
                        "count=" + count +
                        "&start=" + start +
                        "&order=recency",
                Group.GroupPosts.class, groupId);
        return groupPosts.getPosts();
    }
}
