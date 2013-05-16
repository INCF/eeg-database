package cz.zcu.kiv.eegdatabase.wui.ui.experiments.components;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampPropertyColumn;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ListExperimentsDataProvider;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import cz.zcu.kiv.eegdatabase.wui.components.table.CheckBoxColumn;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.components.LicenseEditForm;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.model.AbstractCheckBoxModel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Panel for experiment package management. Adding/removing experiments and
 * licenses.
 *
 * @author Jakub Danek
 */
public class ExperimentPackageManagePanel extends Panel {

    private static final int EXPERIMENTS_PER_PAGE = 10;

    @SpringBean
    private ExperimentsFacade experimentsFacade;
    @SpringBean
    private ExperimentPackageFacade experimentPackageFacade;
	@SpringBean
	private LicenseFacade licenseFacade;

    /**
     * Main model of the component - experiment package
     */
    private IModel<ExperimentPackage> epModel;
    /**
     * Set of selected experiments.
     */
    private Set<Experiment> selectedExperiments;

	/**
	 * List of experiments attached to the package
	 */
    private IModel<List<Experiment>> experimentsModel;
	/**
	 * List of experiments which can be added to the package
	 */
	private IModel<List<Experiment>> experimentsToAddModel;

	private IModel<List<License>> licenses;

	/**
	 * Model of the addLicenseWindow
	 */
	private IModel<License> licenseModel;
    
    //containers
    private WebMarkupContainer experimentListCont;
    private WebMarkupContainer header;
    private ModalWindow addExperimentsWindow;
	private ModalWindow addLicenseWindow;

    /**
     *
     * @param id component id
     * @param model model with the experiment package for which the info shall be displayed
     */
    public ExperimentPackageManagePanel(String id, IModel<ExperimentPackage> model) {
		super(id);

		this.epModel = model;
		this.licenseModel = new Model();
		this.selectedExperiments = new HashSet<Experiment>();

		experimentListCont = new WebMarkupContainer("experimentListCont");
		experimentListCont.setOutputMarkupPlaceholderTag(true);
		this.add(experimentListCont);

		this.addHeader();
		this.addExperimentListToCont(experimentListCont);
		this.addExperimentsAddWindow();
		this.addLicenseAddWindow();
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

		header.add(new Label("packageTitle", new PropertyModel(epModel, "name")));

		header.add(createVisibilityLink("showListLink", true));
		header.add(createVisibilityLink("hideListLink", false));
		header.add(createRemoveSelectedLink("removeSelectedLink"));
		header.add(createAddExperimentsLink("addExperimentsLink"));
		header.add(createRemovePackageLink("removePackageLink"));

		this.addLicenseList(header);
    }

	private Link createRemovePackageLink(String id) {
		Link removeLink = new Link(id) {

			@Override
			public void onClick() {
				experimentPackageFacade.removeExperimentPackage(epModel.getObject());
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
	private void addLicenseList(WebMarkupContainer cont ) {
		licenses = new LoadableDetachableModel<List<License>>() {

			@Override
			protected List<License> load() {
				List<License> l = licenseFacade.getLicensesForPackage(epModel.getObject());

				if(l.size() > 1) { //do not display owner license if there are others as well
					Iterator<License> it = l.iterator();
					while(it.hasNext()) {
						if(it.next().getLicenseType() == LicenseType.OWNER) {
							it.remove();
							break;
						}
					}
				}

				return l;
			}
		};

		ListView<License> view = new ListView<License>("licenseView", licenses) {

			@Override
			protected void populateItem(ListItem<License> item) {
				AjaxLink<License> link = new AjaxLink<License>("licenseItem", item.getModel()) {

					@Override
					public void onClick(AjaxRequestTarget target) {
						licenseModel.setObject(this.getModelObject());
						addLicenseWindow.show(target);
					}
				};

				switch(item.getModelObject().getLicenseType()) {
					case OPEN_DOMAIN://public license not editable, fixed name
						link.setEnabled(false);//public domain
						link.add(new Label("licenseItemLabel", ResourceUtils.getString("LicenseType.PUBLIC")));
						break;
					case OWNER://OWNER license not editable, fixed name
						link.setEnabled(false);
						link.add(new Label("licenseItemLabel", ResourceUtils.getString("LicenseType.OWNER")));
						break;
					default:
						link.add(new Label("licenseItemLabel", new PropertyModel(item.getModel(), "title")));
						break;
				}

				item.add(link);
			}
		};

		AjaxLink link = new AjaxLink("addLicenseLink") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				licenseModel.setObject(new License());
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
		addLicenseWindow.setWidthUnit("px");

		IModel<List<License>> blpModel = new LoadableDetachableModel<List<License>>() {

			@Override
			protected List<License> load() {
				List<LicenseType> typs = new ArrayList<LicenseType>();
				typs.add(LicenseType.ACADEMIC);
				typs.add(LicenseType.BUSINESS);
				return licenseFacade.getLicensesForGroup(epModel.getObject().getResearchGroup(), typs);
			}

		};

		Panel content = new LicenseEditForm(addLicenseWindow.getContentId(), licenseModel, blpModel, new PropertyModel<Boolean>(epModel, "researchGroup.paidAccount")) {

			@Override
			protected void onSubmitAction(IModel<License> model, AjaxRequestTarget target, Form<?> form) {
				License obj = model.getObject();
				if(obj.getLicenseId() == 0) {
					licenseFacade.addLicenseForPackage(model.getObject(), epModel.getObject());
				} else {
					licenseFacade.update(obj);
				}
				ModalWindow.closeCurrent(target);
				target.add(header);
			}

			@Override
			protected void onCancelAction(IModel<License> model, AjaxRequestTarget target, Form<?> form) {
				ModalWindow.closeCurrent(target);
				target.add(header);
			}

		};
		addLicenseWindow.setContent(content);
		this.add(addLicenseWindow);
    }
    /**
     * returns link which initializes the action of
     * adding experiments to package
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
     * Creates link which provides action for removing selected experiments
     * from the package
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
     * Create link which changes visibility settings of the experimentListCont container
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
				target.add(header);
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();

				boolean contVisible = ExperimentPackageManagePanel.this.experimentListCont.isVisible();
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

		columns.add(new CheckBoxColumn<Experiment, String> (Model.of("")) {

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
