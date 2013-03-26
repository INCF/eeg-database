package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.wui.core.experiments.AddExperimentEnvironmentDTO;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.CompoundPropertyModel;

import java.util.Arrays;
import java.util.List;

public class AddExperimentEnvironmentForm extends Form<AddExperimentEnvironmentDTO> {
    public AddExperimentEnvironmentForm(String id){
        super(id, new CompoundPropertyModel<AddExperimentEnvironmentDTO>(new AddExperimentEnvironmentDTO()));

        List values = Arrays.asList(new String[] {"Value 1", "Value 2", "Value 3", "Value 4", "Value 5", "Value 6" });

        ListMultipleChoice hw = new ListMultipleChoice("hardware", values).setMaxRows(5);
        add(hw);
        ListMultipleChoice sw = new ListMultipleChoice("software", values).setMaxRows(5);
        add(sw);

        add(new DropDownChoice("weather", values));

        TextField<String> weatherNote = new TextField<String>("weatherNote");
        add(weatherNote);

        NumberTextField<Integer> temperature = new NumberTextField<Integer>("temperature");
        temperature.setRequired(true);
        add(temperature);

        TextField<String> disease = new TextField<String>("disease");
        disease.setRequired(true);
        add(disease);

        TextField<String> pharmaceutical = new TextField<String>("pharmaceutical");
        pharmaceutical.setRequired(true);
        add(pharmaceutical);

        TextField<String> privateNote = new TextField<String>("privateNote");
        add(privateNote);
    }
}
