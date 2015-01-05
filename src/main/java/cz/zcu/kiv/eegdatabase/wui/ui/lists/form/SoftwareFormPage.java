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
 *   SoftwareFormPage.java, 2015/01/05 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.lists.form;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.wui.core.common.SoftwareFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListSoftwareDefinitionsPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.CoreConstants;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListsLeftPageMenu;

/**
 * Page add / edit action of software definitions.
 *
 * @author Honza
 *
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class SoftwareFormPage extends MenuPage {

    private static final long serialVersionUID = -761588925622549613L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    SoftwareFacade facade;

    @SpringBean
    ResearchGroupFacade groupFacade;

    public SoftwareFormPage(PageParameters parameters) {

        StringValue groupParam = parameters.get(PageParametersUtils.GROUP_PARAM);
        StringValue softwareParam = parameters.get(DEFAULT_PARAM_ID);

        if (groupParam.isNull() || groupParam.isEmpty())
            throw new RestartResponseAtInterceptPageException(ListSoftwareDefinitionsPage.class);

        add(new ButtonPageMenu("leftMenu", ListsLeftPageMenu.values()));

        if (softwareParam.isNull() || softwareParam.isEmpty())
            setupAddComponents(groupParam.toInt());
        else
            setupEditComponents(groupParam.toInt(), softwareParam.toInt());
    }

    private void setupAddComponents(int researchGroupId) {
        setPageTitle(ResourceUtils.getModel("pageTitle.addSoftwareDefinition"));
        add(new Label("title", ResourceUtils.getModel("pageTitle.addSoftwareDefinition")));

        Software newSoftware = new Software();

        add(new SoftwareForm("form", new CompoundPropertyModel<Software>(newSoftware), new Model<ResearchGroup>(getGroup(researchGroupId)), getFeedback(), facade));
    }

    private void setupEditComponents(int researchGroupId, int softwareId) {
        setPageTitle(ResourceUtils.getModel("pageTitle.editSoftwareDefinition"));
        add(new Label("title", ResourceUtils.getModel("pageTitle.editSoftwareDefinition")));

        add(new SoftwareForm("form", new CompoundPropertyModel<Software>(facade.read(softwareId)),
                new Model<ResearchGroup>(getGroup(researchGroupId)), getFeedback(), facade));
    }

    private ResearchGroup getGroup(int researchGroupId) {
        ResearchGroup group;
        if (researchGroupId == CoreConstants.DEFAULT_ITEM_ID)
            group = new ResearchGroup(CoreConstants.DEFAULT_ITEM_ID, EEGDataBaseSession.get().getLoggedUser(), ResourceUtils.getString("label.defaultSoftware"), "-");
        else
            group = groupFacade.read(researchGroupId);
        return group;
    }

    private class SoftwareForm extends Form<Software> {

        private static final long serialVersionUID = 1L;

        public SoftwareForm(String id, IModel<Software> model, final Model<ResearchGroup> groupModel,
                            final FeedbackPanel feedback, final SoftwareFacade facade) {
            super(id, model);

            TextField<String> group = new TextField<String>("group", new PropertyModel<String>(groupModel.getObject(), "title"));
            group.setEnabled(false);
            group.setLabel(ResourceUtils.getModel("label.researchGroup"));

            TextField<String> title = new TextField<String>("title");
            title.setLabel(ResourceUtils.getModel("label.title"));
            title.setRequired(true);


            TextArea<String> description = new TextArea<String>("description");
            description.setLabel(ResourceUtils.getModel("label.description"));
            description.setRequired(true);

            AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.save"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    this.setEnabled(false);
                    target.add(this);
                    target.add(feedback);

                    Software software = SoftwareForm.this.getModelObject();

                    int softwareId = software.getSoftwareId();
                    ResearchGroup group = groupModel.getObject();
                    int researchGroupId = group.getResearchGroupId();

                    if (researchGroupId == CoreConstants.DEFAULT_ITEM_ID) {
                        if (!facade.canSaveDefaultTitle(software.getTitle(), softwareId)) {
                            getFeedback().error(ResourceUtils.getString("error.valueAlreadyInDatabase"));
                            return;
                        }
                    } else {
                        if (!facade.canSaveTitle(software.getTitle(), researchGroupId, softwareId)) {
                            getFeedback().error(ResourceUtils.getString("error.valueAlreadyInDatabase"));
                            return;
                        }
                    }

                    if (softwareId > 0) {
                        // Editing one
                        log.debug("Editing existing software object.");

                        facade.update(software);
                    } else {

                        // Creating new
                        if (researchGroupId == CoreConstants.DEFAULT_ITEM_ID) {
                            log.debug("Creating new default software object.");
                            facade.createDefaultRecord(software);
                        } else {
                            log.debug("Creating new group software object.");
                            facade.createGroupRel(software, group);
                            facade.create(software);


//                            Ha hardwareGroupRelId = new HardwareGroupRelId(pkHardware, researchGroupId);
//                            HardwareGroupRel hardwareGroupRel = new HardwareGroupRel(hardwareGroupRelId, group, software);
//                            facade.createGroupRel(hardwareGroupRel);
                        }

                    }

                    setResponsePage(ListSoftwareDefinitionsPage.class);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }
            };

            add(submit, group, description, title);
        }

    }
}
