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
 *   ArticlesPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.articles;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.social.linkedin.api.Group;

import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.social.LinkedInManager;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.article.ArticleFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.components.ArticleListItemPanel;

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ArticlesPage extends MenuPage {

    private static final int ARTICLES_PER_PAGE = 10;

    @SpringBean
    private ArticleFacade articleFacade;

    @SpringBean
    private LinkedInManager linkedInManager;

    private static final long serialVersionUID = -1967810037377960414L;

    public ArticlesPage() {

        setPageTitle(ResourceUtils.getModel("title.page.articles"));
        add(new ButtonPageMenu("leftMenu", ArticlesPageLeftMenu.values()));

        // title component
        final Label pageTitle = new Label("title", ResourceUtils.getModel("pageTitle.allArticles"));
        pageTitle.setOutputMarkupId(true);
        add(pageTitle);

        // internal articles container
        final WebMarkupContainer internalContainer = new WebMarkupContainer("internalContainer");
        internalContainer.setOutputMarkupPlaceholderTag(true);
        add(internalContainer);

        // linkedin articles container
        final WebMarkupContainer linkedInContainer = new WebMarkupContainer("linkedInContainer");
        linkedInContainer.setOutputMarkupPlaceholderTag(true);
        add(linkedInContainer);

        // show configuration form
        Form<Void> form = new Form<Void>("form");
        form.setOutputMarkupId(true);
        add(form);
        final Model<Boolean> internalModel = new Model<Boolean>(Boolean.TRUE);
        final Model<Boolean> linkedInModel = new Model<Boolean>(Boolean.TRUE);
        form.add(new AjaxCheckBox("showInternal", internalModel) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                updatePageTitleAndView(pageTitle, internalContainer, linkedInContainer,
                        internalModel, linkedInModel, target);
            }

        });
        form.add(new AjaxCheckBox("showLinkedIn", linkedInModel) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                updatePageTitleAndView(pageTitle, internalContainer, linkedInContainer,
                        internalModel, linkedInModel, target);
            }
        });

        // linkedin articles components
        Group groupDetails = linkedInManager.getGroupDetails();
        linkedInContainer.add(new Label("groupDetails.name", new PropertyModel<Group>(groupDetails, "name")));
        linkedInContainer.add(new Label("groupDetails.description", new PropertyModel<Group>(groupDetails, "description")));

        // internal articles components
        Person loggedPerson = EEGDataBaseSession.get().getLoggedUser();
        List<Article> list = articleFacade.getArticlesForUser(loggedPerson);
        // list of internal articles with page control
        PageableListView<Article> articlesListView = new PageableListView<Article>("articles", list, ARTICLES_PER_PAGE) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Article> item) {
                item.add(new ArticleListItemPanel("item", item.getModel()));
            }
        };
        internalContainer.add(new AjaxPagingNavigator("topPaginator", articlesListView));
        internalContainer.add(new AjaxPagingNavigator("bottomPaginator", articlesListView));
        internalContainer.add(articlesListView);

    }

    private void updatePageTitleAndView(final Label pageTitle, final WebMarkupContainer internalContainer, final WebMarkupContainer linkedInContainer, final Model<Boolean> internalModel,
            final Model<Boolean> linkedInModel, AjaxRequestTarget target) {

        // get show configuration from form
        Boolean showInternalArticles = internalModel.getObject();
        Boolean showLinkedInArticles = linkedInModel.getObject();

        // update visibility with data from form
        internalContainer.setVisible(showInternalArticles);
        linkedInContainer.setVisible(showLinkedInArticles);

        // change date for updated visibility configuration
        if (showLinkedInArticles && showInternalArticles) {
            pageTitle.setDefaultModelObject(ResourceUtils.getString("pageTitle.allArticles"));
        } else if (!showLinkedInArticles && !showInternalArticles) {
            pageTitle.setDefaultModelObject(ResourceUtils.getString("pageTitle.noArticles"));
        } else if (showLinkedInArticles) {
            pageTitle.setDefaultModelObject(ResourceUtils.getString("pageTitle.linkedInArticles"));
        } else {
            pageTitle.setDefaultModelObject(ResourceUtils.getString("pageTitle.internalArticles"));
        }

        // ajax refresh
        target.add(pageTitle, internalContainer, linkedInContainer);
    }
}
