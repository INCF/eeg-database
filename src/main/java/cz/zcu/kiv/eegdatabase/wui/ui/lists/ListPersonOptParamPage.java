package cz.zcu.kiv.eegdatabase.wui.ui.lists;

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

import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.CoreConstants;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.param.PersonOptParamFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ListModelWithResearchGroupCriteria;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ResearchGroupSelectForm;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.form.PersonOptParamFormPage;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ListPersonOptParamPage extends MenuPage {

    private static final long serialVersionUID = 1429684247549031445L;

    @SpringBean
    PersonOptParamFacade facade;

    @SpringBean
    ResearchGroupFacade researchGroupFacade;

    @SpringBean
    SecurityFacade security;

    public ListPersonOptParamPage() {

        setupComponents();
    }

    private void setupComponents() {

        setPageTitle(ResourceUtils.getModel("pageTitle.personOptionalParameterList"));
        add(new ButtonPageMenu("leftMenu", ListsLeftPageMenu.values()));

        final WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupPlaceholderTag(true);

        final ListModelWithResearchGroupCriteria<PersonOptParamDef> model = new ListModelWithResearchGroupCriteria<PersonOptParamDef>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected List<PersonOptParamDef> loadList(ResearchGroup group) {
                if (group == null)
                    return facade.getDefaultRecords();
                else {
                    return facade.getRecordsByGroup(group.getResearchGroupId());
                }
            }
        };

        List<ResearchGroup> groups;
        final boolean isAdmin = security.isAdmin();
        final boolean isExperimenter = security.userIsExperimenter();
        if (isAdmin) {
            ResearchGroup defaultGroup = new ResearchGroup(CoreConstants.DEFAULT_ITEM_ID,
                    EEGDataBaseSession.get().getLoggedUser(), ResourceUtils.getString("label.defaultPersonOptParamDef"), "-");
            groups = researchGroupFacade.getAllRecords();
            groups.add(0, defaultGroup);
        } else
            groups = researchGroupFacade.getResearchGroupsWhereMember(EEGDataBaseSession.get().getLoggedUser());

        PropertyListView<PersonOptParamDef> params = new PropertyListView<PersonOptParamDef>("params", model) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<PersonOptParamDef> item) {
                item.add(new Label("personOptParamDefId"));
                item.add(new Label("paramName"));
                item.add(new Label("paramDataType"));

                PageParameters parameters = PageParametersUtils.getDefaultPageParameters(item.getModelObject().getPersonOptParamDefId())
                        .add(PageParametersUtils.GROUP_PARAM, model.getCriteriaModel().getObject().getResearchGroupId());

                item.add(new BookmarkablePageLink<Void>("edit", PersonOptParamFormPage.class, parameters));
                item.add(new AjaxLink<Void>("delete") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {

                        int id = item.getModelObject().getPersonOptParamDefId();
                        ResearchGroup group = model.getCriteriaModel().getObject();

                        if (facade.canDelete(id)) {
                            if (group != null) {
                                int groupId = group.getResearchGroupId();

                                if (groupId == CoreConstants.DEFAULT_ITEM_ID) { // delete default weather if it's from default group
                                    if (!facade.hasGroupRel(id)) { // delete only if it doesn't have group relationship
                                        facade.delete(item.getModelObject());
                                        getFeedback().info(ResourceUtils.getString("text.itemWasDeletedFromDatabase"));
                                    } else {
                                        getFeedback().error(ResourceUtils.getString("text.itemInUse"));
                                    }
                                } else {
                                    PersonOptParamDefGroupRel h = facade.getGroupRel(id, groupId);
                                    if (!facade.isDefault(id)) { // delete only non default weather
                                        facade.delete(item.getModelObject());
                                    }
                                    facade.deleteGroupRel(h);
                                    getFeedback().info(ResourceUtils.getString("text.itemWasDeletedFromDatabase"));
                                }
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
        container.add(params);

        AjaxLink<Void> link = new AjaxLink<Void>("addParamLink") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                int researchGroupId = model.getCriteriaModel().getObject().getResearchGroupId();
                setResponsePage(PersonOptParamFormPage.class, PageParametersUtils.getPageParameters(PageParametersUtils.GROUP_PARAM, researchGroupId));
            }
        };
        link.setVisibilityAllowed(isAdmin || isExperimenter);

        add(new ResearchGroupSelectForm("form", model.getCriteriaModel(), new ListModel<ResearchGroup>(groups), container, isAdmin));
        add(link, container);
    }
}
