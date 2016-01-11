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
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentLicence;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxDropDownChoice;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampPropertyColumn;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experimentLicense.ExperimentLicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageLicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ListExperimentsDataProvider;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.components.ViewLicensePanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.*;
import org.apache.wicket.spring.injection.annot.SpringBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This panel shows an experiment package with its content.
 *
 * @author Jakub Danek
 * @author Jakub Krauz
 */
public class ExperimentPackagePanel extends Panel {

    private static final int EXPERIMENTS_PER_PAGE = 10;

    @SpringBean
    private ExperimentsFacade experimentsFacade;
    
    @SpringBean
    private ExperimentPackageFacade experimentPackageFacade;
	
    @SpringBean
	private LicenseFacade licenseFacade;
    
    @SpringBean
    private ExperimentLicenseFacade experimentLicenseFacade;
    
    @SpringBean
    private ExperimentPackageLicenseFacade experimentPackagelicenseFacade;

    /** Main model of the component - experiment package */
    private IModel<ExperimentPackage> epModel;

	/** List of experiments attached to the package */
    private IModel<List<Experiment>> experimentsModel;

	/** Model of the addLicenseWindow */
	private IModel<ExperimentPackageLicense> packageLicenseModel = new Model<ExperimentPackageLicense>();
	
	private IModel<License> licenseModel = new Model<License>();

    //containers
    private WebMarkupContainer experimentListCont;
    private WebMarkupContainer header, footer;
	private DataTable<Experiment, String> table;

    private ModalWindow viewLicenseWindow;

    private WebMarkupContainer priceCont;
    private AjaxLink<License> viewLicenseLink;
    
    private ExperimentPackageBuyDownloadLinkPanel buyDownloadPanel;
    
    private final boolean noPackage;

    
    /**
     *
     * @param id component id
     * @param model model with the experiment package for which the info shall be displayed
     */
    public ExperimentPackagePanel(String id, IModel<ExperimentPackage> model) {
		super(id);

		this.epModel = model;
		this.noPackage = (model.getObject().getExperimentPackageId() == 0);
		
		experimentListCont = new WebMarkupContainer("experimentListCont");
		experimentListCont.setOutputMarkupPlaceholderTag(true);
		this.add(experimentListCont);

		this.addHeader();
		this.addFooter();
		this.addExperimentListToCont(experimentListCont);
		
		buyDownloadPanel = new ExperimentPackageBuyDownloadLinkPanel("buyDownloadLinkPanel", model.getObject(), new Model<ExperimentPackageLicense>());
        buyDownloadPanel.setVisibilityAllowed(!(epModel.getObject().getExperimentPackageId() == 0));
        buyDownloadPanel.setOutputMarkupId(true);
		add(buyDownloadPanel);
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

        WebMarkupContainer licenseCont = new WebMarkupContainer("licensesCont");
		addPackageLicenseList(licenseCont); 
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
    private void addPackageLicenseList(WebMarkupContainer cont ) {

        viewLicenseWindow = new ModalWindow("viewLicenseWindow");
        viewLicenseWindow.setAutoSize(true);
        viewLicenseWindow.setResizable(false);
        viewLicenseWindow.setMinimalWidth(600);
        viewLicenseWindow.setWidthUnit("px");
        viewLicenseWindow.showUnloadConfirmation(false);
        add(viewLicenseWindow);
        
        priceCont = new WebMarkupContainer("priceCont") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setVisible(packageLicenseModel.getObject() != null);
            }
        };
        priceCont.setOutputMarkupPlaceholderTag(true);
        Label price = new Label("price", new PropertyModel<String>(packageLicenseModel, "price"));
        priceCont.add(price);
	    
        AjaxDropDownChoice<?> ddc = noPackage ? createLicenseChoice() : createPackageLicenseChoice();
		
