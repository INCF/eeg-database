package cz.zcu.kiv.eegdatabase.logic.controller.article;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.logic.controller.social.LinkedInManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import cz.zcu.kiv.eegdatabase.logic.util.Paginator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestParam;
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
import java.util.*;
import org.springframework.social.linkedin.api.Group;
import org.springframework.social.linkedin.api.Post;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for adding and editing an article
 *
 * @author Jiri Vlasimsky, Ladislav Janák
 */
public class ArticleMultiController extends MultiActionController {

    private Log log = LogFactory.getLog(getClass());
    private AuthorizationManager auth;
    private PersonDao personDao;
    private ArticleDao articleDao;
    private ArticleCommentDao articleCommentDao;
    private ResearchGroupDao researchGroupDao;
    private SimpleKeywordsDao simpleKeywordsDao;
    private String domain;

    private static final int ARTICLES_PER_PAGE = 10;

    /**
     * Posts from EEG/ERP portal LinkedIn group
     */
    List<Post> linkedInArticles = null;
    /**
     * Filtered posts from EEG/ERP portal LinkedIn group
     */
    List<Post> linkedInArticlesFiltered = null;
    /**
     * Text with keywords
     */
    List<String> keywordsSettings = new ArrayList<String>();
    /**
     * Groip details from EEG/ERP portal LinkedIn group
     */
    Group groupDetails = null;
    /**
     * Reference to LinkedInManager bean
     */
    private final LinkedInManager linkedInManager;
    /**
     * Whether has to be internal articles showed
     */
    boolean showInternal = true;
    /**
     * Whether has to be linkedIn articles showed
     */
    boolean showLinkedIn = false;


    /**
     * Constructor
     * @param linkedInManager Reference to linkedInManager bean
     */
    public ArticleMultiController(LinkedInManager linkedInManager) throws IOException {
        this.linkedInManager = linkedInManager;
    }

