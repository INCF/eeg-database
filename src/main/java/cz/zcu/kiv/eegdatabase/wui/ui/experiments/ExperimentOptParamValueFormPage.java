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
 *   ExperimentOptParamValueFormPage.html, 2014/12/16 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamValId;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.param.ExperimentsOptParamFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ExperimentOptParamValueFormPage extends MenuPage {

    private static final long serialVersionUID = -6878359066793272948L;

    @SpringBean
    private ExperimentsOptParamFacade facade;

    @SpringBean
    private ExperimentsFacade experimentFacade;

    @SpringBean
    private SecurityFacade securityFacade;

    public ExperimentOptParamValueFormPage(final PageParameters parameters) {

        setPageTitle(ResourceUtils.getModel("pageTitle.addExperimentOptionalParameter"));

        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));
        final Experiment experiment = experimentFacade.read(parseParameters(parameters));

        boolean allowed = true;
        if ((!securityFacade.userIsOwnerOrCoexperimenter(experiment.getExperimentId())) && (!securityFacade.isAdmin())) {
            warn(ResourceUtils.getString("error.mustBeOwnerOfExperimentOrCoexperimenter"));
            allowed = false;
        }

        final CompoundPropertyModel<ExperimentOptParamVal> model = new CompoundPropertyModel<ExperimentOptParamVal>(new ExperimentOptParamVal());
        final Form<ExperimentOptParamVal> form = new Form<ExperimentOptParamVal>("form");
        TextField<String> textField = new TextField<String>("paramValue");
        textField.setLabel(ResourceUtils.getModel("label.parameterValue"));
        textField.setRequired(true);
        form.add(textField);
        form.setOutputMarkupId(true);
        form.setModel(model);
        form.setVisibilityAllowed(allowed);

        List<ExperimentOptParamDef> paramList = facade.getRecordsByGroup(experiment.getResearchGroup().getResearchGroupId());
        ChoiceRenderer<ExperimentOptParamDef> renderer = new ChoiceRenderer<ExperimentOptParamDef>("paramName", "experimentOptParamDefId");
        final DropDownChoice<ExperimentOptParamDef> paramChoice = new DropDownChoice<ExperimentOptParamDef>("paramChoice",
                new Model<ExperimentOptParamDef>(), paramList, renderer);

        paramChoice.setLabel(ResourceUtils.getModel("label.parameterType"));
        paramChoice.add(new AjaxFormComponentUpdatingBehavior("OnChange") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {

                int experimentOptParamDefId = paramChoice.getModelObject().getExperimentOptParamDefId();
                ExperimentOptParamValId id = new ExperimentOptParamValId(experiment.getExperimentId(), experimentOptParamDefId);

                ExperimentOptParamVal entity = facade.read(id);
                form.setModelObject(entity == null ? new ExperimentOptParamVal() : entity);

                target.add(form);
            }
        });
        paramChoice.setRequired(true);
        form.add(paramChoice);

        form.add(new AjaxButton("submit", ResourceUtils.getModel("button.addExperimentOptionalParameter")) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(getFeedback());
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                ExperimentOptParamVal newValue = model.getObject();
                ExperimentOptParamDef experimentOptParamDef = paramChoice.getModelObject();
                ExperimentOptParamValId id = new ExperimentOptParamValId(experiment.getExperimentId(), experimentOptParamDef.getExperimentOptParamDefId());
                newValue.setExperiment(experiment);

                ExperimentOptParamVal val = facade.read(id);

                if (newValue.getId() == null) {
                    if (val != null) { // field already exists
                        error(ResourceUtils.getString("invalid.paramIdAlreadyInserted"));
                    } else {
                        newValue.setId(id);
                        facade.create(newValue);
                    }
                } else {
                    facade.update(newValue);
                }

                setResponsePage(ExperimentsDetailPage.class, parameters);
            }

        });

        add(form);
    }

    private int parseParameters(PageParameters parameters) {

        StringValue value = parameters.get(BasePage.DEFAULT_PARAM_ID);
        if (value.isNull() || value.isEmpty())
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
        return value.toInt();
    }
}
