package cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters;

import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;

import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 12.5.13
 * Time: 15:19
 */
public class ScenarioConverter implements IConverter<Scenario> {
    private ScenariosFacade scenariosFacade;

    public ScenarioConverter(ScenariosFacade scenariosFacade){
        this.scenariosFacade = scenariosFacade;
    }

    @Override
    public Scenario convertToObject(String s, Locale locale) {
        if(Strings.isEmpty(s)){
            return null;
        }

        Scenario scenario = new Scenario();
        scenario.setTitle(s);
        List<Scenario> scenarios = scenariosFacade.getUnique(scenario);
        return (scenarios != null && scenarios.size() > 0) ? scenarios.get(0) : scenario;
    }

    @Override
    public String convertToString(Scenario scenario, Locale locale) {
        return scenario.getAutoCompleteData();
    }
}
