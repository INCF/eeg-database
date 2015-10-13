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
 *   ExperimentPackageManagePanel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.components;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageLicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.components.ViewLicensePanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.model.AbstractCheckBoxModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.wui.components.table.CheckBoxColumn;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampPropertyColumn;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ListExperimentsDataProvider;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.components.LicensePriceForm;

/**
 * Panel for experiment package management. Adding/removing experiments and
 * licenses.
 *
 * @author Jakub Danek
 */
@SuppressWarnings("serial")
public class ExperimentPackageManagePanel extends Panel {
    
    protected Log log = LogFactory.getLog(getClass());

	private static final int EXPERIMENTS_PER_PAGE = 10;
	
	@SpringBean
	private ExperimentsFacade experimentsFacade;
	
	@SpringBean
	private ExperimentPackageFacade experimentPackageFacade;
	
	@SpringBean
	private LicenseFacade licenseFacade;
    
	@SpringBean
    private ExperimentPackageLicenseFacade experimentPackageLicenseFacade;
	
    /** Main model of the component - experiment package */
	private IModel<ExperimentPackage> epModel;
	
	/** Set of selected experiments. */
	private Set<Experiment> selectedExperiments;
	
	/** List of experiments attached to the package */
	private IModel<List<Experiment>> experimentsModel;
	
	/** List of experiments which can be added to the package */
	private IModel<List<Experiment>> experimentsToAddModel;
	
	//private IModel<List<License>> licenses;
	
	/**
	 * Model of the viewLicenseWindow
	 */
	private IModel<License> licenseModel = new Model<License>();
	
	//containers
	private WebMarkupContainer experimentListCont;
	private WebMarkupContainer header, footer;
	private ModalWindow addExperimentsWindow;
	private ModalWindow addLicenseWindow;
    private ModalWindow viewLicenseWindow;
    

	/**
	 *
	 * @param id component id
	 * @param model model with the experiment package for which the info shall
	 * be displayed
	 */
	public ExperimentPackageManagePanel(String id, IModel<ExperimentPackage> model) {
		super(id);

		this.epModel = model;
		this.selectedExperiments = new HashSet<Experiment>();

		experimentListCont = new WebMarkupContainer("experimentListCont");
		experimentListCont.setOutputMarkupPlaceholderTag(true);
		this.add(experimentListCont);

		this.addHeader();
		this.addFooter();
		this.addExperimentListToCont(experimentListCont);
		this.addExperimentsAddWindow();
        this.addExperimentsViewWindow();
		this.addLicenseAddWindow();
	}

    private void addExperimentsViewWindow() {
        viewLicenseWindow = new ModalWindow("viewLicenseWindow");
        viewLicenseWindow.setAutoSize(true);
        viewLicenseWindow.setResizable(false);
        viewLicenseWindow.setMinimalWidth(600);
        viewLicenseWindow.setWidthUnit("px");
        viewLicenseWindow.showUnloadConfirmation(false);
        add(viewLicenseWindow);

        viewLicenseWindow.setContent(new ViewLicensePanel(viewLicenseWindow.getContentId(), licenseModel, true) {
            @Override
            protected void onRemoveAction(IModel<License> model, AjaxRequestTarget target, Form<?> form) {
                licenseFacade.removeLicenseFromPackage(model.getObject(), epModel.getObject());
                ModalWindow.closeCurrent(target);
                target.add(header);
            }
        });
        viewLicenseWindow.setTitle(ResourceUtils.getModel("dataTable.heading.licenseTitle"));
    }

	/**
	 * Add window which allows to add experiments to the package.
	 */
	private void addExperimentsAddWindow() {
		addExperimentsWindow = new ModalWindow("addExperimentsWindow");
		addExperimentsWindow.setAutoSize(true);
		addExperimentsWindow.setResizable(false);
		addExperimentsWindow.setMinimalWidth(600);
		addExperimentsWindow.setWidthUnit("px");

		experimentsToAddModel = this.listExperimentsToAdd();

		Panel content = new ExperimentListForm(addExperimentsWindow.getContentId(), ResourceUtils.getModel("pageTitle.addExperimenToPackage"), experimentsToAddModel) {
			@Override
			protected void onSubmitAction(List<Experiment> selectedExperiments, AjaxRequestTarget target, Form<?> form) {
				experimentPackageFacade.addExperimentsToPackage(selectedExperiments, epModel.getObject());
				ModalWindow.closeCurrent(target);
				experimentsModel.detach();
				experimentsToAddModel.detach();
				target.add(experimentListCont);
			}

			@Override
			protected void onCancelAction(List<Experiment> selectedExperiments, AjaxRequestTarget target, Form<?> form) {
				ModalWindow.closeCurrent(target);
				target.add(experimentListCont);
			}
		};
		addExperimentsWindow.setContent(content);
		this.add(addExperimentsWindow);
	}
	

