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
 *   ExperimentPackagePanel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.components;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxDropDownChoice;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampPropertyColumn;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageLicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDownloadPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ListExperimentsDataProvider;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.LicenseRequestPage;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.components.ViewLicensePanel;
import cz.zcu.kiv.eegdatabase.wui.ui.signalProcessing.MethodListPage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.CloseButtonCallback;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.*;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Jakub Danek
 * 
 *
 */
//
// TODO kuba licence (zobrazeni balicku na Experiments -> All experiments)
//
public class ExperimentPackagePanel extends Panel {

    private static final int EXPERIMENTS_PER_PAGE = 10;

    @SpringBean
    private ExperimentsFacade experimentsFacade;
    
    @SpringBean
    private ExperimentPackageFacade experimentPackageFacade;
	
    @SpringBean
	private LicenseFacade licenseFacade;
    
    @SpringBean
    private ExperimentPackageLicenseFacade experimentPackagelicenseFacade;

    /**
     * Main model of the component - experiment package
     */
    private IModel<ExperimentPackage> epModel;

	/**
	 * List of experiments attached to the package
	 */
    private IModel<List<Experiment>> experimentsModel;

	private IModel<List<ExperimentPackageLicense>> licenses;

	/**
	 * Model of the addLicenseWindow
	 */
	private IModel<ExperimentPackageLicense> licenseModel = new Model<ExperimentPackageLicense>();

    //containers
    private WebMarkupContainer experimentListCont;
    private WebMarkupContainer header, footer;
	private DataTable<Experiment, String> table;


    private ModalWindow viewLicenseWindow;

