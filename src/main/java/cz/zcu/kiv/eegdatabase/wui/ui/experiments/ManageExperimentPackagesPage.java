package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.components.ExperimentPackageListPanel;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ListModelWithResearchGroupCriteria;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ResearchGroupSelectForm;
import java.util.List;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Page for experiment package management. Adding/removing experiments to/from
 * packages. Adding/removing experiments.
 *
 * @author Jakub Danek
 */
@AuthorizeInstantiation(value = {"ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ManageExperimentPackagesPage extends MenuPage {

    @SpringBean
    private ResearchGroupFacade researchGroupFacade;
    @SpringBean
    private ExperimentPackageFacade experimentPackageFacade;

    /**
     * Model of package list for the selected research group.
     */
    private ListModelWithResearchGroupCriteria<ExperimentPackage> packagesModel;

    /**
     * Default constructor
     */
    public ManageExperimentPackagesPage() {
	this.initializeModel();

	this.add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

	this.addContent();
    }

    /**
     * Initialize components model.
     */
    private void initializeModel() {
	packagesModel = new ListModelWithResearchGroupCriteria<ExperimentPackage>() {

	    @Override
	    protected List<ExperimentPackage> loadList(ResearchGroup group) {
		return experimentPackageFacade.listExperimentPackagesByGroup(group.getResearchGroupId());
	    }
	};
    }

    /**
     * Add all inner components to the page.
     */
    private void addContent() {
	WebMarkupContainer packageList = new WebMarkupContainer("packages");
	packageList.setOutputMarkupId(true);
	this.add(packageList);

	this.addResearchGroupSelector(packageList);
	packageList.add(new ExperimentPackageListPanel("packageList", packagesModel));
    }

    /**
     * Add dropdown list with research groups for which user can modify package
     * settings.
     * @param dataContainer The component which displays list of packages. Must ouput
     * markupId.
     */
    private void addResearchGroupSelector(WebMarkupContainer dataContainer) {
	ListModel<ResearchGroup> choicesModel = loadUsersResearchGroups();
	this.add(new ResearchGroupSelectForm("rgSelect", packagesModel.getCriteriaModel(), choicesModel, dataContainer, false));
    }

    /**
     *
     * @return list of research groups for which user can edit packages
     */
    private ListModel<ResearchGroup> loadUsersResearchGroups() {
	List<ResearchGroup> groups = researchGroupFacade.getResearchGroupsWhereAbleToWriteInto(EEGDataBaseSession.get().getLoggedUser());
	return new ListModel<ResearchGroup>(groups);
    }
}
