package cz.zcu.kiv.eegdatabase.wui.ui.experiments.components;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.wui.components.repeater.CustomAjaxPagingNavigator;
import java.util.List;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Panel which displays list of experiment packages and experiments
 * they contain.
 *
 * @author Jakub Danek
 */
public class ExperimentPackageListPanel extends Panel {

    private static final int ITEMS_PER_PAGE = 10;

    /**
     * Main model - list of experiment packages
     */
    private IModel<List<ExperimentPackage>> epListModel;

	private final boolean managedMode;

    /**
	 * Constructor for displaying list of packages in view mode.
     *
     * @param id components id
     * @param model model with packages that shall be displayed
     */
    public ExperimentPackageListPanel(String id, IModel<List<ExperimentPackage>> model) {
		this(id, model, false);
    }

	/**
	 * 
	 * @param id
	 * @param model
	 * @param manageMode
	 */
	public ExperimentPackageListPanel(String id, IModel<List<ExperimentPackage>> model, boolean manageMode) {
		super(id);

		this.setOutputMarkupId(true);
		this.managedMode = manageMode;
		this.epListModel = model;
		this.addPackageList();
	}

    /**
     * Add view for the list of packages.
     */
    private void addPackageList() {
		PageableListView<ExperimentPackage> listView = new PageableListView<ExperimentPackage>("packageList", epListModel, ITEMS_PER_PAGE) {

			@Override
			protected void populateItem(ListItem<ExperimentPackage> item) {
				if(managedMode) {
					item.add(new ExperimentPackageManagePanel("item", item.getModel()));
				} else {
					item.add(new ExperimentPackagePanel("item", item.getModel()));
				}
			}
		};

		listView.setOutputMarkupId(true);

		add(listView);
		PagingNavigator navig = new PagingNavigator("navigation", listView);
		add(navig);
    }

}
