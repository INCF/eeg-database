package cz.zcu.kiv.eegdatabase.wui.ui.experiments.components.license;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxDropDownChoice;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.WicketUtils;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author Jakub Danek
 */
public class LicenseEditForm extends Panel {

    private IModel<List<License>> blueprintsModel;
    private IModel<License> licenseModel;

    private Form form;

    public LicenseEditForm(String id, IModel<License> model) {
	this(id, model, null);
    }

    public LicenseEditForm(String id, IModel<License> model, IModel<List<License>> blueprints) {
	super(id);

	this.licenseModel = model;
	this.blueprintsModel = blueprints;

	this.form = new Form("form");
	this.add(form);
	
	this.addFormFields();
	this.addBlueprintSelect();
	this.addControls();
    }

    private void addFormFields() {
	FormComponent c = new RequiredTextField("title", new PropertyModel(licenseModel, "title"));
	c.setLabel(ResourceUtils.getModel("label.license.title"));
	form.add(c);

	c = new TextArea("description", new PropertyModel(licenseModel, "description"));
	c.setLabel(ResourceUtils.getModel("label.license.description"));
	c.setRequired(true);
	form.add(c);

	c = new NumberTextField("price", new PropertyModel(licenseModel, "price"), Float.class);
	c.setLabel(ResourceUtils.getModel("label.license.price"));
	form.add(c);

	c = new RadioGroup<LicenseType>("licenseType", new PropertyModel<LicenseType>(licenseModel, "licenseType"));
	c.setLabel(ResourceUtils.getModel("label.license.type"));
	c.setRequired(true);
	c.add(new Radio("academic", new Model(LicenseType.ACADEMIC)));
	c.add(new Radio("business", new Model(LicenseType.BUSINESS)));
	form.add(c);

	WicketUtils.addLabelsAndFeedback(form);
    }

    /**
     * Add window controls - buttons, etc
     * @param cont
     */
    private void addControls() {
	AjaxButton button = new AjaxButton("submitButton", ResourceUtils.getModel("button.save")) {

	    @Override
	    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		onSubmitAction(licenseModel, target, form);
	    }
	};
	form.add(button);

	button = new AjaxButton("cancelButton", ResourceUtils.getModel("button.cancel")) {

	    @Override
	    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		onCancelAction(licenseModel, target, form);
	    }

	};
	form.add(button);
    }
    private void addBlueprintSelect() {
	AjaxDropDownChoice<License> ddc = new AjaxDropDownChoice<License>("blueprintSelect", blueprintsModel, new ChoiceRenderer<License>("title")) {

	    @Override
	    protected void onConfigure() {
		super.onConfigure();
		boolean viz = false;
		if (blueprintsModel != null) {
		    viz = blueprintsModel.getObject() != null ? !blueprintsModel.getObject().isEmpty() : false;
		}
		this.setVisible(viz);
	    }

	    @Override
	    protected void onSelectionChangeAjaxified(AjaxRequestTarget target, License option) {
		licenseModel.setObject(option);
		target.add(form);
	    }
	    
	};

	form.add(ddc);
    }

    /**
     * Override this method to provide onSubmit action
     * @param model license model
     * @param target ajax target
     * @param form form of the window
     */
    protected void onSubmitAction(IModel<License> model, AjaxRequestTarget target, Form<?> form) {

    }

    /**
     * Override this method to provide onCancel action
     * @param model license model
     * @param target ajax target
     * @param form form of the window
     */
    protected void onCancelAction(IModel<License> model, AjaxRequestTarget target, Form<?> form) {

    }
}
