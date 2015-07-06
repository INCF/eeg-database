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
 *   ListTemplatePage.java, 2015/02/26 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.metadata.template;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.Template;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.UserRole;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.metadata.TemplateFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdministrationPageLeftMenu;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPageLeftMenu;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ListTemplatePage extends MenuPage {

    private static final long serialVersionUID = 3947094412605147087L;

    private static final int ITEMS_PER_PAGE = 20;

    @SpringBean
    private TemplateFacade templateFacade;

    public ListTemplatePage() {

        setPageTitle(ResourceUtils.getModel("pageTitle.template.list"));
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        ListTemplateProvider provider = new ListTemplateProvider(templateFacade, EEGDataBaseSession.get().getLoggedUser().getPersonId());
        DefaultDataTable<Template, String> list = new DefaultDataTable<Template, String>("list", createListColumns(),
                provider, ITEMS_PER_PAGE);

        add(new BookmarkablePageLink<Void>("addTemplateLink", TemplateFormPage.class));
        add(new BookmarkablePageLink<Void>("shareTemplateLink", TemplateSharePage.class));

        add(list);
    }

    public ListTemplatePage(PageParameters params) {

        boolean isAdmin = EEGDataBaseSession.get().hasRole(UserRole.ROLE_ADMIN.name());

        if (!isAdmin) {
            throw new RestartResponseAtInterceptPageException(ListTemplatePage.class);
        }

        setPageTitle(ResourceUtils.getModel("pageTitle.template.system"));
        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()));

        ListTemplateProvider provider = new ListTemplateProvider(templateFacade);
        DefaultDataTable<Template, String> list = new DefaultDataTable<Template, String>("list", createListColumns(),
                provider, ITEMS_PER_PAGE);

        add(new BookmarkablePageLink<Void>("addTemplateLink", TemplateFormPage.class));
        add(new BookmarkablePageLink<Void>("shareTemplateLink", TemplateSharePage.class));

        add(list);
    }

    private List<? extends IColumn<Template, String>> createListColumns() {

        List<IColumn<Template, String>> columns = new ArrayList<IColumn<Template, String>>();

        columns.add(new PropertyColumn<Template, String>(ResourceUtils.getModel("dataTable.heading.number"), "templateId", "templateId"));
        columns.add(new PropertyColumn<Template, String>(ResourceUtils.getModel("dataTable.heading.name"), "name", "name"));

        columns.add(new PropertyColumn<Template, String>(null, null, null) {

            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(Item<ICellPopulator<Template>> item, String componentId, IModel<Template> rowModel) {
                item.add(new ViewLinkPanel(componentId, TemplateFormPage.class, "templateId", rowModel, ResourceUtils.getModel("label.edit")));
            }
        });

        columns.add(new PropertyColumn<Template, String>(null, null, null) {

            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(Item<ICellPopulator<Template>> item, String componentId, IModel<Template> rowModel) {
                item.add(new RemoveTemplateLinkPanel(componentId, rowModel));
            }
        });
        return columns;
    }

}
