package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.wui.core.common.HardwareFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.WeatherFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.AddExperimentEnvironmentDTO;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.*;
import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.CompoundPropertyModel;
import org.springframework.jmx.access.InvocationFailureException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
        add(new DropDownChoice<String>("weather",
                new PropertyModel(experiment, "weather"), weatherTitles).setRequired(true));

        add(new TextArea<String>("weatherNote"));
        add(new NumberTextField<Integer>("temperature").setRequired(true));
        //TODO autocomplete and create DiseaseFacade
        add(new TextField<String>("disease").setRequired(true));
        

        //TODO autocomplete and create PharmaceuticalFacade
        /* TODO check if this attribute is required as stated in prototype*/
        add(new TextField<String>("pharmaceutical").setRequired(true));

        add(new TextField<String>("privateNote"));
    }


    private void addModalWindowAndButton(Form form, String modalWindowName, String cookieName,
                                         String buttonName, final String targetClass){

        final ModalWindow addedWindow;
        form.add(addedWindow = new ModalWindow(modalWindowName));
        addedWindow.setCookieName(cookieName);

        addedWindow.setPageCreator(new ModalWindow.PageCreator() {

            @Override
            public Page createPage() {
                try{
                    Constructor<?> cons = Class.forName(targetClass).getConstructor(
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