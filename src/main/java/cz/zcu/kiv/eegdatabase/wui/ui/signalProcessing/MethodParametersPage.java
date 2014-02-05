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
 * http://www.apache.org/licenses/LICENSE-2.0
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
import cz.zcu.kiv.eegdatabase.webservices.EDPClient.DataFile;
import cz.zcu.kiv.eegdatabase.webservices.EDPClient.MethodParameters;
import cz.zcu.kiv.eegdatabase.webservices.EDPClient.SupportedFormat;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.FileUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.core.file.DataFileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.signalProcessing.SignalProcessingService;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPageLeftMenu;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    protected Log log = LogFactory.getLog(getClass());
    @SpringBean
    SignalProcessingService service;
    private int experimentId;
    private String methodName;
    private String dataName;
    private static final int CHANNEL_POSITION = 2;

    public MethodParametersPage(PageParameters parameters) throws SQLException {

        methodName = parameters.get(PageParametersUtils.METHOD_NAME).toString();
        experimentId = parameters.get(BasePage.DEFAULT_PARAM_ID).toInt();
        dataName = parameters.get(PageParametersUtils.DATA).toString();
        Label label = new Label("methodName", methodName);
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));
        add(label);
        add(new MethodParametersForm("methodParametersForm", new Model(), service, getFeedback()));
    }

    private class MethodParametersForm extends Form<List<String>> {
        public MethodParametersForm(String id, IModel<List<String>> model, final SignalProcessingService service,
                                    final FeedbackPanel feedback) throws SQLException {
            super(id, new CompoundPropertyModel<List<String>>(model));
            List<ChannelInfo> infoList = service.getChannelInfo(experimentId, dataName);
            List<String> channels = new ArrayList<String>(infoList.size());
            for (ChannelInfo channel : infoList) {
                channels.add(channel.getName());
            }
            final DropDownChoice<String> channelList = new DropDownChoice<String>("channelList", new Model<String>(),
                    channels);
            List<MethodParameters> paramList = service.getMethodParameters(methodName);
            List<String> paramListToString = new ArrayList<String>(paramList.size());
            for (MethodParameters param : paramList) {
                if (!param.getDescription().startsWith("Channel")) {
                    paramListToString.add(param.getDescription());

                }
            }
            final ListView<String> params = new ListView<String>("params",
                    paramListToString) {
                @Override
                protected void populateItem(final ListItem<String> components) {

                    Label label = new Label("paramName", components.getModelObject());
                    components.add(label);
                    TextField<String> field = new TextField<String>("text", components.getModel());
                    field.setDefaultModelObject("");
                    if (label.getDefaultModelObjectAsString().equals("From sample")) {
                        field.setDefaultModelObject("1");
                    }
                    components.add(field);

                }
            };
            params.setReuseItems(true);

            SubmitLink submit = new SubmitLink("submit") {
                @Override
                public void onSubmit() {
                    List<String> test = (List<String>) params.getList();
                    if (test == null) {
                        log.error("no parameters are found.");
                    } else {
                        test.add(CHANNEL_POSITION, channelList.getModelObject());
                    }
                    List<DataFile> files = service.getDataFiles(experimentId, dataName);
                    String output = new String(service.processService(files, SupportedFormat.KIV_FORMAT, methodName, test));
                    DataFileDTO outputFile = new DataFileDTO();
                    outputFile.setFileName(methodName + "_result.xml");
                    outputFile.setFileContent(output.getBytes());

                    getRequestCycle().scheduleRequestHandlerAfterCurrent(FileUtils.prepareDownloadFile(outputFile));
                }
            };
            channelList.setRequired(true);
            add(submit, channelList, params);
        }
    }
}