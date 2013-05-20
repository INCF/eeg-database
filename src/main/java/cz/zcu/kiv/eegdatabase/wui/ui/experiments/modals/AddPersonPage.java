package cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals;

import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampConverter;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.Gender;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;
import cz.zcu.kiv.eegdatabase.wui.core.Laterality;
import cz.zcu.kiv.eegdatabase.wui.core.educationlevel.EducationLevelFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.AjaxFeedbackUpdatingBehavior;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms.PersonForm;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.GenericValidator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.yui.calendar.DateField;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;
import java.util.Date;

import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;

import javax.management.relation.Role;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 3.4.13
 * Time: 19:33
 */
public class AddPersonPage extends WebPage {

    public AddPersonPage(final PageReference modalWindowPage,
                         final ModalWindow window) {

        PersonForm form = new PersonForm("addForm", window);
        add(form);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl("/files/wizard-style.css"));
        super.renderHead(response);
    }
}