/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.ui.licenses;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPageLeftMenu;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ListExperimentsByPackagePage;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.components.LicenseRequestForm;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Page for license request.
 *
 * @author Jakub Danek
 */
@AuthorizeInstantiation(value = {"ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class LicenseRequestPage extends MenuPage {

	public static final String PARAM_LICENSE_ID = "licenseId";

	@SpringBean
	private LicenseFacade licenseFacade;

	public LicenseRequestPage(PageParameters params) {
		int licenseId = params.get(PARAM_LICENSE_ID).toInt();
		License l = licenseFacade.read(licenseId);

		this.add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

		this.add(new LicenseRequestForm("form", new Model<License>(l)) {

			@Override
			protected void onSubmitAction() {
				super.onSubmitAction();
				redirect();
			}

			@Override
			protected void onCancelAction() {
				super.onCancelAction();
				redirect();
			}

			private void redirect() {
				setResponsePage(ListExperimentsByPackagePage.class);
			}
		});
	}

}
