package cz.zcu.kiv.eegdatabase.wui.ui.licenses;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.wui.components.repeater.BasicDataProvider;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lichous on 4.5.15.
 */
public class ListLicensesDataProvider extends BasicDataProvider<License> {

    private static final long serialVersionUID = -5624915754710275898L;

    public ListLicensesDataProvider(LicenseFacade licenseFacade, boolean template) {
        super("title",SortOrder.ASCENDING);

        List<License> licenses;

        //licenses = licenseFacade.readByParameter("template",template);
        licenses = licenseFacade.getAllRecords();
        
        if(licenses == null ) {
            licenses = new ArrayList<License>();
        }

        super.listModel.setObject(licenses);
    }
}
