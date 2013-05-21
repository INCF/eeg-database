package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms;

import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.SoftwareFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.AddExperimentEnvironmentForm;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.WizardTabbedPanelPage;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.resource.ResourceUtil;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.opensaml.util.resource.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 23.4.13
 * Time: 14:32
 * To change this template use File | Settings | File Templates.
 */
public class SoftwareForm extends Form<Software> {
    @SpringBean
    SoftwareFacade facade;

    public SoftwareForm(String id, final ModalWindow window) {
            super(id, new CompoundPropertyModel<Software>(new Software()));

            final FeedbackPanel feedback = new FeedbackPanel("feedback");
            feedback.setOutputMarkupId(true);
            add(feedback);

            add(new Label("addSWHeader", ResourceUtils.getModel("pageTitle.addSoftwareDefinition")));

            TextField<String> title =  new TextField<String>("title");
            title.setRequired(true);
            title.setLabel(ResourceUtils.getModel("label.title"));
            add(title);
            TextArea<String> description = new TextArea<String>("description");
            description.setRequired(true);
            description.setLabel(ResourceUtils.getModel("label.description"));
            add(description);
            CheckBox defaultNumber = new CheckBox("defaultNumber");
            defaultNumber.setLabel(ResourceUtils.getModel("label.defaultSoftware"));
            add(defaultNumber);

            add(
                new AjaxButton("submitForm", ResourceUtils.getModel("button.save"), this) {

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    Software newSw = (Software)form.getModelObject();
                    if (!facade.canSaveDefaultTitle(newSw.getTitle(), newSw.getSoftwareId())) {
                        feedback.error(ResourceUtils.getString("error.valueAlreadyInDatabase"));
                        return;
                    }
                    validate();
                    target.add(feedback);
                    if(!hasError()){
                        if (newSw.getDefaultNumber() == 0){
                            facade.create(newSw);
                        }
                        else {
                            facade.createDefaultRecord(newSw);
                        }
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
