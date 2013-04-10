package cz.zcu.kiv.eegdatabase.wui.ui.search;

import cz.zcu.kiv.eegdatabase.logic.controller.searchsolr.FullTextResult;
import cz.zcu.kiv.eegdatabase.logic.controller.searchsolr.FulltextSearchService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.util.Iterator;

/**
 * User: Jan Koren
 * Date: 27.3.13
 */
public class SearchResultDataProvider extends ListDataProvider<FullTextResult> {

    @SpringBean
    FulltextSearchService searchService;

    private String searchQuery;

    public SearchResultDataProvider(StringValue searchString) {
        Injector.get().inject(this);

        System.out.println(searchString);
        if(searchString.isNull()) {
            this.searchQuery = "";
        }
        else {
            this.searchQuery = searchString.toString();
        }
    }

    @Override
    public Iterator<? extends FullTextResult> iterator(long first, long count) {
        return searchService.getResultsForQuery(searchQuery, (int) first, (int) count).iterator();
    }

    @Override
    public long size() {
        return searchService.getTotalNumberOfDocumentsForQuery(searchQuery);
    }

    @Override
    public IModel<FullTextResult> model(final FullTextResult object) {
        return new LoadableDetachableModel<FullTextResult>() {
            @Override
            protected FullTextResult load() {
                return object;
            }
        };
    }
}
