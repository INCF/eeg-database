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
 *   ArticleCommentFormPage.java, 2014/05/13 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.articles;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.UserRole;
import cz.zcu.kiv.eegdatabase.wui.core.article.ArticleFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.components.ArticleCommentFormPanel;

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ArticleCommentFormPage extends MenuPage {

    private static final long serialVersionUID = -6483764280064711699L;

    @SpringBean
    private ArticleFacade facade;
    
    @SpringBean
    private SecurityFacade securityFacade;

    public ArticleCommentFormPage(PageParameters parameters) {
        
        
        setPageTitle(ResourceUtils.getModel("label.addComment"));
        add(new ButtonPageMenu("leftMenu", ArticlesPageLeftMenu.values()));

        StringValue articleParam = parameters.get(PageParametersUtils.ARTICLE);
        StringValue commentParam = parameters.get(PageParametersUtils.COMMENT);

        if (articleParam.isNull() || articleParam.isEmpty() || commentParam.isEmpty() || commentParam.isNull()) {
            throw new RestartResponseAtInterceptPageException(ArticlesPage.class);
        }

        int articleId = articleParam.toInt();
        int commentId = commentParam.toInt();

        Article article = facade.read(articleId);

        boolean isPublicArticle = article.getResearchGroup() == null;
        testUserCanAddArticleComment(isPublicArticle);
        
        ArticleComment parentComment = facade.readComment(commentId);
        Person person = EEGDataBaseSession.get().getLoggedUser();

        ArticleComment newComment = new ArticleComment();
        newComment.setArticle(article);
        newComment.setPerson(person);
        newComment.setParent(parentComment);

        add(new ArticleCommentFormPanel("addCommentFormPanel", new Model<ArticleComment>(newComment), getFeedback()));

    }
    
    private void testUserCanAddArticleComment(boolean isPublicArticle) {
        
        boolean isUserAdmin = EEGDataBaseSession.get().hasRole(UserRole.ROLE_ADMIN.name());
        boolean userCanAddArticle = securityFacade.userIsGroupAdmin() || securityFacade.userIsExperimenter() || isPublicArticle || isUserAdmin;
        if (!userCanAddArticle) {
            throw new RestartResponseAtInterceptPageException(ArticlesPage.class);
        }
    }
}
