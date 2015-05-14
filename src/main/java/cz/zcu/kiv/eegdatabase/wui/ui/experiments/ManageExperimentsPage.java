package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonMembershipPlan;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxDropDownChoice;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampPropertyColumn;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.PersonMembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.components.ExperimentBuyDownloadLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.components.LicenseDropDownChoicePanel;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.components.LicenseEditForm;
import cz.zcu.kiv.eegdatabase.wui.ui.signalProcessing.MethodListPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.markup.html.basic.Label;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lichous on 23.4.15.
 */

@AuthorizeInstantiation(value = {"ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ManageExperimentsPage extends MenuPage {

    private static final long serialVersionUID = 612188590190861897L;
    private static final int ITEMS_PER_PAGE = 20;

    private ModalWindow addLicenseWindow;

    protected Log log = LogFactory.getLog(getClass());

    private IModel<License> licenseModel;

    private IModel<List<License>> licenses;

    @SpringBean
    private ExperimentsFacade experimentsFacade;

    @SpringBean
    private LicenseFacade licenseFacade;

    @SpringBean
    private PersonMembershipPlanFacade personMembershipPlanFacade;

    public ManageExperimentsPage(){

        this.add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));
        this.licenseModel = new Model();
        setupComponents();

    }

    private void setupComponents() {

        Person loggedUser = EEGDataBaseSession.get().getLoggedUser();

        add(new Label("title", ResourceUtils.getModel("pageTitle.manageExperiments")));

        boolean owner = true;
        boolean subject = false;

        DefaultDataTable<Experiment, String> list = new DefaultDataTable<Experiment, String>("experiments", createListColumns(),
                new ListExperimentsDataProvider(experimentsFacade, loggedUser, owner, subject), ITEMS_PER_PAGE);

        add(list);

    }

    private List<? extends IColumn<Experiment, String>> createListColumns() {
        List<IColumn<Experiment, String>> columns = new ArrayList<IColumn<Experiment, String>>();

        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.number"), "experimentId", "experimentId"));
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.scenarioTitle"), "scenario.title", "scenario.title"));
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.date"), "startTime", "startTime"));
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.gender"), "personBySubjectPersonId.gender", "personBySubjectPersonId.gender"));
        columns.add(new TimestampPropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.yearOfBirth"), "personBySubjectPersonId.dateOfBirth",
                "personBySubjectPersonId.dateOfBirth", "yyyy"));


        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.detail"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
                item.add(new ViewLinkPanel(componentId, ExperimentsDetailPage.class, "experimentId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });

        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.licenses"), null, null) {

            @Override
            public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, final IModel<Experiment> rowModel) {

                item.add(new LicenseDropDownChoicePanel(componentId,rowModel, ManageExperimentsPage.class, true));
            }
        });



        return columns;

    }

}
