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

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ServiceResult;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.FileUtils;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.signalProcessing.SignalProcessingService;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPageLeftMenu;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Created by stebjan on 11.2.14.
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ResultListPage extends MenuPage {

    @SpringBean
    SignalProcessingService service;

    public ResultListPage() {
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        Person loggedPerson = service.getLoggedPerson();

        PropertyListView<ServiceResult> results = new PropertyListView<ServiceResult>
                ("results", new ListModel<ServiceResult>(new ArrayList<ServiceResult>
                (service.getResults(loggedPerson)))) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void populateItem(final ListItem<ServiceResult> item) {
                        final ServiceResult result = item.getModelObject();
                        item.add(new Label("title", result.getTitle()));
                        item.add(new Label("status", result.getStatus()));
                        item.add(new Label("filename", result.getFilename()));

                        item.add(new Link<Void>("download") {
                            @Override
                            public void onClick() {
                                ServiceResult result = item.getModelObject();
                                FileDTO outputFile = service.getResultFile(result.getServiceResultId());
                                getRequestCycle().scheduleRequestHandlerAfterCurrent(FileUtils.prepareDownloadFile(outputFile));
                            }
                        });
                        item.add(new AjaxLink<Void>("delete") {
                            @Override
                            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                                service.delete(item.getModelObject());
                                setResponsePage(ResultListPage.class);
                            }
                            @Override
                            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                                super.updateAjaxAttributes(attributes);

                                AjaxCallListener ajaxCallListener = new AjaxCallListener();
                                ajaxCallListener.onPrecondition("return confirm('Are you sure you want to delete item?');");
                                attributes.getAjaxCallListeners().add(ajaxCallListener);
                            }
                        });
                    }
                };
        add(results);
    }
}
