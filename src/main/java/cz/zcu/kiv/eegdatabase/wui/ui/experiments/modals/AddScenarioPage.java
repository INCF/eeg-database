package cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals;

import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.yui.calendar.DateTimeField;
import org.apache.wicket.markup.html.WebPage;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 3.4.13
 * Time: 19:33
 */
public class AddScenarioPage extends WebPage {
    public AddScenarioPage(final PageReference modalWindowPage,
                        final ModalWindow window){
        add(new AjaxLink<Void>("closeOK")
        {
            @Override
            public void onClick(AjaxRequestTarget target)
            {
                window.close(target);
            }
        });

        add(new AjaxLink<Void>("closeCancel")
        {
            @Override
            public void onClick(AjaxRequestTarget target)
            {
                window.close(target);
            }
        });

        add(new DateTimeField("dateTimeField"));
    }
}
