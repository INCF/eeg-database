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
 * MethodListPage.java, 11. 12. 2013 10:08:47, Jan Stebetak
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.signalProcessing;

import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.core.signalProcessing.SignalProcessingService;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPageLeftMenu;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stebjan
 * Date: 11.12.13
 * Time: 13:13
 * To change this template use File | Settings | File Templates.
 */
public class MethodListPage extends MenuPage {

    @SpringBean
    SignalProcessingService service;
    private int experimentId;


    public MethodListPage(PageParameters parameters) {
        experimentId = parseParameters(parameters);
        add(new SelectServiceForm("selectServiceForm", new Model<String>(), service, getFeedback()));

        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        PropertyListView<String> methods = new PropertyListView<String>("methods", new ListModel<String>(new ArrayList<String>(service.getAvailableMethods()))) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<String> item) {
                final String method = item.getModelObject();
                item.add(new Label("name", method));

            }
        };
        add(methods);
        List<String> eegData = service.getSuitableHeaders(experimentId);
        if (eegData.isEmpty()) {
            eegData.add("No data Available");
        }
        PropertyListView<String> data = new PropertyListView<String>("data", new ListModel<String>
                (new ArrayList<String>(eegData))) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<String> item) {
                final String data = item.getModelObject();
                item.add(new Label("name", data));

            }
        };
        add(data);
    }

    private int parseParameters(PageParameters parameters) {

        StringValue value = parameters.get(BasePage.DEFAULT_PARAM_ID);
        if (value.isNull() || value.isEmpty())
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
        return value.toInt();
    }

    private class SelectServiceForm extends Form<String> {
        private SubmitLink submit;

        public SelectServiceForm(String id, IModel<String> model, final SignalProcessingService service, final FeedbackPanel feedback) {
            super(id, new CompoundPropertyModel<String>(model));
            final DropDownChoice<String> method = new DropDownChoice<String>("methodList", new Model<String>(),
                    service.getAvailableMethods());


            final DropDownChoice<String> data = new DropDownChoice<String>("dataList", new Model<String>(),
                    service.getSuitableHeaders(experimentId));


            submit = new SubmitLink("submit") {
                @Override
                public void onSubmit() {
                    PageParameters param = PageParametersUtils.getDefaultPageParameters(experimentId);
                    param = PageParametersUtils.addParameters(param, PageParametersUtils.METHOD_NAME,
                            method.getModel().getObject());
                    param = PageParametersUtils.addParameters(param, PageParametersUtils.DATA,
                            data.getModel().getObject());
                    setResponsePage(MethodParametersPage.class, param);
                }


            };
            method.setRequired(true);
            data.setRequired(true);
            add(method, data, submit);
        }

    }

}
