package cz.zcu.kiv.eegdatabase.wui.components.form.input;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 *
 * @param <T>
 * @author Jakub Daněk
 */
public class AjaxDropDownChoice<T> extends DropDownChoice<T> {

    public AjaxDropDownChoice(String id) {
        super(id, new Model(), new ArrayList<T>());
    }

    public AjaxDropDownChoice(String id, IModel<T> model, IModel<? extends List<? extends T>> choices, IChoiceRenderer<? super T> renderer) {
        super(id, model, choices, renderer);
    }

    public AjaxDropDownChoice(String id, IModel<? extends List<? extends T>> choices, IChoiceRenderer<? super T> renderer) {
        super(id, new Model(), choices, renderer);
    }

    public AjaxDropDownChoice(String id, IModel<T> model, IModel<? extends List<? extends T>> choices) {
        super(id, model, choices);
    }

    public AjaxDropDownChoice(String id, IModel<? extends List<? extends T>> choices) {
        super(id, new Model(), choices);
    }

    public AjaxDropDownChoice(String id, IModel<T> model, List<? extends T> choices, IChoiceRenderer<? super T> renderer) {
        super(id, model, choices, renderer);
    }

    public AjaxDropDownChoice(String id, IModel<T> model, List<? extends T> choices) {
        super(id, model, choices);
    }

    public AjaxDropDownChoice(String id, List<? extends T> choices, IChoiceRenderer<? super T> renderer) {
        super(id, new Model(), choices, renderer);
    }

    public AjaxDropDownChoice(String id, List<? extends T> choices) {
        super(id, new Model(), choices);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new AjaxFormComponentUpdatingBehavior("onchange") {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                onSelectionChangeAjaxified(target, getModelObject());
            }
        });
    }

    protected void onSelectionChangeAjaxified(AjaxRequestTarget target, T option) {
        //override
    }

}
