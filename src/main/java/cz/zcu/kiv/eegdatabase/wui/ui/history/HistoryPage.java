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
 *   HistoryPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.history;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.controller.history.ChoiceHistory;
import cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.StyleClassPropertyColumn;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ChartUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.history.HistoryFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.home.HomePage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.PersonDetailPage;

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class HistoryPage extends MenuPage {

    private static final long serialVersionUID = -1967810037377960414L;

    private static final int ITEMS_PER_PAGE = 20;

    @SpringBean
    private ResearchGroupFacade researchGroupFacade;

    @SpringBean
    private HistoryFacade historyFacade;

    @SpringBean
    private SecurityFacade securityFacade;

    private boolean isGroupAdmin;
    private int groupId = -1;
    private ChoiceHistory choice;
    private NonCachingImage chartImage = null;

    public HistoryPage() {
        addComponents(ChoiceHistory.DAILY);
    }

    public HistoryPage(PageParameters parameters) {

        StringValue value = parseParameters(parameters);

        ChoiceHistory choice = ChoiceHistory.DAILY;

        try {
            choice = ChoiceHistory.valueOf(ChoiceHistory.class, value.toString());
        } catch (IllegalArgumentException e) {
            throw new RestartResponseAtInterceptPageException(HistoryPage.class);
        }

        addComponents(choice);
    }

    public void addComponents(final ChoiceHistory choice) {

        this.choice = choice;

        isGroupAdmin = securityFacade.userIsGroupAdmin();
        Person loggedUser = EEGDataBaseSession.get().getLoggedUser();
        ResearchGroup defaultGroup = loggedUser.getDefaultGroup();
        groupId = defaultGroup == null ? 0 : loggedUser.getDefaultGroup().getResearchGroupId();

        final boolean admin = securityFacade.isAdmin();
        if (!admin && !isGroupAdmin) {
            throw new RestartResponseAtInterceptPageException(HomePage.class);
        }

        if (admin) {
            groupId = 0;
            isGroupAdmin = false;
        }

        setPageTitle(ResourceUtils.getModel("title.page.history"));
        add(new ButtonPageMenu("leftMenu", HistoryLeftPageMenu.values()));

        final WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);

        final HistoryTopDownloadsDataProvider topDownloadsDataProvider = new HistoryTopDownloadsDataProvider(historyFacade);
        topDownloadsDataProvider.setData(choice, isGroupAdmin, groupId);
        topDownloadsDataProvider.setSort("count", SortOrder.DESCENDING);

        final HistoryLastDownloadedDataProvider lastDownloadedDataProvider = new HistoryLastDownloadedDataProvider(historyFacade);
        lastDownloadedDataProvider.setData(choice, isGroupAdmin, groupId);
        lastDownloadedDataProvider.setSort("dateOfDownload", SortOrder.DESCENDING);

        final HistoryAllTimeRangeRecordsDataProvider allTimeRangeDataProvider = new HistoryAllTimeRangeRecordsDataProvider(historyFacade);
        allTimeRangeDataProvider.setData(choice, isGroupAdmin, groupId);
        allTimeRangeDataProvider.setSort("dateOfDownload", SortOrder.DESCENDING);

        if (choice == ChoiceHistory.DAILY) {
            container.add(new Label("title", ResourceUtils.getModel("pageTitle.dailyDownloadHistory")));
            container.add(new Label("timePeriodStatistic", ResourceUtils.getModel("title.dailyStatistic")));
            container.add(new Label("allTimePeriodRecords", ResourceUtils.getModel("title.allDailyRecords")));

        } else if (choice == ChoiceHistory.WEEKLY) {
            container.add(new Label("title", ResourceUtils.getModel("pageTitle.weeklyDownloadHistory")));
            container.add(new Label("timePeriodStatistic", ResourceUtils.getModel("title.weeklyStatistic")));
            container.add(new Label("allTimePeriodRecords", ResourceUtils.getModel("title.allWeeklyRecords")));

        } else if (choice == ChoiceHistory.MONTHLY) {
            container.add(new Label("title", ResourceUtils.getModel("pageTitle.monthlyDownloadHistory")));
            container.add(new Label("timePeriodStatistic", ResourceUtils.getModel("title.monthlyStatistic")));
            container.add(new Label("allTimePeriodRecords", ResourceUtils.getModel("title.allMonthlyRecords")));

        } else {
            throw new RestartResponseAtInterceptPageException(HistoryPage.class);
        }

        container.add(new Label("downloadFiles", ResourceUtils.getModel("text.downloadFiles")).setRenderBodyOnly(true));

        final Model<String> countModel = new Model<String>("" + allTimeRangeDataProvider.getCountOfHistory());
        container.add(new Label("count", countModel).setRenderBodyOnly(true));

        container.add(new Label("topDownloads", ResourceUtils.getModel("title.topDownloads")));

        DefaultDataTable<DownloadStatistic, String> topDownloadedFilelist = new DefaultDataTable<DownloadStatistic, String>("topDownloadedFilelist",
                createListColumnsTopDownloads(), topDownloadsDataProvider, ITEMS_PER_PAGE);
        container.add(topDownloadedFilelist);

        getChartImage();
        container.add(chartImage);

        container.add(new Label("lastDownloaded", ResourceUtils.getModel("title.lastDownloaded")));

        DefaultDataTable<History, String> lastDownloadedFilesHistoryList = new DefaultDataTable<History, String>("lastDownloadedFilesHistoryList",
                createListColumnsLastDownloaded(), lastDownloadedDataProvider, ITEMS_PER_PAGE);
        container.add(lastDownloadedFilesHistoryList);

        DefaultDataTable<History, String> historyList = new DefaultDataTable<History, String>("historyList", createListColumnsAllTimeRangeRecords(), allTimeRangeDataProvider,
                ITEMS_PER_PAGE);
        container.add(historyList);

        Form<Void> groupForm = new Form<Void>("form");
        ChoiceRenderer<ResearchGroup> renderer = new ChoiceRenderer<ResearchGroup>("title", "researchGroupId");
        List<ResearchGroup> choices = researchGroupFacade.getResearchGroupsWhereUserIsGroupAdmin(loggedUser);
        final DropDownChoice<ResearchGroup> researchGroupChoice = new DropDownChoice<ResearchGroup>("researchGroup", new Model<ResearchGroup>(), choices, renderer);
        
        if(defaultGroup != null)
            researchGroupChoice.setModelObject(defaultGroup);
        
        if (admin) {
            ResearchGroup showAll = new ResearchGroup();
            showAll.setResearchGroupId(0);
            showAll.setTitle(ResourceUtils.getString("select.option.allGroups"));
            choices.add(0, showAll);
            researchGroupChoice.setModelObject(showAll);
        }

        researchGroupChoice.setRequired(true);
        researchGroupChoice.setLabel(ResourceUtils.getModel("label.group"));
        researchGroupChoice.add(new AjaxFormComponentUpdatingBehavior("onChange") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                isGroupAdmin = securityFacade.userIsGroupAdmin();
                ResearchGroup group = researchGroupChoice.getModelObject();
                groupId = group == null ? -1 : group.getResearchGroupId();

                if (groupId == 0 && admin) {
                    isGroupAdmin = false;
                }

                topDownloadsDataProvider.setData(choice, isGroupAdmin, groupId);
                lastDownloadedDataProvider.setData(choice, isGroupAdmin, groupId);
                allTimeRangeDataProvider.setData(choice, isGroupAdmin, groupId);

                countModel.setObject("" + allTimeRangeDataProvider.getCountOfHistory());

                getChartImage();

                target.add(container);
            }
        });

        groupForm.add(researchGroupChoice);
        container.add(groupForm);

        add(container);

    }

    private void getChartImage() {

        List<DownloadStatistic> topDownloadedFilesList = historyFacade.getTopDownloadHistory(choice, isGroupAdmin, groupId);
        long countOfFilesHistory = historyFacade.getCountOfFilesHistory(choice, isGroupAdmin, groupId);

        BufferedImage chart = ChartUtils.gererateChartForTopDownloadHistory(choice, groupId == -1 ? true : false, topDownloadedFilesList, countOfFilesHistory);

        BufferedDynamicImageResource res = new BufferedDynamicImageResource();
        res.setImage(chart);

        if (chartImage == null)
            chartImage = new NonCachingImage("chart", res);
        else
            chartImage.setImageResource(res);

        chartImage.setOutputMarkupId(true);
    }

    private List<? extends IColumn<DownloadStatistic, String>> createListColumnsTopDownloads() {
        
        List<IColumn<DownloadStatistic, String>> columns = new ArrayList<IColumn<DownloadStatistic, String>>();

        columns.add(new StyleClassPropertyColumn<DownloadStatistic, String>(ResourceUtils.getModel("dataTable.heading.fileName"), "fileName", "fileName", "width100"));

        columns.add(new StyleClassPropertyColumn<DownloadStatistic, String>(ResourceUtils.getModel("dataTable.heading.count"), "count", "count", "width100"));

        return columns;
    }

    private List<? extends IColumn<History, String>> createListColumnsLastDownloaded() {
        
        List<IColumn<History, String>> columns = new ArrayList<IColumn<History, String>>();

        columns.add(new StyleClassPropertyColumn<History, String>(ResourceUtils.getModel("dataTable.heading.date"), "dateOfDownload", "dateOfDownload", "width100"));

        columns.add(new StyleClassPropertyColumn<History, String>(ResourceUtils.getModel("dataTable.heading.fileName"), "dataFile.filename", "dataFile.filename", "width100"));

        columns.add(new StyleClassPropertyColumn<History, String>(ResourceUtils.getModel("dataTable.heading.detailOfUser"), null, null, "width50") {

            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(Item<ICellPopulator<History>> item, String componentId, IModel<History> rowModel) {
                item.add(new ViewLinkPanel(componentId, PersonDetailPage.class, "person.personId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });

        return columns;
    }

    private List<? extends IColumn<History, String>> createListColumnsAllTimeRangeRecords() {
        
        List<IColumn<History, String>> columns = new ArrayList<IColumn<History, String>>();

        columns.add(new StyleClassPropertyColumn<History, String>(ResourceUtils.getModel("dataTable.heading.date"), "dateOfDownload", "dateOfDownload", "width100"));

        columns.add(new PropertyColumn<History, String>(ResourceUtils.getModel("dataTable.heading.id"), "historyId", "historyId"));

        columns.add(new StyleClassPropertyColumn<History, String>(ResourceUtils.getModel("dataTable.heading.fileName"), "dataFile.filename", "dataFile.filename", "width100"));

        columns.add(new StyleClassPropertyColumn<History, String>(ResourceUtils.getModel("dataTable.heading.scenarioTitle"), "scenario.title", "scenario.title", "width100"));

        columns.add(new StyleClassPropertyColumn<History, String>(ResourceUtils.getModel("dataTable.heading.user"), "person.fullName", "person.fullName", "width100"));

        columns.add(new StyleClassPropertyColumn<History, String>(ResourceUtils.getModel("dataTable.heading.detailOfUser"), null, null, "width50") {

            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(Item<ICellPopulator<History>> item, String componentId, IModel<History> rowModel) {
                item.add(new ViewLinkPanel(componentId, PersonDetailPage.class, "person.personId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });

        return columns;
    }

    private StringValue parseParameters(PageParameters parameters) {

        StringValue value = parameters.get(BasePage.DEFAULT_PARAM_ID);
        if (value.isNull() || value.isEmpty())
            throw new RestartResponseAtInterceptPageException(HistoryPage.class);

        return value;
    }

}
