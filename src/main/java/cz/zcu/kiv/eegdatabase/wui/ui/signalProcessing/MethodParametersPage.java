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

import cz.zcu.kiv.eegdatabase.logic.signal.ChannelInfo;
import cz.zcu.kiv.eegdatabase.webservices.EDPClient.MethodParameters;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.core.signalProcessing.SignalProcessingService;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPageLeftMenu;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    private String methodName;
    private String dataName;

    public MethodParametersPage(PageParameters parameters) throws SQLException {

        methodName = parameters.get(PageParametersUtils.METHOD_NAME).toString();
        experimentId = parameters.get(BasePage.DEFAULT_PARAM_ID).toInt();
        dataName = parameters.get(PageParametersUtils.DATA).toString();
        Label label = new Label("methodName", methodName);
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));
        add(label);
        add(new MethodParametersForm("methodParametersForm", new Model<String>(), service, getFeedback()));
    }

    private class MethodParametersForm extends Form<String> {
        public MethodParametersForm(String id, IModel<String> model, final SignalProcessingService service,
                                    final FeedbackPanel feedback) throws SQLException {
            super(id, new CompoundPropertyModel<String>(model));
            List<ChannelInfo> infoList = service.getChannelInfo(experimentId, dataName);
            List<String> channels = new ArrayList<String>(infoList.size());
            for (ChannelInfo channel : infoList) {
                channels.add(channel.getName());
            }
            final DropDownChoice<String> channelList = new DropDownChoice<String>("channelList", new Model<String>(),
                    channels);
            List<MethodParameters> paramList = service.getMethodParameters(methodName);
            for (MethodParameters param: paramList) {
                if (param.getDescription().startsWith("Channel")) {
                    paramList.remove(param);
                    break;
                }
            }
            final ListView<MethodParameters> params = new ListView<MethodParameters>("params",
                    paramList) {
                @Override
                protected void populateItem(final ListItem<MethodParameters> components) {
                    final String paramName = components.getModelObject().getDescription();
                    Label label = new Label("paramName", paramName);
                    components.add(label);
                    TextField<String> field = new TextField<String>("text", String.class);
                    components.add(field);

                }
            };
            params.setReuseItems(true);

            SubmitLink submit = new SubmitLink("submit") {
                @Override
                public void onSubmit() {

                }
            };
            add(submit, channelList, params);
        }
    }
}
