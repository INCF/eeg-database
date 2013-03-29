package cz.zcu.kiv.eegdatabase.wui.ui.people;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.IButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.page.UnderConstructPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.form.PersonFormPage;

/**
 * Enumeration of left menu page for person section.
 * 
 * @author Jakub Rinkes
 *
 */
public enum PersonPageLeftMenu implements IButtonPageMenu {

    LIST_OF_PERSONS(ListPersonPage.class, "pageTitle.listOfPeople", null),
    SEARCH_PERSON(UnderConstructPage.class, "menuItem.searchPeople", null),
    ADD_PERSON(PersonFormPage.class, "button.addPerson", null),
    ;
    
    private Class<? extends MenuPage> pageClass;
    private String pageTitleKey;
    private PageParameters parameters;

    private PersonPageLeftMenu(Class<? extends MenuPage> pageClass, String pageTitleKey, PageParameters parameters) {
        this.pageClass = pageClass;
        this.pageTitleKey = pageTitleKey;
        this.parameters = parameters;
    }

    @Override
    public Class<? extends MenuPage> getPageClass() {
        return pageClass;
    }

    @Override
    public String getPageTitleKey() {
        return pageTitleKey;
    }

    @Override
    public PageParameters getPageParameters() {
        return parameters;
    }
}