        viewLicenseWindow.setContent(new ViewLicensePanel(viewLicenseWindow.getContentId(), licenseModel, false));
        viewLicenseWindow.setTitle(ResourceUtils.getModel("dataTable.heading.licenseTitle"));
        viewLicenseLink = new AjaxLink<License>("viewLicenseLink", licenseModel) {

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
	
	
	private AjaxDropDownChoice<ExperimentPackageLicense> createPackageLicenseChoice() {
	    
	    IModel<List<ExperimentPackageLicense>> licenses = new LoadableDetachableModel<List<ExperimentPackageLicense>>() {
            @Override
            protected List<ExperimentPackageLicense> load() {
                return experimentPackagelicenseFacade.getExperimentPackageLicensesForPackage(epModel.getObject());
            }
        };

        AjaxDropDownChoice<ExperimentPackageLicense> ddc = new AjaxDropDownChoice<ExperimentPackageLicense>("licenses", packageLicenseModel, licenses, new ChoiceRenderer<ExperimentPackageLicense>("license.title", "experimentPackageLicenseId")) {

            @Override
            protected void onSelectionChangeAjaxified(AjaxRequestTarget target, ExperimentPackageLicense option) {
                super.onSelectionChangeAjaxified(target, option);
                licenseModel.setObject((option != null) ? option.getLicense() : null);
                target.add(viewLicenseLink);
                target.add(priceCont);
                buyDownloadPanel.setModelObject(option);
                target.add(buyDownloadPanel);
            }

        };
        
        return ddc;
	}
	
	
	private AjaxDropDownChoice<License> createLicenseChoice() {
	    
	    IModel<List<License>> licenses = new LoadableDetachableModel<List<License>>() {
            @Override
            protected List<License> load() {
                return licenseFacade.getAllRecords();
            }
        };

        AjaxDropDownChoice<License> ddc = new AjaxDropDownChoice<License>("licenses", licenseModel, licenses, new ChoiceRenderer<License>("title", "licenseId")) {

            @Override
            protected void onSelectionChangeAjaxified(AjaxRequestTarget target, License option) {
                super.onSelectionChangeAjaxified(target, option);
                target.add(viewLicenseLink);
                experimentsModel.detach();
                target.add(experimentListCont);
            }

        };
        
        return ddc;
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
				if (!noPackage) {
					return experimentsFacade.getExperimentsByPackage(epModel.getObject().getExperimentPackageId());
				} else {
				    return experimentsFacade.getExperimentsWithoutPackageWithLicense(licenseModel.getObject());
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
    @SuppressWarnings("serial")
    private List<? extends IColumn<Experiment, String>> createListColumns() {
		List<IColumn<Experiment, String>> columns = new ArrayList<IColumn<Experiment, String>>();

        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.number"), "experimentId", "experimentId"));
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.scenarioTitle"), "scenario.title", "scenario.title"));
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.date"), "startTime", "startTime"));
        
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.gender"), "personBySubjectPersonId.gender", "personBySubjectPersonId.gender"));
        columns.add(new TimestampPropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.yearOfBirth"), "personBySubjectPersonId.dateOfBirth",

        
		// XXX service page missing.
        /*columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.services"), null, null) {
            @Override      // TODO make me visible.
            public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
                item.add(new ViewLinkPanel(componentId, MethodListPage.class, "experimentId", rowModel, ResourceUtils.getModel("menuItem.services")).setVisible(false));
            }
        });*/
        
		columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.detail"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
                item.add(new ViewLinkPanel(componentId, ExperimentsDetailPage.class, "experimentId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });

        if (noPackage) {
            
            columns.add(new PropertyColumn<Experiment, String>(new ResourceModel("dataTable.heading.price"), null, null) {
                
                @Override
                public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
                    if (licenseModel.getObject() != null) {
                        ExperimentLicence expLic = findExperimentLicense(rowModel.getObject().getExperimentLicences(), licenseModel.getObject());
                        IModel<String> priceLabel = new StringResourceModel("price.currency", (IModel<?>) null, new PropertyModel<String>(expLic, "price"));
                        item.add(new Label(componentId, priceLabel));
                    } else {
                        item.add(new Label(componentId).setVisible(false));
                    }
                }
                
            });

            columns.add(new PropertyColumn<Experiment, String>(null, null, null) {
                
                @Override
                public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
                    ExperimentBuyDownloadLinkPanel buyDownloadPanel = new ExperimentBuyDownloadLinkPanel(componentId, rowModel.getObject(), new Model<ExperimentLicence>());
                    if (licenseModel.getObject() != null) {
                        ExperimentLicence expLic = findExperimentLicense(rowModel.getObject().getExperimentLicences(), licenseModel.getObject());
                        buyDownloadPanel.setModelObject(expLic);
                    }
                    item.add(buyDownloadPanel);
                }
                
            });
            
        }

		return columns;
    }
    
    
    private ExperimentLicence findExperimentLicense(Collection<ExperimentLicence> experimentLicenses, License license) {
        for (ExperimentLicence item : experimentLicenses) {
            if (item.getLicense().equals(license))
                return item;
        }
        return new ExperimentLicence();
    }
    

}
