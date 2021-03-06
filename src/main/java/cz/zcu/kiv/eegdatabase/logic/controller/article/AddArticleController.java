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
 *   AddArticleController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.article;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.logic.controller.social.LinkedInManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author Jiri Vlasimsky
 */
public class AddArticleController extends SimpleFormController {

    private AuthorizationManager auth;
    private GenericDao<Article, Integer> articleDao;
    private GenericDao<ArticleComment, Integer> articleCommentDao;
    private PersonDao personDao;
    private ResearchGroupDao researchGroupDao;
    private Log log = LogFactory.getLog(getClass());
    private String domain;
    private JavaMailSenderImpl mailSender;
    private SimpleMailMessage mailMessage;
    private HierarchicalMessageSource messageSource;
    @Autowired
    private LinkedInManager linkedin;

    
    public AddArticleController() {
        setCommandClass(ArticleCommand.class);
        setCommandName("addArticle");
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        ModelAndView mav = super.showForm(request, response, errors);
        setPermissionsToView(mav);
        return mav;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        ArticleCommand data = (ArticleCommand) super.formBackingObject(request);
        String articleIdString = request.getParameter("articleId");
        if (articleIdString != null) {
            int articleId = Integer.parseInt(articleIdString);
            if (articleId > 0) {  // it is a form for editing

                data.setArticleId(articleId);

                log.debug("Filling backing object with data from article object #" + articleId);
                data.setArticleId(articleId);
                Article article = articleDao.read(articleId);

                log.debug("Setting article title");
                data.setTitle(article.getTitle());

                log.debug("Setting article text");
                data.setText(article.getText());
            }
        }
        return data;
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map map = new HashMap<String, Object>();
        List<ResearchGroup> researchGroupList =
                researchGroupDao.getResearchGroupsWhereAbleToWriteInto(personDao.getLoggedPerson());
        ResearchGroup defaultGroup = personDao.getLoggedPerson().getDefaultGroup();
        int defaultGroupId = (defaultGroup != null) ? defaultGroup.getResearchGroupId() : 0;
        map.put("defaultGroupId", defaultGroupId);
        map.put("researchGroupList", researchGroupList);
        return map;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        ModelAndView mav = new ModelAndView(getSuccessView());

        ArticleCommand data = (ArticleCommand) command;

        Article article;

        if (data.getArticleId() > 0) {
            log.debug("Processing article form - editing existing article");
            log.debug("Checking the permission level.");
            article = articleDao.read(data.getArticleId());
            Person loggedUser = personDao.getLoggedPerson();

            if (!canEdit(loggedUser, article)) {
                log.debug("User is not group or system admininstrator - unable to edit article. Returning MAV.");
                mav.setViewName("article/unableToEditArticle");
                return mav;
            }
        } else {
            article = new Article();

            log.debug("Setting the owner to the logged user.");
            article.setPerson(personDao.getLoggedPerson());

            log.debug("Setting the group to which is the new article assigned");
            ResearchGroup researchGroup = new ResearchGroup();
            if (data.getResearchGroup() != 0) {
                researchGroup.setResearchGroupId(data.getResearchGroup());
                article.setResearchGroup(researchGroup);
            }


            log.debug("Setting article time");
            Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            article.setTime(currentTimestamp);
        }

        log.debug("Setting article title");
        article.setTitle(data.getTitle());

        log.debug("Setting article text");
        article.setText(data.getText());

        if (data.getArticleId() > 0) {
            log.debug("Processing an article form - editing an existing article");
            articleDao.update(article);
        } else {
            log.debug("Processing an article form - adding a new article");
            articleDao.create(article);

            if (article.getResearchGroup() != null) {
                ResearchGroup group = researchGroupDao.read(article.getResearchGroup().getResearchGroupId());
                for (Person subscriber : group.getArticlesSubscribers()) {
                    if (!subscriber.equals(article.getPerson())) {
                        this.sendNotification(subscriber.getEmail(), article, request);
                    }
                }
            }
        }
        
        //Publish article on LinkedIn
        if ((request.getParameter("publishOnLinkedIn") != null) && (request.getParameter("publishOnLinkedIn").equals("publish"))){
            log.debug("Publishing an article on LinkedIn");
            linkedin.publish(data.getTitle(), data.getText()); 
        }
        
        return mav;
    }

    private void sendNotification(String email, Article article, HttpServletRequest request) throws MessagingException {
        String articleURL = "http://" + domain + "/articles/detail.html?articleId=" + article.getArticleId();
        //System.out.println(articleURL);
        String subject = messageSource.getMessage("articles.group.email.subscribtion.subject", new String[]{article.getTitle(), article.getPerson().getUsername()}, RequestContextUtils.getLocale(request));
        //System.out.println(subject);
        String emailBody = "<html><body>";

        emailBody += "<p>" + messageSource.getMessage("articles.comments.email.subscribtion.body.text.part1",
                new String[]{article.getTitle()},
                RequestContextUtils.getLocale(request)) + "";
        emailBody += "&nbsp;(<a href=\"" + articleURL + "\" target=\"_blank\">" + articleURL + "</a>)</p><br />";
        emailBody += "<h3>" + article.getTitle() + "</h3> <p>" + article.getText() + "</p><br />";
        emailBody += "<p>" + messageSource.getMessage("articles.comments.email.subscribtion.body.text.part2", null, RequestContextUtils.getLocale(request)) + "</p>";
        emailBody += "</body></html>";

        //System.out.println(emailBody);
        log.debug("email body: " + emailBody);


        log.debug("Composing e-mail message");
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        message.setFrom(mailMessage.getFrom());

        //  message.setContent("text/html");
        message.setTo(email);
        //helper.setFrom(messageSource.getMessage("registration.email.from", null, RequestContextUtils.getLocale(request)));
        message.setSubject(subject);
        message.setText(emailBody, true);

        try {
            log.debug("Sending e-mail" + message);
            log.debug("mailSender" + mailSender);
            log.debug("smtp " + mailSender.getHost());
            mailSender.send(mimeMessage);
            log.debug("E-mail was sent");
        } catch (MailException e) {
            log.error("E-mail was NOT sent");
            log.error(e);
        }
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public SimpleMailMessage getMailMessage() {
        return mailMessage;
    }

    public void setMailMessage(SimpleMailMessage mailMessage) {
        this.mailMessage = mailMessage;
    }

    public JavaMailSenderImpl getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    public HierarchicalMessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(HierarchicalMessageSource messageSource) {
        this.messageSource = messageSource;
    }

}
