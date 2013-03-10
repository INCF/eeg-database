package cz.zcu.kiv.eegdatabase.wui.ui.lists;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.Artifact;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.ArtifactFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ListModelWithResearchGroupCriteria;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ResearchGroupSelectForm;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.form.ArtifactFormPage;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ListArtifactDefinitionsPage extends MenuPage {

    private static final long serialVersionUID = -8825403613513488101L;

    @SpringBean
    ArtifactFacade facade;

    @SpringBean
    ResearchGroupFacade researchGroupFacade;

    @SpringBean
    SecurityFacade security;

    public ListArtifactDefinitionsPage() {

        setupComponents();
    }

    private void setupComponents() {

        setPageTitle(ResourceUtils.getModel("pageTitle.artifactList"));
        add(new ButtonPageMenu("leftMenu", ListsLeftPageMenu.values()));

        final WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupPlaceholderTag(true);

        final ListModelWithResearchGroupCriteria<Artifact> model = new ListModelWithResearchGroupCriteria<Artifact>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected List<Artifact> loadList(ResearchGroup group) {
                if (group == null)
                    return Collections.EMPTY_LIST;
                else {
                    return facade.getRecordsByGroup(group.getResearchGroupId());
                }
            }
        };

        List<ResearchGroup> groups;
        final boolean isAdmin = security.isAdmin();
        final boolean isExperimenter = security.userIsExperimenter();
        if (isAdmin)
            groups = researchGroupFacade.getAllRecords();
        else
            groups = researchGroupFacade.getResearchGroupsWhereMember(EEGDataBaseSession.get().getLoggedUser());

        PropertyListView<Artifact> artifacts = new PropertyListView<Artifact>("artifacts", model) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<Artifact> item) {
                item.add(new Label("artifactId"));
                item.add(new Label("compensation"));
                item.add(new Label("rejectCondition"));

                PageParameters parameters = PageParametersUtils.getDefaultPageParameters(item.getModelObject().getArtifactId())
                        .add(PageParametersUtils.GROUP_PARAM, model.getCriteriaModel().getObject().getResearchGroupId());

                item.add(new BookmarkablePageLink<Void>("edit", ArtifactFormPage.class, parameters));
                item.add(new AjaxLink<Void>("delete") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {

                        int id = item.getModelObject().getArtifactId();
                        ResearchGroup group = model.getCriteriaModel().getObject();

                        if (facade.canDelete(id)) {
                            if (group != null) {
                                facade.deleteGroupRel(item.getModelObject(), researchGroupFacade.getResearchGroupById(group.getResearchGroupId()));
                                facade.delete(item.getModelObject());
                                getFeedback().info(ResourceUtils.getString("text.itemWasDeletedFromDatabase"));
                            }
                        } else {
                            getFeedback().error(ResourceUtils.getString("text.itemInUse"));
                        }

                        target.add(container);
                        target.add(getFeedback());
                    }

                    @Override
                    protected void updateAjaxAttributes(AjaxRequestAttributes attributes)
                    {
                        super.updateAjaxAttributes(attributes);

                        AjaxCallListener ajaxCallListener = new AjaxCallListener();
                        ajaxCallListener.onPrecondition("return confirm('Are you sure you want to delete item?');");
                        attributes.getAjaxCallListeners().add(ajaxCallListener);
                    }

                }.setVisibilityAllowed(isAdmin || isExperimenter));

            }
        };
        container.add(artifacts);

        AjaxLink<Void> link = new AjaxLink<Void>("addArtifactLink") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                int researchGroupId = model.getCriteriaModel().getObject().getResearchGroupId();
                setResponsePage(ArtifactFormPage.class, PageParametersUtils.getPageParameters(PageParametersUtils.GROUP_PARAM, researchGroupId));
            }
        };
        link.setVisibilityAllowed(isAdmin || isExperimenter);

        add(new ResearchGroupSelectForm("form", model.getCriteriaModel(), new ListModel<ResearchGroup>(groups), container, isAdmin));
        add(link, container);
    }

}
