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
 *   ViewArticlePage.java, 2014/05/13 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.articles;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxConfirmLink;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampLabel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.UserRole;
import cz.zcu.kiv.eegdatabase.wui.core.article.ArticleFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.components.ArticleCommentFormPanel;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.components.ArticleCommentPanel;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.components.SubscribeLink;

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ViewArticlePage extends MenuPage {

    private static final long serialVersionUID = -4755705839078394200L;

    @SpringBean
    private ArticleFacade articleFacade;

    @SpringBean
    private SecurityFacade securityFacade;

    public ViewArticlePage(PageParameters parameters) {

        StringValue param = parameters.get(DEFAULT_PARAM_ID);
        if (param.isNull() || param.isEmpty()) {
            throw new RestartResponseAtInterceptPageException(ArticlesPage.class);
        }
        int articleId = param.toInt();

        setupComponents(articleId);
    }

    private void setupComponents(int articleId) {

        add(new ButtonPageMenu("leftMenu", ArticlesPageLeftMenu.values()));
        PageParameters pageParameters = PageParametersUtils.getDefaultPageParameters(articleId);

        final Article article = articleFacade.getArticleDetail(articleId, EEGDataBaseSession.get().getLoggedUser());
        CompoundPropertyModel<Article> model = new CompoundPropertyModel<Article>(article);
        setDefaultModel(model);

        Label pageTitle = new Label("title");
        add(pageTitle);

        ResearchGroup group = article.getResearchGroup();
        add(new Label("group", group == null ? ResourceUtils.getString("label.publicArticle") : group.getTitle()));
        add(new TimestampLabel("time", article.getTime(), StringUtils.DATE_FORMAT_PATTER));
        add(new Label("person.givenname").setRenderBodyOnly(true));
        add(new Label("person.surname").setRenderBodyOnly(true));

        BookmarkablePageLink<Void> editLink = new BookmarkablePageLink<Void>("editLink", ArticleFormPage.class, pageParameters);
        AjaxConfirmLink<Void> deleteLink = new AjaxConfirmLink<Void>("deleteLink", ResourceUtils.getString("text.delete.article")) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                // delete article
                articleFacade.delete(article);
                setResponsePage(ArticlesPage.class);
            }
        };
        Person loggedUser = EEGDataBaseSession.get().getLoggedUser();
        boolean showControlLinks = EEGDataBaseSession.get().hasRole(UserRole.ROLE_ADMIN.name()) ||
                loggedUser.getPersonId() == article.getPerson().getPersonId();

        editLink.setVisibilityAllowed(showControlLinks);
        deleteLink.setVisibilityAllowed(showControlLinks);
        add(editLink, deleteLink);

        add(new SubscribeLink("subscribe", model, articleFacade));

        add(new MultiLineLabel("text"));

        ArticleComment comment = new ArticleComment(loggedUser);
        comment.setArticle(article);
        ArticleCommentFormPanel articleCommentFormPanel = new ArticleCommentFormPanel("commentFormPanel", new Model<ArticleComment>(comment), getFeedback());
        boolean canUserAddComment = securityFacade.userIsGroupAdmin() || securityFacade.userIsExperimenter();
        articleCommentFormPanel.setVisibilityAllowed(canUserAddComment);
        add(articleCommentFormPanel);

        List<ArticleComment> commentList = new ArrayList<ArticleComment>(model.getObject().getArticleComments());
        PropertyListView<ArticleComment> comments = new PropertyListView<ArticleComment>("articleComments", commentList) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<ArticleComment> item) {
                item.add(new ArticleCommentPanel("commentPanel", item.getModel()));
            }
        };
        comments.setRenderBodyOnly(true);

        add(comments);

    }

}
