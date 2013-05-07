package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import org.apache.wicket.markup.html.link.Link;
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

	public RejectLicensePanel(String id, IModel<PersonalLicense> request) {
		super(id);

		add(new Link<PersonalLicense>("link", request) {

			@Override
			public void onClick() {
				licenseFacade.rejectRequestForLicense(this.getModelObject());
			}
		});
	}
}
