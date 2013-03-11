package cz.zcu.kiv.eegdatabase.logic.controller.social;

import cz.zcu.kiv.eegdatabase.data.indexing.LinkedInIndexer;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.linkedin.api.Group;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.Post;
import org.springframework.social.linkedin.api.impl.LinkedInTemplate;

import java.util.List;

/**
 * Bean for accessing a spring-social-linkedin API
 * Publishing articles
 * Downloading other groups posts
 *
 * @author Jan Froněk
 */
public class LinkedInManager {
    /**
     * Applications LinkedIn Consumer Key.
     */
    private String consumerKey;
    /**
     * Applications LinkedIn Consumer Secret.
     */
    private String consumerSercet;
    /**
     * Acces token code for EEG/ERP portals profile on LinkedIn.
     */
    private String accessToken;
    /**
     * Acces token secret code for EEG/ERP portals profile on LinkedIn.
     */
    private String accessTokenSecret;
    /**
     * ID of EEG/ERP portal group on LinkedIn.
     */
    public int groupId;

    @Autowired
    private LinkedInIndexer indexer;

    private Log log = LogFactory.getLog(getClass());

    public LinkedInManager(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, int groupId) {
        this.consumerKey = consumerKey;
        this.consumerSercet = consumerSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
        this.groupId = groupId;
    }

    /**
     * Keeps connection to LinkedIn API.
     */
    private LinkedIn linkedin;

    /**
     * Method for connection to LinkedIn API.
     * Acces Token values are generated beforehand to avoid repeated login or confirmations.
     */
    private void connect() {
        if (linkedin == null) {
            try {
                linkedin = new LinkedInTemplate(consumerKey, consumerSercet, accessToken, accessTokenSecret);
            } catch (Exception e) {
                //In case of Exception, no action will be taken.
            }
        }
    }

    /**
     * Publishing article on Linkedin using connection to its API
     *
     * @param title title of the article
     * @param text  text of the article
     */
    public synchronized void publish(String title, String text) {
        try {
            this.connect();
            linkedin.groupOperations().createPost(groupId, title, text);
            Post post = getLastPost();

            indexer.index(post);
        } catch (Exception e) {
            log.debug("Exception occured when publishing article: " + title + " on LinkedIn.");
        }

    }

    //place for read methods

    /**
     * Downloading posts from Linkedin
     *
     * @param groupId ID of EEG/ERP portal group on LinkedIn
     * @return list of post from EEG/ERP portal group on LinkedIn
     */
    public synchronized List<Post> getPosts(int groupId) {
        try {
            this.connect();
            return linkedin.groupOperations().getPosts(groupId).getPosts();
        } catch (Exception e) {
            log.debug("Exception occured when reading posts from group: " + groupId);
        }
        return null;
    }

    /**
     * Returns all posts from the EEG database group.
     * Posts contain all required information.
     * @return list of posts with more detailed information from EEG/ERP portal group on LinkedIn.
     */
    public synchronized List<Post> getGroupPostsWithMoreInfo() {
        Group.GroupPosts groupPosts = linkedin.restOperations().getForObject(
                "http://api.linkedin.com/v1/groups/{group-id}/posts" +
                        ":(creation-timestamp,title,summary,id," +
                        "creator:(first-name,last-name))?order=recency", Group.GroupPosts.class, groupId);
        return groupPosts.getPosts();
    }

    /**
     * Downloading group details from Linkedin
     *
     * @return group details from EEG/ERP portal group on LinkedIn
     */
    public synchronized Group getGroupDetails() {
        try {
            this.connect();
            Group group = linkedin.groupOperations().getGroupDetails(groupId);
            return group;
        } catch (Exception e) {
            log.debug("Exception occured when reading group details from group: " + groupId);
        }
        return null;
    }

    /**
     * Returns the last post added to the EEG Portal LinkedIn group.
     * @return The last post added to the EEG Portal LinkedIn group.
     */
    public synchronized Post getLastPost() {
        // the line below is commented because it returns the null value for summary
        // return linkedInTemplate.groupOperations().getGroupDetails(groupId).getPosts().getPosts().get(0);
        return getGroupPostsWithMoreInfo().get(0);
    }

    /**
     * Return the id value of the last post added to the EEG Portal LinkedIn group.
     * @return the id value of the last post added to the EEG Portal LinkedIn group.
     */
    public synchronized String getLastPostId() {
        Post lastPost = linkedin.groupOperations().getGroupDetails(groupId).getPosts().getPosts().get(0);
        return lastPost.getId();
    }

    /**
     * Gets the LinkedIn post by its unique id.
     * @param id The LinkedIn post id.
     * @return The found LinkedIn post.
     */
    public synchronized Post getPostById(String id) {
        Post myPost = linkedin.restOperations().getForObject(
                "http://api.linkedin.com/v1/posts/{post-id}" +
                        ":(creation-timestamp,title,summary,id," +
                        "creator:(first-name,last-name))", Post.class, id);
        log.info(myPost.getCreationTimestamp());
        log.info(myPost.getId());
        log.info(myPost.getTitle());
        return myPost;
    }

}
