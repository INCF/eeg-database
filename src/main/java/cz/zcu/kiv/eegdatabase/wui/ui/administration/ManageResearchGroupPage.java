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
 *   ManageResearchGroupPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.administration;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.components.ResearchGroupManagementForm;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ResearchGroupSelectForm;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Page for research group management, payments etc.
 *
 * @author Jakub Danek
 */
@AuthorizeInstantiation("ROLE_ADMIN")
public class ManageResearchGroupPage extends MenuPage {
	@SpringBean
	private ResearchGroupFacade researchGroupFacade;

	private ListModel<ResearchGroup> groupChoices;
	private IModel<ResearchGroup> selectedGroup;

	private WebMarkupContainer managementComp;

	public ManageResearchGroupPage() {
		setPageTitle(ResourceUtils.getModel("pageTitle.manageResearchGroup"));

		this.initializeModel();
		this.addComponents();
	}

	private void initializeModel() {
		groupChoices = this.loadGroups();
		selectedGroup = new Model<ResearchGroup>();
		if(!this.groupChoices.getObject().isEmpty()) {
			selectedGroup.setObject(this.groupChoices.getObject().get(0));
		}
	}

	private void addComponents() {
		this.add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()));


		managementComp = new ResearchGroupManagementForm("groupForm", selectedGroup) {

			@Override
			protected void onConfigure() {
				super.onConfigure();
				this.setVisible(selectedGroup.getObject() != null);
			}

		};
		managementComp.setOutputMarkupId(true);
		this.add(managementComp);

		this.add(new ResearchGroupSelectForm("groupSelect", selectedGroup, groupChoices, managementComp, false));
	}
	
	private ListModel<ResearchGroup> loadGroups() {
		return new ListModel<ResearchGroup>(researchGroupFacade.getAllRecords());
	}

}
