package cz.zcu.kiv.eegdatabase.wui.ui.search;

import cz.zcu.kiv.eegdatabase.logic.search.FulltextSearchService;
import cz.zcu.kiv.eegdatabase.logic.search.ResultCategory;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.util.*;

/**
 * Faceted navigation for fulltext search results.
 * User: Jan Koren
 * Date: 8.4.13
 * Time: 5:03
 */
public class SearchFacets extends Panel {

    @SpringBean
    private FulltextSearchService searchService;

    private String searchString;
    private List<FacetCategory> categories;
    private ResultCategory resultCategory;

    public SearchFacets(String id, StringValue searchStringValue, final ResultCategory resultCategory) {
        super(id);
        this.searchString = searchStringValue.toString();
        this.resultCategory = resultCategory;
        categories = new ArrayList<FacetCategory>();

        Map<String, Long> facets = searchService.getCategoryFacets(this.searchString);
        for(Map.Entry<String, Long> entry : facets.entrySet()) {
            if(entry.getValue() > 0) {
                FacetCategory facetCategory = new FacetCategory(entry.getKey(), entry.getValue());
                categories.add(facetCategory);
            }
        }

        Collections.sort(categories);

        add(new ListView<FacetCategory>("categoryList", categories) {
            @Override
            protected void populateItem(ListItem<FacetCategory> category) {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("searchString", searchString);
                pageParameters.add("category", category.getModelObject().getName());

                String categoryName = getPropertyForType(category.getModelObject().getName());
                String categoryValue = categoryName + " (" + category.getModelObject().getCount() + ")";
                Link link = new BookmarkablePageLink<Void>("category", SearchPage.class, pageParameters);
                if (resultCategory != null && category.getModelObject().getName().equals(resultCategory.getValue())) {
                    link.setEnabled(false);
                }
                category.add(link.add(new Label("categoryName", categoryValue)));
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

        categoryToPropertyMap.put(ResultCategory.ALL.getValue(), ResourceUtils.getString("text.search.facet.all"));
        categoryToPropertyMap.put(ResultCategory.ARTICLE.getValue(), ResourceUtils.getString("text.search.facet.articles"));
        categoryToPropertyMap.put(ResultCategory.EXPERIMENT.getValue(), ResourceUtils.getString("text.search.facet.experiments"));
        categoryToPropertyMap.put(ResultCategory.PERSON.getValue(), ResourceUtils.getString("text.search.facet.persons"));
        categoryToPropertyMap.put(ResultCategory.RESEARCH_GROUP.getValue(), ResourceUtils.getString("text.search.facet.researchGroups"));
        categoryToPropertyMap.put(ResultCategory.SCENARIO.getValue(), ResourceUtils.getString("text.search.facet.scenarios"));

        return categoryToPropertyMap.get(category);
    }
}
