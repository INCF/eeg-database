package cz.zcu.kiv.eegdatabase.wui.ui.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamValId;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.IButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.SimpleDownloadLink;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileFacade;
import cz.zcu.kiv.eegdatabase.wui.core.file.metadata.FileMetadataParamFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPageLeftMenu;

/**
 * Page of detail on data file. Form for add file parameter action.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class DataFileDetailPage extends MenuPage {

    private static final long serialVersionUID = 2432720497396689233L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    FileFacade fileFacade;

    @SpringBean
    ExperimentsFacade experimentFacade;

    @SpringBean
    ResearchGroupFacade groupFacade;

    @SpringBean
    SecurityFacade security;

    @SpringBean
    FileMetadataParamFacade metadataParamFacade;

    public DataFileDetailPage(PageParameters parameters) {

        int id = parseParameters(parameters);

        setupComponents(id);

    }

    private void setupComponents(int id) {

        setPageTitle(ResourceUtils.getModel("pageTitle.experimentDetail"));

        add(new ButtonPageMenu("leftMenu", getLeftMenu()));

        DataFile file = fileFacade.read(id);
        Experiment experiment = experimentFacade.read(file.getExperiment().getExperimentId());

        add(new Label("researchGroupTitle", new PropertyModel<DataFile>(groupFacade.read(experiment.getResearchGroup().getResearchGroupId()), "title")));
        add(new Label("filename", new PropertyModel<DataFile>(file, "filename")));
        add(new Label("description", new PropertyModel<DataFile>(file, "description")));
        
        // list of file parameters
        PropertyListView<FileMetadataParamVal> params = new PropertyListView<FileMetadataParamVal>("params", new ListModel<FileMetadataParamVal>(new ArrayList<FileMetadataParamVal>(
                file.getFileMetadataParamVals()))) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<FileMetadataParamVal> item) {
                item.add(new Label("paramName", item.getModelObject().getFileMetadataParamDef().getParamName()));
                item.add(new Label("metadataValue", item.getModelObject().getMetadataValue()));

            }
        };

        boolean coexperimenter = security.userIsOwnerOrCoexpOfCorrespExperiment(file.getDataFileId());
        BookmarkablePageLink<Void> backLink = new BookmarkablePageLink<Void>("backLink", ExperimentsDetailPage.class, PageParametersUtils.getDefaultPageParameters(file.getExperiment()
                .getExperimentId()));
        SimpleDownloadLink downloadLink = new SimpleDownloadLink("downloadLink", id);
        
        // form for add file parameter action
        add(new AddMetadataForm("form", metadataParamFacade, coexperimenter, id, experiment.getResearchGroup().getResearchGroupId(), getFeedback()).add(params));
        add(backLink, downloadLink);
    }

    private IButtonPageMenu[] getLeftMenu() {
        return new IButtonPageMenu[] {
                ExperimentsPageLeftMenu.LIST_OF_EXPERIMENTS,
                ExperimentsPageLeftMenu.LIST_OF_EXPERIMENTS_AS_OWNER,
                ExperimentsPageLeftMenu.LIST_OF_EXPERIMENTS_AS_SUBJECT,
                ExperimentsPageLeftMenu.SEARCH,
                ExperimentsPageLeftMenu.ADD_EXPERIMENTS };
    }

    private int parseParameters(PageParameters parameters) {

        StringValue value = parameters.get(BasePage.DEFAULT_PARAM_ID);
        if (value.isNull() || value.isEmpty())
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
        return value.toInt();
    }
    
    // inner form for add file parameter action.
    class AddMetadataForm extends Form<FileMetadataParamVal> {

        private static final long serialVersionUID = 1L;

        public AddMetadataForm(String id, final FileMetadataParamFacade metadataParamDefFacade, boolean coexperimenter, final int fileId, final int researchGroupId, final FeedbackPanel feedback) {
            super(id, new CompoundPropertyModel<FileMetadataParamVal>(new FileMetadataParamVal()));
            this.setOutputMarkupPlaceholderTag(true);
            
            List<FileMetadataParamDef> listMetadataDef = metadataParamDefFacade.getRecordsByGroup(researchGroupId);
            WebMarkupContainer formComponents = new WebMarkupContainer("container");

            DropDownChoice<FileMetadataParamDef> list = new DropDownChoice<FileMetadataParamDef>("fileMetadataParamDef", new ListModel<FileMetadataParamDef>(listMetadataDef),
                    new ChoiceRenderer<FileMetadataParamDef>("paramName", "fileMetadataParamDefId"));
            list.setRequired(true);
            list.setLabel(ResourceUtils.getModel("dataTable.heading.parameterName"));

            TextField<String> paramValue = new TextField<String>("metadataValue");
            paramValue.setRequired(true);
            paramValue.setLabel(ResourceUtils.getModel("dataTable.heading.value"));

            AjaxButton submit = new AjaxButton("submit", AddMetadataForm.this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    FileMetadataParamVal obj = AddMetadataForm.this.getModelObject();

                    FileMetadataParamVal metadata = new FileMetadataParamVal();
                    FileMetadataParamValId id = new FileMetadataParamValId(obj.getFileMetadataParamDef().getFileMetadataParamDefId(), fileId);
                    
                    if(metadataParamDefFacade.read(id) != null){
                        error(ResourceUtils.getString("invalid.paramIdAlreadyInserted"));
                        target.add(feedback);
                    } else {
                        
                    metadata.setId(id);
                    metadata.setMetadataValue(obj.getMetadataValue());
                    
                    metadataParamDefFacade.create(metadata);
                    setResponsePage(DataFileDetailPage.class, PageParametersUtils.getDefaultPageParameters(fileId));
                    }
                }
                
                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }

            };

            formComponents.add(list, paramValue, submit);
            formComponents.setVisibilityAllowed(coexperimenter);
            add(formComponents);
        }

    }
}
