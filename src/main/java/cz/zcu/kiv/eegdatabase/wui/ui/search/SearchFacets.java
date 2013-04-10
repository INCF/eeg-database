package cz.zcu.kiv.eegdatabase.wui.ui.search;

import cz.zcu.kiv.eegdatabase.logic.controller.searchsolr.FullTextSearchUtils;
import cz.zcu.kiv.eegdatabase.logic.controller.searchsolr.FulltextSearchService;
import cz.zcu.kiv.eegdatabase.logic.controller.searchsolr.ResultCategory;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListItemModel;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Faceted navigation for fulltext search results.
 *
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 8.4.13
 * Time: 5:03
 * To change this template use File | Settings | File Templates.
 */
public class SearchFacets extends Panel {

    @SpringBean
    private FulltextSearchService searchService;

    private String searchString;
    private List<FacetCategory> categories;

    public SearchFacets(String id, StringValue searchStringValue) {
        super(id);
        this.searchString = searchStringValue.toString();
        categories = new ArrayList<FacetCategory>();

        Map<String, Long> facets = searchService.getCategoryFacets(this.searchString);
        for(Map.Entry<String, Long> entry : facets.entrySet()) {
            if(entry.getValue() > 0) {
                FacetCategory facetCategory = new FacetCategory(entry.getKey(), entry.getValue());
                categories.add(facetCategory);
            }
        }

        add(new ListView<FacetCategory>("categoryList", categories) {
            @Override
            protected void populateItem(ListItem<FacetCategory> category) {
                PageParameters pageParameters = new PageParameters();
                String categoryName = getPropertyForType(category.getModelObject().getName());
                pageParameters.add("searchString", searchString
                        + " && class:\"" + category.getModelObject().getName() + "\"");
                String categoryValue = categoryName + " (" + category.getModelObject().getCount() + ")";
                category.add(new BookmarkablePageLink<Void>("category", SearchPage.class, pageParameters)
                        .add(new Label("categoryName", categoryValue)));
            }
        });
    }

    /**
     * Helper method which maps types of fulltext results to their corresponding properties.
     * @param category
     * @return
     */
    private String getPropertyForType(String category) {
        Map<String, String> categoryToPropertyMap = new HashMap<String, String>();

        categoryToPropertyMap.put(ResultCategory.ARTICLE.getValue(), ResourceUtils.getString("text.search.facet.articles"));
        categoryToPropertyMap.put(ResultCategory.EXPERIMENT.getValue(), ResourceUtils.getString("text.search.facet.experiments"));
        categoryToPropertyMap.put(ResultCategory.PERSON.getValue(), ResourceUtils.getString("text.search.facet.persons"));
        categoryToPropertyMap.put(ResultCategory.RESEARCH_GROUP.getValue(), ResourceUtils.getString("text.search.facet.researchGroups"));
        categoryToPropertyMap.put(ResultCategory.SCENARIO.getValue(), ResourceUtils.getString("text.search.facet.scenarios"));

        return categoryToPropertyMap.get(category);
    }
}
