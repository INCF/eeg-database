package cz.zcu.kiv.eegdatabase.wui.ui.experiments.components;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxDropDownChoice;
import cz.zcu.kiv.eegdatabase.wui.components.page.UnderConstructPage;
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
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDownloadPage;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.LicenseRequestPage;
import cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart.BuyLinkPanel;
import java.util.Iterator;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Panel for experiment package management. Adding/removing experiments and
 * licenses.
 *
 * @author Jakub Danek
 */
public class ExperimentPackagePanel extends Panel {

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
	 * List of experiments attached to the package
	 */
    private IModel<List<Experiment>> experimentsModel;

	private IModel<List<License>> licenses;

	/**
	 * Model of the addLicenseWindow
	 */
	private IModel<License> licenseModel;

	private List<License> ownedLicenses;

    //containers
    private WebMarkupContainer experimentListCont;
    private WebMarkupContainer header, footer;
	private Link<License> licenseBuyLink;
	private DataTable<Experiment, String> table;

	private boolean hasLicense = true;

    /**
     *
     * @param id component id
     * @param model model with the experiment package for which the info shall be displayed
     */
    public ExperimentPackagePanel(String id, IModel<ExperimentPackage> model) {
		super(id);

		this.epModel = model;
		this.licenseModel = new Model();
		ownedLicenses = licenseFacade.getUsersLicenses(EEGDataBaseSession.get().getLoggedUser());

		experimentListCont = new WebMarkupContainer("experimentListCont");
		experimentListCont.setOutputMarkupPlaceholderTag(true);
		this.add(experimentListCont);

		this.addHeader();
		this.addFooter();
		this.addExperimentListToCont(experimentListCont);
    }

    /**
     * Add components header - title, controls
     */
    private void addHeader() {
		header = new WebMarkupContainer("header");
		header.setOutputMarkupId(true);
		this.add(header);

		header.add(new Label("packageTitle", new PropertyModel(epModel, "name")));
		header.add(new Label("researchGroupTitle", new PropertyModel(epModel, "researchGroup.title")));

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

		AjaxDropDownChoice<License> ddc = new AjaxDropDownChoice<License>("licenses", licenseModel, licenses, new ChoiceRenderer<License>("title", "licenseId")) {

			@Override
			protected void onSelectionChangeAjaxified(AjaxRequestTarget target, License option) {
				super.onSelectionChangeAjaxified(target, option);

				hasLicense = option == null ? true : ownedLicenses.contains(option);


				target.add(licenseBuyLink);
			}

		};

		licenseBuyLink = new Link<License>("buyLicense", licenseModel) {

			@Override
			public void onClick() {
				License l = this.getModelObject();
				if(l != null) {
					PageParameters params = new PageParameters();
					params.set(LicenseRequestPage.PARAM_LICENSE_ID, l.getLicenseId());
					setResponsePage(LicenseRequestPage.class, params);
				}
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				this.setVisible(!hasLicense);
			}

		};
		licenseBuyLink.setOutputMarkupPlaceholderTag(true);

		cont.add(ddc);
		cont.add(licenseBuyLink);

		
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
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.services"), null, null) {

            @Override
            public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
                item.add(new ViewLinkPanel(componentId, UnderConstructPage.class, "experimentId", rowModel, ResourceUtils.getModel("menuItem.services")));
            }
        });
		columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.detail"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
                item.add(new ViewLinkPanel(componentId, ExperimentsDetailPage.class, "experimentId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });
		if(ownedLicenses.size() > 0) {
			columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.download"), null, null) {

				@Override
				public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
					item.add(new ViewLinkPanel(componentId, ExperimentsDownloadPage.class, "experimentId", rowModel, ResourceUtils.getModel("link.download")));
				}

			});
		}

        //Add to cart
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.buy"), null, null) {

            @Override
            public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
                item.add(new BuyLinkPanel(componentId, rowModel));
            }
        });

		return columns;
    }

}
