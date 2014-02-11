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
 * ResultListPage.java, 11. 02. 2014 10:08:47, Jan Stebetak
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.signalProcessing;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPageLeftMenu;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

/**
 * Created by stebjan on 11.2.14.
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ResultListPage extends MenuPage {

    public ResultListPage() {
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));
    }
}
