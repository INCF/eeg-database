package cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals;

import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.yui.calendar.DateTimeField;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.upload.FileUploadField;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 3.4.13
 * Time: 19:33
 */
public class AddScenarioPage extends WebPage {
    public AddScenarioPage(final PageReference modalWindowPage,
                        final ModalWindow window){

        Form form = new Form("addForm");

        form.add(new Label("addScenarioHeader", "Create scenario"));

        List<String> choices = Arrays.asList("Not selected", "Data1", "Data2");
        form.add(new DropDownChoice<String>("group", choices));

        form.add(new TextField<String>("title").setRequired(true));
        form.add(new TextField<String>("length").setRequired(true));
        form.add(new TextArea<String>("description").setRequired(true));
        form.add(new CheckBox("availableFile"));
        form.add(new CheckBox("isFileAsXML"));

        form.setMultiPart(true);
        form.add(new FileUploadField("dataFile"));
        form.add(new FileUploadField("fileAsXML"));

        /* TODO insert also radio group, which is now as plain HTML only */
        form.add(new DropDownChoice<String>("schemaSelect", choices));

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
