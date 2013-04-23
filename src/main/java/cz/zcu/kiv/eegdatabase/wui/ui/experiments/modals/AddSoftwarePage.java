package cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.SoftwareFacade;
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
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 9.4.13
 * Time: 19:55
 * To change this template use File | Settings | File Templates.
 */
public class AddSoftwarePage extends WebPage {
    public AddSoftwarePage(final PageReference modalWindowPage,
                                final ModalWindow window){

        AddSoftwareForm form = new AddSoftwareForm("addForm", window);
        add(form);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl("/files/wizard-style.css"));
        super.renderHead(response);
    }

    private class AddSoftwareForm extends Form<Software>{
        @SpringBean
        SoftwareFacade facade;

        public AddSoftwareForm(String id, final ModalWindow window) {
            super(id, new CompoundPropertyModel<Software>(new Software()));

            final FeedbackPanel feedback = new FeedbackPanel("feedback");
            feedback.setOutputMarkupId(true);
            add(feedback);

            add(new Label("addSWHeader", "Add software"));

            add(new TextField<String>("title").setRequired(true));
            add(new TextArea<String>("description").setRequired(true));
            add(new CheckBox("defaultNumber"));

            add(
                new AjaxButton("submitForm", ResourceUtils.getModel("button.save"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    window.close(target);
                    }
                }.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        Software newSw = getModelObject();
                        System.out.println("---- HW-B "+newSw.getTitle()+" "+newSw.getDescription());
                        if (!facade.canSaveDefaultTitle(newSw.getTitle(), newSw.getSoftwareId())) {
                            feedback.error(ResourceUtils.getString("error.valueAlreadyInDatabase"));
                            return;
                        }
                        validate();
                        target.add(feedback);
                        if(!hasError()){
                            //facade.create(newSw);
                            window.close(target);
                        }
                    }
                })
            );
            add(
                new AjaxButton("closeForm", this) {
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
}