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
 *   ArticleCommentPanel.java, 2014/05/13 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.articles.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampLabel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ArticleCommentFormPage;

public class ArticleCommentPanel extends Panel {

    private static final long serialVersionUID = -6031347593914148212L;

    public ArticleCommentPanel(String id, IModel<ArticleComment> model) {
        super(id, new CompoundPropertyModel<ArticleComment>(model));

        setRenderBodyOnly(true);
        add(new TimestampLabel("time", model.getObject().getTime(), StringUtils.DATE_TIME_FORMAT_PATTER));
        add(new Label("person.givenname").setRenderBodyOnly(true));
        add(new Label("person.surname").setRenderBodyOnly(true));

        add(new MultiLineLabel("text"));
        List<ArticleComment> commentList = new ArrayList<ArticleComment>(model.getObject().getChildren());
        PropertyListView<ArticleComment> comments = new PropertyListView<ArticleComment>("children", commentList) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<ArticleComment> item) {
                item.add(new ArticleCommentPanel("comments", item.getModel()));
            }
        };
        comments.setRenderBodyOnly(true);

        PageParameters parameters = PageParametersUtils.getPageParameters(PageParametersUtils.ARTICLE, model.getObject().getArticle().getArticleId());
        parameters = PageParametersUtils.addParameters(parameters, PageParametersUtils.COMMENT, model.getObject().getCommentId());

        BookmarkablePageLink<Void> addCommentLink = new BookmarkablePageLink<Void>("addCommentLink", ArticleCommentFormPage.class, parameters);

        add(comments, addCommentLink);
    }

}
