package cz.zcu.kiv.eegdatabase.wui.ui.search;

import cz.zcu.kiv.eegdatabase.logic.controller.searchsolr.FullTextResult;
import cz.zcu.kiv.eegdatabase.logic.controller.searchsolr.FulltextSearchService;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.util.List;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class SearchPage extends MenuPage {
    
    private static final long serialVersionUID = -1967810037377960414L;

    @SpringBean
    private FulltextSearchService searchService;

    private boolean isSearchStringEmpty = false;

    public SearchPage(PageParameters params) {
        
        setPageTitle(ResourceUtils.getModel("title.page.search"));

        StringValue searchString = params.get("searchString");
        if (searchString.isEmpty() || searchString.isNull()) {
            isSearchStringEmpty = true;
        }
        IModel<List<FullTextResult>> fullTextResults = new ListModel<FullTextResult>();
        fullTextResults.setObject(searchService.getResultsForQuery(searchString.toString()));

        SearchPanel searchPanel = new SearchPanel("searchPanel");
        SearchResultPanel searchResultPanel = new SearchResultPanel("resultPanel", fullTextResults) {

            @Override
            public boolean isVisible() {
                return !isSearchStringEmpty;
            }
        };
        add(searchPanel);
        add(searchResultPanel);

        add(new FeedbackPanel("feedback"));
    }

    private LoadableDetachableModel<List<FullTextResult>> createModelForFulltext(final TextField<String> textField) {
        return new LoadableDetachableModel<List<FullTextResult>>() {
            @Override
            protected List<FullTextResult> load() {
                List<FullTextResult> queryResults = searchService.getResultsForQuery(textField.getModelObject());

                return queryResults;
            }
        };
    }
}