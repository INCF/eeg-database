package cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals;

import cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms.DiseaseForm;
import org.apache.wicket.PageReference;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 9.4.13
 * Time: 19:55
 * To change this template use File | Settings | File Templates.
 */
public class AddDiseasePage extends WebPage {
    public AddDiseasePage(final PageReference modalWindowPage,
                          final ModalWindow window){
        Form form = new DiseaseForm("addForm", window);

        add(form);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl("/files/wizard-style.css"));
        super.renderHead(response);
    }
}