	/**
	 *
	 * @return list of experiments that can be added to the package
	 */
	private IModel<List<Experiment>> listExperimentsToAdd() {
		return new LoadableDetachableModel<List<Experiment>>() {
			@Override
			protected List<Experiment> load() {
				return experimentsFacade.getExperimentsWithoutPackage(epModel.getObject());
			}
		};
	}

	
	/**
	 * Add components header - title, controls
	 */
	private void addHeader() {
		header = new WebMarkupContainer("header");
		header.setOutputMarkupId(true);
		this.add(header);

		header.add(new Label("packageTitle", new PropertyModel<String>(epModel, "name")));

		header.add(createRemovePackageLink("removePackageLink"));

		this.addLicenseList(header);
	}
	

	private void addFooter() {
		footer = new WebMarkupContainer("footer");
		footer.setOutputMarkupId(true);
		this.add(footer);
		
		footer.add(createVisibilityLink("showListLink", true));
		footer.add(createVisibilityLink("hideListLink", false));
		footer.add(createRemoveSelectedLink("removeSelectedLink"));
		footer.add(createAddExperimentsLink("addExperimentsLink"));
	}

	
	private Link<?> createRemovePackageLink(String id) {
		Link<?> removeLink = new Link<Void>(id) {
			@Override
			public void onClick() {
				experimentPackageFacade.removeExperimentPackage(epModel.getObject());
				setResponsePage(this.getPage().getPageClass(), this.getPage().getPageParameters());
			}
		};

		removeLink.add(new Label("label", ResourceUtils.getModel("button.remove.package")));

		return removeLink;
	}

	
	/**
	 * Adds list of licenses attached to the package.
	 *
	 * @param cont container to add the list to
	 */
	private void addLicenseList(WebMarkupContainer cont) {
	    /*IModel<List<License>> licenses = new LoadableDetachableModel<List<License>>() {
			@Override
			protected List<License> load() {
				List<License> l = licenseFacade.getLicensesForPackage(epModel.getObject());

				if (l.size() > 1) { //do not display owner license if there are others as well
					Iterator<License> it = l.iterator();
					while (it.hasNext()) {
						if (it.next().getLicenseType() == LicenseType.OWNER) {
							it.remove();
							break;
						}
					}
				}

				return l;
			}
		};*/
		
		IModel<List<ExperimentPackageLicense>> licenses = new LoadableDetachableModel<List<ExperimentPackageLicense>>() {
            @Override
            protected List<ExperimentPackageLicense> load() {
                List<ExperimentPackageLicense> list = experimentPackageLicenseFacade.getExperimentPackageLicensesForPackage(epModel.getObject());

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

		ListView<ExperimentPackageLicense> view = new ListView<ExperimentPackageLicense>("licenseView", licenses) {
			@Override
			protected void populateItem(ListItem<ExperimentPackageLicense> item) {
				AjaxLink<License> link = new AjaxLink<License>("licenseItem", new Model<License>(item.getModelObject().getLicense())) {
					@Override
					public void onClick(AjaxRequestTarget target) {
						licenseModel.setObject(this.getModelObject());
                        viewLicenseWindow.show(target);
					}
				};
                link.add(new Label("licenseItemLabel", new PropertyModel<String>(item.getModelObject().getLicense(), "title")));
				item.add(link);
				item.add(new Label("price", new Model<BigDecimal>(item.getModelObject().getPrice())));
			}
		};

		AjaxLink<Void> link = new AjaxLink<Void>("addLicenseLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				addLicenseWindow.show(target);
			}
		};

		cont.add(link);
		cont.add(view);
	}

	
	/**
	 * Add window which allows to add new license to the package.
	 */
    private void addLicenseAddWindow() {
		addLicenseWindow = new ModalWindow("addLicenseWindow");
		addLicenseWindow.setAutoSize(true);
		addLicenseWindow.setResizable(false);
		addLicenseWindow.setMinimalWidth(600);
		addLicenseWindow.setMinimalHeight(400);
		addLicenseWindow.setWidthUnit("px");
        addLicenseWindow.showUnloadConfirmation(false);

        // prepare list of licenses not associated with the package yet
		IModel<List<License>> licenses = new LoadableDetachableModel<List<License>>() {
			@Override
			protected List<License> load() {
			    List<License> list = licenseFacade.getLicenseTemplates();
                list.removeAll(licenseFacade.getLicensesForPackage(epModel.getObject()));
			    return list;
			}
		};
		
		LicensePriceForm addLicenseForm = new LicensePriceForm(addLicenseWindow.getContentId(), licenses) {
            
            @Override
            protected void onSubmitAction(License license, BigDecimal price, AjaxRequestTarget target, Form<?> form) {
                ExperimentPackageLicense expPacLic = new ExperimentPackageLicense();
                expPacLic.setExperimentPackage(epModel.getObject());
                expPacLic.setLicense(license);
                expPacLic.setPrice(price);
                experimentPackageLicenseFacade.create(expPacLic);
                ModalWindow.closeCurrent(target);
                target.add(header);
            }
            
            @Override
            protected void onCancelAction(AjaxRequestTarget target, Form<?> form) {
                ModalWindow.closeCurrent(target);
                target.add(header);
            }
            
        };
        
        addLicenseWindow.setContent(addLicenseForm);
		this.add(addLicenseWindow);
	}
    

	/**
	 * returns link which initializes the action of adding experiments to
	 * package
	 *
	 * @param id component id
	 * @return the link
	 */
	private AjaxLink createAddExperimentsLink(String id) {
		AjaxLink link = new AjaxLink(id) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				addExperimentsWindow.show(target);
			}
		};

