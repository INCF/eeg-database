package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.components.ExperimentPackageListPanel;
import java.util.List;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Page for listing experiments in package view.
 *
 * @author Jakub Danek
 */
@AuthorizeInstantiation(value = {"ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ListExperimentsByPackagePage extends MenuPage {

    @SpringBean
    private ExperimentPackageFacade experimentPackageFacade;

	private WebMarkupContainer container;

    /**
     * Model of package list for the selected research group.
     */
    private IModel<List<ExperimentPackage>> packagesModel;

    /**
     * Default constructor
     */
    public ListExperimentsByPackagePage() {
		this.initializeModel();

		this.add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

		this.addContent();
    }

    /**
     * Initialize components model.
     */
    private void initializeModel() {
		packagesModel = new LoadableDetachableModel<List<ExperimentPackage>>() {

			@Override
			protected List<ExperimentPackage> load() {
				List<ExperimentPackage> pckgs = experimentPackageFacade.listVisiblePackages(EEGDataBaseSession.get().getLoggedUser());
				ExperimentPackage noPackage = new ExperimentPackage();
				noPackage.setName(getString("title.package.nopackage"));
				pckgs.add(0, noPackage);
				return pckgs;
			}
		};
    }

    /**
     * Add all inner components to the page.
     */
    private void addContent() {
		container = new WebMarkupContainer("packages");
		container.setOutputMarkupId(true);
		this.add(container);

		container.add(new ExperimentPackageListPanel("packageList", packagesModel));
    }

}
