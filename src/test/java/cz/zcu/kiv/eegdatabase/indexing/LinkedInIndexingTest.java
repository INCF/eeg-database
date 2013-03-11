package cz.zcu.kiv.eegdatabase.indexing;

import cz.zcu.kiv.eegdatabase.data.indexing.Indexer;
import cz.zcu.kiv.eegdatabase.data.indexing.LinkedInIndexer;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.linkedin.api.Group;
import org.springframework.social.linkedin.api.LinkedInProfile;
import org.springframework.social.linkedin.api.Post;
import org.springframework.social.linkedin.api.impl.LinkedInTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 8.3.13
 * Time: 19:50
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
public class LinkedInIndexingTest {

    @Autowired
    @Qualifier("solrServer")
    private SolrServer solrServer;

    @Autowired
    @Qualifier(value = "linkedInIndexer")
    private Indexer indexer;

    @Autowired
    private LinkedInTemplate linkedInTemplate;

    @Value("${linkedin.groupId}")
    private int groupId;

    private Log log = LogFactory.getLog("cz.zcu.kiv.eegdatabase.Tests");

    //@Ignore
    @Test
    public void addNewArticle() throws IllegalAccessException, SolrServerException, IOException {

        List<Post> posts = getGroupPostsWithMoreInfo();
        //List<Post> posts = linkedInTemplate.groupOperations().getGroupDetails(groupId).getPosts().getPosts();
        for(Post post : posts) {
            LinkedInProfile profile = post.getCreator();
            log.info("post id: " + post.getId());
            log.info("post created: " + post.getCreationTimestamp());
            log.info("post title: " + post.getTitle());
            log.info("post summary: " + post.getSummary());
            log.info("post creator: " + profile.getFirstName() + " " + profile.getLastName());
        }

        Post lastPost = getPostById("g-4394884-S-221382883");
        LinkedInProfile profile = lastPost.getCreator();
        log.info("post id: " + lastPost.getId());
        log.info("post title: " + lastPost.getTitle());
        log.info("post summary: " + lastPost.getSummary());
        log.info("post creator: " + profile.getFirstName() + " " + profile.getLastName());

        String groupDescription = linkedInTemplate.groupOperations().getGroupDetails(groupId).getDescription();

        log.info("group description: " + groupDescription);

        Post lastOne = getLastPost();
        log.info("post id: " + lastOne.getId());

        indexer.index(lastOne);


    }

    @Test
    public void indexAllLinkedInPosts() throws IllegalAccessException, SolrServerException, IOException {
        List<Post> posts = new ArrayList<Post>();
        String postId = createPost("prvni post", "de kad peofd alsd lala mads");
        //Post firstPost = getPostById(postId);
        Post firstPost = getLastPost();
        postId = createPost("druhy post", "popo laodl oedps ldaspof novy");
        //Post secondPost = getPostById(postId);
        Post secondPost = getLastPost();
        postId = createPost("treti post", "juchacha juchuchu");
        //Post thirdPost = getPostById(postId);
        Post thirdPost = getLastPost();
        posts.add(firstPost);
        posts.add(secondPost);
        posts.add(thirdPost);
        indexer.indexAll(posts);
    }

    /**
     * Returns all posts from the EEG database group.
     * Posts contain all required information.
     * @return
     */
    private List<Post> getGroupPostsWithMoreInfo() {
        Group.GroupPosts groupPosts = linkedInTemplate.restOperations().getForObject(
                "http://api.linkedin.com/v1/groups/{group-id}/posts" +
                ":(creation-timestamp,title,summary,id," +
                "creator:(first-name,last-name))?order=recency", Group.GroupPosts.class, groupId);
        return groupPosts.getPosts();
    }

    private synchronized String createPost(String title, String text) {
        log.info("Creating a new article...");

        linkedInTemplate.groupOperations().createPost(groupId, title, text);

        log.info("article created...");

        return getLastPostId();
    }

    private synchronized Post getLastPost() {
        // the line below is commented because it returns the null value for summary
        // return linkedInTemplate.groupOperations().getGroupDetails(groupId).getPosts().getPosts().get(0);
        return getGroupPostsWithMoreInfo().get(0);
    }

    private synchronized String getLastPostId() {
        Post lastPost = linkedInTemplate.groupOperations().getGroupDetails(groupId).getPosts().getPosts().get(0);
        return lastPost.getId();
    }

    private synchronized Post getPostById(String id) {
        // gets a post via REST by the post id
        Post myPost = linkedInTemplate.restOperations().getForObject(
                "http://api.linkedin.com/v1/posts/{post-id}" +
                        ":(creation-timestamp,title,summary,id," +
                        "creator:(first-name,last-name))", Post.class, id);
        log.info(myPost.getCreationTimestamp());
        log.info(myPost.getId());
        log.info(myPost.getTitle());
        return myPost;
    }

    @Ignore
    @Test
    public void editTestPost() {

    }
}
