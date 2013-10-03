package cz.zcu.kiv.eegdatabase.wui.ui.administration.components;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.WicketUtils;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Panel for research group management - payment settings,
 * potential future settings...
 *
 * @author Jakub Danek
 */
public class ResearchGroupManagementForm extends Panel {

	@SpringBean
	private ResearchGroupFacade researchGroupFacade;
	
	private IModel<ResearchGroup> groupModel;
	private Form form;

	/**
	 *
	 * @param id component id
	 * @param groupModel model with the group to display settings for
	 */
	public ResearchGroupManagementForm(String id, IModel<ResearchGroup> groupModel) {
		super(id);
		this.groupModel = groupModel;

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

		this.addControls(f);
	}

	/**
	 * Adds form controls to a container (e.g. the form)
	 * @param f the container the controls will be added to
 	 */
	private void addControls(WebMarkupContainer f) {
		Button button = new AjaxButton("submitButton", ResourceUtils.getModel("button.save")) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				researchGroupFacade.update(groupModel.getObject());
				target.add(form);
			}

		};

		f.add(button);

		button = new AjaxButton("cancelButton", ResourceUtils.getModel("button.cancel")) {

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
