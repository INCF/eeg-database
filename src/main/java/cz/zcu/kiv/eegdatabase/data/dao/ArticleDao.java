/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;

import java.io.Serializable;
import java.util.List;

/**
 * @author Jiri Vlasimsky
 */
public interface ArticleDao<T, PK extends Serializable> extends GenericDao<T, PK> {
    public List<Article> getAllArticles();

    List getArticlesForHomepage(Person person, int limit);
}
