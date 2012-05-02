package cz.zcu.kiv.eegdatabase.logic.controller.social;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 private final String CONSUMER_KEY = "uolsasj3786o";
 private final String CONSUMER_SECRET = "QNUNAwWRwlw1FQ4j";
 /** Acces token code for EEG/ERP portals profile on LinkedIn. */
 private final String ACCESS_TOKEN = "6ef21209-86f3-4cfe-93f5-3674e532bda2";
 /** Acces token secret code for EEG/ERP portals profile on LinkedIn. */
 private final String ACCESS_TOKEN_SECRET = "9233947f-3216-4b0a-b382-8fd27ef64b86";
 /** ID of EEG/ERP portal group on LinkedIn. */
 private final int GROUP_ID = 4394884;
 
 private Log log = LogFactory.getLog(getClass());
 
 /** Keeps connection to LinkedIn API. */
 private LinkedIn linkedin;
 
 /**
  * Method for connection to LinkedIn API.
  * Acces Token values are generated beforehand to avoid repeated login or confirmations.
  */
 private void connect(){
     if(linkedin == null){
         try {
         linkedin = new LinkedInTemplate(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET); 
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
         linkedin.groupOperations().createPost(GROUP_ID, title, text);
     } catch (Exception e) {
         log.debug("Exception occured when publishing article: " + title + " on LinkedIn.");
     }     
     
 }
 
 //place for read methods
 public synchronized List<Post> getPosts(int groupId){
     try {
         this.connect();
         return linkedin.groupOperations().getPosts(groupId).getPosts();
     } catch (Exception e) {
         log.debug("Exception occured when reading posts from group: " + groupId);
     }
     return null;
 }
    
}
