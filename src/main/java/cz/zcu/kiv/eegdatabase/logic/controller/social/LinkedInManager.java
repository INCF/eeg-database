package cz.zcu.kiv.eegdatabase.logic.controller.social;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.social.linkedin.api.Group;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.Post;
import org.springframework.social.linkedin.api.impl.LinkedInTemplate;

/**
 * Bean for accessing a spring-social-linkedin API
 * Publishing articles
 * Downloading other groups posts
 * @author Jan Froněk
 */
public class LinkedInManager {
 /** Applications LinkedIn Consumer Key. */
 private String consumerKey;
 /** Applications LinkedIn Consumer Secret. */
 private String consumerSercet;
 /** Acces token code for EEG/ERP portals profile on LinkedIn. */
 private String accessToken;
 /** Acces token secret code for EEG/ERP portals profile on LinkedIn. */
 private String accessTokenSecret;
 /** ID of EEG/ERP portal group on LinkedIn. */
 public int groupId;
 
 private Log log = LogFactory.getLog(getClass());
 
 public LinkedInManager(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, int groupId){
     this.consumerKey = consumerKey;
     this.consumerSercet = consumerSecret;
     this.accessToken = accessToken;
     this.accessTokenSecret = accessTokenSecret;
     this.groupId = groupId;
 }
 
 /** Keeps connection to LinkedIn API. */
 private LinkedIn linkedin;
 
 /**
  * Method for connection to LinkedIn API.
  * Acces Token values are generated beforehand to avoid repeated login or confirmations.
  */
 private void connect(){
     if(linkedin == null){
         try {
         linkedin = new LinkedInTemplate(consumerKey, consumerSercet, accessToken, accessTokenSecret); 
         } catch (Exception e) {
         //In case of Exception, no action will be taken.
         }
     }    
 }  
 
 /** 
  * Publishing article on Linkedin using connection to its API
  * @param title title of the article
  * @param text text of the article
  */
 public synchronized void publish(String title, String text){
     try {
         this.connect();
         linkedin.groupOperations().createPost(groupId, title, text);
     } catch (Exception e) {
         log.debug("Exception occured when publishing article: " + title + " on LinkedIn.");
     }     
     
 }
 
 //place for read methods
 
 /**
  * Downloading posts from Linkedin
  * @param groupId ID of EEG/ERP portal group on LinkedIn
  * @return list of post from EEG/ERP portal group on LinkedIn
  */
 public synchronized List<Post> getPosts(int groupId){
     try {
         this.connect();
         return linkedin.groupOperations().getPosts(groupId).getPosts();
     } catch (Exception e) {
         log.debug("Exception occured when reading posts from group: " + groupId);
     }
     return null;
 }
 
 /**
  * Downloading group details from Linkedin
  * @return group details from EEG/ERP portal group on LinkedIn
  */
 public synchronized Group getGroupDetails(){
      try {
         this.connect();
         Group group = linkedin.groupOperations().getGroupDetails(groupId);   
         return group;
      } catch (Exception e) {
         log.debug("Exception occured when reading group details from group: " + groupId);
     }
     return null;
 }
    
}
