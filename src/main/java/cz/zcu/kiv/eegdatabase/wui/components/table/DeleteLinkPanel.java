package cz.zcu.kiv.eegdatabase.wui.components.table;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.MembershipPlan;
import cz.zcu.kiv.eegdatabase.data.pojo.PromoCode;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxConfirmLink;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.MembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.core.promocode.PromoCodeFacade;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.lang.reflect.Member;

/**
 * Created by Lichous on 21.4.15.
 */


public class DeleteLinkPanel extends Panel {

    private static final long serialVersionUID = -4501432748937938302L;

    @SpringBean
    MembershipPlanFacade membershipPlanFacade;

    @SpringBean
    PromoCodeFacade promoCodeFacade;

    @SpringBean
    LicenseFacade licenseFacade;

    public DeleteLinkPanel(String id, final Class<? extends MenuPage> page,String propertyExpression, final IModel model, IModel<String> displayModel, String confirmMessage) {

        super(id);
        final PropertyModel paramModel = new PropertyModel(model, propertyExpression);

        System.out.println((Integer)paramModel.getObject());
        System.out.println(model.getClass());



        add(new AjaxConfirmLink<Void>("link", model, confirmMessage) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {

                if(model.getObject().getClass() == MembershipPlan.class) {
                    MembershipPlan plan = membershipPlanFacade.getMembershipPlanById((Integer)paramModel.getObject());
                    plan.setValid(false);
                    membershipPlanFacade.update(plan);
                } else if(model.getObject().getClass() == PromoCode.class)   {
                    PromoCode code = promoCodeFacade.getPromoCodeById((Integer)paramModel.getObject());
                    code.setValid(false);
                    promoCodeFacade.update(code);
                    //System.out.println(promoCodeFacade.getPromoCodeByKeyword("ABCK"));
                    //System.out.println(promoCodeFacade.getPromoCodeByKeyword("FFDSFSDG"));
                } else if (model.getObject().getClass() == License.class) {
                    License license = licenseFacade.read((Integer)paramModel.getObject());
                    licenseFacade.delete(license);

                }
                setResponsePage(page);
            }

        });

    }
}
