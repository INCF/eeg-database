package cz.zcu.kiv.eegdatabase.indexing;

import cz.zcu.kiv.eegdatabase.data.indexing.IndexingService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
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
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 13.3.13
 * Time: 19:05
 * To change this template use File | Settings | File Templates.
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

    //@Ignore
    @Test
    public void testDatabaseIndexing() throws ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException, IOException, SolrServerException {
        indexingService.indexDatabase();
    }

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
