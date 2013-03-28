package cz.zcu.kiv.eegdatabase.wui.components.menu.ddm;

import org.apache.wicket.Application;
import org.apache.wicket.Localizer;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.request.cycle.RequestCycle;

import cz.zcu.kiv.eegdatabase.wui.app.menu.EEGDatabaseMainMenu;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

/**
 * Component for Main menu on page. Generated html code which is used like drop down horizontal menu on pages. Menu structure have to implement interface MenuDefinition. MenuItem
 * is only help object for preprocess menu structure.
 * 
 * @author Jakub Rinkes
 * 
 */
public class MainMenu extends WebMarkupContainer {

    private static final long serialVersionUID = -4669294618890833486L;

    private static final String LI_START_TAG = "<li";
    private static final String LI_END_TAG = "</li>";
    private static final String A_START_TAG_LINK = "><a href=\'";
    private static final String A_START_TAG_FOOTER = "\' title=\'";
    private static final String A_START_TAG_FOOTER2 = "\'>";
    private static final String A_END_TAG = "</a>";

    public MainMenu(String id) {
        super(id);
    }

    @Override
    public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {

        StringBuilder buf = new StringBuilder(2048);
        MenuItem menu = new MenuItem(EEGDatabaseMainMenu.Main);

        processMenu(menu, buf, RequestCycle.get(), Application.get().getResourceSettings().getLocalizer());
        getResponse().write(buf);
    }

    /**
     * Generate string which is html code with menu structure. String is added by write method in response.
     * 
     * @param menu
     * @param buf
     * @param requestCycle
     * @param localizer
     */
    @SuppressWarnings("unchecked")
    private void processMenu(final MenuItem menu, final StringBuilder buf, final RequestCycle requestCycle, final Localizer localizer) {

        if (menu.isLinkable() && hasRoleForThisPage(menu)) {
            // create final link in menu.
            String title = ResourceUtils.getString(menu.getString());
            buf.append(A_START_TAG_LINK).append(RequestCycle.get().urlFor(menu.getClassForUrl(), null)).append(A_START_TAG_FOOTER).append(title).append(A_START_TAG_FOOTER2).append(title)
                    .append(A_END_TAG);
        }

        if (menu.hasSubmenu()) {
            // create submenu of links.
            for (MenuItem item : menu.getSubmenu()) {

                buf.append(LI_START_TAG);
                processMenu(item, buf, requestCycle, localizer);
                buf.append(LI_END_TAG);
            }

        }
    }

    @SuppressWarnings("unchecked")
    private boolean hasRoleForThisPage(MenuItem menu) {

        AuthorizeInstantiation annotation = ((AuthorizeInstantiation) menu.getClassForUrl().getAnnotation(AuthorizeInstantiation.class));

        if (annotation != null) {
            Roles roles = new Roles(annotation.value());

            return EEGDataBaseSession.get().hasAnyRole(roles);
        }

        return true;

    }

}
