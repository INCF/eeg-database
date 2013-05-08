package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.wui.core.common.HardwareFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.SoftwareFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.WeatherFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.AddExperimentEnvironmentDTO;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.DiseaseFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.*;
import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.time.Duration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
    private TextField<String> disease = null;
    private TextField<String> pharmaceutical = null;
    final int AUTOCOMPLETE_ROWS = 10;

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
        weather.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                weather.setChoices(getWeatherTitles());
                target.add(weather);
            }
        });
        add(weather);

        add(new TextArea<String>("weatherNote"));
        temperature = new NumberTextField<Integer>("temperature");
        temperature.setRequired(true);
        add(temperature);

        disease = new AutoCompleteTextField<String>("disease")
        {
            @Override
            protected Iterator<String> getChoices(String input)
            {
                if (Strings.isEmpty(input))
                {
                    List<String> emptyList = Collections.emptyList();
                    return emptyList.iterator();
                }
                List<String> choices = new ArrayList<String>(AUTOCOMPLETE_ROWS);
                List<Disease> diseases = diseaseFacade.getAllRecords();
                for (final Disease disease : diseases)
                {
                    final String data = disease.getTitle();
                    if (data.toUpperCase().startsWith(input.toUpperCase()))
                    {
                        choices.add(data);
                        if (choices.size() == AUTOCOMPLETE_ROWS) break;
                    }
                }
                return choices.iterator();
            }
        };
        disease.setRequired(true);
        disease.setOutputMarkupId(true);
        add(disease);

        //TODO autocomplete and create PharmaceuticalFacade
        /* TODO check if this attribute is required as stated in prototype*/
        pharmaceutical = new TextField<String>("pharmaceutical");
        pharmaceutical.setRequired(true);
        add(pharmaceutical);

        add(new TextField<String>("privateNote"));

        setOutputMarkupId(true);
        AjaxFormValidatingBehavior.addToAllFormComponents(this, "onblur", Duration.ONE_SECOND);
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
        if (disease.getValue().isEmpty()) error("Disease is required!");
        if (pharmaceutical.getValue().isEmpty()) error("Pharmaceutical is required!");
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
}
