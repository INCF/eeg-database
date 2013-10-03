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
 *   ManageExperimentPackagesPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.components.ExperimentPackageDetailPanel;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.components.ExperimentPackageListPanel;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ListModelWithResearchGroupCriteria;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ResearchGroupSelectForm;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
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

	private boolean editMode = false;
	private WebMarkupContainer container;

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
		this.addControls();
    }

    /**
     * Initialize components model.
     */
    private void initializeModel() {
		packagesModel = new ListModelWithResearchGroupCriteria<ExperimentPackage>() {

			@Override
			protected List<ExperimentPackage> loadList(ResearchGroup group) {
				return experimentPackageFacade.listExperimentPackagesByGroup(group);
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

		this.addResearchGroupSelector(container);
		container.add(new ExperimentPackageListPanel("packageList", packagesModel, true) {

			@Override
			protected void onConfigure() {
				super.onConfigure();
				this.setVisible(!editMode);
			}

		});

		container.add(new ExperimentPackageDetailPanel("packageDetail", packagesModel.getCriteriaModel()){

			@Override
			protected void onSubmitAction(AjaxRequestTarget target, Form<?> form) {
				super.onSubmitAction(target, form);
				ManageExperimentPackagesPage.this.editMode = false;
				setResponsePage(ManageExperimentPackagesPage.this);
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				this.setVisible(editMode);
			}

		});
    }

	private void addControls() {
		Link addLink = new Link("addLink") {

			@Override
			public void onClick() {
				ManageExperimentPackagesPage.this.editMode = true;
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				this.setVisible(!editMode && packagesModel.getCriteriaModel().getObject() != null);
			}

		};
		this.add(addLink);

		Link listLink = new Link("listLink") {

			@Override
			public void onClick() {
				ManageExperimentPackagesPage.this.editMode = false;
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				this.setVisible(editMode);
			}

		};
		this.add(listLink);
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
