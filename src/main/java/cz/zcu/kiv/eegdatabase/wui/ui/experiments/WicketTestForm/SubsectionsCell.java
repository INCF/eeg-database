package cz.zcu.kiv.eegdatabase.wui.ui.experiments.WicketTestForm;

import cz.zcu.kiv.eegdatabase.data.xmlObjects.odMLSection.SectionType;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/***********************************************************************************************************************
 *
 * This file is part of the ${PROJECT_NAME} project

 * ==========================================
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
 * ${NAME}, 2014/06/30 15:38 Prokop
 *
 **********************************************************************************************************************/
     public class SubsectionsCell extends Panel {

        public SubsectionsCell(String id, IModel model)
        {
            super(id, model);
            SectionType subsections = (SectionType) model.getObject();
            ListView view = new PropertyListView("row", subsections.getSubsections()) {
                @Override
                protected void populateItem(ListItem item) {
                    SectionCell subCell = new SectionCell("subCell", item.getModel());
                    item.add(subCell);
                }
            };
            add(view);
        }
}
