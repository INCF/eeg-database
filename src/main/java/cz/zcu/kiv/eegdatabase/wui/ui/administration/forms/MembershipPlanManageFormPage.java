package cz.zcu.kiv.eegdatabase.wui.ui.administration.forms;

import cz.zcu.kiv.eegdatabase.data.pojo.MembershipPlan;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.MembershipPlanType;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.MembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdminManageMembershipPlansPage;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdministrationPageLeftMenu;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Created by Lichous on 12.4.15.
 */

@AuthorizeInstantiation(value = {"ROLE_ADMIN" })
public class MembershipPlanManageFormPage extends MenuPage {
    private static final long serialVersionUID = -1273834828386853594L;

    @SpringBean
    MembershipPlanFacade membershipPlanFacade;

    public MembershipPlanManageFormPage() {
        add(new Label("title", ResourceUtils.getModel("pageTitle.addMembershipPlan")));
        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()));
        add(new MembershipPlanForm("form",new Model<MembershipPlan>(new MembershipPlan()),membershipPlanFacade,getFeedback()));

    }

    public MembershipPlanManageFormPage(PageParameters parameters) {
        StringValue membershipPlanId = parameters.get(DEFAULT_PARAM_ID);
        if (membershipPlanId.isNull() || membershipPlanId.isEmpty())
            throw new RestartResponseAtInterceptPageException(AdminManageMembershipPlansPage.class);

        MembershipPlan membershipPlan = membershipPlanFacade.getMembershipPlanById(membershipPlanId.toInteger());

        add(new Label("title", ResourceUtils.getModel("pageTitle.editMembershipPlan")));
        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()));
        add(new MembershipPlanForm("form",new Model<MembershipPlan>(membershipPlan),membershipPlanFacade,getFeedback()));

    }

    private class MembershipPlanForm extends Form<MembershipPlan> {

        private static final long serialVersionUID = 3335277334799636281L;

        public MembershipPlanForm(String id, IModel<MembershipPlan> model,final MembershipPlanFacade membershipPlanFacade, final FeedbackPanel feedback) {

            super(id,new CompoundPropertyModel <MembershipPlan>(model));

            TextField<String> name = new TextField<String>("name");
            name.setLabel(ResourceUtils.getModel("label.name"));
            name.setRequired(true);
            name.add(StringValidator.maximumLength(255));
            name.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
            FormComponentLabel nameLabel = new FormComponentLabel("nameLb", name);
            add(name, nameLabel);

            TextArea<String> description = new TextArea<String>("description");
            description.setLabel(ResourceUtils.getModel("label.description"));
            description.add(StringValidator.maximumLength(255));
            FormComponentLabel descriptionLabel = new FormComponentLabel("descriptionLb", description);
            add(description,descriptionLabel);

            TextField<Integer> length = new TextField<Integer>("length",Integer.class);
            length.setLabel(ResourceUtils.getModel("label.length"));
            length.setRequired(true);
            length.add(RangeValidator.minimum(1));
            FormComponentLabel lengthLabel = new FormComponentLabel("lengthLb", length);
            add(length,lengthLabel);

            TextField<Integer> price = new TextField<Integer>("price",Integer.class);
            price.setLabel(ResourceUtils.getModel("label.price"));
            price.setRequired(true);
            price.add(RangeValidator.minimum(1));
            FormComponentLabel priceLabel = new FormComponentLabel("priceLb", price);
            add(price,priceLabel);

            RadioChoice<Integer> type = new RadioChoice<Integer>("type", MembershipPlanType.getMembershipPlanTypes(), new ChoiceRenderer<Integer>() {

                private static final long serialVersionUID = 1L;

                @Override
                public Object getDisplayValue(Integer object) {
                    MembershipPlanType enumValue = MembershipPlanType.getMembershipPlanByType(object);
                    return getString(Classes.simpleName(enumValue.getDeclaringClass()) + '.' + enumValue.name());
                }

            });

            type.setSuffix("\n");
            type.setRequired(true);
            type.setLabel(ResourceUtils.getModel("label.type"));
            FormComponentLabel typeLabel = new FormComponentLabel("typeLb", type);
            add(type,typeLabel);


            AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.saveMembershipPlan"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    MembershipPlan plan = MembershipPlanForm.this.getModelObject();
                    boolean isEdit = plan.getMembershipId() > 0;

                    if (validation(plan)) {
                        if (isEdit) {
                            membershipPlanFacade.update(plan);
                        } else {
                            plan.setValid(true);
                            membershipPlanFacade.create(plan);
                        }
                        setResponsePage(AdminManageMembershipPlansPage.class);
                    }
                    target.add(feedback);
                }
            };
            add(submit);
        }

        private boolean validation (MembershipPlan plan) {

            return true;
        }
    }
}
