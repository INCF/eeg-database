package cz.zcu.kiv.eegdatabase.wui.ui.search;

import cz.zcu.kiv.eegdatabase.logic.controller.searchsolr.ResultCategory;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class SearchPage extends MenuPage {
    
    private static final long serialVersionUID = -1967810037377960414L;

    private boolean isSearchStringEmpty = false;

    public SearchPage(PageParameters params) {

        setPageTitle(ResourceUtils.getModel("title.page.search"));

        StringValue searchString = params.get("searchString");
        if (searchString.isEmpty() || searchString.isNull()) {
            isSearchStringEmpty = true;
        }
        ResultCategory category = ResultCategory.getCategory(params.get("category").toString());

        SearchPanel searchPanel = new PageSearchPanel("searchPanel", searchString);
        SearchResultPanel searchResultPanel = new SearchResultPanel("resultPanel", searchString, category) {

            @Override
            public boolean isVisible() {
                return !isSearchStringEmpty;
            }
        };
        add(searchPanel);
        add(searchResultPanel);

        add(new FeedbackPanel("feedback"));
    }
}