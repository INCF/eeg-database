package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.IModel;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.components.repeater.BasicDataProvider;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;

/**
 * Dataprovider implementation used in table on ListExperimentPage.
 * 
 * @author Jakub Rinkes
 *
 */
public class ListExperimentsDataProvider extends BasicDataProvider<Experiment> {

    /**
     * Specialized constructor which loads experiments based on given properties
     * @param facade ExperimentsFacade which will be used to load experiments
     * @param person person object used for filtering
     * @param owner search by isOwner (related to the person parameter)
     * @param subject  search by isSubject (related to the person parameter)
     */
    public ListExperimentsDataProvider(ExperimentsFacade facade, Person person, boolean owner, boolean subject) {
		super("experimentId", SortOrder.ASCENDING);
		List<Experiment> list;

		int size;
			if (owner) {
				size = facade.getCountForExperimentsWhereOwner(person);
				list = facade.getExperimentsWhereOwner(person, (int) 0, size);
			} else if (subject) {
				size = facade.getCountForExperimentsWhereSubject(person);
				list = facade.getExperimentsWhereSubject(person, (int) 0, size);
			} else {
				size = facade.getCountForAllExperimentsForUser(person);
				list = facade.getAllExperimentsForUser(person, (int) 0, size);
			}

		super.listModel.setObject(list);

    }

	/**
     * Constructor which wraps a list of experiments.
     * @param experiments
     */
    public ListExperimentsDataProvider(List<Experiment> experiments) {
		super(experiments, "experimentId", SortOrder.ASCENDING);
    }

    /**
     * Constructor which allows user to provide his own list model. Suitable
     * when you need to update the model from the outside (e.g. with ajax and
     * LoadableDetachableModel).
     * @param listModel
     */
    public ListExperimentsDataProvider(IModel<List<Experiment>> listModel) {
		super(listModel, "experimentId", SortOrder.ASCENDING);
    }

}
