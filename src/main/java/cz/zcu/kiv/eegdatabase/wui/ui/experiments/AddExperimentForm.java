package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentAddDTO;
import cz.zcu.kiv.eegdatabase.wui.ui.welcome.WelcomePage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 25.3.13
 * Time: 19:45
 * To change this template use File | Settings | File Templates.
 */
public class AddExperimentForm extends Form<ExperimentAddDTO> {
    public AddExperimentForm(String id){
        super(id, new CompoundPropertyModel<ExperimentAddDTO>(new ExperimentAddDTO()));

        TextField<String> userName = new TextField<String>("name");
        userName.setRequired(true);

        add(userName);

        Button submit = new Button("submit", ResourceUtils.getModel("action.login")) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                ExperimentAddDTO experiment = AddExperimentForm.this.getModelObject();
                setResponsePage(WelcomePage.class);
            }
        };
        add(submit);
    }
}
