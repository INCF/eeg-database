package cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals;

import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.PharmaceuticalFacade;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 9.4.13
 * Time: 19:56
 * To change this template use File | Settings | File Templates.
 */
public class AddPharmaceuticalsPage extends WebPage {

    @SpringBean
    PharmaceuticalFacade pharmaceuticalFacade;

    public AddPharmaceuticalsPage(final PageReference modalWindowPage,
                          final ModalWindow window){

        AddPharmaceuticalsForm form = new AddPharmaceuticalsForm("addForm", window);
        add(form);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl("/files/wizard-style.css"));
        super.renderHead(response);
    }

    private class AddPharmaceuticalsForm extends Form<Pharmaceutical> {

        public AddPharmaceuticalsForm(String id, final ModalWindow window){
            super(id, new CompoundPropertyModel<Pharmaceutical>(new Pharmaceutical()));

            final FeedbackPanel feedback = new FeedbackPanel("feedback");
            feedback.setOutputMarkupId(true);
            add(feedback);

            add(new Label("addPharmaHeader", "Add new pharmaceuticals"));

            TextField<String> title = new TextField<String>("title");
            title.setLabel(ResourceUtils.getModel("label.title"));
            title.add(new TitleExistsValidator());
            title.setRequired(true);
            FormComponentLabel titleLabel = new FormComponentLabel("titleLb", title);
            add(title, titleLabel);

            TextArea<String> description = new TextArea<String>("description");
            description.setLabel(ResourceUtils.getModel("label.pharmaceutical.description"));
            description.setRequired(true);
            FormComponentLabel descriptionLabel = new FormComponentLabel("descriptionLb", description);
            add(description, descriptionLabel);

            AjaxButton submit = new AjaxButton("submitForm", ResourceUtils.getModel("button.save"), this) {

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    Pharmaceutical pharmaceutical = AddPharmaceuticalsForm.this.getModelObject();

                    validate();
                    target.add(feedback);

                    if (!hasError()) {
                        pharmaceuticalFacade.create(pharmaceutical);
                        window.close(target);
                    }
                }
            };
            add(submit);

            add(
                    new AjaxButton("closeForm", ResourceUtils.getModel("button.close"), this) {
                        @Override
                        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                            window.close(target);
                        }
                    }.add(new AjaxEventBehavior("onclick") {
                        @Override
                        protected void onEvent(AjaxRequestTarget target) {
                            window.close(target);
                        }
                    })
            );

            setOutputMarkupId(true);
            AjaxFormValidatingBehavior.addToAllFormComponents(this, "focus", Duration.ONE_SECOND);
        }
    }

    private class TitleExistsValidator implements IValidator<String> {

        @Override
        public void validate(IValidatable<String> validatable) {
            final String title = validatable.getValue();

            if(!pharmaceuticalFacade.canSaveTitle(title)){
                System.out.println("valueAlreadyInDatabase");
                error(ResourceUtils.getString("error.valueAlreadyInDatabase"));
            }
            else
                System.out.println("value is unique");
        }
    }
}
