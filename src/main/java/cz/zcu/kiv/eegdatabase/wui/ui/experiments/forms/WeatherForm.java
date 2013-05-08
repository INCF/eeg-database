package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms;

import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.WeatherFacade;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.resource.ResourceUtil;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 23.4.13
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 */
public class WeatherForm extends Form<Weather> {
    @SpringBean
    WeatherFacade facade;

    public WeatherForm(String id, final ModalWindow window) {
            super(id, new CompoundPropertyModel<Weather>(new Weather()));

            final FeedbackPanel feedback = new FeedbackPanel("feedback");
            feedback.setOutputMarkupId(true);
            add(feedback);

            add(new Label("addWeatherHeader", ResourceUtils.getModel("pageTitle.addWeatherDefinition")));

            TextField<String> title =  new TextField<String>("title");
            title.setRequired(true);
            title.setLabel(ResourceUtils.getModel("label.title"));
            add(title);

            TextArea<String> description = new TextArea<String>("description");
            description.setRequired(true);
            description.setLabel(ResourceUtils.getModel("label.description"));
            add(description);

            add(
                new AjaxButton("submitForm", ResourceUtils.getModel("button.save"), this) {

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    Weather newW = (Weather)form.getModelObject();
                    if (!facade.canSaveDefaultTitle(newW.getTitle(), newW.getWeatherId())) {
                        feedback.error(ResourceUtils.getString("error.valueAlreadyInDatabase"));
                        return;
                    }
                    validate();
                    target.add(feedback);
                    if(!hasError()){
                        facade.create(newW);
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
