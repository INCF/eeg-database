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
 *   ListScenariosPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.scenarios;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.ScenarioDownloadLink;
import cz.zcu.kiv.eegdatabase.wui.components.table.StyleClassPropertyColumn;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;

/**
 * Page with list of scenarios.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ListScenariosPage extends MenuPage {

    private static final long serialVersionUID = -1967810037377960414L;

    private static final int ITEMS_PER_PAGE = 20;
    public static final String MY_SCENARIOS_PARAM = "MY_SCENARIOS";

    @SpringBean
    ScenariosFacade scenariosFacade;

    public ListScenariosPage() {

        setPageTitle(ResourceUtils.getModel("pageTitle.listOfScenarios"));
        add(new Label("title", ResourceUtils.getModel("pageTitle.listOfScenarios")));
        setupComponents(new ListScenariosDataProvider(scenariosFacade));
    }
    
    public ListScenariosPage(PageParameters parameters)
    {
        StringValue paramValue = parameters.get(MY_SCENARIOS_PARAM);
        if(paramValue.isNull() || paramValue.isEmpty())
        {
            throw new RestartResponseAtInterceptPageException(ListScenariosPage.class);
        } else {
            setPageTitle(ResourceUtils.getModel("menuItem.myScenarios"));
            add(new Label("title", ResourceUtils.getModel("menuItem.myScenarios")));
            setupComponents(new MyScenarioDataProvider(scenariosFacade));
        }
    }

    private void setupComponents(ISortableDataProvider<Scenario, String> provider) {

        add(new ButtonPageMenu("leftMenu", ScenariosPageLeftMenu.values()));

        DefaultDataTable<Scenario, String> list = new DefaultDataTable<Scenario, String>("list", createListColumns(),
                provider, ITEMS_PER_PAGE);

        add(list);

    }

    private List<? extends IColumn<Scenario, String>> createListColumns() {
        List<IColumn<Scenario, String>> columns = new ArrayList<IColumn<Scenario, String>>();

        columns.add(new StyleClassPropertyColumn<Scenario, String>(ResourceUtils.getModel("dataTable.heading.id"), "scenarioId", "scenarioId", "width30"));
        columns.add(new StyleClassPropertyColumn<Scenario, String>(ResourceUtils.getModel("dataTable.heading.title"), "title", "title", "width300"));
        columns.add(new PropertyColumn<Scenario, String>(ResourceUtils.getModel("dataTable.heading.scenarioLength"), "scenarioLength", "scenarioLength"));

        columns.add(new StyleClassPropertyColumn<Scenario, String>(ResourceUtils.getModel("dataTable.heading.detail"), null, null, "width80") {

            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(Item<ICellPopulator<Scenario>> item, String componentId, IModel<Scenario> rowModel) {
                item.add(new ViewLinkPanel(componentId, ScenarioDetailPage.class, "scenarioId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });
        columns.add(new StyleClassPropertyColumn<Scenario, String>(ResourceUtils.getModel("dataTable.heading.file"), null, null, "width80") {

            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(Item<ICellPopulator<Scenario>> item, String componentId, IModel<Scenario> rowModel) {

                item.add(new ScenarioDownloadLink(componentId, rowModel));
            }
        });

        return columns;
    }
}
