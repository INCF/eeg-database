package cz.zcu.kiv.eegdatabase.logic.controller.article;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;


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
    private ResearchGroupDao researchGroupDao;
    private String domain;

    public ArticleMultiController() {

    }

    public ModelAndView rss(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("articles/rss");
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z");
        String today = formatter.format(new Date());
        List<Article> articleList = articleDao.getAllArticles();
        int groupId;

        String copyright = "Copyright © The University of West Bohemia";
        String title = "EEGbase articles RSS feed";
        String description = "Articles from EEGbase content management system";
        String language = "en";
        String link = domain + "/files/articles.rss";

        Feed rssFeeder = new Feed(title, link, description, language, copyright, today);


        // Now add one example entry

        for (Article article : articleList) {
            FeedMessage feed = new FeedMessage();
            feed.setTitle(article.getTitle());
            feed.setDescription(article.getText().substring(0,(article.getText().length() > 200) ? 200 : article.getText().length()-1));
            feed.setAuthor(article.getPerson().getUsername());
            feed.setGuid("");
            feed.setLink(domain + "/articles/detail.html?articleId=" + article.getArticleId());
            rssFeeder.getMessages().add(feed);
        }


        // Now write the file
        RSSFeedWriter writer = new RSSFeedWriter(rssFeeder, request.getRealPath("/") + "/files/articles.rss");
        String rssFeedBuffer = "";
        try {
            rssFeedBuffer = writer.write();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mav.addObject("rssFeedBuffer", rssFeedBuffer);
        mav.addObject("articleList", articleList);
        mav.addObject("articleListTitle", "pageTitle.allArticles");
        mav = new ModelAndView("redirect:/files/articles.rss");
        return mav;
    }

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("articles/list");
        setPermissionsToView(mav);
        Person loggedUser = personDao.getLoggedPerson();
        log.debug("Logged user from database is: " + loggedUser.getPersonId());
        List<Article> articleList = articleDao.getAllArticles();
        int groupId;
        for (Article item : articleList) {
            item.setUserMemberOfGroup(canView(loggedUser, item));
            item.setUserIsOwnerOrAdmin(canEdit(loggedUser, item));
        }
        mav.addObject("articleList", articleList);
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

        mav.addObject("userCanView", canView(loggedUser, article));
        command.setArticleId(id);
        mav.addObject("command", command);
        mav.addObject("userCanEdit", canEdit(loggedUser, article));
        mav.addObject("article", article);

        List<ArticleComment> articleComments = articleCommentDao.getAllWithNoParent(article);
        mav.addObject("commentsList", articleComments);

        Set<Person> subscribers = article.getSubscribers();
        mav.addObject("subscribed", subscribers.contains(loggedUser));
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

    public ModelAndView settings(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("articles/articleSettings");
        setPermissionsToView(mav);
        Person loggedUser = personDao.getLoggedPerson();
        Set<ResearchGroup> articlesGroupSubscriptions = loggedUser.getArticlesGroupSubscribtions();
        List<ResearchGroup> list = researchGroupDao.getResearchGroupsWhereMember(loggedUser);
        mav.addObject("researchGroupList", list);
        mav.addObject("articlesGroupSubscribtions", articlesGroupSubscriptions);

        return mav;
    }

    public ModelAndView subscribe(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("articles/subscribe");
        setPermissionsToView(mav);
        Person loggedUser = personDao.getLoggedPerson();
        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("articleId"));
        } catch (NumberFormatException e) {
            log.debug("Unable to determine article id");
        }
        Article article = (Article) articleDao.read(id);
        Boolean subscribe = Boolean.parseBoolean(request.getParameter("subscribe"));
        Set<Person> subscribers = article.getSubscribers();
        if (subscribe) {
            subscribers.add(loggedUser);
        } else {
            subscribers.remove(loggedUser);
        }
        article.setSubscribers(subscribers);
        articleDao.update(article);

        mav = new ModelAndView("redirect:/articles/detail.html?articleId=" + id);
        return mav;
    }

    public ModelAndView subscribeGroupArticles(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("articles/subscribeGroupArticles");
        setPermissionsToView(mav);
        Person loggedUser = personDao.getLoggedPerson();
        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("groupId"));
        } catch (NumberFormatException e) {
            log.debug("Unable to determine research group id");
        }
        ResearchGroup group = (ResearchGroup) researchGroupDao.read(id);

        Boolean subscribe = Boolean.parseBoolean(request.getParameter("subscribe"));

        Set<Person> subscribers = group.getArticlesSubscribers();
        if (subscribe) {
            subscribers.add(loggedUser);
        } else {
            subscribers.remove(loggedUser);
        }
        group.setArticlesSubscribers(subscribers);
        researchGroupDao.update(group);

        mav = new ModelAndView("redirect:/articles/settings.html");
        return mav;
    }

    /**
     * Determines if the logged user can view the supposed article
     *
     * @param loggedUser Person object (Usually logged user)
     * @param article    Article object
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
     *
     * @param loggedUser Person object (Usually logged user)
     * @param article    Article object
     * @return true if admin or member of group
     */
    public boolean canEdit(Person loggedUser, Article article) {
        boolean isAdmin = loggedUser.getAuthority().equals("ROLE_ADMIN");
        boolean isOwner = article.getPerson().getPersonId() == loggedUser.getPersonId();
        return (isOwner || isAdmin);
    }

    /**
     * Checks if user is admin in any group
     *
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

    private void createNode(XMLEventWriter eventWriter, String name, String value) {
        try {
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n");
            XMLEvent tab = eventFactory.createDTD("\t");
            // Create Start node
            StartElement sElement = eventFactory.createStartElement("", "", name);
            eventWriter.add(tab);
            eventWriter.add(sElement);
            // Create Content
            Characters characters = eventFactory.createCharacters(value);
            eventWriter.add(characters);
            // Create End node
            EndElement eElement = eventFactory.createEndElement("", "", name);
            eventWriter.add(eElement);
            eventWriter.add(end);
        } catch (Exception e) {

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

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
