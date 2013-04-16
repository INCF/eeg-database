package cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.WeatherFacade;
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
 * User: Prasek
 * Date: 16.4.13
 * Time: 11:51
 * To change this template use File | Settings | File Templates.
 */
public class AddWeatherPage extends WebPage {

    public AddWeatherPage(final PageReference modalWindowPage,
                                final ModalWindow window){

        AddWeatherForm form = new AddWeatherForm("addForm", window);
        add(form);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl("/files/wizard-style.css"));
        super.renderHead(response);
    }

    private class AddWeatherForm extends Form<Weather> {
        @SpringBean
        WeatherFacade facade;

        public AddWeatherForm(String id, final ModalWindow window) {
            super(id, new CompoundPropertyModel<Weather>(new Weather()));

            final FeedbackPanel feedback = new FeedbackPanel("feedback");
            feedback.setOutputMarkupId(true);
            add(feedback);

            add(new Label("addWeatherHeader", "Add new weather"));
            add(new TextField<String>("title").setRequired(true));
            add(new TextArea<String>("description").setRequired(true));

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
                        Weather newW = getModelObject();
                        System.out.println("---- HW-B "+newW.getTitle()+" "+newW.getDescription());
                        if (!facade.canSaveDefaultTitle(newW.getTitle(), newW.getWeatherId())) {
                            feedback.error(ResourceUtils.getString("error.valueAlreadyInDatabase"));
                            return;
                        }
                        validate();
                        target.add(feedback);
                        if(!hasError()){
                            //TODO create object in database
                            //facade.createDefaultRecord(newW);
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
