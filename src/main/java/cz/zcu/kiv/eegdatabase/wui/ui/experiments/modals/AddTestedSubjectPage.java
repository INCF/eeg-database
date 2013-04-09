package cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals;

import com.googlecode.wicket.jquery.ui.form.RadioChoice;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.yui.calendar.DateField;
import org.apache.wicket.extensions.yui.calendar.DateTimeField;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 3.4.13
 * Time: 19:33
 */
public class AddTestedSubjectPage extends WebPage {
    public AddTestedSubjectPage(final PageReference modalWindowPage,
                        final ModalWindow window) {

        Form form = new Form("addForm");

        form.add(new Label("addPersonHeader", "Add tested subject"));

        form.add(new TextField<String>("name").setRequired(true));
        form.add(new TextField<String>("surname").setRequired(true));
        form.add(new DateField("dateOfBirth").setRequired(true));

        /* TODO this element has not been transformed to Wicket yet */
//        form.add(new RadioChoice("gender").setSuffix("").setRequired(true));

        List<String> choices = Arrays.asList("Not selected", "Elementary", "Secondary",
                "Secondary with graduation", "Higher education");
        form.add(new DropDownChoice<String>("education", choices));

        form.add(new TextField<String>("email").setRequired(true)
                .add(EmailAddressValidator.getInstance()));

        form.add(new TextField<String>("phone").setRequired(true));

        form.add(new TextArea<String>("notes"));

        form.add(
                new AjaxButton("submitForm", form) {
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

        form.add(
                new AjaxButton("closeForm", form) {
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

        add(form);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl("/files/wizard-style.css"));
        super.renderHead(response);
    }
}