    private AjaxLink<License> viewLicenseLink;

    
    /**
     *
     * @param id component id
     * @param model model with the experiment package for which the info shall be displayed
     */
    public ExperimentPackagePanel(String id, IModel<ExperimentPackage> model) {
		super(id);

		this.epModel = model;
		experimentListCont = new WebMarkupContainer("experimentListCont");
		experimentListCont.setOutputMarkupPlaceholderTag(true);
		this.add(experimentListCont);

		this.addHeader();
		this.addFooter();
		this.addExperimentListToCont(experimentListCont);
		
		add(new ExperimentPackageBuyDownloadLinkPanel("buyDownloadLinkPanel", epModel)
		.setVisibilityAllowed(!(epModel.getObject().getExperimentPackageId() == 0)));
    }

    
    /**
     * Add components header - title, controls
     */
    private void addHeader() {
		header = new WebMarkupContainer("header");
		header.setOutputMarkupId(true);
		this.add(header);
		
		header.add(new WebMarkupContainer("experimentPackageLabelCont").setVisible(!(epModel.getObject().getExperimentPackageId() == 0)));
		header.add(new Label("packageTitle", new PropertyModel<String>(epModel, "name")));		

		@SuppressWarnings("serial")
        WebMarkupContainer licenseCont = new WebMarkupContainer("licensesCont") {

			@Override
			protected void onConfigure() {
				super.onConfigure();
				boolean vis = licenses.getObject() != null ? licenses.getObject().size() > 0 : false;
				this.setVisible(vis);
			}

		};
		this.addLicenseList(licenseCont);
		header.add(licenseCont);
    }

    
	private void addFooter() {
		footer = new WebMarkupContainer("footer");
		footer.setOutputMarkupId(true);
		this.add(footer);

		footer.add(createVisibilityLink("showListLink", true));
		footer.add(createVisibilityLink("hideListLink", false));
	}

	
	/**
	 * Adds list of licenses attached to the package.
	 *
	 * @param cont container to add the list to
	 */
	@SuppressWarnings("serial")
    private void addLicenseList(WebMarkupContainer cont ) {

        viewLicenseWindow = new ModalWindow("viewLicenseWindow");
        viewLicenseWindow.setAutoSize(true);
        viewLicenseWindow.setResizable(false);
        viewLicenseWindow.setMinimalWidth(600);
        viewLicenseWindow.setWidthUnit("px");
        viewLicenseWindow.showUnloadConfirmation(false);
        add(viewLicenseWindow);
        
        final WebMarkupContainer priceCont = new WebMarkupContainer("priceCont") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setVisible(licenseModel.getObject() != null);
            }
        };
        priceCont.setOutputMarkupPlaceholderTag(true);
        Label price = new Label("price", new PropertyModel<String>(licenseModel, "price"));
        priceCont.add(price);
	    
		licenses = new LoadableDetachableModel<List<ExperimentPackageLicense>>() {

			@Override
			protected List<ExperimentPackageLicense> load() {
				List<ExperimentPackageLicense> list = experimentPackagelicenseFacade.getExperimentPackageLicensesForPackage(epModel.getObject());

				if (list.size() > 1) { //do not display owner license if there are others as well
					Iterator<ExperimentPackageLicense> it = list.iterator();
					while (it.hasNext()) {
						if (it.next().getLicense().getLicenseType() == LicenseType.OWNER) {
							it.remove();
							break;
						}
					}
				}

				return list;
			}
		};

		AjaxDropDownChoice<ExperimentPackageLicense> ddc = new AjaxDropDownChoice<ExperimentPackageLicense>("licenses", licenseModel, licenses, new ChoiceRenderer<ExperimentPackageLicense>("license.title", "experimentPackageLicenseId")) {

			@Override
			protected void onSelectionChangeAjaxified(AjaxRequestTarget target, ExperimentPackageLicense option) {
				super.onSelectionChangeAjaxified(target, option);
				target.add(viewLicenseLink);
				target.add(priceCont);
			}

		};
		
        viewLicenseWindow.setContent(new ViewLicensePanel(viewLicenseWindow.getContentId(), new PropertyModel<License>(licenseModel, "license"), false));
        viewLicenseWindow.setTitle(ResourceUtils.getModel("dataTable.heading.licenseTitle"));
        viewLicenseLink = new AjaxLink<License>("viewLicenseLink", new PropertyModel<License>(licenseModel, "license")) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                viewLicenseWindow.show(target);
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setVisible(licenseModel.getObject() != null);
            }

        };
        viewLicenseLink.setOutputMarkupPlaceholderTag(true);

		cont.add(ddc);
		cont.add(viewLicenseLink);
		cont.add(priceCont);
	}



    /**
     * Create link which changes visibility settings of the experimentListCont container
     * @param id link's id
     * @param makeVisible makes the container visible or invisible?
     * @return ajax link
     */
    private AjaxLink createVisibilityLink(String id, boolean makeVisible) {
		AjaxLink link = new AjaxLink<Boolean>(id, new Model<Boolean>(makeVisible)) {

			@Override
			public void onClick(AjaxRequestTarget target) {
				ExperimentPackagePanel.this.experimentListCont.setVisible(this.getModelObject());
				target.add(ExperimentPackagePanel.this.experimentListCont);
				target.add(footer);
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();

				boolean contVisible = ExperimentPackagePanel.this.experimentListCont.isVisible();
				if(this.getModelObject()) {
					//link for making cont visible is accessible only if the cont
					//is currently invisible
					this.setVisible(!contVisible);
				} else {
					//and vice versa
					this.setVisible(contVisible);
				}
			}
		};

		String resourceKey = makeVisible ? "button.show" : "button.hide";

		link.add(new Label("linkLabel", new StringResourceModel(resourceKey, this, null)));

		return link;
    }

    
    /**
     * Add view for the list of experiments to a container given
     * @param cont the container
     */
    private void addExperimentListToCont(WebMarkupContainer cont) {
		List<? extends IColumn<Experiment, String>> columns = this.createListColumns();

		experimentsModel = new LoadableDetachableModel<List<Experiment>>() {

			@Override
			protected List<Experiment> load() {
				if(epModel.getObject().getExperimentPackageId() > 0) {
					return experimentsFacade.getExperimentsByPackage(epModel.getObject().getExperimentPackageId());
				} else {
					return experimentsFacade.getExperimentsWithoutPackage();
				}
			}

		};

		table = new AjaxFallbackDefaultDataTable<Experiment, String>("list", columns,
								new ListExperimentsDataProvider(experimentsModel), EXPERIMENTS_PER_PAGE);

		cont.add(table);
    }

    
    /**
     *
     * @return list of columns the table of experiments shall display
     */
    private List<? extends IColumn<Experiment, String>> createListColumns() {
		List<IColumn<Experiment, String>> columns = new ArrayList<IColumn<Experiment, String>>();

        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.number"), "experimentId", "experimentId"));
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.scenarioTitle"), "scenario.title", "scenario.title"));
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.date"), "startTime", "startTime"));
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.gender"), "personBySubjectPersonId.gender", "personBySubjectPersonId.gender"));
        columns.add(new TimestampPropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.yearOfBirth"), "personBySubjectPersonId.dateOfBirth",
                "personBySubjectPersonId.dateOfBirth", "yyyy"));
        
		// TODO service page missing.
        /*columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.services"), null, null) {

            @Override
            public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
                item.add(new ViewLinkPanel(componentId, MethodListPage.class, "experimentId", rowModel, ResourceUtils.getModel("menuItem.services")));
            }
        });*/
        
		columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.detail"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
                item.add(new ViewLinkPanel(componentId, ExperimentsDetailPage.class, "experimentId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });

        if (epModel.getObject().getExperimentPackageId() == 0) {

            columns.add(new PropertyColumn<Experiment, String>(null, null, null) {

                @Override
                public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
                    item.add(new ExperimentBuyDownloadLinkPanel(componentId, rowModel));
                }
            });
        }

		return columns;
    }
    

}
