/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   GrantedLicensesPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.ui.licenses;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPageLeftMenu;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.components.PersonalLicenseListPanel;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ListModelWithResearchGroupCriteria;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ResearchGroupSelectForm;
import java.util.List;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Page with list of granted licenses (filtered by group)
 *
 * @author Jakub Danek
 */
@AuthorizeInstantiation(value = {"ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class GrantedLicensesPage extends MenuPage {
	@SpringBean
	private ResearchGroupFacade researchGroupFacade;
	@SpringBean
	private LicenseFacade licenseFacade;

	private ListModelWithResearchGroupCriteria<PersonalLicense> licenseModel;

	private WebMarkupContainer container;

	public GrantedLicensesPage() {
		this.initializeModel();
		this.add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

		this.addContent();
	}

	private void initializeModel() {
		licenseModel = new ListModelWithResearchGroupCriteria<PersonalLicense>() {

			@Override
			protected List<PersonalLicense> loadList(ResearchGroup group) {
				return licenseFacade.getGrantedLicenses(group);
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
		container.add(new PersonalLicenseListPanel("licenseList", licenseModel));
    }

	/**
     * Add dropdown list with research groups for which user can see granted
	 * licenses
     * @param dataContainer The component which displays list of licenses. Must ouput
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