		link.add(new Label("label", new StringResourceModel("button.add.experiment", this, null)));
		return link;
	}

	
	/**
	 * Creates link which provides action for removing selected experiments from
	 * the package
	 *
	 * @param id link component id
	 * @return the link
	 */
	private Link createRemoveSelectedLink(String id) {
		Link link = new Link(id) {
			@Override
			public void onClick() {
				experimentPackageFacade.removeExperimentsFromPackage(new ArrayList(selectedExperiments), epModel.getObject());
			}
		};

		link.add(new Label("label", new StringResourceModel("button.removeSelected", this, null)));
		return link;
	}

	
	/**
	 * Create link which changes visibility settings of the experimentListCont
	 * container
	 *
	 * @param id link's id
	 * @param makeVisible makes the container visible or invisible?
	 * @return ajax link
	 */
	private AjaxLink createVisibilityLink(String id, boolean makeVisible) {
		AjaxLink link = new AjaxLink<Boolean>(id, new Model<Boolean>(makeVisible)) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				ExperimentPackageManagePanel.this.experimentListCont.setVisible(this.getModelObject());
				target.add(ExperimentPackageManagePanel.this.experimentListCont);
				target.add(footer);
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();

				boolean contVisible = ExperimentPackageManagePanel.this.experimentListCont.isVisible();
				if (this.getModelObject()) {
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
	 *
	 * @param cont the container
	 */
	private void addExperimentListToCont(WebMarkupContainer cont) {
		List<? extends IColumn<Experiment, String>> columns = this.createListColumns();

		experimentsModel = new LoadableDetachableModel<List<Experiment>>() {
			@Override
			protected List<Experiment> load() {
				return experimentsFacade.getExperimentsByPackage(epModel.getObject().getExperimentPackageId());
			}
		};

		DataTable<Experiment, String> list = new AjaxFallbackDefaultDataTable<Experiment, String>("list", columns,
				new ListExperimentsDataProvider(experimentsModel), EXPERIMENTS_PER_PAGE);

		cont.add(list);
	}
	

	/**
	 *
	 * @return list of columns the table of experiments shall display
	 */
	private List<? extends IColumn<Experiment, String>> createListColumns() {
		List<IColumn<Experiment, String>> columns = new ArrayList<IColumn<Experiment, String>>();

		columns.add(new CheckBoxColumn<Experiment, String>(Model.of("")) {
			@Override
			protected IModel<Boolean> newCheckBoxModel(final IModel<Experiment> rowModel) {
				return new AbstractCheckBoxModel() {
					@Override
					public boolean isSelected() {
						return selectedExperiments.contains(rowModel.getObject());
					}

					@Override
					public void select() {
						selectedExperiments.add(rowModel.getObject());
					}

					@Override
					public void unselect() {
						selectedExperiments.add(rowModel.getObject());
					}

					@Override
					public void detach() {
						rowModel.detach();
					}
				};

			}
		});
		columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.number"), "experimentId", "experimentId"));
		columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.scenarioTitle"), "scenario.title", "scenario.title"));
		columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.date"), "startTime", "startTime"));
		columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.gender"), "personBySubjectPersonId.gender", "personBySubjectPersonId.gender"));
		columns.add(new TimestampPropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.yearOfBirth"), "personBySubjectPersonId.dateOfBirth",
				"personBySubjectPersonId.dateOfBirth", "yyyy"));
		columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.detail"), null, null) {
			@Override
			public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
				item.add(new ViewLinkPanel(componentId, ExperimentsDetailPage.class, "experimentId", rowModel, ResourceUtils.getModel("link.detail")));
			}
		});

		return columns;
	}
}
