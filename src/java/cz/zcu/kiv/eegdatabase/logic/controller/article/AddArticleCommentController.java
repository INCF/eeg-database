/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.article;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.validation.BindException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jiri Vlasimsky
 */
public class AddArticleCommentController extends SimpleFormController {

  private AuthorizationManager auth;
  private GenericDao<Article, Integer> articleDao;
  private GenericDao<ArticleComment, Integer> articleCommentDao;
  private PersonDao personDao;
  private ResearchGroupDao researchGroupDao;
  private Log log = LogFactory.getLog(getClass());

  public AddArticleCommentController() {
    setCommandClass(ArticleCommentCommand.class);
    setCommandName("addArticleComment");
  }

  @Override
  protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
    ModelAndView mav = super.showForm(request, response, errors);
    setPermissionsToView(mav);
    return mav;
  }

  @Override
  protected Object formBackingObject(HttpServletRequest request) throws Exception {
    ArticleCommentCommand data = (ArticleCommentCommand) super.formBackingObject(request);

    String articleIdCommentString = request.getParameter("articleCommentId");
    int articleId = 0;
    int parentId = 0;

    try {
      articleId = Integer.parseInt(request.getParameter("articleId"));
      parentId = Integer.parseInt(request.getParameter("parentId"));
    } catch (Exception e) {
    }

    if (articleIdCommentString != null) {
      int articleCommentId = Integer.parseInt(articleIdCommentString);
      if (articleCommentId > 0) {  // it is a form for editing

        log.debug("Filling backing object with data from articleComment object #" + articleCommentId);
        data.setCommentId(articleCommentId);


        ArticleComment articleComment = articleCommentDao.read(articleCommentId);


        log.debug("Setting article comment´s parent comment");
        data.setParentId(articleComment.getParent().getCommentId());

        log.debug("Setting article comment´s text");
        data.setText(articleComment.getText());

        log.debug("Setting article comment´s parent article");
        data.setArticleId(articleComment.getArticle().getArticleId());
      }


    }
    log.debug("Setting comment´s parent article");
    data.setArticleId(articleId);

    log.debug("Setting comment´s parent comment");
    data.setParentId(parentId);
    return data;
  }

  @Override
  protected Map referenceData(HttpServletRequest request) throws Exception {
    Map map = new HashMap<String, Object>();
    return map;
  }

  @Override
  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
    ModelAndView mav = new ModelAndView(getSuccessView());

    ArticleCommentCommand data = (ArticleCommentCommand) command;
    ArticleComment comment;

    if (data.getCommentId() > 0) {
      log.debug("Processing article comment form - editing existing comment");
      log.debug("Checking the permission level");
    } else {
      comment = new ArticleComment();
//      String articleIdCommentString = request.getParameter("articleCommentId");

      log.debug("Setting comment owner to logged user");
      comment.setPerson(personDao.getLoggedPerson());

      log.debug("Setting comment´s parent comment");
      comment.setParent(articleCommentDao.read(data.getParentId()));

      log.debug("Setting comment parent article");
      comment.setArticle(articleDao.read(data.getArticleId()));

      log.debug("Setting comment text");
      comment.setText(data.getText());

      log.debug("Setting comment time");
      Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
      comment.setTime(currentTimestamp);

      if (data.getCommentId() > 0) {
        log.debug("Processing an article comment form - editing an existing comment");
        articleCommentDao.create(comment);
      } else {
        log.debug("Processing an article comment form - adding a new article");
        articleCommentDao.create(comment);
      }
    }

    mav = new ModelAndView("redirect:/articles/detail.html?articleId=" + data.getArticleId());

    return mav;
  }

  /**
   * Determines if the logged user can view the supposed article
   * @param loggedUser Person object (Usually logged user)
   * @param article Article object
   * @return true if admin or member of group
   */
  public boolean canView(Person loggedUser, Article article) {
    if (loggedUser.getAuthority().equals("ROLE_ADMIN") || article.getResearchGroup() == null) {
      return true;
    }
    Set<ResearchGroupMembership> researchGroupMemberships = article.getResearchGroup().getResearchGroupMemberships();
    for (ResearchGroupMembership member : researchGroupMemberships) {
      if (member.getPerson().getPersonId() == loggedUser.getPersonId()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Determines if the logged user can edit the supposed article
   * @param loggedUser Person object (Usually logged user)
   * @param article Article object
   * @return true if admin or member of group
   */
  public boolean canEdit(Person loggedUser, Article article) {
    boolean isAdmin = loggedUser.getAuthority().equals("ROLE_ADMIN");
    boolean isOwner = article.getPerson().getPersonId() == loggedUser.getPersonId();
    return (isOwner || isAdmin);
  }

  /**
   * Checks if user is admin in any group
   * @param mav ModelAndView for display
   */
  public void setPermissionsToView(ModelAndView mav) {
    // isAdmin
    Person loggedUser = personDao.getLoggedPerson();
    if (loggedUser.getAuthority().equals("ROLE_ADMIN")) {
      mav.addObject("userIsAdminInAnyGroup", true);
      return;
    }
    // check all groups for admin role
    Set<ResearchGroupMembership> researchGroupMemberShips = loggedUser.getResearchGroupMemberships();
    for (ResearchGroupMembership member : researchGroupMemberShips) {
      if (auth.userIsAdminInGroup(member.getResearchGroup().getResearchGroupId())) {
        mav.addObject("userIsAdminInAnyGroup", true);
        return;
      }
    }
  }

  public boolean supports(Class type) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public GenericDao<Article, Integer> getArticleDao() {
    return articleDao;
  }

  public void setArticleDao(GenericDao<Article, Integer> articleDao) {
    this.articleDao = articleDao;
  }

  public void validate(Object command, Errors errors) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public AuthorizationManager getAuth() {
    return auth;
  }

  public void setAuth(AuthorizationManager auth) {
    this.auth = auth;
  }

  public PersonDao getPersonDao() {
    return personDao;
  }

  public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
  }

  public ResearchGroupDao getResearchGroupDao() {
    return researchGroupDao;
  }

  public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
    this.researchGroupDao = researchGroupDao;
  }

  public GenericDao<ArticleComment, Integer> getArticleCommentDao() {
    return articleCommentDao;
  }

  public void setArticleCommentDao(GenericDao<ArticleComment, Integer> articleCommentDao) {
    this.articleCommentDao = articleCommentDao;
  }
}
