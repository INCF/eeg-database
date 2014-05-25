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
 *   ArticlesSettingsPage.java, 2014/05/13 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.articles;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.Keywords;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.KeywordsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.components.SubscribeGroupLink;

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ArticlesSettingsPage extends MenuPage {

    private static final long serialVersionUID = 7335700157186853057L;

    @SpringBean
    private ResearchGroupFacade groupFacade;

    @SpringBean
    private KeywordsFacade keywordsFacade;

    @SpringBean
    private SecurityFacade securityFacade;

    public ArticlesSettingsPage() {

        add(new ButtonPageMenu("leftMenu", ArticlesPageLeftMenu.values()));
        setPageTitle(ResourceUtils.getModel("heading.ArticlesSettings"));

        setupComponents();

    }

    private void setupComponents() {

        final Person loggedUser = EEGDataBaseSession.get().getLoggedUser();
        List<ResearchGroup> groupList = groupFacade.getResearchGroupsWhereMember(loggedUser);

        List<String> list = new ArrayList<String>();
        for (ResearchGroup item : groupList) {
            // gets keywords from DB
            String keyword = keywordsFacade.getKeywords(item.getResearchGroupId());
            list.add(keyword);
        }

        String keywords = list.size() > 0 ? list.get(0) : "";

        Form<Void> form = new Form<Void>("filterForm");
        final TextArea<String> textarea = new TextArea<String>("keywords", new Model<String>(keywords));
        add(textarea);

        AjaxButton filter = new AjaxButton("filter", ResourceUtils.getModel("button.changeFilter"), form) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                String keywords = textarea.getModelObject();
                setKeywordsFilter(loggedUser, keywords);
                setResponsePage(ArticlesSettingsPage.class);
            }

        };

        PropertyListView<ResearchGroup> groups = new PropertyListView<ResearchGroup>("groups", groupList) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<ResearchGroup> item) {
                item.add(new Label("title"));
                item.add(new SubscribeGroupLink("subscribe", item.getModel(), groupFacade));
            }
        };

        form.add(textarea, filter);
        add(form, groups);
    }

    private void setKeywordsFilter(final Person loggedUser, String keywords) {
        Keywords keywordsRecord;
        ResearchGroup researchGroup;
        int keywordID;
        List<ResearchGroup> groups = groupFacade.getResearchGroupsWhereMember(loggedUser);

        for (ResearchGroup item : groups) {
            keywordID = keywordsFacade.getID(item.getResearchGroupId());
            // research group id not exist in Keywords
            if (keywordID == -1) {
                keywordsRecord = new Keywords();
                researchGroup = new ResearchGroup();
                researchGroup.setResearchGroupId(item.getResearchGroupId());
                keywordsRecord.setKeywordsText(keywords);
                keywordsRecord.setResearchGroup(researchGroup);
                keywordsFacade.create(keywordsRecord);
                // research group already exist in keywords
            } else {
                researchGroup = new ResearchGroup();
                researchGroup.setResearchGroupId(item.getResearchGroupId());
                keywordsRecord = keywordsFacade.read(keywordsFacade.getID(item.getResearchGroupId()));
                keywordsRecord.setKeywordsText(keywords);
                keywordsRecord.setResearchGroup(researchGroup);
                keywordsFacade.update(keywordsRecord);
            }
        }
    }
}
