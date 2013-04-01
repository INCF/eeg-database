package cz.zcu.kiv.eegdatabase.wui.ui.lists.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDefGroupRelId;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.CoreConstants;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.param.PersonOptParamFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListPersonOptParamPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListsLeftPageMenu;

/**
 * Page add / edit action of person opt parameters.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class PersonOptParamFormPage extends MenuPage {

    private static final long serialVersionUID = 2786930680478056041L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    PersonOptParamFacade facade;

    @SpringBean
    ResearchGroupFacade groupFacade;

    public PersonOptParamFormPage(PageParameters parameters) {

        StringValue groupParam = parameters.get(PageParametersUtils.GROUP_PARAM);
        StringValue personParam = parameters.get(DEFAULT_PARAM_ID);

        if (groupParam.isNull() || groupParam.isEmpty())
            throw new RestartResponseAtInterceptPageException(ListPersonOptParamPage.class);

        add(new ButtonPageMenu("leftMenu", ListsLeftPageMenu.values()));

        if (personParam.isNull() || personParam.isEmpty())
            setupAddComponents(groupParam.toInt());
        else
            setupEditComponents(groupParam.toInt(), personParam.toInt());
    }

    private void setupAddComponents(int researchGroupId) {
        setPageTitle(ResourceUtils.getModel("pageTitle.addPersonOptionalParameter"));
        add(new Label("title", ResourceUtils.getModel("pageTitle.addPersonOptionalParameter")));

        PersonOptParamDef newPersonOptParam = new PersonOptParamDef();

        add(new PersonOptParamForm("form", new CompoundPropertyModel<PersonOptParamDef>(newPersonOptParam), new Model<ResearchGroup>(getGroup(researchGroupId)), getFeedback(), facade));
    }

    private void setupEditComponents(int researchGroupId, int personOptParamId) {
        setPageTitle(ResourceUtils.getModel("pageTitle.editPersonOptionalParameterDefinition"));
        add(new Label("title", ResourceUtils.getModel("pageTitle.editPersonOptionalParameterDefinition")));

        add(new PersonOptParamForm("form", new CompoundPropertyModel<PersonOptParamDef>(facade.read(personOptParamId)),
                new Model<ResearchGroup>(getGroup(researchGroupId)), getFeedback(), facade));
    }

    private ResearchGroup getGroup(int researchGroupId) {
        ResearchGroup group;
        if (researchGroupId == CoreConstants.DEFAULT_ITEM_ID)
            group = new ResearchGroup(CoreConstants.DEFAULT_ITEM_ID, EEGDataBaseSession.get().getLoggedUser(), ResourceUtils.getString("label.defaultPersonOptParamDef"), "-");
        else
            group = groupFacade.read(researchGroupId);
        return group;
    }

    private class PersonOptParamForm extends Form<PersonOptParamDef> {

        private static final long serialVersionUID = 1L;

        public PersonOptParamForm(String id, IModel<PersonOptParamDef> model, final Model<ResearchGroup> groupModel,
                final FeedbackPanel feedback, final PersonOptParamFacade facade) {
            super(id, model);

            TextField<String> group = new TextField<String>("group", new PropertyModel<String>(groupModel.getObject(), "title"));
            group.setEnabled(false);
            group.setLabel(ResourceUtils.getModel("label.researchGroup"));

            TextField<String> paramName = new TextField<String>("paramName");
            paramName.setLabel(ResourceUtils.getModel("label.parameterName"));
            paramName.setRequired(true);

            TextField<String> paramDataType = new TextField<String>("paramDataType");
            paramDataType.setLabel(ResourceUtils.getModel("label.parameterDataType"));
            paramDataType.setRequired(true);

            AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.save"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    this.setEnabled(false);
                    target.add(this);
                    target.add(feedback);
                    
                    PersonOptParamDef personOptParam = PersonOptParamForm.this.getModelObject();

                    int personOptParamId = personOptParam.getPersonOptParamDefId();
                    ResearchGroup group = groupModel.getObject();
                    int researchGroupId = group.getResearchGroupId();

                    if (personOptParamId > 0) {
                        // Editing
                        log.debug("Editing existing personOptParamDef object.");

                        if (facade.isDefault(personOptParamId)) {
                            
                            if (researchGroupId != CoreConstants.DEFAULT_ITEM_ID) {
                                // new personOptParamDef
                                PersonOptParamDef newPersonOptParamDef = new PersonOptParamDef();
                                newPersonOptParamDef.setDefaultNumber(0);
                                newPersonOptParamDef.setParamName(personOptParam.getParamName());
                                newPersonOptParamDef.setParamDataType(personOptParam.getParamDataType());
                                int newId = facade.create(newPersonOptParamDef);
                                PersonOptParamDefGroupRel rel = facade.getGroupRel(personOptParamId, researchGroupId);
                                // delete old rel, create new one
                                PersonOptParamDefGroupRelId newRelId = new PersonOptParamDefGroupRelId();
                                PersonOptParamDefGroupRel newRel = new PersonOptParamDefGroupRel();
                                newRelId.setPersonOptParamDefId(newId);
                                newRelId.setResearchGroupId(researchGroupId);
                                newRel.setId(newRelId);
                                newRel.setPersonOptParamDef(newPersonOptParamDef);
                                newRel.setResearchGroup(group);
                                facade.deleteGroupRel(rel);
                                facade.createGroupRel(newRel);
                            } else {
                                if (!facade.hasGroupRel(personOptParamId) && facade.canDelete(personOptParamId)) {
                                    facade.update(personOptParam);
                                } else {
                                    getFeedback().error(ResourceUtils.getString("text.itemInUse"));
                                    this.setEnabled(true);
                                    return;
                                }
                            }
                        } else {
                            facade.update(personOptParam);
                        }
                    } else {
                        
                        // Creating new
                        if (researchGroupId == CoreConstants.DEFAULT_ITEM_ID) {
                            log.debug("Creating new default personOptParamDef object.");
                            facade.createDefaultRecord(personOptParam);
                        } else {
                            log.debug("Creating new group personOptParamDef object.");
                            int pkPersonOptParamDef = facade.create(personOptParam);

                            PersonOptParamDefGroupRelId personOptParamDefGroupRelId = new PersonOptParamDefGroupRelId(pkPersonOptParamDef, researchGroupId);
                            PersonOptParamDefGroupRel personOptParamDefGroupRel = new PersonOptParamDefGroupRel(personOptParamDefGroupRelId, group, personOptParam);
                            facade.createGroupRel(personOptParamDefGroupRel);
                        }

                    }

                    setResponsePage(ListPersonOptParamPage.class);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }
            };

            add(submit, group, paramName, paramDataType);
        }

    }
}
