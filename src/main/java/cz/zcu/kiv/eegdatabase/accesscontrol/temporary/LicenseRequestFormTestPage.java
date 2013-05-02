/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.accesscontrol.temporary;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.components.LicenseRequestForm;
import org.apache.wicket.model.Model;

/**
 *
 * @author veveri
 */
public class LicenseRequestFormTestPage extends BasePage {

	public LicenseRequestFormTestPage() {
		License lic = new License();
		lic.setLicenseId(22);
		lic.setTitle("My Testing License");
		this.add(new LicenseRequestForm("form", new Model(lic)));
	}

}
