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

/**
 * Panel which displays list of experiments inside a package.
 *
 * @author Jakub Danek
 */
public class ExperimentPackageContentPanel extends Panel {

    private static final int EXPERIMENTS_PER_PAGE = 10;

    @SpringBean
    private ExperimentsFacade experimentsFacade;

    /**
     * Main model of the component - experiment package
     */
    private IModel<ExperimentPackage> epModel;
    
    //containers
    private WebMarkupContainer experimentListCont;
    private WebMarkupContainer header;

    /**
     *
     * @param id component id
     * @param model model with the experiment package for which the info shall be displayed
     */
    public ExperimentPackageContentPanel(String id, IModel<ExperimentPackage> model) {
	super(id);

	this.epModel = model;
	
	experimentListCont = new WebMarkupContainer("experimentListCont");
	experimentListCont.setOutputMarkupPlaceholderTag(true);
	this.add(experimentListCont);

	this.addHeader();
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
	
	header.add(createVisibilityLink("showListLink", true));
	header.add(createVisibilityLink("hideListLink", false));
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
	List<Experiment> experiments = this.loadExperiments();
	List<? extends IColumn<Experiment, String>> columns = this.createListColumns();

	DataTable<Experiment, String> list = new AjaxFallbackDefaultDataTable<Experiment, String>("list", columns,
						    new ListExperimentsDataProvider(experiments), EXPERIMENTS_PER_PAGE);

	cont.add(list);
    }

    /**
     *
     * @return list of experiments to be displayed
     */
    private List<Experiment> loadExperiments() {
	return experimentsFacade.getExperimentsByPackage(epModel.getObject().getExperimentPackageId());
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
	columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.detail"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
                item.add(new ViewLinkPanel(componentId, ExperimentsDetailPage.class, "experimentId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });

	return columns;
    }

}
