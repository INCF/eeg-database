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
 *   ArticleListItemPanel.java, 2014/05/13 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.articles.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxConfirmLink;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampLabel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.UserRole;
import cz.zcu.kiv.eegdatabase.wui.core.article.ArticleFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ArticleFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ArticlesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ViewArticlePage;

public class ArticleListItemPanel extends Panel {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private ArticleFacade facade;

    public ArticleListItemPanel(String id, final IModel<Article> model) {
        super(id, new CompoundPropertyModel<Article>(model));

        PageParameters pageParameters = PageParametersUtils.getDefaultPageParameters(model.getObject().getArticleId());
        BookmarkablePageLink<Void> viewLink = new BookmarkablePageLink<Void>("viewLink", ViewArticlePage.class, pageParameters);
        viewLink.add(new Label("title"));
        add(viewLink);

        String text = model.getObject().getText();
        add(new Label("text", text.length() > 500 ? text.substring(0, 500) : text));

        BookmarkablePageLink<Void> readMoreLink = new BookmarkablePageLink<Void>("readMoreLink", ViewArticlePage.class, PageParametersUtils.getDefaultPageParameters(model.getObject()
                .getArticleId()));
        readMoreLink.setVisibilityAllowed(text.length() > 500);
        add(readMoreLink);

        add(new TimestampLabel("time", model.getObject().getTime(), StringUtils.DATE_FORMAT_PATTER));

        ResearchGroup group = model.getObject().getResearchGroup();
        add(new Label("groupLabel", ResourceUtils.getString("researchGroup")).setVisibilityAllowed(group != null));
        add(new Label("group", group == null ? ResourceUtils.getString("label.publicArticle") : group.getTitle()));
        add(new Label("person.givenname"));
        add(new Label("person.surname"));
        add(new Label("articleComments.size", model.getObject().getArticleComments().size()));

        // edit / delete links
        BookmarkablePageLink<Void> editLink = new BookmarkablePageLink<Void>("editLink", ArticleFormPage.class, pageParameters);
        AjaxConfirmLink<Void> deleteLink = new AjaxConfirmLink<Void>("deleteLink", ResourceUtils.getString("text.delete.article")) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                // delete article
                facade.delete(model.getObject());
                setResponsePage(ArticlesPage.class);
            }
        };
        boolean showControlLinks = EEGDataBaseSession.get().hasRole(UserRole.ROLE_ADMIN.name()) ||
                EEGDataBaseSession.get().getLoggedUser().getPersonId() == model.getObject().getPerson().getPersonId();

        editLink.setVisibilityAllowed(showControlLinks);
        deleteLink.setVisibilityAllowed(showControlLinks);
        add(editLink, deleteLink);

    }

}
