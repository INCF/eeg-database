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
 *   ArticleFormPage.java, 2014/05/13 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.articles;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.validator.StringValidator;

import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.controller.social.LinkedInManager;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.UserRole;
import cz.zcu.kiv.eegdatabase.wui.core.article.ArticleFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ArticleFormPage extends MenuPage {

    private static final long serialVersionUID = 8092717501137834521L;

    @SpringBean
    private ArticleFacade articleFacade;

    @SpringBean
    private SecurityFacade securityFacade;

    @SpringBean
    private ResearchGroupFacade groupFacade;

    @SpringBean
    private LinkedInManager linkedInManager;

    public ArticleFormPage() {

        testUserCanAddArticle();

        setPageTitle(ResourceUtils.getModel("pageTitle.addArticle"));
        add(new Label("title", ResourceUtils.getModel("pageTitle.addArticle")));

        setupComponents(new Model<Article>(new Article()));
    }

    public ArticleFormPage(PageParameters parameters) {

        testUserCanAddArticle();

        setPageTitle(ResourceUtils.getModel("pageTitle.editArticle"));
        add(new Label("title", ResourceUtils.getModel("pageTitle.editArticle")));

        StringValue param = parameters.get(DEFAULT_PARAM_ID);
        if (param.isNull() || param.isEmpty()) {
            throw new RestartResponseAtInterceptPageException(ArticlesPage.class);
        }

        int articleId = param.toInt();
        Article article = articleFacade.getArticleDetail(articleId, EEGDataBaseSession.get().getLoggedUser());
        setupComponents(new Model<Article>(article));
    }

    private void setupComponents(IModel<Article> model) {

        add(new ButtonPageMenu("leftMenu", ArticlesPageLeftMenu.values()));

        add(new ArticleForm("form", model, getFeedback()));
    }

    private void testUserCanAddArticle() {
        boolean userAdmin = EEGDataBaseSession.get().hasRole(UserRole.ROLE_ADMIN.name());
        boolean userCanAddArticle = securityFacade.userIsGroupAdmin() || securityFacade.userIsExperimenter() || userAdmin;
        if (!userCanAddArticle) {
            throw new RestartResponseAtInterceptPageException(ArticlesPage.class);
        }
    }

    private class ArticleForm extends Form<Article> {

        private static final long serialVersionUID = 1L;

        public ArticleForm(String id, IModel<Article> model, final FeedbackPanel feedback) {
            super(id, new CompoundPropertyModel<Article>(model));
            setOutputMarkupId(true);
            final Person loggedUser = EEGDataBaseSession.get().getLoggedUser();

            boolean userAdmin = EEGDataBaseSession.get().hasRole(UserRole.ROLE_ADMIN.name());

            List<ResearchGroup> groups = groupFacade.getResearchGroupsWhereAbleToWriteInto(loggedUser);
            if (userAdmin) {
                ResearchGroup defaultGroup = new ResearchGroup();
                defaultGroup.setResearchGroupId(0);
                defaultGroup.setTitle(ResourceUtils.getString("select.option.article.public"));
                groups.add(0, defaultGroup);
            }

            ChoiceRenderer<ResearchGroup> groupRenderer = new ChoiceRenderer<ResearchGroup>("title", "researchGroupId");
            DropDownChoice<ResearchGroup> groupChoice = new DropDownChoice<ResearchGroup>("researchGroup", groups, groupRenderer);
            groupChoice.setRequired(true);
            groupChoice.setLabel(ResourceUtils.getModel("label.researchGroup"));
            groupChoice.setEnabled(model.getObject().getArticleId() == 0);
            add(groupChoice);

            if (userAdmin) {
                groupChoice.setModelObject(groups.get(0));
            }

            TextField<String> titleField = new TextField<String>("title");
            titleField.setRequired(true);
            titleField.add(StringValidator.maximumLength(150));
            titleField.setLabel(ResourceUtils.getModel("label.title"));
            add(titleField);

            TextArea<String> textArea = new TextArea<String>("text");
            textArea.setRequired(true);
            textArea.setLabel(ResourceUtils.getModel("label.text"));
            textArea.setEscapeModelStrings(true);
            add(textArea);

            final CheckBox linkedIn = new CheckBox("linkedin", new Model<Boolean>(Boolean.FALSE));
            add(linkedIn);

            AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.save")) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    Article article = ArticleForm.this.getModelObject();

                    // if research group is default, save article without group.
                    if (article.getResearchGroup().getResearchGroupId() == 0) {
                        article.setResearchGroup(null);
                    }

                    if (article.getArticleId() > 0) {
                        // edit article
                        articleFacade.update(article);
                        setResponsePage(ViewArticlePage.class, PageParametersUtils.getDefaultPageParameters(article.getArticleId()));
                    } else {
                        // add new article
                        article.setPerson(loggedUser);
                        article.setTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        Integer articleId = articleFacade.create(article);
                        setResponsePage(ViewArticlePage.class, PageParametersUtils.getDefaultPageParameters(articleId));
                    }

                    Boolean publishOnLinkedIn = linkedIn.getModelObject();
                    if (publishOnLinkedIn) {
                        linkedInManager.publish(article.getTitle(), article.getText());
                    }
                }
            };

            add(submit);

        }

    }
}
