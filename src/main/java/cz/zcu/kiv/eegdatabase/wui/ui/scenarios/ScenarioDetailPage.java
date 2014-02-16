/**
 * *****************************************************************************
 * This file is part of the EEG-database project
 *
 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 *  ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *  ***********************************************************************************************************************
 *
 * ScenarioDetailPage.java, 2013/10/02 00:01 Jakub Rinkes
 * ****************************************************************************
 */
package cz.zcu.kiv.eegdatabase.wui.ui.scenarios;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.FileUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenarioXMLProvider;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.form.ScenarioFormPage;

/**
 * Page of detail for scenario.
 *
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = {"ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN"})
public class ScenarioDetailPage extends MenuPage {

	private static final long serialVersionUID = 1L;
	protected Log log = LogFactory.getLog(getClass());
	@SpringBean
	ScenariosFacade scenarioFacade;
	@SpringBean
	ScenarioXMLProvider xmlProvider;
	@SpringBean
	SecurityFacade security;

	public ScenarioDetailPage(PageParameters params) {

		int scenarioId = parseParameters(params);

		setupComponents(scenarioId);
	}

	private void setupComponents(final int scenarioId) {

		setPageTitle(ResourceUtils.getModel("pageTitle.scenarioDetail"));

		add(new ButtonPageMenu("leftMenu", ScenariosPageLeftMenu.values()));

		final Scenario scenario = scenarioFacade.read(scenarioId);

		add(new Label("title", scenario.getTitle()));
		add(new Label("description", scenario.getDescription()));
		add(new Label("length", scenario.getScenarioLength() + ""));
		add(new Label("private", scenario.isPrivateScenario() + ""));

		boolean existFile = scenario.getScenarioFile() != null;

		add(new Link<Void>("download") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {

			    FileDTO file = scenarioFacade.getScenarioFile(scenario.getScenarioId());

                getRequestCycle().scheduleRequestHandlerAfterCurrent(FileUtils.prepareDownloadFile(file));

			}
		}.setVisibilityAllowed(existFile));

		add(new Label("noFile", ResourceUtils.getModel("scenarioDetail.noFile")).setVisibilityAllowed(!existFile));

		BookmarkablePageLink<Void> editLink = new BookmarkablePageLink<Void>("edit", ScenarioFormPage.class, PageParametersUtils.getDefaultPageParameters(scenarioId));

		boolean isOwnerOrAdmin = (security.userIsOwnerOfScenario(scenarioId) || security.isAdmin());
		editLink.setVisibilityAllowed(isOwnerOrAdmin);
		add(editLink);
	}

	private int parseParameters(PageParameters parameters) {

		StringValue value = parameters.get(BasePage.DEFAULT_PARAM_ID);
		if (value.isNull() || value.isEmpty()) {
			throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
		}
		return value.toInt();
	}
}
