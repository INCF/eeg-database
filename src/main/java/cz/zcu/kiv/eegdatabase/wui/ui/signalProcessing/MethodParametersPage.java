/***********************************************************************************************************************
 *
 * This file is part of the EEG-database project
 *
 * =============================================
 *
 * Copyright (C) 2014 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * MethodListPage.java, 13. 1. 2014 13:45:47, Jan Stebetak
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.signalProcessing;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.core.signalProcessing.SignalProcessingService;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPageLeftMenu;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created with IntelliJ IDEA.
 * User: stebjan
 * Date: 13.1.14
 * Time: 13:45
 * To change this template use File | Settings | File Templates.
 */
public class MethodParametersPage extends MenuPage {
    @SpringBean
    SignalProcessingService service;
    private int experimentId;

    public MethodParametersPage(PageParameters parameters) {

        System.out.println(parameters.get(PageParametersUtils.METHOD_NAME));
        Label label = new Label("methodName", parameters.get(PageParametersUtils.METHOD_NAME));
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));
        add(label);
    }
}
