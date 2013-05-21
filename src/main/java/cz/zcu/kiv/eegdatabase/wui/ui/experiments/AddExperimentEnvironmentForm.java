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
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.GenericFactory;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.GenericModel;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.GenericValidator;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.RepeatableInputPanel;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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

    private Experiment experiment;
    public ListMultipleChoice hw = null;
    private ListMultipleChoice sw = null;
    private DropDownChoice<String> weather = null;
    private TextField temperature = null;
    private TextField<String> environmentNote;
    final int SELECT_ROWS = 5;
    private boolean swUpdate = false;

    private List<Hardware> hwListForModel;
    private List<Software> swListForModel;
    private List<Hardware> hwChoices;
    private List<Software> swChoices;
    private List<Weather> weatherChoices;
    private Weather weatherForModel;
    private Integer integerForModel;
    private String environmentNoteForModel;
    private GenericModel<Weather> weatherModel;
    private List<GenericModel<Disease>> diseases;
    private List<GenericModel<Pharmaceutical>> pharmaceuticals;
    RepeatableInputPanel<Disease> repeatableD;

    public AddExperimentEnvironmentForm(String id, Experiment experiment){
        super(id);
        this.experiment = experiment;

        addHardware();
        addSoftware();
        addWeather();
        addTemperature();
        addEnvironmentNote();
        addDisease();
        addPharmaceutical();

        createModalWindows();
    }

    private void addHardware(){
        hwListForModel = new ArrayList<Hardware>();
        hwChoices = getHardwares();
        hw = new ListMultipleChoice("hardwares", new ListModel(hwListForModel), hwChoices).setMaxRows(SELECT_ROWS);
        hw.setLabel(ResourceUtils.getModel("label.hardware"));

        ComponentFeedbackMessageFilter hwFilter = new ComponentFeedbackMessageFilter(hw);
        final FeedbackPanel hwFeedback = new FeedbackPanel("hwFeedback", hwFilter);
        hwFeedback.setOutputMarkupId(true);
        hw.add(new AjaxFeedbackUpdatingBehavior("blur", hwFeedback));



        add(hw);
        add(hwFeedback);
    }

    private void addSoftware(){
        swListForModel = new ArrayList<Software>();
        swChoices = getSoftwares();
        sw = new ListMultipleChoice("softwares", new GenericModel(swListForModel), swChoices).setMaxRows(SELECT_ROWS);
        sw.setLabel(ResourceUtils.getModel("label.software"));

        sw.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if(swUpdate){
                    swUpdate = false;
                    swChoices = getSoftwares();
                    sw.setChoices(swChoices);
                    target.add(sw);
                }
            }
        });
        ComponentFeedbackMessageFilter swFilter = new ComponentFeedbackMessageFilter(sw);
        final FeedbackPanel swFeedback = new FeedbackPanel("swFeedback", swFilter);
        swFeedback.setOutputMarkupId(true);
        sw.add(new AjaxFeedbackUpdatingBehavior("blur", swFeedback));

        add(sw);
        add(swFeedback);
    }

    private void validateRequiredEntities(){

        if (hwListForModel.isEmpty())
            hw.error(ResourceUtils.getString("required.hardware"));

        if (swListForModel.isEmpty())
            sw.error(ResourceUtils.getString("required.software"));

        if (weatherForModel == null || weatherForModel.getTitle() == null)
            weather.error(ResourceUtils.getString("required.weather"));

        if (integerForModel == null)
            temperature.error(ResourceUtils.getString("required.field"));
        else if(integerForModel < (-273))
            temperature.error(ResourceUtils.getString("invalid.minTemp"));
    }

    private void addWeather(){
        weatherForModel = new Weather();
        weatherChoices = getWeathers();
        weatherModel = new GenericModel<Weather>(weatherForModel);
        weather = new DropDownChoice("weather", weatherModel, weatherChoices);
        weather.setLabel(ResourceUtils.getModel("label.weather"));
        weather.setNullValid(true);
        weather.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
                weatherForModel = weatherModel.getObject();
            }
        });

        ComponentFeedbackMessageFilter weatherFilter = new ComponentFeedbackMessageFilter(weather);
        final FeedbackPanel weatherFeedback = new FeedbackPanel("weatherFeedback", weatherFilter);
        weatherFeedback.setOutputMarkupId(true);
        weather.add(new AjaxFeedbackUpdatingBehavior("blur", weatherFeedback));

        add(weather);
        add(weatherFeedback);
    }

    private void addTemperature(){
        temperature = new TextField("temperature", new Model(integerForModel), Integer.class);
        temperature.setLabel(ResourceUtils.getModel("label.temperature"));

        ComponentFeedbackMessageFilter temperatureFilter = new ComponentFeedbackMessageFilter(temperature);
        final FeedbackPanel temperatureFeedback = new FeedbackPanel("temperatureFeedback", temperatureFilter);
        temperatureFeedback.setOutputMarkupId(true);
        temperature.add(new AjaxFeedbackUpdatingBehavior("blur", temperatureFeedback));
        final Integer[] temp = null;
        temperature.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
               integerForModel = (Integer)temperature.getModel().getObject();
            }
        });


        add(temperature);
        add(temperatureFeedback);
    }

    private void addEnvironmentNote(){
        final Model environmentNoteModel = new Model(environmentNoteForModel);
        environmentNote = new TextField<String>("environmentNote", environmentNoteModel, String.class);
        environmentNote.setLabel(ResourceUtils.getModel("label.environmentNote"));
        environmentNote.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
                environmentNoteForModel = environmentNoteModel.getObject().toString();
            }
        });
        add(environmentNote);
    }

    private void addDisease() {
        GenericFactory<Disease> factory = new GenericFactory<Disease>(Disease.class);
        GenericValidator<Disease> validator = new GenericValidator<Disease>(diseaseFacade);

        repeatableD =
                new RepeatableInputPanel<Disease>("diseases", factory,
                        validator, diseaseFacade);
        diseases = repeatableD.getData();
        validator.setList(diseases);
        add(repeatableD);
    }

    private void addPharmaceutical() {
        GenericFactory<Pharmaceutical> factory = new GenericFactory<Pharmaceutical>(Pharmaceutical.class);
        GenericValidator<Pharmaceutical> validator = new GenericValidator<Pharmaceutical>(pharmaceuticalFacade);

        RepeatableInputPanel<Pharmaceutical> repeatable =
                new RepeatableInputPanel<Pharmaceutical>("pharmaceuticals", factory,
                        validator, pharmaceuticalFacade);
        pharmaceuticals = repeatable.getData();
        validator.setList(pharmaceuticals);
        add(repeatable);
    }

    private void createModalWindows(){
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
    }

    public boolean isValid(){
        validate();
        validateRequiredEntities();
        return !hasError();
    }

    private List<Weather> getWeathers(){
        return weatherFacade.getAllRecords();
    }

    private List<Software> getSoftwares(){
        return softwareFacade.getAllRecords();
    }

    public void updateSoftwares(){
        swUpdate = true;
    }

    public void updateHardwares(){
        hw.setChoices(getHardwares());
    }

    public void updateWeathers(){
        //weather.setChoices(getWeathers());
    }

    private List<Hardware> getHardwares(){
        return hardwareFacade.getAllRecords();
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

    /**
     * It takes data from the model and based on them get valid data.
     */
    public void save() {
        validate();
        if(!hasError()) {
            experiment.setHardwares(getSet(this.hwListForModel));
            experiment.setSoftwares(getSet(this.swListForModel));
            experiment.setWeather(this.weatherForModel);
            experiment.setTemperature(Integer.parseInt(this.temperature.getModelObject().toString()));
            experiment.setEnvironmentNote(this.environmentNote.getModelObject());
            experiment.setDiseases(getSet(this.diseases));
            experiment.setPharmaceuticals(getSet(this.pharmaceuticals));
        }
    }

    private Set getSet(List objects) {
        Set result = new HashSet();
        for(Object object: objects) {
            result.add(((GenericModel) object).getObject());
        }
        return result;
    }
}
