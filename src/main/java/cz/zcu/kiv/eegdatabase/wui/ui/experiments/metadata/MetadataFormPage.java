package cz.zcu.kiv.eegdatabase.wui.ui.experiments.metadata;

import java.io.InputStream;

import odml.core.Reader;
import odml.core.Section;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;

public class MetadataFormPage extends WebPage {
    private static final long serialVersionUID = 1L;

    public MetadataFormPage(final PageParameters parameters) {
        super(parameters);

        InputStream template = EEGDataBaseApplication.get().getServletContext().getResourceAsStream("/files/odML/testtemplate.xml");
        
        Section section = null;
        Reader reader = new Reader();
        try {
            section = reader.load(template);
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        add(new MetadataForm("metadata-form", new Model<Section>(section)));
    }
}
