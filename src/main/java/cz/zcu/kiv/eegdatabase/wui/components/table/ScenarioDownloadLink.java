package cz.zcu.kiv.eegdatabase.wui.components.table;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.file.DataFileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenarioXMLProvider;

public class ScenarioDownloadLink extends Panel {
    

    @SpringBean
    ScenarioXMLProvider xmlProvider;

    private static final long serialVersionUID = 1L;

    public ScenarioDownloadLink(String id, IModel<Scenario> model) {
        super(id);
        
        final Scenario scenario = model.getObject();
        boolean fileExist = scenario.getScenarioType().getScenarioXml() != null;
            add(new Link<Void>("download") {

                private static final long serialVersionUID = 1L;

                @Override
                public void onClick() {
                    final DataFileDTO file = xmlProvider.getXmlFileForScenario(scenario.getScenarioId(), EEGDataBaseSession.get().getLoggedUser().getUsername());

                    if (file == null || file.getFileContent() == null)
                        return;

                    AbstractResourceStreamWriter stream = new AbstractResourceStreamWriter()
                    {
                        private static final long serialVersionUID = 1L;

                        public void write(OutputStream output) throws IOException
                        {
                            output.write(file.getFileContent());
                        }
                    };

                    getRequestCycle().scheduleRequestHandlerAfterCurrent(new
                            ResourceStreamRequestHandler(stream).setFileName(scenario.getScenarioName()));

                }
            }).setVisibilityAllowed(fileExist);
            add(new Label("noFile", ResourceUtils.getModel("label.notAvailable")).setVisibilityAllowed(!fileExist));
    }
}
