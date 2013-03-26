package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentAddDTO;
import cz.zcu.kiv.eegdatabase.wui.ui.welcome.WelcomePage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

public class AddExperimentEnvironmentForm extends Form<ExperimentAddDTO> {
    public AddExperimentEnvironmentForm(String id){
        super(id, new CompoundPropertyModel<ExperimentAddDTO>(new ExperimentAddDTO()));

        TextField<String> weatherNote = new TextField<String>("weatherNote");
        weatherNote.setRequired(true);
        add(weatherNote);

        TextField<String> temperature = new TextField<String>("temperature");
        temperature.setRequired(true);
        add(temperature);

        TextField<String> disease = new TextField<String>("disease");
        disease.setRequired(true);
        add(disease);

        TextField<String> pharmaceutical = new TextField<String>("pharmaceutical");
        pharmaceutical.setRequired(true);
        add(pharmaceutical);

        TextField<String> privateNote = new TextField<String>("privateNote");
        privateNote.setRequired(true);
        add(privateNote);

        Button submit = new Button("submit", ResourceUtils.getModel("action.login")) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                ExperimentAddDTO experiment = AddExperimentEnvironmentForm.this.getModelObject();
                setResponsePage(WelcomePage.class);
            }
        };
        add(submit);
    }
}
