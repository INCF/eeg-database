package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import java.util.List;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Extends PersonalLicenseListPanel with confirm/request links for each table row.
 *
 * Usable for list of personal license requests.
 *
 * @author Jakub Danek
 */
public class LicenseRequestListPanel extends PersonalLicenseListPanel {

	public LicenseRequestListPanel(String id, IModel<List<PersonalLicense>> requestsModel) {
		super(id, requestsModel);
	}

	@Override
	protected void addListColumns(List<IColumn<PersonalLicense, String>> columns) {
		columns.add(new PropertyColumn<PersonalLicense, String>(ResourceUtils.getModel("dataTable.heading.approve"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<PersonalLicense>> item, String componentId, IModel<PersonalLicense> rowModel) {
                item.add(new ConfirmLicensePanel(componentId, rowModel));
            }
        });
		columns.add(new PropertyColumn<PersonalLicense, String>(ResourceUtils.getModel("dataTable.heading.reject"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<PersonalLicense>> item, String componentId, IModel<PersonalLicense> rowModel) {
                item.add(new RejectLicensePanel(componentId, rowModel));
            }
        });
	}
	
}
