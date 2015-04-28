package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxDropDownChoice;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.MembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.PersonMembershipPlanFacade;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lichous on 26.4.15.
 */
public class LicenseDropDownChoicePanel extends Panel {

    private static final long serialVersionUID = 1875827209151471736L;
    private IModel<License> licenseModel;
    private IModel<List<License>> licenses;
    private ModalWindow viewLicenseWindow;
    private AjaxLink<License> viewLicenseLink;

    @SpringBean
    private LicenseFacade licenseFacade;

    @SpringBean
    private PersonMembershipPlanFacade personMembershipPlanFacade;


    public LicenseDropDownChoicePanel (String id, final IModel<Experiment> model) {
        super(id);
        this.licenseModel = new Model();
        Person loggedUser = EEGDataBaseSession.get().getLoggedUser();
        boolean hasActiveMembershipPlan = personMembershipPlanFacade.hasActiveMembershipPlan(loggedUser);

        licenses = new LoadableDetachableModel<List<License>>() {

            @Override
            protected List<License> load() {
                List<License> l = licenseFacade.getLicensesForExperiment(model.getObject().getExperimentId());
                return l;
            }
        };

        AjaxDropDownChoice<License> licensesChoice = new AjaxDropDownChoice<License>("licenses",licenseModel,licenses, new ChoiceRenderer<License>("title", "licenseId")){

            @Override
            protected void onSelectionChangeAjaxified(AjaxRequestTarget target, License option) {
                super.onSelectionChangeAjaxified(target, option);

                target.add(viewLicenseLink);

            }

        };

        licensesChoice.setVisibilityAllowed(hasActiveMembershipPlan);
        add(licensesChoice);

        viewLicenseWindow = new ModalWindow("viewLicenseWindow");
        viewLicenseWindow.setAutoSize(true);
        viewLicenseWindow.setResizable(false);
        viewLicenseWindow.setMinimalWidth(600);
        viewLicenseWindow.setWidthUnit("px");
        add(viewLicenseWindow);

        viewLicenseWindow.setContent(new ViewLicensePanel(viewLicenseWindow.getContentId(), licenseModel));
        viewLicenseWindow.setTitle(ResourceUtils.getModel("dataTable.heading.licenseTitle"));
        viewLicenseLink = new AjaxLink<License>("viewLicenseLink", licenseModel) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                License l = this.getModelObject();
                if (l!=null) System.out.println(l.getTitle());
                viewLicenseWindow.show(target);
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                System.out.println(licenseModel.getObject());
                this.setVisible(this.getModelObject()!=null);
            }

        };
        viewLicenseLink.setOutputMarkupPlaceholderTag(true);
        add(viewLicenseLink);
    }
}