    /*
     * public void init() { keyWordsList = new ArrayList<String>(); for
     * (Enumeration en = this.propertiesHolder.keys(); en.hasMoreElements(); ) {
     * String key = (String) en.nextElement();
     * this.keyWordsList.add(this.propertiesHolder.getProperty(key)); } }
     */
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
            feed.setDescription(article.getText().substring(0, (article.getText().length() > 200) ? 200 : article.getText().length() - 1));
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
        mav.addObject("articleListTitle", "pageTitle.internalArticles");
        mav = new ModelAndView("redirect:/files/articles.rss");
        return mav;
    }

   
    /**
     * Get method for list.jsp form, it shows internal and linkedIn articles
     * @return model and view object which shows internal and linkedIn articles
     * @throws Exception 
     */
    @RequestMapping(value = "articles/list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "page", required = false) Integer page) throws Exception {


        ModelAndView mav = new ModelAndView("articles/list");
        Person loggedUser = personDao.getLoggedPerson();
        setPermissionsToView(mav);
        Paginator paginator = new Paginator(articleDao.getArticleCountForPerson(loggedUser), ARTICLES_PER_PAGE, "list.html?page=%1$d");
        if (page == null) {
            page = 1;
        }
        paginator.setActualPage(page);
        mav.addObject("paginator", paginator.getLinks());
        List articleList = new ArrayList();


        if (showInternal) {
            articleList = articleDao.getArticlesForList(loggedUser, paginator.getFirstItemIndex(), ARTICLES_PER_PAGE);
        }

        if (showLinkedIn) {
            mav.addObject("articleListTitle", "pageTitle.linkedInArticles");
        }
        if (showInternal) {
            mav.addObject("articleListTitle", "pageTitle.internalArticles");
        }
        if (showInternal && showLinkedIn) {
            mav.addObject("articleListTitle", "pageTitle.allArticles");
        }
        if (!showInternal && !showLinkedIn) {
            mav.addObject("articleListTitle", "pageTitle.noArticles");
        }

        mav.addObject("showLinkedIn", showLinkedIn);
        mav.addObject("showInternal", showInternal);
        mav.addObject("groupDetails", groupDetails);
        mav.addObject("linkedInArticles", linkedInArticlesFiltered);

        mav.addObject("articleList", articleList);
        mav.addObject("articleListTitle", "pageTitle.allArticles");
        mav.addObject("userIsGlobalAdmin", loggedUser.getAuthority().equals("ROLE_ADMIN"));
        mav.addObject("loggedUserId", loggedUser.getPersonId());
        return mav;
    }

    /**
     * Post method for list.jsp form, it controls two checkboxes, which deside which articles will be showed
     * From LinkedIn EEG/ERP group returns filtered articles by keywords filter, this filter is not enabled yet and is commented.
     * After new release of linkedIn API can be filter uncomment
     * @param request HTTP request
     * @param response HTTP response
     * @return new model and view
     */
    @RequestMapping(value = "articles/list", method = RequestMethod.POST)
    public ModelAndView submitShowArticles(HttpServletRequest request, HttpServletResponse response) {

        //shows linkedIn aricles
        if (request.getParameter("showOnLinkedInArticles") != null && (request.getParameter("showOnLinkedInArticles").equals("1"))) {

            List<String> keywords = new ArrayList<String>();
            Person loggedUser = personDao.getLoggedPerson();

            linkedInArticlesFiltered = new ArrayList<Post>();
            List<ResearchGroup> groups = researchGroupDao.getResearchGroupsWhereMember(loggedUser);
            for (ResearchGroup item : groups) {
                //gets keywords from DB
                keywords.add(simpleKeywordsDao.getKeywords(item.getResearchGroupId()));
            }
            
            if (keywords.isEmpty())
                return new ModelAndView("redirect:list.html"); 
            
            String keywordsText = keywords.get(0);

            //keywords are splitted by ","
            String parseKeywords[] = keywordsText.split(",");

            //downloads posts from EEG/ERP group on linkedIn
            linkedInArticles = linkedInManager.getPosts(linkedInManager.groupId);

            //***************FILTER for correct function delete this comment**************/
            /*
             * for (Post item : linkedInArticles) { for (int i = 0; i <
             * parseKeywords.length; i++ ){ if
             * (item.getSummary().contains(parseKeywords[i].trim())){
             * linkedInArticlesPom.add(item); break; } }     
           }
           //***************FILTER for correct function delete this comment**************/
            
            groupDetails = linkedInManager.getGroupDetails();
            showLinkedIn = true;
        } else {
            showLinkedIn = false;
            groupDetails = null;
            linkedInArticles = null;
        }
        //shows internal articles
        if (request.getParameter("showInternalArticles") != null && (request.getParameter("showInternalArticles").equals("1"))) {
            showInternal = true;
        } else {
            showInternal = false;
        }

        return new ModelAndView("redirect:list.html");
    }

    @Override
    protected Object newCommandObject(Class myClass)
            throws Exception {
        return new ArticleCommentCommand();
    }

    @RequestMapping("articles/detail")
    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response) {
        ArticleCommentCommand command = new ArticleCommentCommand();

        ModelAndView mav = new ModelAndView("articles/detail");
        Person loggedUser = personDao.getLoggedPerson();
        setPermissionsToView(mav);
        int id = -1;
        try {
            id = Integer.parseInt(request.getParameter("articleId"));
        } catch (Exception e) {
            log.debug("Unable to determine article id");
        }
        Article article = (Article) articleDao.getArticleDetail(id, loggedUser);
        // If the user is not permitted to view the article, null is returned. We will redirect to article list instead of displaying the article page.
        if (article == null) {
            return new ModelAndView("redirect:list.html");
        }

        command.setArticleId(id);
        mav.addObject("command", command);
        mav.addObject("userCanEdit", canEdit(loggedUser, article));
        mav.addObject("article", article);

        // First we load all article comments for the article, so the most of them are already loaded and don't need to be lazily loaded
        List<ArticleComment> articleComments = articleCommentDao.getCommentsForArticle(id);
        List<ArticleComment> noParents = new ArrayList<ArticleComment>();
        // Then we filter out the comments with no parent
        for (ArticleComment comment : articleComments) {
            if (comment.getParent() == null) {
                noParents.add(comment);
            }
        }
        mav.addObject("commentsList", noParents);

        mav.addObject("subscribed", article.getSubscribers().contains(loggedUser));
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
     * Post method for articleSettings.jsp form, it allows store new keywords to the database.
     * It allows set up new keywords for users research groups
     * If all users research group IDs already exist in Keywords are these records updated, otherwise new research group Id is stored into Keywords table 
     * @param request HTTP request
     * @param response HTTP request
     * @return new model and view
     */
    @RequestMapping(value = "articles/settings", method = RequestMethod.POST)
    public ModelAndView submitArticleFilterSettings(HttpServletRequest request, HttpServletResponse response) {
        String keywords = request.getParameter("keywords");
        
        if (keywords.equals("No keywords defined!"))
        return new ModelAndView("redirect:settings.html");
    
        Keywords keywordsRecord;
        ResearchGroup researchGroup;
        int keywordID;
        Person loggedUser = personDao.getLoggedPerson();
        List<ResearchGroup> groups = researchGroupDao.getResearchGroupsWhereMember(loggedUser);
        
        for (ResearchGroup item : groups) {
            keywordID = simpleKeywordsDao.getID(item.getResearchGroupId());
            //research group id not exist in Keywords
            if (keywordID == -1) {
                keywordsRecord = new Keywords();
                researchGroup = new ResearchGroup();
                researchGroup.setResearchGroupId(item.getResearchGroupId());
                keywordsRecord.setKeywordsText(keywords);
                keywordsRecord.setResearchGroup(researchGroup);
                simpleKeywordsDao.create(keywordsRecord);
            //research group already exist in keywords
            } else {
                researchGroup = new ResearchGroup();
                researchGroup.setResearchGroupId(item.getResearchGroupId());
                keywordsRecord = simpleKeywordsDao.read(simpleKeywordsDao.getID(item.getResearchGroupId()));
                keywordsRecord.setKeywordsText(keywords);
                keywordsRecord.setResearchGroup(researchGroup);
                simpleKeywordsDao.update(keywordsRecord);
            }
        }
        return new ModelAndView("redirect:settings.html");
    }

    /**
     * Get method for articleSetting.jsp form, it showes keywords of users research groups in textarea
     * @param request HTTP request
     * @param response HTTP response
     * @param model model for view 
     * @return new model and view 
     * @throws Exception 
     */
    @RequestMapping(value = "articles/settings", method = RequestMethod.GET)
    public ModelAndView settings(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
        keywordsSettings.clear();
        ModelAndView mav = new ModelAndView("articles/articleSettings");
        setPermissionsToView(mav);
        Person loggedUser = personDao.getLoggedPerson();

        List<ResearchGroup> groups = researchGroupDao.getResearchGroupsWhereMember(loggedUser);
        
        if (groups.isEmpty())
            return new ModelAndView("redirect:list.html"); 

        for (ResearchGroup item : groups) {
            //gets keywords from DB
            String keyword = simpleKeywordsDao.getKeywords(item.getResearchGroupId());
            keywordsSettings.add(keyword);
        }
        Set<ResearchGroup> articlesGroupSubscriptions = loggedUser.getArticlesGroupSubscribtions();
        List<ResearchGroup> list = researchGroupDao.getResearchGroupsWhereMember(loggedUser);
        mav.addObject("researchGroupList", list);
        mav.addObject("articlesGroupSubscribtions", articlesGroupSubscriptions);
        model.addAttribute("keywords", keywordsSettings.get(0));
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
     *
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

    public SimpleKeywordsDao getSimpleKeywordsDao() {
        return simpleKeywordsDao;
    }

    public void setSimpleKeywordsDao(SimpleKeywordsDao simpleKeywordsDao) {
        this.simpleKeywordsDao = simpleKeywordsDao;
    }
}
