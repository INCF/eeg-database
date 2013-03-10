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
