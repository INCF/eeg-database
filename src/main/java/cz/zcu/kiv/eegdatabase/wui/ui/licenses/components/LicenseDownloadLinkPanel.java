package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import org.apache.wicket.markup.html.basic.Label;

import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created by Lichous on 5.5.15.
 */
public class LicenseDownloadLinkPanel extends Panel {


    @SpringBean
    LicenseFacade licenseFacade;

    public LicenseDownloadLinkPanel(String id, IModel<License> model) {
        super(id);

        final License license = model.getObject();

        boolean isContent = license != null && license.getAttachmentFileName() != null;

        ByteArrayResource res;
        if (isContent) {
            res = new ByteArrayResource("", licenseFacade.getLicenseAttachmentContent(license.getLicenseId()), license.getAttachmentFileName());
        } else {
            res = new ByteArrayResource("");
        }
        ResourceLink<Void> downloadLink = new ResourceLink<Void>("download", res);
        downloadLink.setVisibilityAllowed(isContent);
        downloadLink.add(new Label("fileName", new Model<String>(license.getAttachmentFileName())).setVisibilityAllowed(isContent));
        add(downloadLink);
        //add(new Label("noFile", new Model<String>("")).setVisibilityAllowed(!isContent));


    }
}


