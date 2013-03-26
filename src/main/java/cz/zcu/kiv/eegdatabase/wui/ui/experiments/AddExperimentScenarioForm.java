package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentAddDTO;
import cz.zcu.kiv.eegdatabase.wui.ui.welcome.WelcomePage;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DateField;
import org.apache.wicket.extensions.yui.calendar.DateTimeField;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 26.3.13
 * Time: 17:43
 * To change this template use File | Settings | File Templates.
 */
public class AddExperimentScenarioForm extends Form<AddExperimentScenarioDTO> {

    public AddExperimentScenarioForm(String id){
        super(id, new CompoundPropertyModel<AddExperimentScenarioDTO>(new AddExperimentScenarioDTO()));

        TextField<String> scenario = new TextField<String>("scenario");
        scenario.setRequired(true);
        add(scenario);

        TextField<String> group = new TextField<String>("group");
        group.setRequired(true);
        add(group);

        CheckBox isDefaultGroup = new CheckBox("isDefaultGroup");
        isDefaultGroup.setRequired(true);
        add(isDefaultGroup);

        TextField<String> project = new TextField<String>("project");
        project.setRequired(true);
        add(project);
/*
        DateField startDate = new DateField("startDate");
        startDate.setRequired(true);
        add(startDate);

        DateTimeField startTime = new DateTimeField("startTime");
        startTime.setRequired(true);
        add(startTime);

        DateField finishDate = new DateField("finishDate");
        finishDate.setRequired(true);
        add(finishDate);

        DateTimeField finishTime = new DateTimeField("finishTime");
        finishTime.setRequired(true);
        add(finishTime);
*/
        TextField<String> startDate = new TextField<String>("startDate");
        startDate.setRequired(true);
        add(startDate);

        TextField<String> startTime = new TextField<String>("startTime");
        startTime.setRequired(true);
        add(startTime);

        TextField<String> finishDate = new TextField<String>("finishDate");
        finishDate.setRequired(true);
        add(finishDate);

        TextField<String> finishTime = new TextField<String>("finishTime");
        finishTime.setRequired(true);
        add(finishTime);

        TextField<String> testSubject = new TextField<String>("subjects");
        testSubject.setRequired(true);
        add(testSubject);

        TextField<String> stimulus = new TextField<String>("stimulus");
        stimulus.setRequired(true);
        add(stimulus);

        TextField<String> coExperimenters = new TextField<String>("coExperimenters");
        coExperimenters.setRequired(true);
        add(coExperimenters);
    }
}
