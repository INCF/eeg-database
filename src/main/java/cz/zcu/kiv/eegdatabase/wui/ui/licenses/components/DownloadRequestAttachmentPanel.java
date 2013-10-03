package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;


import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ByteArrayResource;

/**
 * Panel with confirm link for the table (or any repeater) of PersonalLicense objects.
 *
 * The link confirms the license request.
 *
 * @author Jakub Danek
 */
public class DownloadRequestAttachmentPanel extends Panel {
	public DownloadRequestAttachmentPanel(String id, IModel<PersonalLicense> request) {
		super(id);
		ByteArrayResource res = new ByteArrayResource("", request.getObject().getAttachmentContent(), request.getObject().getAttachmentFileName());

		add(new ResourceLink("link", res));
	}
}