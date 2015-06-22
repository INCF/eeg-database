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
 *   WelcomePage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.welcome;

import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampLabel;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.article.ArticleFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ArticlesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ViewArticlePage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ListExperimentsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.ListResearchGroupsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.ResearchGroupsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ListScenariosPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ScenarioDetailPage;

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class WelcomePage extends MenuPage {

    private static final long serialVersionUID = -1967810037377960414L;

    private static final int LIST_LIMIT = 5;

    @SpringBean
    ArticleFacade articleFacade;

    @SpringBean
    ResearchGroupFacade groupFacade;

    @SpringBean
    ExperimentsFacade experimentsFacade;

    @SpringBean
    ScenariosFacade scenariosFacade;

    public WelcomePage() {

        setPageTitle(ResourceUtils.getModel("pageTitle.homePage"));

        Person loggedUser = EEGDataBaseSession.get().getLoggedUser();
        List<Article> articles = articleFacade.getArticlesForUser(loggedUser, LIST_LIMIT);
        List<Scenario> scenarios = scenariosFacade.getScenariosWhereOwner(loggedUser, LIST_LIMIT);
        List<Experiment> experimentsWhereOwner = experimentsFacade.getExperimentsWhereOwner(loggedUser, LIST_LIMIT);
        List<Experiment> experimentsWhereSubject = experimentsFacade.getExperimentsWhereSubject(loggedUser, LIST_LIMIT);
        List<ResearchGroup> myGroups = groupFacade.getResearchGroupsWhereMember(loggedUser, LIST_LIMIT);

        if (articles != null && !articles.isEmpty())
            add(new MyArticlesFragment("articles", "articlesFrag", this, new ListModel<Article>(articles)));
        else
            add(new Fragment("articles", "emptyFrag", this));

        if (experimentsWhereOwner != null && !experimentsWhereOwner.isEmpty())
            add(new MyExperimentsFragment("experiments", "experimentsFrag", this, new ListModel<Experiment>(experimentsWhereOwner)));
        else
            add(new Fragment("experiments", "emptyFrag", this));

        if (experimentsWhereSubject != null && !experimentsWhereSubject.isEmpty())
            add(new MeAsSubjectFragment("meAsSubject", "meAsSubjectFrag", this, new ListModel<Experiment>(experimentsWhereSubject)));
        else
            add(new Fragment("meAsSubject", "emptyFrag", this));

        if (scenarios != null && !scenarios.isEmpty())
            add(new MyScenariosFragment("myScenarios", "myScenariosFrag", this, new ListModel<Scenario>(scenarios)));
        else
            add(new Fragment("myScenarios", "emptyFrag", this));

        if (myGroups != null && !myGroups.isEmpty())
            add(new MyGroupsFragment("myGroups", "myGroupsFrag", this, new ListModel<ResearchGroup>(myGroups)));
        else
            add(new Fragment("myGroups", "emptyFrag", this));

        add(new BookmarkablePageLink<Void>("articlesList", ArticlesPage.class));
        add(new BookmarkablePageLink<Void>("experimentList", ListExperimentsPage.class, PageParametersUtils.getDefaultPageParameters(ListExperimentsPage.PARAM_OWNER)));
        add(new BookmarkablePageLink<Void>("meAsSubjectList", ListExperimentsPage.class, PageParametersUtils.getDefaultPageParameters(ListExperimentsPage.PARAM_SUBJECT)));
        add(new BookmarkablePageLink<Void>("scenariosList", ListScenariosPage.class));
        add(new BookmarkablePageLink<Void>("groupList", ListResearchGroupsPage.class));
    }
    

    private class MyArticlesFragment extends Fragment {

        private static final long serialVersionUID = 1L;

        public MyArticlesFragment(String id, String markupId, MarkupContainer markupProvider, IModel<List<Article>> model) {
            super(id, markupId, markupProvider, model);

            PropertyListView<Article> list = new PropertyListView<Article>("list", model) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void populateItem(ListItem<Article> item) {
                    item.add(new TimestampLabel("time", item.getModelObject().getTime(), StringUtils.DATE_TIME_FORMAT_PATTER));
                    item.add(new Label("groupTitle", item.getModelObject().getResearchGroup() != null ? item.getModelObject().getResearchGroup().getTitle() : "Public Article"));
                    item.add(new Label("comments", item.getModelObject().getArticleComments().size() + ""));
                    item.add(new ViewLinkPanel("articleTitle", ViewArticlePage.class, "articleId", item.getModel(), "title"));
                }
            };

            add(list);
        }
    }

    private class MyExperimentsFragment extends Fragment {

        private static final long serialVersionUID = 1L;

        public MyExperimentsFragment(String id, String markupId, MarkupContainer markupProvider, IModel<List<Experiment>> model) {
            super(id, markupId, markupProvider, model);

            PropertyListView<Experiment> list = new PropertyListView<Experiment>("list", model) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void populateItem(ListItem<Experiment> item) {

                    item.add(new Label("startTime"));
                    item.add(new Label("experimentId"));
                    item.add(new Label("scenario.title"));
                    item.add(new BookmarkablePageLink<Void>("detailLink", ExperimentsDetailPage.class,
                            PageParametersUtils.getDefaultPageParameters(item.getModelObject().getExperimentId())));
                }
            };

            add(list);
        }
    }

    private class MeAsSubjectFragment extends Fragment {

        private static final long serialVersionUID = 1L;

        public MeAsSubjectFragment(String id, String markupId, MarkupContainer markupProvider, IModel<List<Experiment>> model) {
            super(id, markupId, markupProvider, model);

            PropertyListView<Experiment> list = new PropertyListView<Experiment>("list", model) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void populateItem(ListItem<Experiment> item) {

                    item.add(new Label("startTime"));
                    item.add(new Label("experimentId"));
                    item.add(new Label("scenario.title"));
                    item.add(new BookmarkablePageLink<Void>("detailLink", ExperimentsDetailPage.class,
                            PageParametersUtils.getDefaultPageParameters(item.getModelObject().getExperimentId())));
                }
            };

            add(list);
        }
    }

    private class MyScenariosFragment extends Fragment {

        private static final long serialVersionUID = 1L;

        public MyScenariosFragment(String id, String markupId, MarkupContainer markupProvider, IModel<List<Scenario>> model) {
            super(id, markupId, markupProvider, model);

            PropertyListView<Scenario> list = new PropertyListView<Scenario>("list", model) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void populateItem(ListItem<Scenario> item) {
                    item.add(new Label("title"));
                    item.add(new BookmarkablePageLink<Void>("detailLink", ScenarioDetailPage.class,
                            PageParametersUtils.getDefaultPageParameters(item.getModelObject().getScenarioId())));
                }
            };

            add(list);
        }
    }

    private class MyGroupsFragment extends Fragment {

        private static final long serialVersionUID = 1L;

        public MyGroupsFragment(String id, String markupId, MarkupContainer markupProvider, IModel<List<ResearchGroup>> model) {
            super(id, markupId, markupProvider, model);

            PropertyListView<ResearchGroup> list = new PropertyListView<ResearchGroup>("list", model) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void populateItem(ListItem<ResearchGroup> item) {
                    item.add(new Label("title"));
                    item.add(new BookmarkablePageLink<Void>("detailLink", ResearchGroupsDetailPage.class,
                            PageParametersUtils.getDefaultPageParameters(item.getModelObject().getResearchGroupId())));
                }
            };

            add(list);
        }
    }
}
