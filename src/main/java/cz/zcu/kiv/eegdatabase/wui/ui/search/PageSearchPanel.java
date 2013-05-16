package cz.zcu.kiv.eegdatabase.wui.ui.search;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

/**
 * The search panel that appears on the search page.
 * User: Jan Koren
 * Date: 20.4.13
 * Time: 14:17
 */
public class PageSearchPanel extends SearchPanel {

    private static final long serialVersionUID = 1867810137477960415L;

    public PageSearchPanel(String id, StringValue searchString) {
        super(id, searchString);
    }
}
