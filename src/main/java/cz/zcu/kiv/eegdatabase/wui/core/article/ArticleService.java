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
 *   ArticleService.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.article;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

public interface ArticleService extends GenericService<Article, Integer> {

    List<Article> getAllArticles();

    List<Article> getArticlesForUser(Person person);

    List<Article> getArticlesForUser(Person person, int limit);

    List<Article> getArticlesForList(Person person, int min, int count);

    int getArticleCountForPerson(Person person);

    Article getArticleDetail(int id, Person loggedPerson);

    Integer create(ArticleComment newInstance);

    ArticleComment readComment(Integer id);

    List<ArticleComment> readCommentByParameter(String parameterName, int parameterValue);

    List<ArticleComment> readCommentByParameter(String parameterName, String parameterValue);

    void updateComment(ArticleComment transientObject);

    void deleteComment(ArticleComment persistentObject);

    List<ArticleComment> getAllCommentRecords();

    List<ArticleComment> getCommentRecordsAtSides(int first, int max);

    int getCountCommentRecords();

    List<ArticleComment> getCommentsForArticle(int articleId);
}
