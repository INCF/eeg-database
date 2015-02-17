package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;

public class ViewLicensePanel extends Panel {

    private static final long serialVersionUID = 1794869820176582709L;

    @SpringBean
    private LicenseFacade facade;
    private ResourceLink<Void> downloadLink;
    private IModel<License> model;

    public ViewLicensePanel(String id, IModel<License> model) {
        super(id, new CompoundPropertyModel<License>(model));
        this.model = model;

        add(new Label("title"));
        add(new Label("price"));
        add(new Label("licenseType"));
        add(new MultiLineLabel("description"));
        add(new Label("attachmentFileName"));

        boolean isContent = model.getObject() != null && model.getObject().getAttachmentFileName() != null;
        ByteArrayResource res;
        if (isContent) {
            res = new ByteArrayResource("", facade.getLicenseAttachmentContent(model.getObject().getLicenseId()), model.getObject()
                    .getAttachmentFileName());
        } else {
            res = new ByteArrayResource("");
        }
        downloadLink = new ResourceLink<Void>("download", res);
        downloadLink.setVisible(isContent);
        add(downloadLink);

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onConfigure() {
        super.onConfigure();

        boolean isContent = model.getObject() != null && model.getObject().getAttachmentFileName() != null;
        ByteArrayResource res;
        if (isContent) {
            res = new ByteArrayResource("", facade.getLicenseAttachmentContent(model.getObject().getLicenseId()), model.getObject()
                    .getAttachmentFileName());
        } else {
            res = new ByteArrayResource("");
        }

        ResourceLink<Void> newLink = new ResourceLink<Void>("download", res);
        downloadLink = (ResourceLink<Void>) downloadLink.replaceWith(newLink);
        downloadLink.setVisible(isContent);
    }

}
