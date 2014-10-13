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
 *   FileMetadataFormPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.lists.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDefGroupRelId;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.CoreConstants;
import cz.zcu.kiv.eegdatabase.wui.core.file.metadata.FileMetadataParamFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListFileMetadataPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListsLeftPageMenu;

/**
 * Page add / edit action of file metadata parameters.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class FileMetadataFormPage extends MenuPage {

    private static final long serialVersionUID = 6771143769833304954L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    FileMetadataParamFacade facade;

    @SpringBean
    ResearchGroupFacade groupFacade;

    public FileMetadataFormPage(PageParameters parameters) {

        StringValue groupParam = parameters.get(PageParametersUtils.GROUP_PARAM);
        StringValue fileMetadataParam = parameters.get(DEFAULT_PARAM_ID);

        if (groupParam.isNull() || groupParam.isEmpty())
            throw new RestartResponseAtInterceptPageException(ListFileMetadataPage.class);

        add(new ButtonPageMenu("leftMenu", ListsLeftPageMenu.values()));

        if (fileMetadataParam.isNull() || fileMetadataParam.isEmpty())
            setupAddComponents(groupParam.toInt());
        else
            setupEditComponents(groupParam.toInt(), fileMetadataParam.toInt());
    }

    private void setupAddComponents(int researchGroupId) {
        setPageTitle(ResourceUtils.getModel("pageTitle.addFileMetadataDefinition"));
        add(new Label("title", ResourceUtils.getModel("pageTitle.addFileMetadataDefinition")));

        FileMetadataParamDef fileMetadata = new FileMetadataParamDef();

        add(new FileMetadataForm("form", new CompoundPropertyModel<FileMetadataParamDef>(fileMetadata), new Model<ResearchGroup>(getGroup(researchGroupId)), getFeedback(), facade));
    }

    private void setupEditComponents(int researchGroupId, int fileMetadataId) {
        setPageTitle(ResourceUtils.getModel("pageTitle.editFileMetadataDefinition"));
        add(new Label("title", ResourceUtils.getModel("pageTitle.editFileMetadataDefinition")));

        add(new FileMetadataForm("form", new CompoundPropertyModel<FileMetadataParamDef>(facade.read(fileMetadataId)),
                new Model<ResearchGroup>(getGroup(researchGroupId)), getFeedback(), facade));
    }

    private ResearchGroup getGroup(int researchGroupId) {
        ResearchGroup group;
        if (researchGroupId == CoreConstants.DEFAULT_ITEM_ID)
            group = new ResearchGroup(CoreConstants.DEFAULT_ITEM_ID, EEGDataBaseSession.get().getLoggedUser(), ResourceUtils.getString("label.defaultFileMetadataParamDef"), "-");
        else
            group = groupFacade.read(researchGroupId);
        return group;
    }

    private class FileMetadataForm extends Form<FileMetadataParamDef> {

        private static final long serialVersionUID = 1L;

        public FileMetadataForm(String id, IModel<FileMetadataParamDef> model, final Model<ResearchGroup> groupModel,
                final FeedbackPanel feedback, final FileMetadataParamFacade facade) {
            super(id, model);

            TextField<String> group = new TextField<String>("group", new PropertyModel<String>(groupModel.getObject(), "title"));
            group.setEnabled(false);
            group.setLabel(ResourceUtils.getModel("label.researchGroup"));

            TextField<String> paramName = new TextField<String>("paramName");
            paramName.setLabel(ResourceUtils.getModel("label.parameterName"));
            paramName.setRequired(true);

            TextField<String> paramDataType = new TextField<String>("paramDataType");
            paramDataType.setLabel(ResourceUtils.getModel("label.parameterDataType"));
            paramDataType.setRequired(true);

            AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.save"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    this.setEnabled(false);
                    target.add(this);
                    target.add(feedback);
                    
                    FileMetadataParamDef fileMetadata = FileMetadataForm.this.getModelObject();

                    int fileMetadataParamDefId = fileMetadata.getFileMetadataParamDefId();
                    ResearchGroup group = groupModel.getObject();
                    int researchGroupId = group.getResearchGroupId();

                    if (fileMetadataParamDefId > 0) {
                        // Editing
                        log.debug("Editing existing fileMetadataParamDef object.");
                        facade.update(fileMetadata);
//                        if (facade.isDefault(fileMetadataParamDefId)) {
//
//                            if (researchGroupId != CoreConstants.DEFAULT_ITEM_ID) {
//                                // new fileMetadataParamDef
//                                FileMetadataParamDef newFileMetadataParamDef = new FileMetadataParamDef();
//                                newFileMetadataParamDef.setDefaultNumber(0);
//                                newFileMetadataParamDef.setParamName(fileMetadata.getParamName());
//                                newFileMetadataParamDef.setParamDataType(fileMetadata.getParamDataType());
//                                int newId = facade.create(newFileMetadataParamDef);
//                                FileMetadataParamDefGroupRel rel = facade.getGroupRel(fileMetadataParamDefId, researchGroupId);
//                                // delete old rel, create new one
//                                FileMetadataParamDefGroupRelId newRelId = new FileMetadataParamDefGroupRelId();
//                                FileMetadataParamDefGroupRel newRel = new FileMetadataParamDefGroupRel();
//                                newRelId.setFileMetadataParamDefId(newId);
//                                newRelId.setResearchGroupId(researchGroupId);
//                                newRel.setId(newRelId);
//                                newRel.setFileMetadataParamDef(newFileMetadataParamDef);
//                                newRel.setResearchGroup(group);
//                                facade.deleteGroupRel(rel);
//                                facade.createGroupRel(newRel);
//                            } else {
//                                if (!facade.hasGroupRel(fileMetadataParamDefId) && facade.canDelete(fileMetadataParamDefId)) {
//                                    facade.update(fileMetadata);
//                                } else {
//                                    getFeedback().error(ResourceUtils.getString("text.itemInUse"));
//                                    this.setEnabled(true);
//                                    return;
//                                }
//                            }
//                        } else {

//                        }
                    } else {

                        // Creating new
                        if (researchGroupId == CoreConstants.DEFAULT_ITEM_ID) {
                            log.debug("Creating new default fileMetadataParamDef object.");
                            facade.createDefaultRecord(fileMetadata);
                        } else {
                            log.debug("Creating new group fileMetadataParamDef object.");
                            int pkFileMetadataParamDef = facade.create(fileMetadata);

                            FileMetadataParamDefGroupRelId fileMetadataParamDefGroupRelId = new FileMetadataParamDefGroupRelId(pkFileMetadataParamDef, researchGroupId);
                            FileMetadataParamDefGroupRel fileMetadataParamDefGroupRel = new FileMetadataParamDefGroupRel(fileMetadataParamDefGroupRelId, group, fileMetadata);
                            facade.createGroupRel(fileMetadataParamDefGroupRel);
                        }

                    }

                    setResponsePage(ListFileMetadataPage.class);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }
            };

            add(submit, group, paramName, paramDataType);
        }

    }
}
