package cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.ProjectType;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.yui.calendar.DateTimeField;
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
import org.apache.wicket.util.time.Duration;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 3.4.13
 * Time: 19:33
 */
public class AddProjectPage extends WebPage {
    public AddProjectPage(final PageReference modalWindowPage,
                                final ModalWindow window){

        AddProjectForm form = new AddProjectForm("addForm", window);
        add(form);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl("/files/wizard-style.css"));
        super.renderHead(response);
    }

    private class AddProjectForm extends Form<ProjectType>{

        public AddProjectForm(String id, final ModalWindow window) {
            super(id, new CompoundPropertyModel<ProjectType>(new ProjectType()));

            final FeedbackPanel feedback = new FeedbackPanel("feedback");
            feedback.setOutputMarkupId(true);
            add(feedback);

            add(new Label("addProjectHeader", "Create project type"));

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
                        ProjectType newPT = getModelObject();
                        System.out.println("---- HW-B "+newPT.getTitle()+" "+newPT.getDescription());
                        validate();
                        target.add(feedback);
                        if(!hasError()){
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
