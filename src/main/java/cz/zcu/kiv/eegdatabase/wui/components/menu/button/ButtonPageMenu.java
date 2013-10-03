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
 *   ButtonPageMenu.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.menu.button;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

/**
 * Component for creating left menu on pages. Works with Enumeration of menu structure. Enumeration have to implements IButtonPageMenu interface. Menu use AuthorizeInstantiation
 * annotation for filtering menu item which logged user cannot see.
 * 
 * @author Jakub Rinkes
 * 
 */
public class ButtonPageMenu extends Panel {

    private static final long serialVersionUID = 6708846586316988188L;

    public ButtonPageMenu(String id, IButtonPageMenu[] listOfPages) {
        super(id);

        List<IButtonPageMenu> list = filterUnauthorizedPages(listOfPages);

        ListView<IButtonPageMenu> menu = new ListView<IButtonPageMenu>("menu", list) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<IButtonPageMenu> item) {
                item.add(new BookmarkablePageLink("link", item.getModelObject().getPageClass(), item.getModelObject().getPageParameters())
                        .add(new Label("label", ResourceUtils.getModel(item.getModelObject().getPageTitleKey()))));
            }
        };

        add(menu);
    }

    private List<IButtonPageMenu> filterUnauthorizedPages(IButtonPageMenu[] listOfPages) {

        List<IButtonPageMenu> list = new ArrayList<IButtonPageMenu>();

        for (IButtonPageMenu item : listOfPages) {

            AuthorizeInstantiation annotation = ((AuthorizeInstantiation) item.getPageClass().getAnnotation(AuthorizeInstantiation.class));

            if (annotation != null) {
                Roles roles = new Roles(annotation.value());

                if (EEGDataBaseSession.get().hasAnyRole(roles)) {
                    list.add(item);
                }
            }
        }
        return list;

    }
}
