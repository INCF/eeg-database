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
 * ScenarioDownloadLink.java, 2013/10/02 00:01 Jakub Rinkes
 * ****************************************************************************
 */
package cz.zcu.kiv.eegdatabase.wui.components.table;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.FileUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.file.DataFileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenarioXMLProvider;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

/**
 * Panel with link and security policy. Prepared download scenario file. Added
 * in table with scenarios.
 *
 * @author Jakub Rinkes
 *
 */
public class ScenarioDownloadLink extends Panel {

	private static final long serialVersionUID = 1L;
	@SpringBean
	ScenariosFacade scenariosFacade;

	public ScenarioDownloadLink(String id, IModel<Scenario> model) {
		super(id);

		final Scenario scenario = model.getObject();
		// added link for download
		boolean fileExist = scenario.getScenarioFile() != null;
		add(new Link<Void>("download") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				AbstractResourceStreamWriter stream = new AbstractResourceStreamWriter() {
					private static final long serialVersionUID = 1L;

					@Override
					public void write(OutputStream output) throws IOException {
						output.write(scenariosFacade.getScenarioFile(scenario.getScenarioId()));
					}
				};

				getRequestCycle().scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(stream).setFileName(scenario.getScenarioName()));

			}
		}).setVisibilityAllowed(fileExist);
		add(new Label("noFile", ResourceUtils.getModel("label.notAvailable")).setVisibilityAllowed(!fileExist));
	}
}
