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
 *   ResearchGroupManagementForm.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.administration.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.WicketUtils;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;

/**
 * Panel for research group management - payment settings,
 * potential future settings...
 *
 * @author Jakub Danek
 */
public class ResearchGroupManagementForm extends Panel {

	private static final long serialVersionUID = -1188319159199447785L;

    @SpringBean
	private ResearchGroupFacade researchGroupFacade;
	
	private IModel<ResearchGroup> groupModel;
	private Form form;
    private FeedbackPanel feedback;

	/**
	 *
	 * @param id component id
	 * @param groupModel model with the group to display settings for
	 */
	public ResearchGroupManagementForm(String id, IModel<ResearchGroup> groupModel, final FeedbackPanel feedback) {
		super(id);
		this.groupModel = groupModel;
        this.feedback = feedback;

		this.form = new Form("form");
		this.form.setOutputMarkupId(true);
		this.add(form);
		
		this.addContent(form);
		WicketUtils.addLabelsAndFeedback(form);
	}

	/**
	 * Handles adding content components to container (e.g. a form)
	 * @param f container the components will be added to
	 */
	private void addContent(WebMarkupContainer f) {
		FormComponent cmp = new CheckBox("paidAccount", new PropertyModel<Boolean>(this.groupModel, "paidAccount"));
		cmp.setLabel(ResourceUtils.getModel("label.paidAccount"));

		f.add(cmp);
		
		final TextField<String> title = new TextField<String>("title", new PropertyModel<String>(this.groupModel, "title"));
        title.setRequired(true);
        title.setLabel(ResourceUtils.getModel("label.researchGroupTitle"));
        title.add(StringValidator.maximumLength(100));

        final TextArea<String> description = new TextArea<String>("description", new PropertyModel<String>(this.groupModel, "description"));
        description.setRequired(true);
        description.setLabel(ResourceUtils.getModel("label.researchGroupDescription"));
        description.add(StringValidator.maximumLength(250));
        
        CheckBox lockCheckBox = new CheckBox("lock", new PropertyModel<Boolean>(this.groupModel, "lock"));
        lockCheckBox.setLabel(ResourceUtils.getModel("label.lock"));
        f.add(lockCheckBox);
        
        f.add(title, description);

		this.addControls(f);
	}

	/**
	 * Adds form controls to a container (e.g. the form)
	 * @param f the container the controls will be added to
 	 */
	private void addControls(WebMarkupContainer f) {
		Button button = new AjaxButton("submitButton", ResourceUtils.getModel("button.save")) {

			private static final long serialVersionUID = 1L;

            @Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				researchGroupFacade.update(groupModel.getObject());
				
				info(ResourceUtils.getString("text.administration.group.changed", groupModel.getObject().getTitle()));
				target.add(form, feedback);
			}

		};

		f.add(button);

		button = new AjaxButton("cancelButton", ResourceUtils.getModel("button.cancel")) {

			private static final long serialVersionUID = 1L;

            @Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				form.clearInput();
				target.add(form);
			}

		};
		button.setDefaultFormProcessing(false);
		f.add(button);

	}

}
