package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.WicketTestForm.SectionCell;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.WicketTestForm.RowData;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.WicketTestForm.SubsectionsCell;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.include.Include;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.transformer.XsltOutputTransformerContainer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

import java.util.*;

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
 * ${NAME}, 2014/07/01 11:57 Prokop
 *
 **********************************************************************************************************************/
@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ExperimentFormPageTest extends MenuPage {

    public ExperimentFormPageTest() {
        setPageTitle(ResourceUtils.getModel("pageTitle.experimentDetail"));
        List<RowData> rowData = generateData();
        Form<List<RowData>> form = new Form<List<RowData>>("form", new CompoundPropertyModel<List<RowData>>(rowData));

        //one table row
        ListView view = new PropertyListView("row", rowData) {
            @Override
            protected void populateItem(ListItem item) {
                SectionCell sectionCell = new SectionCell("cell1", item.getModel());
                SubsectionsCell subsectionsCell= new SubsectionsCell("cell2", item.getModel());
                item.add(sectionCell);
                item.add(subsectionsCell);
            }
        };
        form.add(view);
        add(form);
    }

    private List<RowData> generateData() {
        List<RowData> list = new ArrayList<RowData>();
        RowData data;
        RowData subsec;
        String name;
        Boolean required;
        int maxCount;
        List<RowData> subsections;

        for(int i = 0; i<10; i++){
            name = "name " + i;
            required = i%2==0;
            maxCount = i%4 + 1;
            subsections = new ArrayList<RowData>();

            for(int j = i; j < 6; j++){
                subsec = new RowData("subsec"+j, j%2==0,i%4, null);
                subsections.add(subsec);
            }
            data = new RowData(name, required, maxCount, subsections);
            list.add(data);
        }

        return list;
    }
}
