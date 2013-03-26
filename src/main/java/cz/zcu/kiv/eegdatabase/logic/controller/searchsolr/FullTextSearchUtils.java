package cz.zcu.kiv.eegdatabase.logic.controller.searchsolr;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.wui.components.page.UnderConstructPage;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ArticlesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.data.DataFileDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.ResearchGroupsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListFileMetadataPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListHardwareDefinitionsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListPersonOptParamPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListWeatherDefinitiosPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.PersonDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ScenarioDetailPage;
import org.apache.wicket.Page;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 25.3.13
 * Time: 23:00
 * To change this template use File | Settings | File Templates.
 */
public class FullTextSearchUtils {

    public final static int AUTOCOMPLETE_ROWS = 10;

    private static Map<Class<?>, Class<? extends Page>> classMap = initializeClassMap();
    private static Map<Class<?>, String> resultTypesMap = initializeResultTypes();

    private static final Map<Class<?>, Class<? extends Page>> initializeClassMap() {

        Map<Class<?>, Class<? extends Page>> map = new HashMap<Class<?>, Class<? extends Page>>();

        map.put(Article.class, ArticlesPage.class);
        map.put(DataFile.class, DataFileDetailPage.class);
        map.put(Experiment.class, ExperimentsDetailPage.class);
        map.put(Person.class, PersonDetailPage.class);
        map.put(ResearchGroup.class, ResearchGroupsDetailPage.class);
        map.put(Scenario.class, ScenarioDetailPage.class);
        map.put(Hardware.class, ListHardwareDefinitionsPage.class);
        map.put(FileMetadataParamDef.class, ListFileMetadataPage.class);
        map.put(PersonOptParamDef.class, ListPersonOptParamPage.class);
        map.put(Weather.class, ListWeatherDefinitiosPage.class);

        return map;
    }

    private static final  Map<Class<?>, String> initializeResultTypes() {

        Map<Class<?>, String> map = new HashMap<Class<?>, String>();

        map.put(ArticlesPage.class, "Article");
        map.put(ScenarioDetailPage.class, "Scenario");
        map.put(ExperimentsDetailPage.class, "Experiment");
        map.put(PersonDetailPage.class, "Person");
        map.put(ResearchGroupsDetailPage.class, "Research group");

        return map;
    }

    public static Class<? extends Page> getTargetPage(Class<?> clazz) {

        for(Class<?> listClass : classMap.keySet()) {
            if(listClass.equals(clazz)) {
                return classMap.get(listClass);
            }
        }

        return UnderConstructPage.class;
    }

    public static String getDocumentType(Class<? extends Page> clazz) {
        if(resultTypesMap.containsKey(clazz)) {
            return resultTypesMap.get(clazz);
        }
        else {
            return "Other";
        }
    }
}
