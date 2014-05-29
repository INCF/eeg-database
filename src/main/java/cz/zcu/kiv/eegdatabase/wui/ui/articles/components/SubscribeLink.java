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
 *   SubscribeLink.java, 2014/05/13 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.articles.components;

import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.article.ArticleFacade;

public class SubscribeLink extends AjaxLink<Article> {

    private static final long serialVersionUID = 1L;

    protected Log log = LogFactory.getLog(getClass());

    private ArticleFacade articleFacade;
    private boolean subscribe;
    private IModel<String> subscribeLabel;
    private IModel<String> unsubscribeLabel;
    private Label label;

    public SubscribeLink(String id, IModel<Article> model, ArticleFacade facade) {
        super(id, model);
        setOutputMarkupId(true);

        this.articleFacade = facade;
        this.subscribeLabel = ResourceUtils.getModel("label.subscribe");
        this.unsubscribeLabel = ResourceUtils.getModel("label.unsubscribe");

        Article article = getModelObject();
        article.setSubscribers(new HashSet<Person>(article.getSubscribers()));
        Person person = EEGDataBaseSession.get().getLoggedUser();

        subscribe = article.getSubscribers().contains(person);

        label = new Label("label", !subscribe ? subscribeLabel : unsubscribeLabel);
        label.setRenderBodyOnly(true);

        add(label.setOutputMarkupId(true));

    }

    @Override
    public void onClick(AjaxRequestTarget target) {

        Person person = EEGDataBaseSession.get().getLoggedUser();
        Article article = articleFacade.getArticleDetail(getModelObject().getArticleId(), EEGDataBaseSession.get().getLoggedUser());

        if (subscribe) {
            article.getSubscribers().remove(person);
        } else {
            article.getSubscribers().add(person);
        }
        subscribe = !subscribe;
        label.setDefaultModel(!subscribe ? subscribeLabel : unsubscribeLabel);

        articleFacade.update(article);

        target.add(this);
    }

}
