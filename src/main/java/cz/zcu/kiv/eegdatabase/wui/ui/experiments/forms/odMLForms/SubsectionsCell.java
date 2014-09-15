package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms.odMLForms;

import cz.zcu.kiv.eegdatabase.data.xmlObjects.odMLSection.SectionType;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

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
 * ${NAME}, 2014/06/30 15:38 Prokop
 * <p/>
 * ********************************************************************************************************************
 */
public class SubsectionsCell extends Panel {

    /**
     * Constructor creates content of one cell in table that represents one subsection in form
     * @param id id if this cell
     * @param model model of this cell
     */
    public SubsectionsCell(String id, IModel<SectionType> model) {
        super(id, model);
        final ListView<SectionType> view = new PropertyListView<SectionType>("row",
                model.getObject().getSubsections()) {
            @Override
            protected void populateItem(ListItem<SectionType> item) {
                final SectionCell subCell = new SectionCell("subCell", item.getModel());
                item.add(subCell);
            }
        };

/*        List<String> data = new ArrayList<String>();
        data.add("test 1");
        data.add("test 2");

        final DropDownChoice<String> dropDownChoice = new DropDownChoice<String>("sectionSelect", Model.of(data.get(0)),data);
        dropDownChoice.setOutputMarkupId(true);

        AjaxSubmitLink addLink = new AjaxSubmitLink("addRow", form) {
            @Override
            public void onSubmit(AjaxRequestTarget target, Form form) {
                SectionType section = new SectionType(dropDownChoice.getModelObject(),false, 50,new ArrayList<SectionType>(),1,1, true);
                view.getModelObject().add(section);
                if (target != null)
                    target.add(this);
            }
        };*/

        //addLink.setDefaultFormProcessing(false);

        //add(dropDownChoice);
        //add(addLink);

        //view.setOutputMarkupId(true);
        add(view);
    }
}
