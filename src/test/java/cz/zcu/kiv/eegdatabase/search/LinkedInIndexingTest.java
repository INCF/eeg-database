package cz.zcu.kiv.eegdatabase.search;

import cz.zcu.kiv.eegdatabase.logic.indexing.Indexer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
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

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 8.3.13
 * Time: 19:50
 * To change this template use File | Settings | File Templates.
 */
//@Ignore
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

    /**
     *
     * @throws IllegalAccessException
     * @throws SolrServerException
     * @throws IOException
     */
    //@Ignore
    @Test
    public void indexLastSavedLinkedInPost() throws IllegalAccessException, SolrServerException, IOException {

        Post lastPost = getLastPost();
        LinkedInProfile profile = lastPost.getCreator();
        log.info("post id: " + lastPost.getId());
        log.info("post title: " + lastPost.getTitle());
        log.info("post summary: " + lastPost.getSummary());
        log.info("post creator: " + profile.getFirstName() + " " + profile.getLastName());

        String groupDescription = linkedInTemplate.groupOperations().getGroupDetails(groupId).getDescription();
        log.info("group description: " + groupDescription);

        log.info("post id: " + lastPost.getId());
        indexer.index(lastPost);
    }

    @Ignore
    @Test
    public void ttt() throws IllegalAccessException, SolrServerException, IOException {
        List<Post> posts = new ArrayList<Post>();
        createPost("LinkedIn pokusny prispevek", "stul plakal sluchatka propiska kanoj papir tvaroh");
        Post firstPost = getLastPost();
        log.info(firstPost.getTitle() + " - " + firstPost.getSummary());
        posts.add(firstPost);
        indexer.indexAll(posts);
    }

    /**
     *
     * @throws IllegalAccessException
     * @throws SolrServerException
     * @throws IOException
     */
    //@Ignore
    @Test
    public void indexSampleLinkedInPosts() throws IllegalAccessException, SolrServerException, IOException {
        log.info("indexing sample posts...");
        List<Post> posts = new ArrayList<Post>();
        createPost("prvni post", "de kad peofd alsd lala mads");
        Post firstPost = getLastPost();
        createPost("druhy post", "popo laodl oedps ldaspof novy");
        Post secondPost = getLastPost();
        createPost("treti post", "juchacha juchuchu");
        Post thirdPost = getLastPost();
        posts.add(firstPost);
        posts.add(secondPost);
        posts.add(thirdPost);
        indexer.indexAll(posts);

        deletePost(firstPost.getId());
        deletePost(secondPost.getId());
        deletePost(thirdPost.getId());
    }

    /**
     *
     * @throws IllegalAccessException
     * @throws SolrServerException
     * @throws IOException
     */
    //@Ignore
    @Test
    public void indexAllLinkedInPosts() throws IllegalAccessException, SolrServerException, IOException {
        log.info("indexing all posts...");
        List<Post> posts = getGroupPostsWithMoreInfo();
        log.info("indexing all " + posts.size() + " posts...");
        for(Post post : posts) {
            LinkedInProfile profile = post.getCreator();
            log.info("post id: " + post.getId());
            log.info("post created: " + post.getCreationTimestamp());
            log.info("post title: " + post.getTitle());
            log.info("post summary: " + post.getSummary());
            log.info("post creator: " + profile.getFirstName() + " " + profile.getLastName());
        }

        indexer.indexAll(posts);

        log.info("unindexing all " + posts.size() + " posts...");

        indexer.unindexAll();

        SolrQuery query = new SolrQuery();
        query.set("df", "source");
        query.setRows(100);
        query.setQuery("linkedin");
        QueryResponse response = solrServer.query(query);
        int foundPosts = response.getResults().size();
        log.info("found posts after unindexing: " + foundPosts);

        assertTrue(foundPosts == 0);
    }


    @Ignore
    @Test
    public void editTestPost() {
        //linkedInTemplate.restOperations().put();
    }

    /**
     *
     */
    //@Ignore
    @Test
    public void deletePostsTest() {

        int postsBefore = getGroupPostsWithMoreInfo().size();
        log.info("Posts before deletion: " + postsBefore);

        List<Post> posts = new ArrayList<Post>();
        createPost("first ", "ffffffff iiiiiiiiii rrrrrrrrrr ssssssssss tttttt");
        posts.add(getLastPost());
        createPost("second", "ssssssss eeeeeeeee cccccccc oooooooo nnnnnnn ddddd");
        posts.add(getLastPost());
        createPost("third", "tttttt hhhhhhh iiiiiii rrrrrrrr dddddddd");
        posts.add(getLastPost());

        for(Post post : posts) {
            log.info("Deleting post " + post.getId() + "...");
            deletePost(post.getId());
        }

        int postsAfter = getGroupPostsWithMoreInfo().size();
        log.info("Posts after deletion: " + postsAfter);

        assertTrue(postsBefore == postsAfter);
    }

    /**
     * Returns all posts from the EEG database group.
     * Posts contain all required information.
     * @return
     */
    private synchronized List<Post> getGroupPostsWithMoreInfo() {
        Group.GroupPosts groupPosts = linkedInTemplate.restOperations().getForObject(
                "http://api.linkedin.com/v1/groups/{group-id}/posts" +
                ":(creation-timestamp,title,summary,id," +
                "creator:(first-name,last-name))?count=50&order=recency", Group.GroupPosts.class, groupId);
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

    private synchronized void deletePost(String id) {
        linkedInTemplate.restOperations().delete(
                "http://api.linkedin.com/v1/posts/{post-id}", id);
    }
}
