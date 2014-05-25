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
 *   ArticleCommentFormPanel.java, 2014/05/13 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.articles.components;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.article.ArticleFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ViewArticlePage;

public class ArticleCommentFormPanel extends Panel {

    private static final long serialVersionUID = 1L;

    @SpringBean
    ArticleFacade facade;

    public ArticleCommentFormPanel(String id, IModel<ArticleComment> model, final FeedbackPanel feedback) {
        super(id, model);

        add(new CommentForm("form", model, feedback));
        setRenderBodyOnly(true);
    }

    private class CommentForm extends Form<ArticleComment> {

        private static final long serialVersionUID = 1L;

        public CommentForm(String id, final IModel<ArticleComment> model, final FeedbackPanel feedback) {
            super(id, new CompoundPropertyModel<ArticleComment>(model));

            TextArea<String> text = new TextArea<String>("text");
            text.setEscapeModelStrings(true);
            text.setRequired(true);
            text.setLabel(ResourceUtils.getModel("label.text"));
            add(text);

            AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.save"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    ArticleComment articleComment = CommentForm.this.getModelObject();
                    articleComment.setTime(new Timestamp(new Date().getTime()));

                    if (articleComment.getCommentId() == 0) {
                        facade.create(articleComment);
                    } else {
                        facade.updateComment(articleComment);
                    }

                    setResponsePage(ViewArticlePage.class, PageParametersUtils.getDefaultPageParameters(model.getObject().getArticle().getArticleId()));

                }

            };

            add(submit);

        }

    }

}
