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
 *   HardwareForm.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.HardwareGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.HardwareGroupRelId;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.CoreConstants;
import cz.zcu.kiv.eegdatabase.wui.core.common.HardwareFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;

public class HardwareForm extends Form<Hardware> {

    private static final long serialVersionUID = 6096582661607302594L;

    @SpringBean
    private HardwareFacade hardwareFacade;

    @SpringBean
    private ResearchGroupFacade researchGroupFacade;

    public HardwareForm(String id, final ModalWindow window) {
        super(id, new CompoundPropertyModel<Hardware>(new Hardware()));

        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        add(new Label("addHWHeader", ResourceUtils.getModel("pageTitle.addHardwareDefinition")));

        ChoiceRenderer<ResearchGroup> renderer = new ChoiceRenderer<ResearchGroup>("title", "researchGroupId");
        List<ResearchGroup> choices = researchGroupFacade.getResearchGroupsWhereAbleToWriteInto(EEGDataBaseSession.get().getLoggedUser());
        final DropDownChoice<ResearchGroup> researchGroupChoice = new DropDownChoice<ResearchGroup>("researchGroup", new Model<ResearchGroup>(), choices, renderer);

        researchGroupChoice.setRequired(true);
        researchGroupChoice.setLabel(ResourceUtils.getModel("label.group"));
        add(researchGroupChoice);

        TextField<String> title = new TextField<String>("title");
        title.setRequired(true);
        title.setLabel(ResourceUtils.getModel("label.title"));
        add(title);

        TextField<String> type = new TextField<String>("type");
        type.setRequired(true);
        type.setLabel(ResourceUtils.getModel("label.type"));
        add(type);

        TextArea<String> description = new TextArea<String>("description");
        description.setRequired(true);
        description.setLabel(ResourceUtils.getModel("label.description"));
        add(description);

        add(new AjaxButton("submitForm", ResourceUtils.getModel("button.submitForm"), this) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                Hardware hardware = HardwareForm.this.getModelObject();

                int hardwareId = hardware.getHardwareId();
                ResearchGroup group = researchGroupChoice.getModelObject();
                int researchGroupId = group.getResearchGroupId();

                if (researchGroupId == CoreConstants.DEFAULT_ITEM_ID) {
                    if (!hardwareFacade.canSaveDefaultTitle(hardware.getTitle(), hardwareId)) {
                        error(ResourceUtils.getString("error.titleAlreadyInDatabase"));
                        return;
                    }
                } else {
                    if (!hardwareFacade.canSaveTitle(hardware.getTitle(), researchGroupId, hardwareId)) {
                        error(ResourceUtils.getString("error.titleAlreadyInDatabase"));
                        return;
                    }
                }

                // Creating new
                if (researchGroupId == CoreConstants.DEFAULT_ITEM_ID) {
                    hardwareFacade.createDefaultRecord(hardware);
                } else {
                    int pkHardware = hardwareFacade.create(hardware);

                    HardwareGroupRelId hardwareGroupRelId = new HardwareGroupRelId(pkHardware, researchGroupId);
                    HardwareGroupRel hardwareGroupRel = new HardwareGroupRel(hardwareGroupRelId, group, hardware);
                    hardwareFacade.createGroupRel(hardwareGroupRel);
                }
                
                window.close(target);
                
            }
            
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);
            }
            
        });

        add(new AjaxButton("closeForm", ResourceUtils.getModel("button.close"), this) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                window.close(target);
            }
        }.setDefaultFormProcessing(false));

        setOutputMarkupId(true);
    }
}
