package cz.zcu.kiv.eegdatabase.wui.ui.search;

import cz.zcu.kiv.eegdatabase.data.indexing.IndexingUtils;
import cz.zcu.kiv.eegdatabase.logic.controller.searchsolr.FullTextResult;
import cz.zcu.kiv.eegdatabase.logic.controller.searchsolr.ResultCategory;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.StringValue;

import java.io.Serializable;

/**
 * This component is responsible for displaying full text search results.
 *
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 26.3.13
 * Time: 1:13
 */
public class SearchResultPanel extends Panel {

    private static long serialVersionUID = 1L;
    private static final int ITEMS_PER_PAGE = 20;

    private boolean isModelEmpty = false;
    private boolean foundResultsPerMultiplePages = false;

    public SearchResultPanel(String id, StringValue searchString, ResultCategory category) {
        super(id);
        IDataProvider<FullTextResult> fullTextResults = new SearchResultDataProvider(searchString, category);
        long foundResults = fullTextResults.size();
        if(foundResults == 0) {
            isModelEmpty = true;
        }
        if(foundResults > ITEMS_PER_PAGE) {
            foundResultsPerMultiplePages = true;
        }

        add(new Label("numberOfResults", ResourceUtils.getString("text.search.numberOfResultsFound",
                new Model<Serializable>(foundResults))) {
            @Override
            public boolean isVisible() {
                return !isModelEmpty;
            }
        });

        add(new Label("noResultsFound", ResourceUtils.getString("text.search.noResults")) {
            @Override
            public boolean isVisible() {
                return isModelEmpty;
            }
        });

        add(new SearchFacets("categories", searchString, category));

        SearchResultList searchResultList =  new SearchResultList("results", fullTextResults, ITEMS_PER_PAGE);
        add(searchResultList);
        add(new PagingNavigator("navigator", searchResultList) {
            @Override
            public boolean isVisible() {
                return foundResultsPerMultiplePages;
            }
        });
    }

    /**
     * List that renders full text results.
     */
    private class SearchResultList extends DataView<FullTextResult> {

        protected SearchResultList(String id, IDataProvider<FullTextResult> dataProvider, long itemsPerPage) {
            super(id, dataProvider, itemsPerPage);
        }

        @Override
        public void populateItem(Item<FullTextResult> item) {
            item.setModel(new CompoundPropertyModel<FullTextResult>(item.getModel()));

            //item.add(new Label("uuid"));
            String title = item.getModelObject().getTitle();
            if(title.isEmpty()) {
                title = ResourceUtils.getString("text.search.results.title.empty");
            }

            final String source = item.getModelObject().getSource();
            String sourceString = "";
            if(source.equals(IndexingUtils.SOURCE_LINKEDIN)) {
                sourceString = ResourceUtils.getString("text.search.source.linkedin");
            }
            else {
                sourceString = ResourceUtils.getString("text.search.source.database");
            }

            item.add(new Label("title", title).setEscapeModelStrings(false));
            item.add(new Label("type"));
            item.add(new Label("source", sourceString) {
                @Override
                public boolean isVisible() {
                    return source.equals(IndexingUtils.SOURCE_LINKEDIN);
                }
            });
            item.add(new ListView<String>("textFragments") {
                @Override
                protected void populateItem(ListItem<String> item) {
                    item.add(new Label("textFragment", item.getModelObject()).setEscapeModelStrings(false));
                }
            });
            item.add(new DateLabel("timestamp", new PatternDateConverter(StringUtils.DATE_TIME_FORMAT_PATTER, true)));
            BookmarkablePageLink link = new BookmarkablePageLink<Void>("link", item.getModelObject().getTargetPage(),
                    PageParametersUtils.getDefaultPageParameters(item.getModelObject().getId()));
            item.add(link);
        }
    }
}
