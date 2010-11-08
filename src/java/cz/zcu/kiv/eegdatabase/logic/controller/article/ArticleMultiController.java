package cz.zcu.kiv.eegdatabase.logic.controller.article;

import cz.zcu.kiv.eegdatabase.data.dao.ArticleCommentDao;
import cz.zcu.kiv.eegdatabase.data.dao.ArticleDao;
import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import java.net.BindException;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * Controller for adding and editing an article
 *
 * @author Jiri Vlasimsky
 */
public class ArticleMultiController extends MultiActionController {

  private Log log = LogFactory.getLog(getClass());
  private AuthorizationManager auth;
  private PersonDao personDao;
  private ArticleDao articleDao;
  private ArticleCommentDao articleCommentDao;

  public ArticleMultiController() {
  }

  public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
    ModelAndView mav = new ModelAndView("articles/list");
    setPermissionsToView(mav);
    Person loggedUser = personDao.getLoggedPerson();
    log.debug("Logged user from database is: " + loggedUser.getPersonId());
    List<Article> list = articleDao.getAllRecords();

    int groupId;
    for (Article item : list) {
      item.setUserMemberOfGroup(canView(loggedUser, item));
      item.setUserIsOwnerOrAdmin(canEdit(loggedUser, item));
    }
    mav.addObject("articleList", list);
    mav.addObject("articleListTitle", "pageTitle.allArticles");
    return mav;
  }

  @Override
  protected Object newCommandObject(Class myClass)
          throws Exception {
    return new ArticleCommentCommand();
  }


  public ModelAndView detail(HttpServletRequest request, HttpServletResponse response) {
    ArticleCommentCommand command = new ArticleCommentCommand();
    
    ModelAndView mav = new ModelAndView("articles/detail");
    setPermissionsToView(mav);
    Person loggedUser = personDao.getLoggedPerson();
    int id = 0;
    try {
      id = Integer.parseInt(request.getParameter("articleId"));
    } catch (Exception e) {
      log.debug("Unable to determine article id");
    }
    Article article = (Article) articleDao.read(id);
    if (article.getResearchGroup() != null) {
      mav.addObject("userIsMemberOfGroup", canView(loggedUser, article));
    }
    command.setArticleId(id);
    mav.addObject("command", command);
    mav.addObject("userCanEdit", canEdit(loggedUser, article));
    mav.addObject("article", article);
    List<ArticleComment> articleComments = articleCommentDao.getAllWithNoParent(article);
    mav.addObject("commentsList", articleComments);
    return mav;
  }

  public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
    ModelAndView mav = new ModelAndView("articles/articleDeleted");
    setPermissionsToView(mav);
    Person loggedUser = personDao.getLoggedPerson();
    int id = 0;
    try {
      id = Integer.parseInt(request.getParameter("articleId"));
    } catch (Exception e) {
      log.debug("Unable to determine article id");
    }

    Article article = (Article) articleDao.read(id);
    if (canEdit(loggedUser, article)) {
      articleDao.delete(article);
    }
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

  public ArticleDao getArticleDao() {
    return articleDao;
  }

  public void setArticleDao(ArticleDao articleDao) {
    this.articleDao = articleDao;
  }

  public PersonDao getPersonDao() {
    return personDao;
  }

  public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
  }

  public AuthorizationManager getAuth() {
    return auth;
  }

  public void setAuth(AuthorizationManager auth) {
    this.auth = auth;
  }

  public ArticleCommentDao getArticleCommentDao() {
    return articleCommentDao;
  }

  public void setArticleCommentDao(ArticleCommentDao articleCommentDao) {
    this.articleCommentDao = articleCommentDao;
  }



}
