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
 *   ListSoftwareDefinitionsPage.java, 2015/01/05 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.lists;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.wui.core.common.SoftwareFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.form.SoftwareFormPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.CoreConstants;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ListModelWithResearchGroupCriteria;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ResearchGroupSelectForm;


/**
 * Page with list of software definitions.
 *
 * @author Honza
 *
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ListSoftwareDefinitionsPage extends MenuPage {

    private static final long serialVersionUID = 1429684247549031445L;

    @SpringBean
    SoftwareFacade facade;

    @SpringBean
    ResearchGroupFacade researchGroupFacade;

    @SpringBean
    SecurityFacade security;

    public ListSoftwareDefinitionsPage() {

        setupComponents();
    }

    private void setupComponents() {

        setPageTitle(ResourceUtils.getModel("pageTitle.softwareList"));
        add(new ButtonPageMenu("leftMenu", ListsLeftPageMenu.values()));

        final WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        container.setOutputMarkupPlaceholderTag(true);

        final ListModelWithResearchGroupCriteria<Software> model = new ListModelWithResearchGroupCriteria<Software>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected List<Software> loadList(ResearchGroup group) {
                if (group == null  || group.getResearchGroupId() == CoreConstants.DEFAULT_ITEM_ID)
                    return facade.getDefaultRecords();
                else {
                    return facade.getRecordsByGroup(group.getResearchGroupId());
                }
            }
        };

        List<ResearchGroup> groups;
        final boolean isAdmin = security.isAdmin();
        final boolean isExperimenter = security.userIsExperimenter();
        if (isAdmin) {
            ResearchGroup defaultGroup = new ResearchGroup(CoreConstants.DEFAULT_ITEM_ID,
                    EEGDataBaseSession.get().getLoggedUser(), ResourceUtils.getString("label.defaultSoftware"), "-");
            groups = researchGroupFacade.getAllRecords();
            groups.add(0, defaultGroup);
        } else
            groups = researchGroupFacade.getResearchGroupsWhereMember(EEGDataBaseSession.get().getLoggedUser());

        PropertyListView<Software> software = new PropertyListView<Software>("software", model) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<Software> item) {
                item.add(new Label("softwareId"));
                item.add(new Label("title"));
                item.add(new Label("description"));

                PageParameters parameters = PageParametersUtils.getDefaultPageParameters(item.getModelObject().getSoftwareId())
                        .add(PageParametersUtils.GROUP_PARAM,
                                (model.getCriteriaModel().getObject() == null) ? CoreConstants.DEFAULT_ITEM_ID : model.getCriteriaModel().getObject().getResearchGroupId());

                item.add(new BookmarkablePageLink<Void>("edit", SoftwareFormPage.class, parameters));
                item.add(new AjaxLink<Void>("delete") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {

                        target.add(getFeedback());
                        int id = item.getModelObject().getSoftwareId();
                        ResearchGroup group = model.getCriteriaModel().getObject();

                        if (facade.canDelete(id)) {
                            if (group != null) {
                                int groupId = group.getResearchGroupId();

                                if (groupId == CoreConstants.DEFAULT_ITEM_ID) { // delete default item if it's from default group
                                    if (!facade.hasGroupRel(id)) { // delete only if it doesn't have group relationship
                                        facade.delete(item.getModelObject());
                                        //getFeedback().info(ResourceUtils.getString("text.itemWasDeletedFromDatabase"));
                                        setResponsePage(ListSoftwareDefinitionsPage.class);
                                    } else {
                                        getFeedback().error(ResourceUtils.getString("text.itemInUse"));
                                    }
                                } else {

                                    if (!facade.isDefault(id)) { // delete only non default weather
                                        facade.delete(item.getModelObject());
                                    }
                                    setResponsePage(ListSoftwareDefinitionsPage.class);
                                }
                            }

                        } else {
                            getFeedback().error(ResourceUtils.getString("text.itemInUse"));
                        }


                    }

                    @Override
                    protected void updateAjaxAttributes(AjaxRequestAttributes attributes)
                    {
                        super.updateAjaxAttributes(attributes);

                        AjaxCallListener ajaxCallListener = new AjaxCallListener();
                        ajaxCallListener.onPrecondition("return confirm('Are you sure you want to delete item?');");
                        attributes.getAjaxCallListeners().add(ajaxCallListener);
                    }

                }.setVisibilityAllowed(isAdmin || isExperimenter));

            }
        };
        container.add(software);

        AjaxLink<Void> link = new AjaxLink<Void>("addSoftwareLink") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                int researchGroupId = (model.getCriteriaModel().getObject() == null) ? CoreConstants.DEFAULT_ITEM_ID : model.getCriteriaModel().getObject().getResearchGroupId();
                setResponsePage(SoftwareFormPage.class, PageParametersUtils.getPageParameters(PageParametersUtils.GROUP_PARAM, researchGroupId));
            }
        };
        link.setVisibilityAllowed(isAdmin || isExperimenter);

        add(new ResearchGroupSelectForm("form", model.getCriteriaModel(), new ListModel<ResearchGroup>(groups), container, isAdmin));
        add(link, container);
    }
}
