package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.WicketUtils;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Form for license request.
 *
 * @author Jakub Danek
 */
public class LicenseRequestForm extends Panel {
	@SpringBean
	private LicenseFacade licenseFacade;

	private IModel<License> licenseModel;
	private PersonalLicense persLicense;
	private boolean displayControls = true;

	private Form form;

	public LicenseRequestForm(String id, IModel<License> license) {
		super(id);
		this.licenseModel = license;
		form = new StatelessForm("form");
		this.persLicense = new PersonalLicense();
		this.add(form);
		this.add(new Label("title", ResourceUtils.getModel("heading.license.request", licenseModel)));

		this.addComponents();
		this.addControls();
	}

	private void addComponents() {
		FormComponent c = new RequiredTextField("firstName", new PropertyModel(persLicense, "firstName"));
		c.setLabel(ResourceUtils.getModel("label.license.request.firstname"));
		form.add(c);
		
		c = new RequiredTextField("lastName", new PropertyModel(persLicense, "lastName"));
		c.setLabel(ResourceUtils.getModel("label.license.request.lastname"));
		form.add(c);
		
		c = new RequiredTextField("organisation", new PropertyModel(persLicense, "organization"));
		c.setLabel(ResourceUtils.getModel("label.license.request.organisation"));
		form.add(c);

		WicketUtils.addLabelsAndFeedback(form);
	}

	private void addControls() {
		Button b = new Button("submitButton", ResourceUtils.getModel("button.save")) {

			@Override
			public void onSubmit() {
				super.onSubmit();
				LicenseRequestForm.this.onSubmitAction();
			}

		};
		form.add(b);

		b = new Button("cancelButton", ResourceUtils.getModel("button.cancel")) {

			@Override
			public void onSubmit() {
				super.onSubmit();
				form.clearInput();
				LicenseRequestForm.this.onCancelAction();
			}

		};
		b.setDefaultFormProcessing(false);
		form.add(b);
	}

	/**
	 * Sets license and person for the submited PersonalLicense object and creates
	 * a request for the license.
	 * Override to provide additional actions.
	 */
	protected void onSubmitAction() {
		persLicense.setLicense(licenseModel.getObject());
		persLicense.setPerson(EEGDataBaseSession.get().getLoggedUser());
		licenseFacade.createRequestForLicense(persLicense);
	}

	protected void onCancelAction() {
		//override this
	}

	public boolean isDisplayControls() {
		return displayControls;
	}

	public void setDisplayControls(boolean displayControls) {
		this.displayControls = displayControls;
	}

}
