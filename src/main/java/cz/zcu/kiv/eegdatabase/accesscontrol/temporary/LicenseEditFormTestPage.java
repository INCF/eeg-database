/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.accesscontrol.temporary;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.components.license.LicenseEditForm;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 *
 * @author veveri
 */
public class LicenseEditFormTestPage extends BasePage {
    
    private IModel<List<License>> blueprints = new Model();
    private License model = new License();

    public LicenseEditFormTestPage() {

	List<License> list = new ArrayList<License>(2);
	License tmp = new License();
	tmp.setTitle("Ahoj1");
	tmp.setDescription("ehfiuwefhwiuf hweiu fhiuwe hfuihf iuwehf iuwehfiuweh fiuhfiu whefiu hwef wiufh wiehfwiehf ");
	tmp.setPrice(100);

	list.add(tmp);

	tmp = new License();
	tmp.setTitle("Ahoj2");
	tmp.setDescription("");
	tmp.setPrice(600);

	list.add(tmp);


	blueprints.setObject(list);

	this.add(new LicenseEditForm("test", new Model<License>(model), blueprints));
    }

}
