package cz.zcu.kiv.eegdatabase.wui.ui.search;

import cz.zcu.kiv.eegdatabase.logic.controller.searchsolr.FullTextSearchUtils;
import cz.zcu.kiv.eegdatabase.logic.controller.searchsolr.FulltextSearchService;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.string.Strings;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 26.3.13
 * Time: 1:49
 * To change this template use File | Settings | File Templates.
 */
public class SearchPanel extends Panel {

    @SpringBean
    private FulltextSearchService searchService;

    public SearchPanel(String id, StringValue searchString) {
        super(id);
        add(new SearchForm("searchForm", searchString));
    }

    private class SearchForm extends Form {

        private String searchString;

        public SearchForm(String id, StringValue searchString) {
            super(id);
            if(!searchString.isNull() && !searchString.isEmpty()) {
                this.searchString = searchString.toString();
            }
            final AutoCompleteTextField<String> searchField = createTextField("autocomplete");
            add(searchField);
            final Button searchButton = createSearchButton("searchButton");
            add(searchButton);
        }

        private AutoCompleteTextField<String> createTextField(String id) {
            return new AutoCompleteTextField<String>(
                    id, new PropertyModel<String>(this, "searchString")) {
                @Override
                protected Iterator<String> getChoices(String input) {
                    return getAutoCompleteChoices(input);
                }
            };
        }

        private Button createSearchButton(String id) {
            return new AjaxFallbackButton(id, this) {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    PageParameters pageParameters = new PageParameters();
                    if(getSearchString() != null) {
                        pageParameters.add("searchString", getSearchString());
                        setResponsePage(SearchPage.class, pageParameters);
                    }
                }
            };
        }

        private Iterator<String> getAutoCompleteChoices(String input) {
            if(Strings.isEmpty(input)) {
                List<String> emptyList = Collections.emptyList();
                return emptyList.iterator();
            }

            Set<String> autocompletePhrases = new HashSet<String>(FullTextSearchUtils.AUTOCOMPLETE_ROWS);
            try {
                autocompletePhrases = searchService.getTextToAutocomplete(input);
            } catch (SolrServerException e) {
                e.printStackTrace();
            }
            return autocompletePhrases.iterator();
        }

        public String getSearchString() {
            return searchString;
        }

        public void setSearchString(String searchString) {
            this.searchString = searchString;
        }
    }


}
