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
 *   ExperimentsPageLeftMenu.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.IButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.page.UnderConstructPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.GrantedLicensesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.ManageLicenseRequestsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.RevokedRequestPage;
import cz.zcu.kiv.eegdatabase.wui.ui.pricing.PriceListPage;
import cz.zcu.kiv.eegdatabase.wui.ui.signalProcessing.ResultListPage;

import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Enumeration of left menu page for experiment section.
 * 
 * @author Jakub Rinkes
 *
 */
public enum ExperimentsPageLeftMenu implements IButtonPageMenu {

    LIST_OF_EXPERIMENTS(ListExperimentsByPackagePage.class, "menuItem.experiments.allExperiments", null),
    LIST_OF_EXPERIMENTS_AS_OWNER(ListExperimentsPage.class, "menuItem.experiments.myExperiments", PageParametersUtils.getDefaultPageParameters(ListExperimentsPage.PARAM_OWNER)),
    LIST_OF_EXPERIMENTS_AS_SUBJECT(ListExperimentsPage.class, "menuItem.experiments.meAsSubject", PageParametersUtils.getDefaultPageParameters(ListExperimentsPage.PARAM_SUBJECT)),
    MANAGE_EXPERIMENT_PACKAGES(ManageExperimentPackagesPage.class, "menuItem.experiments.packages", null),
	MANAGE_LICENSE_REQUESTS(ManageLicenseRequestsPage.class, "menuItem.experiments.licenses.requests", null),
	LIST_GRANTED_LICENSES(GrantedLicensesPage.class, "menuItem.experiments.licenses.granted", null),
	LIST_REVOKED_LICENSES(RevokedRequestPage.class, "menuItem.experiments.licenses.revoked", null),
    SEARCH(UnderConstructPage.class, "menuItem.searchMeasuration", null),
    ADD_EXPERIMENTS(ExperimentFormPage.class, "menuItem.experiments.addExperiment", null),
    RESULT(ResultListPage.class, "menuItem.serviceResult", null),
// XXX price list hidden for now.
    //    PRICELIST(PriceListPage.class, "pageTitle.pricelist", null),

    ;

    private Class<? extends MenuPage> pageClass;
    private String pageTitleKey;
    private PageParameters parameters;

    private ExperimentsPageLeftMenu(Class<? extends MenuPage> pageClass, String pageTitleKey, PageParameters parameters) {
        this.pageClass = pageClass;
        this.pageTitleKey = pageTitleKey;
        this.parameters = parameters;
    }

    @Override
    public Class<? extends MenuPage> getPageClass() {
        return pageClass;
    }

    @Override
    public String getPageTitleKey() {
        return pageTitleKey;
    }

    @Override
    public PageParameters getPageParameters() {
        return parameters;
    }

}
