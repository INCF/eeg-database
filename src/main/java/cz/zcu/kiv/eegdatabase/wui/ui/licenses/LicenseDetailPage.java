package cz.zcu.kiv.eegdatabase.wui.ui.licenses;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.core.UserRole;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdminManageLicensesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdministrationPageLeftMenu;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

/**
 * Created by Lichous on 4.5.15.
 */

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class LicenseDetailPage extends MenuPage {

    private static final long serialVersionUID = -1496697219772978689L;

    @SpringBean
    LicenseFacade licenseFacade;

    public LicenseDetailPage(PageParameters parameters) {

        StringValue licenseId = parameters.get(DEFAULT_PARAM_ID);
        if (licenseId.isNull() || licenseId.isEmpty())
            throw new RestartResponseAtInterceptPageException(AdminManageLicensesPage.class);

        setupPageComponents(licenseId.toInteger());
    }

    private void setupPageComponents(Integer licenseId) {
        Person user = EEGDataBaseSession.get().getLoggedUser();

        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()).setVisibilityAllowed(user.getAuthority().equals(UserRole.ROLE_ADMIN.toString())));

        final License license = licenseFacade.read(licenseId);
        add(new Label("title",license.getTitle()));
        add(new Label("description", license.getDescription()));
        add(new Label("price", license.getPrice()));
        add(new Label("type", license.getLicenseType().toString()));
    }
}
