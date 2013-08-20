package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Panel with confirm link for the table (or any repeater) of PersonalLicense objects.
 *
 * The link confirms the license request.
 *
 * @author Jakub Danek
 */
public class RejectLicensePanel extends Panel {

	@SpringBean
	private LicenseFacade licenseFacade;

	private ModalWindow window;

	public RejectLicensePanel(String id, IModel<PersonalLicense> request) {
		super(id);

		window = new ModalWindow("window");
		add(window);

		PersonalLicenseRejectPanel panel = new PersonalLicenseRejectPanel(window.getContentId(), request) {

			@Override
			protected void onSubmitAction(AjaxRequestTarget target) {
				super.onSubmitAction(target);
				window.close(target);
				setResponsePage(this.getPage().getPageClass(), this.getPage().getPageParameters());
			}

			@Override
			protected void onCancelAction(AjaxRequestTarget target) {
				window.close(target);
			}

		};
		window.setAutoSize(true);
		window.setResizable(false);
		window.setContent(panel);

		add(new AjaxLink<PersonalLicense>("link", request) {

			@Override
			public void onClick(AjaxRequestTarget target) {
				window.show(target);
			}
		}.add(new Label("label", getLinkText())));
	}

	protected IModel<String> getLinkText() {
		return ResourceUtils.getModel("link.reject");
	}
}
