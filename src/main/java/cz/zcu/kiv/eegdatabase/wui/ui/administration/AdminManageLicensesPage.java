package cz.zcu.kiv.eegdatabase.wui.ui.administration;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.DeleteLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.forms.LicenseManageFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.LicenseDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.ListLicensesDataProvider;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.components.LicenseDownloadLinkPanel;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lichous on 4.5.15.
 */

@AuthorizeInstantiation(value = {"ROLE_ADMIN"})
public class AdminManageLicensesPage extends MenuPage {

    private static final long serialVersionUID = -6137354728570669469L;

    private static final int ITEMS_PER_PAGE = 20;

    @SpringBean
    LicenseFacade licenseFacade;


    public AdminManageLicensesPage() {

        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()));

        DefaultDataTable<License, String> licenses = new DefaultDataTable<License, String>("licenseTemplates", createLicensesColumns(),
                new ListLicensesDataProvider(licenseFacade, true), ITEMS_PER_PAGE);


        BookmarkablePageLink<Void> addLicense = new BookmarkablePageLink<Void>("addLicense", LicenseManageFormPage.class);

        add(licenses,addLicense);


    }

    private List<? extends IColumn<License, String>> createLicensesColumns() {
        List<IColumn<License, String>> columns = new ArrayList<IColumn<License, String>>();

        columns.add(new PropertyColumn<License, String>(ResourceUtils.getModel("dataTable.heading.title"), "title", "title"));
        //columns.add(new PropertyColumn<License, String>(ResourceUtils.getModel("dataTable.heading.link"), null, "link"));
        columns.add(new PropertyColumn<License, String>(ResourceUtils.getModel("dataTable.heading.attachmentFile"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<License>> item, String componentId, IModel<License> rowModel) {
                item.add(new LicenseDownloadLinkPanel(componentId, rowModel));
            }
        });
        //columns.add(new PropertyColumn<License, String>(ResourceUtils.getModel("dataTable.heading.description"), "description", "description"));
        columns.add(new PropertyColumn<License, String>(ResourceUtils.getModel("dataTable.heading.detail"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<License>> item, String componentId, IModel<License> rowModel) {
                item.add(new ViewLinkPanel(componentId, LicenseDetailPage.class, "licenseId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });
        columns.add(new PropertyColumn<License, String>(ResourceUtils.getModel("dataTable.heading.edit"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<License>> item, String componentId, IModel<License> rowModel) {
                item.add(new ViewLinkPanel(componentId, LicenseManageFormPage.class, "licenseId", rowModel, ResourceUtils.getModel("link.edit")));
            }
        });
        columns.add(new AbstractColumn<License, String>(ResourceUtils.getModel("dataTable.heading.delete")) {
            @Override
            public void populateItem(Item<ICellPopulator<License>> item, String componentId,final IModel<License> rowModel) {

                item.add(new DeleteLinkPanel(componentId,AdminManageLicensesPage.class,"licenseId", rowModel,ResourceUtils.getModel("link.delete"),ResourceUtils.getString("text.delete.licenseTemplate")));
            }
        });


        return columns;
    }


}
