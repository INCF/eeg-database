package cz.zcu.kiv.eegdatabase.wui.ui.search;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 20.4.13
 * Time: 14:17
 * To change this template use File | Settings | File Templates.
 */
public class MenuSearchPanel extends SearchPanel {

    private static final long serialVersionUID = 1867810137477960414L;

    public MenuSearchPanel(String id, StringValue searchString) {
        super(id, searchString);
    }
}
