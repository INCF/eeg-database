package cz.zcu.kiv.eegdatabase.wui.ui.administration.forms;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.MembershipPlanType;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdminManageLicensesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdminManageMembershipPlansPage;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdministrationPageLeftMenu;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Created by Lichous on 4.5.15.
 */

@AuthorizeInstantiation(value = {"ROLE_ADMIN" })
public class LicenseManageFormPage extends MenuPage {
    private static final long serialVersionUID = -1642766215588431080L;

    @SpringBean
    LicenseFacade licenseFacade;

    public LicenseManageFormPage() {
        add(new Label("headTitle", ResourceUtils.getModel("pageTitle.addLicenseTemplate")));
        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()));
        add(new LicenseForm("form",new Model<License>(new License()),licenseFacade,getFeedback()));

    }

    public LicenseManageFormPage(PageParameters parameters) {
        StringValue licenseId = parameters.get(DEFAULT_PARAM_ID);
        if (licenseId.isNull() || licenseId.isEmpty())
            throw new RestartResponseAtInterceptPageException(AdminManageLicensesPage.class);

        License license = licenseFacade.read(licenseId.toInteger());

        /*
        MembershipPlan newPlan = new MembershipPlan();
        newPlan.setPlan(membershipPlan);
        membershipPlan.setValid(false);
        membershipPlanFacade.update(membershipPlan);
        membershipPlanFacade.create(newPlan);
        */

        add(new Label("headTitle", ResourceUtils.getModel("pageTitle.editLicenseTemplate")));
        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()));
        add(new LicenseForm("form",new Model<License>(license),licenseFacade,getFeedback()));

    }

    private class LicenseForm extends Form<License> {

        public LicenseForm(String id, IModel<License> model,final LicenseFacade licenseFacade, final FeedbackPanel feedback) {
            super(id,new CompoundPropertyModel<License>(model));


            //headTitle
            //title,titleLb
            //descriptionLb,description
            //priceLb,price
            //typeLb
            TextField<String> name = new TextField<String>("title");
            name.setLabel(ResourceUtils.getModel("label.name"));
            name.setRequired(true);
            name.add(StringValidator.maximumLength(255));
            name.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
            FormComponentLabel nameLabel = new FormComponentLabel("titleLb", name);
            add(name, nameLabel);

            TextArea<String> description = new TextArea<String>("description");
            description.setLabel(ResourceUtils.getModel("label.description"));
            description.add(StringValidator.maximumLength(255));
            FormComponentLabel descriptionLabel = new FormComponentLabel("descriptionLb", description);
            add(description,descriptionLabel);

            TextField<Integer> price = new TextField<Integer>("price",Integer.class);
            price.setLabel(ResourceUtils.getModel("label.price"));
            price.setRequired(false);
            price.add(RangeValidator.minimum(1));
            FormComponentLabel priceLabel = new FormComponentLabel("priceLb", price);
            add(price,priceLabel);


            RadioGroup<LicenseType> type = new RadioGroup<LicenseType>("licenseType", new PropertyModel<LicenseType>(model, "licenseType"));
            type.setLabel(ResourceUtils.getModel("label.license.type"));
            type.setRequired(true);
            type.add(new Radio("public", new Model(LicenseType.OPEN_DOMAIN)));
            type.add(new Radio("academic", new Model(LicenseType.ACADEMIC)));
            type.add(new Radio("business", new Model(LicenseType.BUSINESS)));

            FormComponentLabel typeLabel = new FormComponentLabel("typeLb", type);
            add(type,typeLabel);


        }
    }

}
