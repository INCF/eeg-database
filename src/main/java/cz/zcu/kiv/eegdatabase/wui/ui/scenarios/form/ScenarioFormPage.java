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
 *   ScenarioFormPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.scenarios.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ListScenariosPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ScenariosPageLeftMenu;

/**
 * Page for add / edit action on scenario.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = {"ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ScenarioFormPage extends MenuPage {

    private static final long serialVersionUID = -7987971485930885797L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    ScenariosFacade scenarioFacade;

    @SpringBean
    SecurityFacade security;

    public ScenarioFormPage() {

        add(new ButtonPageMenu("leftMenu", ScenariosPageLeftMenu.values()));

        setPageTitle(ResourceUtils.getModel("pageTitle.addScenario"));

        add(new Label("title", ResourceUtils.getModel("pageTitle.addScenario")));

        ScenarioForm scenarioForm = new ScenarioForm("form", new Model<Scenario>(new Scenario()), getFeedback());
        add(scenarioForm);

        if (!security.userIsExperimenter() && !security.isAdmin()) {
            warn(ResourceUtils.getString("pageTitle.userNotExperimenter"));
            warn(ResourceUtils.getString("text.youNeedExperimenterRole"));
            scenarioForm.setVisibilityAllowed(false);
        }
    }

    public ScenarioFormPage(PageParameters params) {

        add(new ButtonPageMenu("leftMenu", ScenariosPageLeftMenu.values()));

        setPageTitle(ResourceUtils.getModel("pageTitle.editScenarioSchema"));

        add(new Label("title", ResourceUtils.getModel("pageTitle.editScenarioSchema")));

        int scenarioId = parseParameters(params);

        if (!security.userIsOwnerOfScenario(scenarioId) && !security.isAdmin())
            throw new RestartResponseAtInterceptPageException(ListScenariosPage.class);

        add(new ScenarioForm("form", new Model<Scenario>(scenarioFacade.read(scenarioId)), getFeedback()));
    }

    private int parseParameters(PageParameters parameters) {

        StringValue value = parameters.get(BasePage.DEFAULT_PARAM_ID);
        if (value.isNull() || value.isEmpty())
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
        return value.toInt();
    }
    
}
