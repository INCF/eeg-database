/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.components.page;

import cz.zcu.kiv.eegdatabase.data.dao.LicenseDao;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author veveri
 */
public class TestPage extends BasePage {

    @SpringBean
    LicenseDao licenseDao;


    public TestPage() {

	License lic = new License();
	lic.setTitle("test");
	lic.setDescription("longgggggggggggg desc lwnhfklsdjn kdnfjsdn k");
	lic.setLicenseType(LicenseType.PRIVATE);
	lic.setPrice(250);
	licenseDao.create(lic);
    }

    
}
