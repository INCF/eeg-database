package cz.zcu.kiv.eegdatabase.wui.ui.administration.forms;

import cz.zcu.kiv.eegdatabase.data.pojo.MembershipPlan;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.MembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdministrationPageLeftMenu;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.validation.validator.PatternValidator;
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
        setPageTitle(ResourceUtils.getModel("pageTitle.addMembershipPlan"));
        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()));
        add(new MembershipPlanForm("form",new Model<MembershipPlan>(new MembershipPlan()),membershipPlanFacade));

    }

    private class MembershipPlanForm extends Form<MembershipPlan> {

        private static final long serialVersionUID = 3335277334799636281L;

        public MembershipPlanForm(String id, IModel<MembershipPlan> model,final MembershipPlanFacade membershipPlanFacade) {

            super(id,new CompoundPropertyModel <MembershipPlan>(model));

            TextField<String> name = new TextField<String>("name");
            name.setLabel(ResourceUtils.getModel("label.name"));
            name.setRequired(true);
            name.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
            FormComponentLabel nameLabel = new FormComponentLabel("nameLb", name);
            add(name, nameLabel);

            TextArea<String> description = new TextArea<String>("description");
            description.setLabel(ResourceUtils.getModel("label.description"));
            description.add(StringValidator.maximumLength(255));
            FormComponentLabel descriptionLabel = new FormComponentLabel("descriptionLb", description);
            add(description,descriptionLabel);

            TextField<String> length = new TextField<String>("length");
            length.setLabel(ResourceUtils.getModel("label.length"));
            length.setRequired(true);
            length.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
            FormComponentLabel lengthLabel = new FormComponentLabel("lengthLb", length);
            add(length,lengthLabel);

            TextField<String> price = new TextField<String>("price");
            price.setLabel(ResourceUtils.getModel("label.price"));
            price.setRequired(true);
            price.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
            FormComponentLabel priceLabel = new FormComponentLabel("priceLb", price);
            add(price,priceLabel);
/*
            RadioChoice<Character> type = new RadioChoice<Character>("type", Gender.getShortcutList(), new ChoiceRenderer<Character>() {

                private static final long serialVersionUID = 1L;

                @Override
                public Object getDisplayValue(Character object) {
                    Gender enumValue = Gender.getGenderByShortcut(object);
                    return getString(Classes.simpleName(enumValue.getDeclaringClass()) + '.' + enumValue.name());
                }

            });
            gender.setSuffix("\n");
            gender.setRequired(true);
            gender.setLabel(ResourceUtils.getModel("label.gender"));
            FormComponentLabel genderLabel = new FormComponentLabel("genderLb", gender);
            add(gender, genderLabel);

*/



        }
    }
}
