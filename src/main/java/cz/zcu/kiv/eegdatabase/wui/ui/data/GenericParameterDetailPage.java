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
 *   DataFileDetailPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.data;

import cz.zcu.kiv.eegdatabase.data.nosql.entities.GenericParameter;
import cz.zcu.kiv.eegdatabase.data.nosql.entities.ParameterAttribute;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.IButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPageLeftMenu;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

/**
 * Page of generic parameter detail.
 * 
 * @author Jan Krupička
 *
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class GenericParameterDetailPage extends MenuPage {
    public static final String PARAM_EXPERIMENT_ID = "experimentId";
    public static final String PARAM_PARAMETER_NAME = "parameterName";

    private static final long serialVersionUID = 2432720497396689233L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    ExperimentsFacade experimentFacade;

    @SpringBean
    SecurityFacade security;


    public GenericParameterDetailPage(PageParameters parameters) {

        validateParameters(parameters);

        setupComponents(parameters.get(PARAM_EXPERIMENT_ID).toInt(), parameters.get(PARAM_PARAMETER_NAME).toString());

    }

    private void setupComponents(int experimentId, String parameterName) {

        setPageTitle(ResourceUtils.getModel("pageTitle.experimentDetail"));

        add(new ButtonPageMenu("leftMenu", getLeftMenu()));

        GenericParameter parameter = retrieveGenericParameter(experimentId, parameterName);

        add(new Label("genericParameterName", new PropertyModel<GenericParameter>(parameter, "name")));
        add(new Label("genericParameterValueString", new PropertyModel<GenericParameter>(parameter, "valueString")));
        add(new Label("genericParameterValueInteger", new PropertyModel<GenericParameter>(parameter, "valueInteger")));
        add(new Label("genericParameterAttributeCount", new PropertyModel<GenericParameter>(parameter, "attributes.size")));

        // list of attributes
        PropertyListView<ParameterAttribute> attributes = new PropertyListView<ParameterAttribute>("attributes",
                new ListModel<ParameterAttribute>(parameter.getAttributes())) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<ParameterAttribute> item) {
                item.add(new Label("attributeName", item.getModelObject().getName()));
                item.add(new Label("attributeValue", item.getModelObject().getValue()));
            }

            @Override
            public boolean isVisible() {
                return !getModelObject().isEmpty();
            }
        };
        add(attributes);

        add(new BookmarkablePageLink<Void>("backLink", ExperimentsDetailPage.class, PageParametersUtils.getDefaultPageParameters(experimentId)));
    }

    private GenericParameter retrieveGenericParameter(int experimentId, String parameterName) {
        Experiment experiment = experimentFacade.getExperimentForDetail(experimentId);
        if(experiment == null) {
            log.error("Experiment not found: experimentID=" + experimentId);
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
        }

        for(GenericParameter parameter : experiment.getGenericParameters()) {
            if(parameter.getName().equals(parameterName))
                return parameter;
        }

        log.error("Generic parameter not found: experimentID=" + experimentId + ", parameterName=" + parameterName);
        throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
    }

    private IButtonPageMenu[] getLeftMenu() {
        return new IButtonPageMenu[] {
                ExperimentsPageLeftMenu.LIST_OF_EXPERIMENTS,
                ExperimentsPageLeftMenu.LIST_OF_EXPERIMENTS_AS_OWNER,
                ExperimentsPageLeftMenu.LIST_OF_EXPERIMENTS_AS_SUBJECT,
                ExperimentsPageLeftMenu.SEARCH,
                ExperimentsPageLeftMenu.ADD_EXPERIMENTS };
    }

    private void validateParameters(PageParameters parameters) {
        StringValue experimentId = parameters.get(PARAM_EXPERIMENT_ID);
        if (experimentId.isNull() || experimentId.isEmpty())
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());

        StringValue parameterName = parameters.get(PARAM_PARAMETER_NAME);
        if (parameterName.isNull() || parameterName.isEmpty())
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
    }
}
