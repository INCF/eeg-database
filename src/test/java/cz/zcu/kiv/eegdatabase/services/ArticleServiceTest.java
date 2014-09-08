/*******************************************************************************
 * This file is part of the EEG-database project
 *
 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 * ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * ***********************************************************************************************************************
 *
 * ArticleServiceTest.java, 2014/07/18 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.services;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.article.ArticleService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Honza on 18.7.14.
 */
@Transactional
public class ArticleServiceTest extends AbstractServicesTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ResearchGroupDao researchGroupDao;

    private Article article;
    private Person person;
    private ArticleComment articleComment;


    @Before

    public void setUp() {
        person = TestUtils.createPersonForTesting("test-article@test.com", Util.ROLE_READER);
        personDao.create(person);


        article = new Article();

        article.setText("test-text");
        article.setTitle("test-title");
        article.setTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
        article.setPerson(person);
    }

    @Test
    public void testCreateArticle() {
        int count = articleService.getCountRecords();
        articleService.create(article);
        assertEquals(count + 1, articleService.getCountRecords());
    }

    @Test
    public void testGetArticlesForUser() {
        int countAll = articleService.getCountRecords();
        int count = articleService.getArticlesForUser(person).size();
        articleService.create(article);
        Person tmp = TestUtils.createPersonForTesting("test2@test.com", Util.ROLE_READER);
        personDao.create(tmp);
        //Creating research group and setting it to a new article
        ResearchGroup researchGroup = new ResearchGroup();
        researchGroup.setDescription("test-description");
        researchGroup.setTitle("test-title");
        researchGroup.setPerson(tmp);
        researchGroupDao.create(researchGroup);

        Article newArticle = new Article();

        newArticle.setText("test-text2");
        newArticle.setTitle("test-title2");
        newArticle.setTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
        newArticle.setPerson(tmp);
        newArticle.setResearchGroup(researchGroup);
        articleService.create(newArticle);

        assertEquals(countAll + 2, articleService.getAllArticles().size());
        assertEquals(countAll + 2, articleService.getArticleCountForPerson(tmp));
        //the person "person" should see only one article (the second one is not public and belongs to the person "tmp").
        assertEquals(count + 1, articleService.getArticlesForUser(person).size());
        assertEquals(count + 2, articleService.getArticlesForUser(tmp).size());
    }

    @Test
    public void testGetArticleDetails() {

        int id = articleService.create(article);
        Article fromDB = articleService.read(id);
        assertNotNull(fromDB);
        assertEquals("test-text", fromDB.getText());
        assertEquals("test-title", fromDB.getTitle());
    }

    @Test
    public void testCreateComment() {
        int id = articleService.create(article);
        int count = articleService.read(id).getArticleComments().size();
        articleComment = createComment(article);
        int commentId = articleService.create(articleComment);
//        article.getArticleComments().add(articleComment);
//        articleService.update(article);

        assertEquals(count + 1, articleService.read(id).getArticleComments().size());
        assertEquals("text-comment", articleService.readComment(commentId).getText());
        assertEquals(count + 1, articleService.getCountCommentRecords());
    }

    @Test
    public void testGetCommentsForArticle() {

        articleService.create(article);
        Article newArticle = new Article();

        int countAll = articleService.getCountCommentRecords();
        int countCommentForArticle = articleService.getCommentsForArticle(article.getArticleId()).size();

        newArticle.setText("test-text2");
        newArticle.setTitle("test-title2");
        newArticle.setTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
        newArticle.setPerson(person);
        articleService.create(newArticle);
        articleComment = createComment(article);
        articleService.create(articleComment);

        ArticleComment newComment = createComment(newArticle);
        articleService.create(newComment);

        assertEquals(countAll + 2, articleService.getCountCommentRecords());
        assertEquals(countCommentForArticle + 1, articleService.getCommentsForArticle(article.getArticleId()).size());
    }

    private ArticleComment createComment(Article article) {
        ArticleComment newComment = new ArticleComment();
        newComment.setPerson(person);
        newComment.setArticle(article);
        article.getArticleComments().add(newComment);
        articleService.update(article);
        newComment.setTime(new Timestamp(System.currentTimeMillis()));
        newComment.setText("text-comment");
        return newComment;

    }

}
