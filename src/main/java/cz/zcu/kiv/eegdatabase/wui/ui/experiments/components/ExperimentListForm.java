package cz.zcu.kiv.eegdatabase.wui.ui.experiments.components;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.wui.components.table.CheckBoxColumn;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampPropertyColumn;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ListExperimentsDataProvider;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.model.AbstractCheckBoxModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Panel with form for selecting from list of experiments.
 *
 * @author Jakub Danek
 */
public class ExperimentListForm extends Panel {

    private static final int EXPERIMENTS_PER_PAGE = 20;

    /**
     * Set of selected experiments.
     */
    private Set<Experiment> selectedExperiments;

    private WebMarkupContainer cont;

    /**
     *
     * @param id id of the component
     * @param titleModel title which is displayed at the top of the window
     * @param listOfExperiments list of experiments to choose from
     */
    public ExperimentListForm(String id, IModel<String> titleModel, List<Experiment> listOfExperiments) {
	super(id);

	this.selectedExperiments = new HashSet<Experiment>();

	this.cont = new StatelessForm("cont");
	this.cont.setOutputMarkupId(true);
	this.add(cont);

	this.add(new Label("title", titleModel));
	
	this.addExperimentList(cont, listOfExperiments);
	this.addControls(cont);
    }

    /**
     * Add view for the list of experiments to a container given
     * @param cont the container
     */
    private void addExperimentList(WebMarkupContainer cont, List<Experiment> listOfExperiments) {
	List<? extends IColumn<Experiment, String>> columns = this.createListColumns();

	DataTable<Experiment, String> list = new AjaxFallbackDefaultDataTable<Experiment, String>("list", columns,
						    new ListExperimentsDataProvider(listOfExperiments), EXPERIMENTS_PER_PAGE);

	cont.add(list);
    }

    /**
     * Add window controls - buttons, etc
     * @param cont
     */
    private void addControls(WebMarkupContainer cont) {
	AjaxButton button = new AjaxButton("submitButton", ResourceUtils.getModel("button.save")) {

	    @Override
	    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		onSubmitAction(new ArrayList<Experiment>(selectedExperiments), target, form);
		selectedExperiments.clear();
	    }
	};
	cont.add(button);

	button = new AjaxButton("cancelButton", ResourceUtils.getModel("button.cancel")) {

	    @Override
	    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		onCancelAction(new ArrayList<Experiment>(selectedExperiments), target, form);
		selectedExperiments.clear();
	    }

	};
	cont.add(button);
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

	return columns;
    }

    /**
     * Override this method to provide onSubmit action
     * @param selectedExperiments list of selected elements
     * @param target ajax target
     * @param form form of the window
     */
    protected void onSubmitAction(List<Experiment> selectedExperiments, AjaxRequestTarget target, Form<?> form) {

    }

    /**
     * Override this method to provide onCancel action
     * @param selectedExperiments list of selected elements
     * @param target ajax target
     * @param form form of the window
     */
    protected void onCancelAction(List<Experiment> selectedExperiments, AjaxRequestTarget target, Form<?> form) {

    }

}
