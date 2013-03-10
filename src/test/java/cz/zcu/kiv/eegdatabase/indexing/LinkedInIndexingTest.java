package cz.zcu.kiv.eegdatabase.indexing;

import cz.zcu.kiv.eegdatabase.data.indexing.Indexer;
import cz.zcu.kiv.eegdatabase.data.indexing.SocialIndexer;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.logic.controller.social.LinkedInManager;
import org.apache.solr.client.solrj.SolrServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.linkedin.api.Post;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 8.3.13
 * Time: 19:50
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml", "classpath:/socialContext.xml"})
public class LinkedInIndexingTest {

    @Autowired
    @Qualifier("solrServer")
    private SolrServer solrServer;

    private SocialIndexer<Article> indexer = new SocialIndexer<Article>();

    @Autowired
    private LinkedInManager linkedin;

    @Test
    public void addNewArticle() throws Exception {
        String title = "Test title";
        String text = "Sample text";

        Article article = new Article();
        article.setTitle(title);
        article.setText(text);

        //linkedin.publish(article);
        List<Post> posts = linkedin.getPosts(linkedin.groupId);
        for(Post post : posts) {
            System.out.println(post.getId());
        }
        //indexer.index(article);
    }



}
