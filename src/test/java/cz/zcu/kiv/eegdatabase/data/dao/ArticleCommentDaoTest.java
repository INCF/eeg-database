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
 *   ArticleCommentDaoTest.java, 2014/07/09 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;

/**
 * Created by stebjan on 9.7.14.
 */

public class ArticleCommentDaoTest extends AbstractDataAccessTest {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private ArticleCommentDao commentDao;


    private Article article;
    private Person personReader;
    private ArticleComment comment;

    @BeforeMethod(groups = "unit")
    public void setUp() {
        personReader = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_READER);
        personDao.create(personReader);


        article = new Article();

        article.setText("test-text");
        article.setTitle("test-title");
        article.setTime(new Timestamp(System.currentTimeMillis()));
        article.setPerson(personReader);
        articleDao.create(article);
    }

    @Test(groups = "unit")
    public void testCreateComment() {
        int commentNumber = articleDao.read(article.getArticleId()).getArticleComments().size();
        comment = createComment(article);

        commentDao.create(comment);
        assertEquals(commentNumber + 1, articleDao.read(article.getArticleId()).getArticleComments().size());
    }

    @Test(groups = "unit")
    public void testGetCommentsForArticle() {
        int commentNumber = articleDao.read(article.getArticleId()).getArticleComments().size();
        int allCommentNumber = commentDao.getCountRecords();
        comment = createComment(article);
        commentDao.create(comment);

        Article article2 = new Article();
        article2.setText("test-text");
        article2.setTitle("test-title2");
        article2.setTime(new Timestamp(System.currentTimeMillis()));
        article2.setPerson(personReader);
        articleDao.create(article2);

        comment = createComment(article2);
        commentDao.create(comment);
        assertEquals(commentNumber + 1, articleDao.read(article.getArticleId()).getArticleComments().size());
        assertEquals(allCommentNumber + 2, commentDao.getCountRecords());

    }

    private ArticleComment createComment(Article article) {
        ArticleComment newComment = new ArticleComment();
        newComment.setPerson(personReader);
        newComment.setArticle(article);
        article.getArticleComments().add(newComment);
        articleDao.update(article);
        newComment.setTime(new Timestamp(System.currentTimeMillis()));
        newComment.setText("test comment");
        return newComment;

    }
}
