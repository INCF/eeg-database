package cz.zcu.kiv.eegdatabase.wui.components.table;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.FileUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.file.DataFileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenarioXMLProvider;

/**
 * Panel with link and security policy. Prepared download scenario file. Added in table with scenarios.
 * 
 * @author Jakub Rinkes
 * 
 */
public class ScenarioDownloadLink extends Panel {

    @SpringBean
    ScenarioXMLProvider xmlProvider;

    private static final long serialVersionUID = 1L;

    public ScenarioDownloadLink(String id, IModel<Scenario> model) {
        super(id);

        final Scenario scenario = model.getObject();
        boolean fileExist = scenario.getScenarioType().getScenarioXml() != null;
        // added link for download
        add(new Link<Void>("download") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                // get downloaded file
                final DataFileDTO file = xmlProvider.getXmlFileForScenario(scenario.getScenarioId(), EEGDataBaseSession.get().getLoggedUser().getUsername());
                // get file for download in wicket request cykle.
                getRequestCycle().scheduleRequestHandlerAfterCurrent(FileUtils.prepareDownloadFile(file));

            }
        }).setVisibilityAllowed(fileExist);
        add(new Label("noFile", ResourceUtils.getModel("label.notAvailable")).setVisibilityAllowed(!fileExist));
    }
}
