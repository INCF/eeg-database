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
 *   ResearchGroupSelectForm.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.lists.components;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

/***
 * Filter select option for research group. When you select some value - id of this value is set into model. After that is called ajax update on container. So if u select value
 * then is your selection set into model and in container is filtered output by using this value and called ajax update for container where is output. Model is used for transport
 * selection from filter to output component.
 * 
 * This if just form. You have to add markup for this component into markup where u want use this.
 * 
 * Base markup: <form wicket:id="id"><select wicket:id="groups"></select></form>
 * 
 * @author Jakub Rinkes
 * 
 */
public class ResearchGroupSelectForm extends Form<Void> {

    private static final long serialVersionUID = -1807273865281153399L;

    protected Log log = LogFactory.getLog(getClass());

    public ResearchGroupSelectForm(String id, IModel<ResearchGroup> model, ListModel<ResearchGroup> choices, final MarkupContainer container, boolean isNullValid) {
        super(id);
        this.setOutputMarkupPlaceholderTag(true);
        final DropDownChoice<ResearchGroup> groups = new DropDownChoice<ResearchGroup>("groups", model, choices, new ChoiceRenderer<ResearchGroup>("title", "researchGroupId"));
        groups.setNullValid(!true);

        if (!choices.getObject().isEmpty())
            groups.setModelObject(choices.getObject().get(0));

        groups.setOutputMarkupPlaceholderTag(true);
        groups.add(new AjaxFormComponentUpdatingBehavior("onChange") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(container);
            }
        });

        add(groups);
    }

}
