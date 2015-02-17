package cz.zcu.kiv.eegdatabase.wui.ui.pricing;

import java.math.BigDecimal;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.RangeValidator;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

public abstract class PriceChangePanel extends Panel {

    private static final long serialVersionUID = 4551392100225147976L;

    public PriceChangePanel(String id, IModel<BigDecimal> model, final FeedbackPanel feedback) {
        this(id, model, feedback, null);
    }

    public PriceChangePanel(String id, IModel<BigDecimal> model, final FeedbackPanel feedback, String cssFieldstyle) {
        super(id, model);

        setupComponents(model, feedback, cssFieldstyle);
    }

    private void setupComponents(IModel<BigDecimal> model, final FeedbackPanel feedback, String cssFieldstyle) {
        TextField<BigDecimal> changePriceField = new TextField<BigDecimal>("field", model);
        changePriceField.add(new AjaxFormComponentUpdatingBehavior("onChange") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                PriceChangePanel.this.onUpdate(target);
                target.add(feedback);
            }

            @Override
            protected void onError(AjaxRequestTarget target, RuntimeException e) {
                super.onError(target, e);
                target.add(feedback);
            }

        });

        if (cssFieldstyle != null)
            changePriceField.add(new AttributeModifier("style", cssFieldstyle));

        changePriceField.setLabel(ResourceUtils.getModel("dataTable.heading.price.change"));
        changePriceField.add(RangeValidator.minimum(BigDecimal.ZERO));
        add(changePriceField);
    }

    abstract void onUpdate(AjaxRequestTarget target);

}
