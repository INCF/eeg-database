package cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals;

import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 9.4.13
 * Time: 19:54
 * To change this template use File | Settings | File Templates.
 */
public class AddHardwarePage extends WebPage {
    public AddHardwarePage(final PageReference modalWindowPage,
                                final ModalWindow window){
        Form form = new Form("addForm");

        form.add(new Label("addHWHeader", "Add new hardware"));

        form.add(new TextField<String>("name").setRequired(true));
        form.add(new TextArea<String>("description").setRequired(true));
        form.add(new CheckBox("isDefault"));

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
