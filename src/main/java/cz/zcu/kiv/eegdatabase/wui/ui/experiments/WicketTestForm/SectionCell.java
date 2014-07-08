package cz.zcu.kiv.eegdatabase.wui.ui.experiments.WicketTestForm;

import cz.zcu.kiv.eegdatabase.data.xmlObjects.odMLSection.SectionType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.List;

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

    public SectionCell(final String id, IModel model) {
        super(id, model);
        addCell(model);
    }

    private void addCell(IModel model) {
        SectionType data = (SectionType) model.getObject();
        List<Integer> dropDownVals = new ArrayList<Integer>();
        final int maxCount = data.getMaxCount();
        final int minCount = data.getMinCount();

        for (int i = minCount; i <= maxCount; i++) {
            dropDownVals.add(i);
        }

        final CheckBox box = new CheckBox("selected");
        box.setEnabled(!data.isRequired());

        final DropDownChoice<Integer> dropDownChoice = new DropDownChoice<Integer>("sectionCount",
                new Model<Integer>(((SectionType) model.getObject()).getSelectedCount()), dropDownVals) {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setEnabled(box.getModelObject());
            }
        };
        dropDownChoice.setOutputMarkupId(true);

        OnChangeAjaxBehavior onChangeBehavior = new OnChangeAjaxBehavior() {
            protected void onUpdate(AjaxRequestTarget target) {
                dropDownChoice.setEnabled(box.getModelObject());
                target.add(dropDownChoice);
            }
        };

        box.add(onChangeBehavior);
        add(dropDownChoice);
        add(box);
        add(new Label("name"));
    }
}
