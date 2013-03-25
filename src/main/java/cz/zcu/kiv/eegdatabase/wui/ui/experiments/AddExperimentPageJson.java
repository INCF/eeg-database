package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 25.3.13
 * Time: 21:18
 */
public class AddExperimentPageJson extends WebPage {
    public AddExperimentPageJson(PageParameters pageParameters){
        TextRequestHandler textRequestHandler = new TextRequestHandler(
                "application/json", "UTF-8",
                "[\"Hodnota1\",\"Hodnota2\",\"Hodnota3\",\"hodnota4\"]");
        RequestCycle.get().scheduleRequestHandlerAfterCurrent(textRequestHandler);
    }
}
