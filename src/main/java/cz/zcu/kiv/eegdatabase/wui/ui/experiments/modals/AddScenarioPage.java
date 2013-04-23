package cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals;

import cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms.ScenarioForm;
import org.apache.wicket.PageReference;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 3.4.13
 * Time: 19:33
 */
public class AddScenarioPage extends WebPage {


    public AddScenarioPage(final PageReference modalWindowPage,
                        final ModalWindow window){

        Form form = new ScenarioForm("addForm", window);

        add(form);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl("/files/wizard-style.css"));
        super.renderHead(response);
    }
}
