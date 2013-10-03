package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Panel with logic and texts for changing package licensing from private to
 * public and vice versa.
 *
 * @author Jakub Danek
 */
public class PublicPrivateLicensePanel extends Panel {
	@SpringBean
	private LicenseFacade licenseFacade;

	private IModel<ExperimentPackage> epmodel;

	private boolean isPrivate;

	private Form form;
	
	/**
	 * 
	 * @param id
	 * @param pckg 
	 * @param isPrivate
	 */
	public PublicPrivateLicensePanel(String id, IModel<ExperimentPackage> pckg, boolean isPrivate) {
		super(id);
		this.epmodel = pckg;
		this.isPrivate = isPrivate;

		this.form = new Form("form");
		this.add(form);

		this.addText();
		this.addControls();
	}

	private void addText() {
		String key = isPrivate ? "text.license.add.public" : "text.license.remove.public";
		form.add(new Label("text", ResourceUtils.getModel(key)));
	}

	private void addControls() {
		String submitKey = isPrivate ? "button.add.publicLicense" : "button.remove.publicLicense";

		AjaxButton button = new AjaxButton("submitButton", ResourceUtils.getModel(submitKey)) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSubmitAction(target, form);
			}
		};
		form.add(button);

		button = new AjaxButton("cancelButton", ResourceUtils.getModel("button.cancel")) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onCancelAction(target, form);
			}
		};
		form.add(button);
	}

	/**
	 * Override this method to provide additional onSubmit action
	 *
	 * On default removes public license if isPrivate is false or adds it otherwise.
	 *
	 * @param model license model
	 * @param target ajax target
	 * @param form form of the window
	 */
	protected void onSubmitAction(AjaxRequestTarget target, Form<?> form) {
		License p = licenseFacade.getPublicLicense();
		if(isPrivate) {	
			licenseFacade.addLicenseForPackage(p, epmodel.getObject());
		} else {
			licenseFacade.removeLicenseFromPackage(p, epmodel.getObject());
		}
	}

	/**
	 * Override this method to provide onCancel action
	 *
	 * @param model license model
	 * @param target ajax target
	 * @param form form of the window
	 */
	protected void onCancelAction(AjaxRequestTarget target, Form<?> form) {
	}

}
