package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.HardwareFacade;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.opensaml.util.resource.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 23.4.13
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 */
public class HardwareForm extends Form<Hardware> {
    @SpringBean
    HardwareFacade hardwareFacade;


    public HardwareForm(String id, final ModalWindow window) {
        super(id, new CompoundPropertyModel<Hardware>(new Hardware()));

        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        add(new Label("addHWHeader", ResourceUtils.getModel("pageTitle.addHardwareDefinition")));

        TextField<String> title =  new TextField<String>("title");
        title.setRequired(true);
        title.setLabel(ResourceUtils.getModel("label.title"));
        add(title);

        TextField<String> type = new TextField<String>("type");
        type.setRequired(true);
        type.setLabel(ResourceUtils.getModel("label.type"));
        add(type);

        TextArea<String> description = new TextArea<String>("description");
        description.setRequired(true);
        description.setLabel(ResourceUtils.getModel("label.description"));
        add(description);
        CheckBox defaultNumber = new CheckBox("defaultNumber");
        defaultNumber.setLabel(ResourceUtils.getModel("label.defaultHardware"));
        add(defaultNumber);

        add(
                new AjaxButton("submitForm", this) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        Hardware newHw = (Hardware)form.getModelObject();
                        if (!hardwareFacade.canSaveDefaultTitle(newHw.getTitle(), newHw.getHardwareId())) {
                            feedback.error(ResourceUtils.getString("error.valueAlreadyInDatabase"));
                            return;
                        }
                        validate();
                        target.add(feedback);
                        if(!hasError()){
                            if (newHw.getDefaultNumber() == 0){
                                hardwareFacade.create(newHw);
                            }
                            else {
                                hardwareFacade.createDefaultRecord(newHw);
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
                new AjaxButton("closeForm", ResourceUtils.getModel("button.close"), this) {}.
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
}
