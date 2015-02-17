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
 *   PricingPage.java, 2014/11/20 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.pricing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPageLeftMenu;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ListExperimentsByPackagePage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ListExperimentsDataProvider;
import cz.zcu.kiv.eegdatabase.wui.ui.home.HomePage;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class PriceListPage extends MenuPage {

    private static final long serialVersionUID = 7059398061109450339L;
    private static final int ITEMS_PER_PAGE = 10;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    private SecurityFacade securityFacade;

    @SpringBean
    private ExperimentsFacade experimentFacade;

    @SpringBean
    private ExperimentPackageFacade expPackageFacade;

    @SpringBean
    private ResearchGroupFacade groupFacade;

    private Model<ResearchGroup> selectModel;

    private ListModel<ExperimentPackage> packagesModel;
    private Person person;
    private ListExperimentsDataProvider experimentProvider;

    public PriceListPage() {
        
        // XXX price list hidden for now. exception redirect for access via url.
        throw new RestartResponseAtInterceptPageException(ListExperimentsByPackagePage.class);
        /*
        
        setPageTitle(ResourceUtils.getModel("pageTitle.pricelist"));
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        setupComponents();
        */
    }

    private void setupComponents() {

        person = EEGDataBaseSession.get().getLoggedUser();
        selectModel = new Model<ResearchGroup>();
        packagesModel = new ListModel<ExperimentPackage>();

        final WebMarkupContainer experimentsContainer = new WebMarkupContainer("experimentsContainer");
        experimentsContainer.setOutputMarkupId(true);

        Label emptyExperiments = new Label("emptyExperimentsLabel", ResourceUtils.getModel("label.myexperiments.empty")) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return experimentProvider.size() == 0;
            }
        };
        experimentProvider = new ListExperimentsDataProvider(experimentFacade, person, true, false);
        DefaultDataTable<Experiment, String> list = new DefaultDataTable<Experiment, String>("list", createListColumns(experimentsContainer),
                experimentProvider, ITEMS_PER_PAGE) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return experimentProvider.size() != 0;
            }
        };
        experimentsContainer.add(list, emptyExperiments);

        final WebMarkupContainer priceListForPackageContainer = new WebMarkupContainer("expPackagesContainer");
        priceListForPackageContainer.setOutputMarkupId(true);
        priceListForPackageContainer.setVisibilityAllowed(securityFacade.userIsGroupAdmin());

        List<ResearchGroup> selectGroupChoice = groupFacade.getResearchGroupsWhereUserIsGroupAdmin(person);
        final DropDownChoice<ResearchGroup> selectGroup = new DropDownChoice<ResearchGroup>("selectGroup", selectModel, selectGroupChoice);
        selectGroup.add(new AjaxFormComponentUpdatingBehavior("onChange") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                ResearchGroup selection = selectModel.getObject();
                List<ExperimentPackage> selectPackages = expPackageFacade.listExperimentPackagesByGroup(selection);
                packagesModel.setObject(selectPackages);
                target.add(priceListForPackageContainer);
            }

        });
        priceListForPackageContainer.add(selectGroup);

        PageableListView<ExperimentPackage> packageList = new PageableListView<ExperimentPackage>("packageList", packagesModel, ITEMS_PER_PAGE) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<ExperimentPackage> item) {
                item.setModel(new CompoundPropertyModel<ExperimentPackage>(item.getModel()));
                item.add(new Label("experimentPackageId"));
                item.add(new Label("name"));
                item.add(new Label("price"));
                item.add(new PriceChangePanel("changePrice", new PropertyModel<BigDecimal>(item.getModel(), "price"), getFeedback(), "width: 80px;") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    void onUpdate(AjaxRequestTarget target) {
                        ExperimentPackage experimentPackage = item.getModelObject();
                        expPackageFacade.update(experimentPackage);
                        target.add(priceListForPackageContainer);
                    }

                });
            }

            @Override
            public boolean isVisible() {
                List<ExperimentPackage> list = packagesModel.getObject();
                return list != null && !list.isEmpty();
            }
        };

        Label emptyListLabel = new Label("emptyListLabel", ResourceUtils.getModel("label.experimentPackage.select.empty")) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                List<ExperimentPackage> list = packagesModel.getObject();
                return selectModel.getObject() != null && !(list != null && !list.isEmpty());
            }
        };

        priceListForPackageContainer.add(emptyListLabel);
        priceListForPackageContainer.add(packageList);
        add(priceListForPackageContainer, experimentsContainer);
    }

    private List<? extends IColumn<Experiment, String>> createListColumns(final MarkupContainer container) {

        List<IColumn<Experiment, String>> columns = new ArrayList<IColumn<Experiment, String>>();

        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.number"), "experimentId", "experimentId"));
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.scenarioTitle"), "scenario.title", "scenario.title"));
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.price"), "price", "price"));

        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.price.change"), null, null) {

            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(final Item<ICellPopulator<Experiment>> item, String componentId, final IModel<Experiment> rowModel) {
                item.add(new PriceChangePanel(componentId, new PropertyModel<BigDecimal>(rowModel, "price"), getFeedback(), "width: 80px;") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    void onUpdate(AjaxRequestTarget target) {
                        Experiment experiment = rowModel.getObject();
                        experimentFacade.changePrice(experiment);
                        target.add(container);
                    }

                });
            }
        });

        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.detail"), null, null) {

            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
                item.add(new ViewLinkPanel(componentId, ExperimentsDetailPage.class, "experimentId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });

        return columns;
    }

}
