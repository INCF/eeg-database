package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.HardwareFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.SoftwareFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.WeatherFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.AddExperimentEnvironmentDTO;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.DiseaseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.DiseaseService;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.*;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.models.DiseaseTitleModel;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.models.PharmaceuticalTitleModel;
import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AddingItemsView;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.IAutocompletable;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.time.Duration;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class AddExperimentEnvironmentForm extends Form<AddExperimentEnvironmentDTO> {

    @SpringBean
    private WeatherFacade weatherFacade;
    @SpringBean
    private HardwareFacade hardwareFacade;
    @SpringBean
    private SoftwareFacade softwareFacade;
    @SpringBean
    private DiseaseFacade diseaseFacade;

    private Experiment experiment = new Experiment();
    private ListMultipleChoice hw = null;
    private ListMultipleChoice sw = null;
    private DropDownChoice<String> weather = null;
    private NumberTextField<Integer> temperature = null;
    final int AUTOCOMPLETE_ROWS = 10;
    private final List<Disease> diseaseData = new ArrayList<Disease>();
    private final List<Pharmaceutical> pharmaceuticalData = new ArrayList<Pharmaceutical>();

    public AddExperimentEnvironmentForm(String id){
        super(id, new CompoundPropertyModel<AddExperimentEnvironmentDTO>(new AddExperimentEnvironmentDTO()));

        getModelObject().setDisease("");

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
        diseaseData.add(new Disease());
        final WebMarkupContainer diseaseContainer = new WebMarkupContainer("addDiseaseInputContainer");

        final ListView<Disease> diseaseListView = new ListView<Disease>("addDiseaseInput", diseaseData) {
             @Override
            protected void populateItem(ListItem item) {
                final Disease diseaseItem = (Disease) item.getModelObject();
                final AddingItemsView<String> disease =
                        new AddingItemsView<String>(
                                "disease",
                                new DiseaseTitleModel(diseaseItem),
                                this,
                                diseaseContainer,
                                Disease.class
                        );
                item.add(disease);
            }
        };
        diseaseListView.setOutputMarkupId(true);
        diseaseListView.setReuseItems(true);

        diseaseContainer.add(diseaseListView);
        diseaseContainer.setOutputMarkupId(true);
        add(diseaseContainer);
    }

    private void addPharmaceutical() {
        pharmaceuticalData.add(new Pharmaceutical());
        final WebMarkupContainer pharmaceuticalContainer =
                new WebMarkupContainer("addPharmaceuticalInputContainer");

        final ListView<Pharmaceutical> pharmaceuticalListView =
                new ListView<Pharmaceutical>("addPharmaceuticalInput", pharmaceuticalData) {

            @Override
            protected void populateItem(ListItem item) {
                final Pharmaceutical pharmaceuticalItem = (Pharmaceutical) item.getModelObject();
                final AddingItemsView<String> pharmaceutical =
                        new AddingItemsView<String>(
                                "pharmaceutical",
                                new PharmaceuticalTitleModel(pharmaceuticalItem),
                                this,
                                pharmaceuticalContainer,
                                Pharmaceutical.class
                        );
                item.add(pharmaceutical);
            }
        };
        pharmaceuticalListView.setOutputMarkupId(true);
        pharmaceuticalListView.setReuseItems(true);

        pharmaceuticalContainer.add(pharmaceuticalListView);
        pharmaceuticalContainer.setOutputMarkupId(true);
        add(pharmaceuticalContainer);
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
}
