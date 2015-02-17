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
 *   OrderItemPanel.java, 2014/14/09 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.order;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ibm.icu.text.SimpleDateFormat;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.OrderItem;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampLabel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDetailPage;

public class OrderItemPanel extends Panel {

    private static final long serialVersionUID = 1L;
    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    private ExperimentsFacade facade;

    private IModel<String> showActionModel;
    private IModel<String> hideActionModel;

    public OrderItemPanel(String id, final IModel<OrderItem> model) {
        super(id, new CompoundPropertyModel<OrderItem>(model));

        showActionModel = ResourceUtils.getModel("action.show");
        hideActionModel = ResourceUtils.getModel("action.hide");

        final Experiment experiment = model.getObject().getExperiment();
        final ExperimentPackage experimentPackage = model.getObject().getExperimentPackage();

        // prepare containers
        WebMarkupContainer experimentContainer = new WebMarkupContainer("experiment") {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return experiment != null;
            }
        };

        final WebMarkupContainer packageContainer = new WebMarkupContainer("package") {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return experimentPackage != null;
            }
        };

        // prepare texts for experiment container
        int experimentId;
        String scenarioTitle;
        String date;
        if (experiment != null) {
            experimentId = experiment.getExperimentId();
            scenarioTitle = experiment.getScenario().getTitle();
            date = new SimpleDateFormat(StringUtils.DATE_TIME_FORMAT_PATTER, EEGDataBaseSession.get().getLocale()).format((Date) experiment.getStartTime());
        } else {
            experimentId = 0;
            scenarioTitle = "";
            date = "";
        }

        // add components for experiment container
        experimentContainer.add(new Label("experimentText1", ResourceUtils.getModel("text.order.item.experiment1", Integer.toString(experimentId), scenarioTitle)));
        experimentContainer.add(new Label("experimentText2", ResourceUtils.getModel("text.order.item.experiment2", date)));
        experimentContainer.add(new BookmarkablePageLink<Void>("detail", ExperimentsDetailPage.class, PageParametersUtils.getDefaultPageParameters(experimentId)));

        // prepare texts for package container
        int packageId;
        String name;
        String group;
        if (experimentPackage != null) {
            packageId = experimentPackage.getExperimentPackageId();
            name = experimentPackage.getName();
            group = experimentPackage.getResearchGroup() != null ? experimentPackage.getResearchGroup().getTitle() : "";
        } else {
            packageId = 0;
            name = "";
            group = "";
        }

        // add components for package container
        packageContainer.add(new Label("packageText1", ResourceUtils.getModel("text.order.item.package1", Integer.toString(packageId), name)));
        packageContainer.add(new Label("packageText2", ResourceUtils.getModel("text.order.item.package2", group)));

        // add component for list of experiments in package
        final PropertyListView<Experiment> packageExperimentList = new PropertyListView<Experiment>("experiments", facade.getExperimentsByPackage(packageId)) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Experiment> item) {
                item.add(new Label("experimentId"));
                item.add(new Label("scenario.title"));
                item.add(new TimestampLabel("date", item.getModelObject().getStartTime(), StringUtils.DATE_TIME_FORMAT_PATTER));
                item.add(new BookmarkablePageLink<Void>("detail", ExperimentsDetailPage.class, PageParametersUtils.getDefaultPageParameters(item.getModelObject().getExperimentId())));
            }
        };

        // add show hide link for list of experiments in package
        final Label showHideLinkLabel = new Label("showHideLinkLabel", showActionModel);
        AjaxLink<Void> showHideLink = new AjaxLink<Void>("detail") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {

                packageExperimentList.setVisible(!packageExperimentList.isVisible());
                if (packageExperimentList.isVisible())
                    showHideLinkLabel.setDefaultModel(hideActionModel);
                else
                    showHideLinkLabel.setDefaultModel(showActionModel);

                target.add(packageContainer);
            }
        };

        showHideLink.add(showHideLinkLabel);
        packageContainer.add(showHideLink);
        packageContainer.add(packageExperimentList);
        packageExperimentList.setVisible(false);

        experimentContainer.setOutputMarkupId(true);
        packageContainer.setOutputMarkupId(true);
        add(experimentContainer, packageContainer);

    }

}
