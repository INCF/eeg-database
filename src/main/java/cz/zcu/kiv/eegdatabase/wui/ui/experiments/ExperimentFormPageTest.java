package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.WicketTestForm.SectionCell;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.WicketTestForm.SectionStructure;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.WicketTestForm.SubsectionsCell;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.CompoundPropertyModel;

import java.util.*;

/**
 * ********************************************************************************************************************
 * <p/>
 * This file is part of the ${PROJECT_NAME} project
 * <p/>
 * ==========================================
 * <p/>
 * Copyright (C) 2014 by University of West Bohemia (http://www.zcu.cz/en/)
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * ${NAME}, 2014/07/01 11:57 Prokop
 * <p/>
 * ********************************************************************************************************************
 */
@AuthorizeInstantiation(value = {"ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN"})
public class ExperimentFormPageTest extends MenuPage {

    public ExperimentFormPageTest() {
        setPageTitle(ResourceUtils.getModel("pageTitle.experimentDetail"));
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        List<SectionStructure> sectionStructure = generateData();
        Form<List<SectionStructure>> form = new Form<List<SectionStructure>>("form", new CompoundPropertyModel<List<SectionStructure>>(sectionStructure));

        //one table row
        ListView view = new PropertyListView("row", sectionStructure) {
            @Override
            protected void populateItem(ListItem item) {
                SectionCell sectionCell = new SectionCell("cell1", item.getModel());
                SubsectionsCell subsectionsCell = new SubsectionsCell("cell2", item.getModel());
                item.add(sectionCell);
                item.add(subsectionsCell);
            }
        };
        form.add(view);
        add(form);
    }

    private List<SectionStructure> generateData() {
        List<SectionStructure> list = new ArrayList<SectionStructure>();
        SectionStructure section;
        SectionStructure subsection;
        String name;
        Boolean required;
        int maxCount;
        List<SectionStructure> subsections;
        boolean selected;
        int minCount = 1;
        int selectedCount = 1;

        for (int i = 0; i < 10; i++) {
            name = "name " + i;
            required = i % 3 == 0;
            maxCount = i % 4 + 1;
            subsections = new ArrayList<SectionStructure>();
            if (required) selected = required;
            else selected = i % 2 == 0;

            for (int j = i; j < 6; j++) {
                subsection = new SectionStructure("subsection" + j, j % 2 == 0, i % 4, null, minCount, selectedCount, selected);
                subsections.add(subsection);
            }
            section = new SectionStructure(name, required, maxCount, subsections, minCount, selectedCount, selected);
            list.add(section);
        }

        return list;
    }
}
