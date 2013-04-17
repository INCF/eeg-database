package cz.zcu.kiv.eegdatabase.wui.ui.experiments.components;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
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
import java.util.HashSet;
import java.util.Set;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.model.AbstractCheckBoxModel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Panel which displays list of experiments inside a package.
 *
 * @author Jakub Danek
 */
public class ExperimentPackageContentPanel extends Panel {

    private static final int EXPERIMENTS_PER_PAGE = 10;

    @SpringBean
    private ExperimentsFacade experimentsFacade;
    @SpringBean
    private ExperimentPackageFacade experimentPackageFacade;

    /**
     * Main model of the component - experiment package
     */
    private IModel<ExperimentPackage> epModel;
    /**
     * Set of selected experiments.
     */
    private Set<Experiment> selectedExperiments;

    private IModel<List<Experiment>> experimentsModel;
    
    //containers
    private WebMarkupContainer experimentListCont;
    private WebMarkupContainer header;
    private ModalWindow window;

    /**
     *
     * @param id component id
     * @param model model with the experiment package for which the info shall be displayed
     */
    public ExperimentPackageContentPanel(String id, IModel<ExperimentPackage> model) {
	super(id);

	this.epModel = model;
	this.selectedExperiments = new HashSet<Experiment>();
	
	experimentListCont = new WebMarkupContainer("experimentListCont");
	experimentListCont.setOutputMarkupPlaceholderTag(true);
	this.add(experimentListCont);

	this.addHeader();
	this.addExperimentListToCont(experimentListCont);
	this.addExperimentsAddWindow();
    }

    /**
     * Add window which allows to add experiments to the package.
     */
    private void addExperimentsAddWindow() {
	window = new ModalWindow("addExperimentsWindow");
	window.setAutoSize(true);
	window.setResizable(false);
	window.setMinimalWidth(600);
	window.setWidthUnit("px");

	List<Experiment> exps = this.listExperimentsToAdd();

	Panel content = new ExperimentListForm(window.getContentId(), ResourceUtils.getModel("pageTitle.addExperimenToPackage"), exps) {

	    @Override
	    protected void onSubmitAction(List<Experiment> selectedExperiments, AjaxRequestTarget target, Form<?> form) {
		experimentPackageFacade.addExperimentsToPackage(selectedExperiments, epModel.getObject());
		ModalWindow.closeCurrent(target);
		experimentsModel.detach();
		target.add(experimentListCont);
	    }

	    @Override
	    protected void onCancelAction(List<Experiment> selectedExperiments, AjaxRequestTarget target, Form<?> form) {
		ModalWindow.closeCurrent(target);
		target.add(experimentListCont);
	    }
	    
	};
	window.setContent(content);
	this.add(window);
    }

    /**
     *
     * @return list of experiments that can be added to the package
     */
    private List<Experiment> listExperimentsToAdd() {
	return experimentsFacade.getExperimentsWithoutPackage(epModel.getObject());
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
		window.show(target);
	    }
	};

	link.add(new Label("label", new StringResourceModel("button.add", this, null)));
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
		ExperimentPackageContentPanel.this.experimentListCont.setVisible(this.getModelObject());
		target.add(ExperimentPackageContentPanel.this.experimentListCont);
		target.add(header);
	    }

	    @Override
	    protected void onConfigure() {
		super.onConfigure();
		
		boolean contVisible = ExperimentPackageContentPanel.this.experimentListCont.isVisible();
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
