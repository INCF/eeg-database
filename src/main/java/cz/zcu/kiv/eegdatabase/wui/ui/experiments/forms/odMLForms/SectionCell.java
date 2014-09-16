package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms.odMLForms;

import cz.zcu.kiv.eegdatabase.data.xmlObjects.odMLSection.SectionType;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.RangeValidator;

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
public class SectionCell extends Panel {

    public SectionCell(final String id, IModel<SectionType> model) {
        super(id, model);
        addCell(model);
    }

    /**
     * Creates content of one cell in table that represents one section in form
     * @param model model of this cell
     */
    private void addCell(IModel<SectionType> model) {
        final CheckBox requiredBox = new CheckBox("required");
        final CheckBox enabledBox = new CheckBox("sectionUsed");
        final TextField<Integer> min = new RequiredTextField<Integer>("min");
        final TextField<Integer> max = new RequiredTextField<Integer>("max");

        min.add(RangeValidator.<Integer>range(0, 50));
        max.add(RangeValidator.<Integer>range(0, 50));
        add(requiredBox);
        add(enabledBox);
        add(min);
        add(max);
        add(new Label("name"));
    }
}
