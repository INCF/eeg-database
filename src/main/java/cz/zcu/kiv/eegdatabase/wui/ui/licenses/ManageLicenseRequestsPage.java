package cz.zcu.kiv.eegdatabase.wui.ui.licenses;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPageLeftMenu;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.components.LicenseRequestListPanel;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ListModelWithResearchGroupCriteria;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ResearchGroupSelectForm;
import java.util.List;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Jakub Danek
 */
@AuthorizeInstantiation(value = {"ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ManageLicenseRequestsPage extends MenuPage {

	@SpringBean
	private ResearchGroupFacade researchGroupFacade;
	@SpringBean
	private LicenseFacade licenseFacade;

	private ListModelWithResearchGroupCriteria<PersonalLicense> licenseModel;

	private WebMarkupContainer container;

	public ManageLicenseRequestsPage() {
		this.initializeModel();
		this.add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

		this.addContent();
	}

	private void initializeModel() {
		licenseModel = new ListModelWithResearchGroupCriteria<PersonalLicense>() {

			@Override
			protected List<PersonalLicense> loadList(ResearchGroup group) {
				return licenseFacade.getLicenseRequests(group);
			}
		};
	}

	   /**
     * Add all inner components to the page.
     */
    private void addContent() {
		container = new WebMarkupContainer("licenses");
		container.setOutputMarkupId(true);
		this.add(container);

		this.addResearchGroupSelector(container);
		container.add(new LicenseRequestListPanel("requestList", licenseModel));
    }

	/**
     * Add dropdown list with research groups for which user can manage license
	 * requests
     * @param dataContainer The component which displays list of requests. Must ouput
     * markupId.
     */
    private void addResearchGroupSelector(WebMarkupContainer dataContainer) {
		ListModel<ResearchGroup> choicesModel = loadUsersResearchGroups();
		this.add(new ResearchGroupSelectForm("rgSelect", licenseModel.getCriteriaModel(), choicesModel, dataContainer, false));
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
