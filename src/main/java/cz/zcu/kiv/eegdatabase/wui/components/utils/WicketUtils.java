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
 *   WicketUtils.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.components.utils;

import cz.zcu.kiv.eegdatabase.wui.components.form.MyFormComponentLabel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

/**
 *
 * @author Jakub Danek
 */
public class WicketUtils {

    /**
     * Adds labels and feedback panels for input fields to the given form
     * The label ids are of the form <original id>.label
     * The feedback ids are of the form <original id>.feedback
     *
     * FormComponentPanel instances wont get a label or a feedback unless they
     * implement ISingleLabelFormComponentPanel interface.
     * If they do, their children dont get any labels or feedbacks.
     *
     * @param form
     */
    public static void addLabelsAndFeedback(Form<?> form) {
        final Set<String> ids = new HashSet<String>();
        final Map<String, MarkupContainer> parents = new HashMap<String, MarkupContainer>();
        final Map<String, ComponentFeedbackPanel> feedbacks = new HashMap<String, ComponentFeedbackPanel>();
        final Map<String, FormComponentLabel> labels = new HashMap<String, FormComponentLabel>();

        form.visitFormComponents(new IVisitor<FormComponent<?>, Void>() {

            @Override
            public void component(FormComponent<?> object, IVisit<Void> visit) {
                if (object instanceof FormComponent && !(object instanceof Button)
                        && object.findParent(FormComponentPanel.class) == null) {
                    ids.add(object.getId());
                    parents.put(object.getId(), object.getParent());
                    //create a feedback panel for the component
                    ComponentFeedbackPanel temp = new ComponentFeedbackPanel(object.getId() + ".feedback",object);
                    temp.setVisible(object.isVisible()); //do not display component if field is not visible
                    feedbacks.put(object.getId(), temp);

                    if (object.isRequired()) {
                        String lab = object.getLabel().getObject();
                        object.setLabel(new Model<String>(lab.concat("*")));
                    }
                    FormComponentLabel lab = new MyFormComponentLabel(object.getId() + ".label", object);
                    lab.setVisible(object.isVisible());
                    labels.put(object.getId(), lab);
                }
            }
        });

        for (String id : ids) {
            parents.get(id).addOrReplace(labels.get(id));
            parents.get(id).addOrReplace(feedbacks.get(id));
        }
    }

}
