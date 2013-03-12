package cz.zcu.kiv.eegdatabase.wui.ui.people.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamValId;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.param.PersonOptParamFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.people.ListPersonPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.PersonDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.PersonPageLeftMenu;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class PersonAddParamFormPage extends MenuPage {

    private static final long serialVersionUID = 6562681123141402753L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    PersonOptParamFacade facade;

    @SpringBean
    PersonFacade personFacade;

    public PersonAddParamFormPage(PageParameters parameters) {

        StringValue paramId = parameters.get(DEFAULT_PARAM_ID);

        if (paramId.isNull() || paramId.isEmpty())
            throw new RestartResponseAtInterceptPageException(ListPersonPage.class);

        setPageTitle(ResourceUtils.getModel("pageTitle.addPersonOptionalParameter"));

        add(new ButtonPageMenu("leftMenu", PersonPageLeftMenu.values()));

        PersonOptParamVal param = new PersonOptParamVal();
        param.setPerson(personFacade.getPersonForDetail(paramId.toInt()));

        add(new PersonAddParamForm("form", new Model<PersonOptParamVal>(param), facade, getFeedback()));
    }

    class PersonAddParamForm extends Form<PersonOptParamVal> {

        private static final long serialVersionUID = 1L;

        public PersonAddParamForm(String id, IModel<PersonOptParamVal> model, final PersonOptParamFacade facade, final FeedbackPanel feedback) {

            super(id, new CompoundPropertyModel<PersonOptParamVal>(model));

            DropDownChoice<PersonOptParamDef> params = new DropDownChoice<PersonOptParamDef>("personOptParamDef", prepareList(facade, model.getObject().getPerson()),
                    new ChoiceRenderer<PersonOptParamDef>("paramName", "personOptParamDefId") {

                        private static final long serialVersionUID = 1L;

                        @Override
                        public Object getDisplayValue(PersonOptParamDef object) {
                            return object.getPersonOptParamDefId() + " " + super.getDisplayValue(object);
                        }

                    });
            params.setRequired(true);
            params.setLabel(ResourceUtils.getModel("label.parameterType"));

            TextField<String> paramValue = new TextField<String>("paramValue");
            paramValue.setLabel(ResourceUtils.getModel("label.parameterValue"));
            paramValue.setRequired(true);

            AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.addPersonOptionalParameter"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    PersonOptParamVal paramVal = PersonAddParamForm.this.getModelObject();

                    boolean isEdit = paramVal.getId() != null;

                    if (isEdit) {
                        facade.update(paramVal);
                    } else {

                        PersonOptParamValId id = new PersonOptParamValId(paramVal.getPerson().getPersonId(), paramVal.getPersonOptParamDef().getPersonOptParamDefId());
                        if (facade.read(id) == null) {
                            paramVal.setId(id);
                            facade.create(paramVal);
                        } else
                            error(ResourceUtils.getString("invalid.paramIdAlreadyInserted"));
                    }
                    setResponsePage(PersonDetailPage.class, PageParametersUtils.getDefaultPageParameters(paramVal.getPerson().getPersonId()));
                    target.add(feedback);
                }
            };

            add(params, paramValue, submit);
        }

        private List<PersonOptParamDef> prepareList(PersonOptParamFacade facade, Person person) {

            List<PersonOptParamDef> list = new ArrayList<PersonOptParamDef>();
            Person loggedUser = EEGDataBaseSession.get().getLoggedUser();

            if (loggedUser.getAuthority().equals("ROLE_ADMIN")) {
                list.addAll(facade.getAllRecords());
            } else {
                for (ResearchGroup group : person.getResearchGroups()) {
                    list.addAll(facade.getRecordsByGroup(group.getResearchGroupId()));
                }
            }
            return list;
        }

    }
}
