package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.wui.core.common.HardwareFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.WeatherFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.AddExperimentEnvironmentDTO;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddExperimentEnvironmentForm extends Form<AddExperimentEnvironmentDTO> {

    @SpringBean
    private WeatherFacade weatherFacade;
    @SpringBean
    private HardwareFacade hardwareFacade;

    private Experiment experiment = new Experiment();
    final int AUTOCOMPLETE_ROWS = 10;

    public AddExperimentEnvironmentForm(String id){
        super(id, new CompoundPropertyModel<AddExperimentEnvironmentDTO>(new AddExperimentEnvironmentDTO()));

        List<Hardware> hardwares = hardwareFacade.getAllRecords();
        ArrayList<String> hardwareTitles = new ArrayList<String>();
        for (Hardware hw : hardwares){
            hardwareTitles.add(hw.getTitle());
        }
        ListMultipleChoice hw = new ListMultipleChoice("hardware", new PropertyModel(experiment, "hardwares"), hardwareTitles).setMaxRows(5);
        add(hw);

        List values = Arrays.asList(new String[] {"Value 1", "Value 2", "Value 3", "Value 4", "Value 5", "Value 6" });
        //TODO create SoftwareFacade
        ListMultipleChoice sw = new ListMultipleChoice("software", values).setMaxRows(5);
        add(sw);

        List<Weather> weathers = weatherFacade.getAllRecords();
        ArrayList<String> weatherTitles = new ArrayList<String>();
        for (Weather w : weathers){
            weatherTitles.add(w.getTitle());
        }
        add(new DropDownChoice("weather", new PropertyModel(experiment, "weather"), weatherTitles));

        TextField<String> weatherNote = new TextField<String>("weatherNote");
        add(weatherNote);

        NumberTextField<Integer> temperature = new NumberTextField<Integer>("temperature");
        temperature.setRequired(true);
        add(temperature);

        TextField<String> disease = new TextField<String>("disease");
        //TODO autocomplete and create DiseaseFacade
        disease.setRequired(true);
        add(disease);

        TextField<String> pharmaceutical = new TextField<String>("pharmaceutical");
        //TODO autocomplete and create PharmaceuticalFacade
        pharmaceutical.setRequired(true);
        add(pharmaceutical);

        TextField<String> privateNote = new TextField<String>("privateNote");
        add(privateNote);
    }
}