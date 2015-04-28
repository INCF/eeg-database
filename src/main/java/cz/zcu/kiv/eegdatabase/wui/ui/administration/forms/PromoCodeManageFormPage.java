package cz.zcu.kiv.eegdatabase.wui.ui.administration.forms;

import cz.zcu.kiv.eegdatabase.data.pojo.PromoCode;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.DateTimeFieldPicker;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampConverter;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.MembershipPlanType;
import cz.zcu.kiv.eegdatabase.wui.core.promocode.PromoCodeFacade;
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
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Created by Lichous on 20.4.15.
 */

@AuthorizeInstantiation(value = {"ROLE_ADMIN" })
public class PromoCodeManageFormPage extends MenuPage {

    private static final long serialVersionUID = -4202562313231803528L;

    @SpringBean
    PromoCodeFacade promoCodeFacade;

    public PromoCodeManageFormPage() {
        add(new Label("title", ResourceUtils.getModel("pageTitle.addPromoCode")));
        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()));
        add(new PromoCodeForm("form",new Model<PromoCode>(new PromoCode()),promoCodeFacade,getFeedback()));

    }

    public PromoCodeManageFormPage(PageParameters parameters) {
        StringValue promoCodeId = parameters.get(DEFAULT_PARAM_ID);
        if (promoCodeId.isNull() || promoCodeId.isEmpty())
            throw new RestartResponseAtInterceptPageException(AdminManageMembershipPlansPage.class);

        PromoCode promoCode = promoCodeFacade.getPromoCodeById(promoCodeId.toInteger());
        PromoCode newCode = new PromoCode();
        newCode.setPromoCode(promoCode);
        promoCode.setValid(false);
        promoCodeFacade.update(promoCode);
        promoCodeFacade.create(newCode);

        add(new Label("title", ResourceUtils.getModel("pageTitle.editPromoCode")));
        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()));
        add(new PromoCodeForm("form",new Model<PromoCode>(newCode),promoCodeFacade,getFeedback()));

    }



    private class PromoCodeForm extends Form<PromoCode> {

        public PromoCodeForm(String id, IModel<PromoCode> model,final PromoCodeFacade promoCodeFacade, final FeedbackPanel feedback) {
            super(id,new CompoundPropertyModel<PromoCode>(model));

            TextField<String> keyword = new TextField<String>("keyword");
            keyword.setLabel(ResourceUtils.getModel("label.keyword"));
            keyword.setRequired(true);
            keyword.add(StringValidator.maximumLength(255));
            //name.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
            FormComponentLabel keywordLabel = new FormComponentLabel("keywordLb", keyword);
            add(keyword, keywordLabel);

            TextArea<String> description = new TextArea<String>("description");
            description.setLabel(ResourceUtils.getModel("label.description"));
            description.add(StringValidator.maximumLength(255));
            FormComponentLabel descriptionLabel = new FormComponentLabel("descriptionLb", description);
            add(description,descriptionLabel);

            TextField<Integer> discount = new TextField<Integer>("discount",Integer.class);
            discount.setLabel(ResourceUtils.getModel("label.discount"));
            discount.setRequired(true);
            discount.add(RangeValidator.minimum(1));
            discount.add(RangeValidator.maximum(100));
            FormComponentLabel discountLabel = new FormComponentLabel("discountLb", discount);
            add(discount, discountLabel);

            DateTimeFieldPicker from = new DateTimeFieldPicker("from") {

                private static final long serialVersionUID = 1L;

                @Override
                public <C> IConverter<C> getConverter(Class<C> type) {
                    return (IConverter<C>) new TimestampConverter();
                }
            };

            from.setLabel(ResourceUtils.getModel("label.From"));
            from.setRequired(true);
            FormComponentLabel fromLabel = new FormComponentLabel("fromLb", from);
            add(from,fromLabel);

            DateTimeFieldPicker to = new DateTimeFieldPicker("to") {

                private static final long serialVersionUID = 1L;

                @Override
                public <C> IConverter<C> getConverter(Class<C> type) {
                    return (IConverter<C>) new TimestampConverter();
                }
            };

            to.setLabel(ResourceUtils.getModel("label.To"));
            to.setRequired(true);
            FormComponentLabel toLabel = new FormComponentLabel("toLb", to);
            add(to,toLabel);

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


            //TODO FROM, TO


            AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.savePromoCode"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    PromoCode promoCode = PromoCodeForm.this.getModelObject();
                    boolean isEdit = promoCode.getPromoCodeId() > 0;

                    if (validation(promoCode)) {
                        if (isEdit) {
                            promoCodeFacade.update(promoCode);
                        } else {
                            promoCodeFacade.create(promoCode);
                        }
                        setResponsePage(AdminManageMembershipPlansPage.class);
                    }
                    target.add(feedback);
                }
            };
            add(submit);
        }

        private boolean validation (PromoCode promoCode) {
            return true;
        }


}
}