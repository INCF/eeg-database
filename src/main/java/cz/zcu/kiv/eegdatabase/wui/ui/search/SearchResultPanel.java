package cz.zcu.kiv.eegdatabase.wui.ui.search;

import cz.zcu.kiv.eegdatabase.logic.controller.searchsolr.FullTextResult;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 26.3.13
 * Time: 1:13
 * To change this template use File | Settings | File Templates.
 */

public class SearchResultPanel extends Panel {

    private static long serialVersionUID = 1L;
    private static final int ITEMS_PER_PAGE = 20;

    private boolean isModelEmpty = false;
    private boolean foundResultsPerMultiplePages = false;

    public SearchResultPanel(String id, IModel<List<FullTextResult>> fullTextResults) {
        super(id);
        if(fullTextResults.getObject().isEmpty()) {
            isModelEmpty = true;
        }
        if(fullTextResults.getObject().size() > ITEMS_PER_PAGE) {
            foundResultsPerMultiplePages = true;
        }

        SearchResultList searchResultList =  new SearchResultList("results", fullTextResults, ITEMS_PER_PAGE);
        add(searchResultList);
        add(new PagingNavigator("navigator", searchResultList) {
            @Override
            public boolean isVisible() {
                return foundResultsPerMultiplePages;
            }
        });

        add(new Label("noResultsFound", "No results found") {
            @Override
            public boolean isVisible() {
                return isModelEmpty;
            }
        });
    }

    private class SearchResultList extends PageableListView<FullTextResult> {

        public SearchResultList(final String id, final IModel<List<FullTextResult>> model, int itemsPerPage) {
            super(id, model, itemsPerPage);
        }

        @Override
        protected void populateItem(ListItem<FullTextResult> item) {
            item.setModel(new CompoundPropertyModel<FullTextResult>(item.getModel()));

            item.add(new Label("uuid"));
            item.add(new Label("title"));
            item.add(new Label("type"));
            item.add(new Label("text"));
            BookmarkablePageLink link = new BookmarkablePageLink<Void>("link", item.getModelObject().getInstance(),
                    PageParametersUtils.getDefaultPageParameters(item.getModelObject().getId()));
            item.add(link);
        }
    }
}
