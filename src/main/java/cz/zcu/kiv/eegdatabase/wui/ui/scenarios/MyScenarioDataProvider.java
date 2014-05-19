package cz.zcu.kiv.eegdatabase.wui.ui.scenarios;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.repeater.BasicDataProvider;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;

public class MyScenarioDataProvider extends BasicDataProvider<Scenario> {

    private static final long serialVersionUID = 3555979400074686801L;

    protected Log log = LogFactory.getLog(getClass());

    ScenariosFacade facade;


    public MyScenarioDataProvider(ScenariosFacade facade) {
        super("scenarioId", SortOrder.ASCENDING);

        List<Scenario> list;
        
        Person loggedUser = EEGDataBaseSession.get().getLoggedUser();
        list = facade.getScenariosWhereOwner(loggedUser);
        
        super.listModel.setObject(list);
    }

}
