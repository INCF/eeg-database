package cz.zcu.kiv.eegdatabase.wui.ui.lists;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.IButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;

/**
 * Enumeration of left menu page for lists section.
 * 
 * @author Jakub Rinkes
 *
 */
public enum ListsLeftPageMenu implements IButtonPageMenu {
    
    HARDWARE_DEFINITIONS(ListHardwareDefinitionsPage.class, "menuItem.hardwareDefinitions", null),
    PEOPLE_OPT_PARAM(ListPersonOptParamPage.class, "menuItem.optionalParametersForPeople", null),
    EXPERIMENT_OPT_PARAM(ListExperimentOptParamPage.class, "menuItem.optionalParametersForExperiments", null),
    FILE_METADATA_DEFINITIONS(ListFileMetadataPage.class, "menuItem.fileMetadataDefinitions", null),
    WEATHER_DEFINITIONS(ListWeatherDefinitiosPage.class, "menuItem.weatherDefinitions", null),
    ARTIFACT_DEFINITIONS(ListArtifactDefinitionsPage.class, "menuItem.artifactDefinitions", null)
    ;

    private Class<? extends MenuPage> pageClass;
    private String pageTitleKey;
    private PageParameters parameters;

    private ListsLeftPageMenu(Class<? extends MenuPage> pageClass, String pageTitleKey, PageParameters parameters) {
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
