package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.HardwareFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.PharmaceuticalFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.SoftwareFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.WeatherFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.DiseaseFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.*;
import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.GenericFactory;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.GenericModel;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.GenericValidator;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.RepeatableInputPanel;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class AddExperimentEnvironmentForm extends Form<Experiment> {

    @SpringBean
    private WeatherFacade weatherFacade;
    @SpringBean
    private HardwareFacade hardwareFacade;
    @SpringBean
    private SoftwareFacade softwareFacade;
    @SpringBean
    private DiseaseFacade diseaseFacade;
    @SpringBean
    private PharmaceuticalFacade pharmaceuticalFacade;

    private Experiment experiment = new Experiment();
    private ListMultipleChoice hw = null;
    private ListMultipleChoice sw = null;
    private DropDownChoice<String> weather = null;
    private NumberTextField<Integer> temperature = null;
    final int AUTOCOMPLETE_ROWS = 10;
    private final List<Disease> diseaseData = new ArrayList<Disease>();
    private final List<Pharmaceutical> pharmaceuticalData = new ArrayList<Pharmaceutical>();

    private List<GenericModel<Disease>> diseases;
    private List<GenericModel<Pharmaceutical>> pharmaceuticals;

    public AddExperimentEnvironmentForm(String id, Experiment experiment){
        super(id, new CompoundPropertyModel<Experiment>(experiment));

        getModelObject().setDiseases(null);

        addModalWindowAndButton(this, "addDiseaseModal", "add-disease",
                "addDisease", AddDiseasePage.class.getName());

        addModalWindowAndButton(this, "addPharmaModal", "add-pharma",
                "addPharma", AddPharmaceuticalsPage.class.getName());

        addModalWindowAndButton(this, "addHWModal", "add-hw",
                "addHW", AddHardwarePage.class.getName());

        addModalWindowAndButton(this, "addSWModal", "add-sw",
                "addSW", AddSoftwarePage.class.getName());

        addModalWindowAndButton(this, "addWeatherModal", "add-weather",
                "addWeather", AddWeatherPage.class.getName());

        ArrayList<String> hardwareTitles = getHardwareTitles();
        hw = new ListMultipleChoice("hardware", hardwareTitles).setMaxRows(5);
        hw.setRequired(true);
        hw.setLabel(ResourceUtils.getModel("label.hardware"));
        hw.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                hw.setChoices(getHardwareTitles());
                target.add(hw);
            }
        });
        add(hw);

        ArrayList<String> softwareTitles = getSoftwareTitles();
        sw = new ListMultipleChoice("software", softwareTitles).setMaxRows(5);
        sw.setRequired(true);
        sw.setLabel(ResourceUtils.getModel("label.software"));
        sw.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                sw.setChoices(getSoftwareTitles());
                target.add(sw);
            }
        });
        add(sw);

        ArrayList<String> weatherTitles = getWeatherTitles();
        weather = new DropDownChoice<String>("weather", weatherTitles);
        weather.setRequired(true);
        weather.setLabel(ResourceUtils.getModel("label.weather"));
        weather.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                weather.setChoices(getWeatherTitles());
                target.add(weather);
            }
        });
        add(weather);

        TextArea<String> weatherNote = new TextArea<String>("weatherNote");
        weatherNote.setLabel(ResourceUtils.getModel("label.weatherNote"));
        add(weatherNote);
        temperature = new NumberTextField<Integer>("temperature");
        temperature.setRequired(true);
        temperature.setLabel(ResourceUtils.getModel("label.temperature"));
        add(temperature);

        addDisease();

        addPharmaceutical();

        TextField<String> privateNote = new TextField<String>("privateNote");
        privateNote.setLabel(ResourceUtils.getModel("label.private"));
        add(privateNote);

        setOutputMarkupId(true);
        AjaxFormValidatingBehavior.addToAllFormComponents(this, "onblur", Duration.ONE_SECOND);
    }

    private void addDisease() {
        GenericFactory<Disease> factory = new GenericFactory<Disease>(Disease.class);
        GenericValidator<Disease> validator = new GenericValidator<Disease>(diseaseFacade);

        RepeatableInputPanel<Disease> repeatable =
                new RepeatableInputPanel<Disease>("disease", factory,
                        validator, diseaseFacade);
        diseases = repeatable.getData();
        add(repeatable);
    }

    private void addPharmaceutical() {
        GenericFactory<Pharmaceutical> factory = new GenericFactory<Pharmaceutical>(Pharmaceutical.class);
        GenericValidator<Pharmaceutical> validator = new GenericValidator<Pharmaceutical>(pharmaceuticalFacade);

        RepeatableInputPanel<Pharmaceutical> repeatable =
                new RepeatableInputPanel<Pharmaceutical>("pharmaceutical", factory,
                        validator, pharmaceuticalFacade);
        pharmaceuticals = repeatable.getData();
        add(repeatable);
    }

    public boolean isValid(){
        if (hw.getValue().isEmpty()) error("Hardware is required!");
        if (sw.getValue().isEmpty()) error("Software is required!");
        if (weather.getValue().isEmpty()) error("Weather is required!");
        if (temperature.getValue().isEmpty()) error("Temperature is required!");
        else {
            try {
                 Float.parseFloat(temperature.getValue());
            }
            catch (NumberFormatException e){
                error("Temperature must be a number!");
            }
        }
        return !hasError();
    }

    private ArrayList<String> getWeatherTitles(){
        List<Weather> weathers = weatherFacade.getAllRecords();
        ArrayList<String> weatherTitles = new ArrayList<String>();
        if (weathers != null){
            for (Weather w : weathers){
                weatherTitles.add(w.getTitle());
            }
        }
        return weatherTitles;
    }

    private ArrayList<String> getSoftwareTitles(){
        List<Software> softwares = softwareFacade.getAllRecords();
        ArrayList<String> softwareTitles = new ArrayList<String>();
        if (softwares != null){
            for (Software sw : softwares){
                softwareTitles.add(sw.getTitle());
            }
        }
        return softwareTitles;
    }

    private ArrayList<String> getHardwareTitles(){
        List<Hardware> hardwares = hardwareFacade.getAllRecords();
        ArrayList<String> hardwareTitles = new ArrayList<String>();
        if (hardwares != null){
            for (Hardware hw : hardwares){
                hardwareTitles.add(hw.getTitle());
            }
        }
        return hardwareTitles;
    }

    private void addModalWindowAndButton(final Form form, String modalWindowName, String cookieName,
                                         final String buttonName, final String targetClass){

        final ModalWindow addedWindow;
        form.add(addedWindow = new ModalWindow(modalWindowName));
        addedWindow.setCookieName(cookieName);
        addedWindow.setPageCreator(new ModalWindow.PageCreator() {

            @Override
            public Page createPage() {
                try{
                    Constructor<?> cons = null;
                    cons = Class.forName(targetClass).getConstructor(
                    PageReference.class, ModalWindow.class);

                    return (Page) cons.newInstance(
                            getPage().getPageReference(), addedWindow);
                }catch (NoSuchMethodException e){
                    e.printStackTrace();
                }catch (IllegalAccessException e){
                    e.printStackTrace();
                }catch (InstantiationException e){
                    e.printStackTrace();
                }catch (InvocationTargetException e){
                    e.printStackTrace();
                }catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
                return null;
            }
        });

        AjaxButton ajaxButton = new AjaxButton(buttonName, this)
        {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form){
                addedWindow.show(target);
            }
        };
        ajaxButton.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                addedWindow.show(target);
            }
        });
        form.add(ajaxButton);
    }

    /*
    public Experiment getValidExperimentPart(Experiment experiment) {
        AddExperimentEnvironmentDTO dto = getModelObject();
        if(dto == null) return null;

        if(dto.getWeather() == null) return null;
        if(dto.getTemperature() == null) return null;
        if(dto.getHardware() == null || dto.getHardware().isEmpty())
            return null;
        if(dto.getSoftware() == null || dto.getSoftware().isEmpty())
            return null;
        debug("AddExperimentEnvironmentForm required attributes are set");

        List<Weather> weathers = weatherFacade.getAllRecords();
        Weather weather = null;
        for (Weather w : weathers){
            if(w.getTitle().equals(dto.getWeather())){
                weather = w;
            }
        }
        if(weather == null) return null;
        experiment.setWeather(weather);
        debug("AddExperimentEnvironmentForm.weather was valid");

        // TODO change type of temperature in DTO to int
        int temperature = dto.getTemperature().intValue();
        experiment.setTemperature(temperature);
        debug("AddExperimentEnvironmentForm.temperature was valid");

        //TODO find more effective way
        List<Hardware> hwList = hardwareFacade.getItemsForList();
        Set<Hardware> hardwareSet = new HashSet<Hardware>();
        for (Hardware hw : hwList){
            for(String hwTitle : dto.getHardware()){
                if(hw.getTitle().equals(hwTitle))
                    hardwareSet.add(hw);
            }
        }
        if(hardwareSet.size() != dto.getHardware().size())
            return null;
        experiment.setHardwares(hardwareSet);
        debug("AddExperimentEnvironmentForm.hardware was valid");

        List<Software> swList = softwareFacade.getAllRecords();
        Set<Software> softwareSet = new HashSet<Software>();
        for (Software sw : swList){
            for(String swTitle : dto.getSoftware()){
                if(sw.getTitle().equals(swTitle))
                    softwareSet.add(sw);
            }
        }
        if(softwareSet.size() != dto.getSoftware().size())
            return null;
        experiment.setSoftwares(softwareSet);
        debug("AddExperimentEnvironmentForm.software was valid");

        Set<Disease> diseaseSet = new HashSet<Disease>();
        for (Disease disease : diseaseData){
            diseaseSet.add(disease);
        }
        if(diseaseSet.size() != diseaseData.size()) return null;
        experiment.setDiseases(diseaseSet);
        debug("AddExperimentEnvironmentForm.diseases was valid");

        Set<Pharmaceutical> pharmaceuticalSet = new HashSet<Pharmaceutical>();
        for (Pharmaceutical ph : pharmaceuticalData){
            pharmaceuticalSet.add(ph);
        }
        if(pharmaceuticalSet.size() != pharmaceuticalData.size())
            return null;
        experiment.setPharmaceuticals(pharmaceuticalSet);
        debug("AddExperimentEnvironmentForm.pharmaceuticals was valid");

        debug("AddExperimentEnvironmentForm was valid");
        return experiment;
    }
    */

    /**
     *
     */
    public void save() {
        //To change body of created methods use File | Settings | File Templates.
    }
}
