package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
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
    private AjaxButton button;
    private Form form;

    public ViewLicensePanel(String id, final IModel<License> model, boolean showRemoveButton) {
        super(id, new CompoundPropertyModel<License>(model));
        this.model = model;

        add(new Label("title"));
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

        this.form = new Form("form");
        add(form);
        button = new AjaxButton("removeButton", ResourceUtils.getModel("button.remove")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                onRemoveAction(model, target, form);
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setVisible(true);
            }
        };
        button.setVisibilityAllowed(showRemoveButton);
        form.add(button);

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

    protected void onRemoveAction(IModel<License> model, AjaxRequestTarget target, Form<?> form) {
    }

}
