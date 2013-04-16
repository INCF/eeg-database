package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms;

import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.DiseaseFacade;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 11.4.13
 * Time: 12:46
 */
public class DiseaseForm extends Form<Disease> {
    @SpringBean
    DiseaseFacade diseaseFacade;


    public DiseaseForm(String id, final ModalWindow window) {
        super(id, new CompoundPropertyModel<Disease>(new Disease()));

        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);
        add(new Label("addDiseaseHeader", "Add new disease"));

        add(
                new RequiredTextField<String>("title").
                        add(new TitleExistsValidator())
        );
        add(new TextArea<String>("description").setRequired(true));

        add(
                new AjaxButton("submitForm", this) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        Disease disease = (Disease) form.getModelObject();

                        validate();
                        target.add(feedback);

                        if(!hasError()){
                            diseaseFacade.create(disease);
                            window.close(target);
                        }
                    }
                }.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                    }
                })
        );

        add(
                new AjaxButton("closeForm", this) {}.
                        add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        window.close(target);
                    }
                })
        );

        setOutputMarkupId(true);
        AjaxFormValidatingBehavior.addToAllFormComponents(this, "focus", Duration.ONE_SECOND);
    }

    private class TitleExistsValidator implements IValidator<String>{

        @Override
        public void validate(IValidatable<String> validatable) {
            final String title = validatable.getValue();

            if(diseaseFacade.existsDisease(title)){
                error("Disease with given name already exists.");
            }
        }
    }
}
