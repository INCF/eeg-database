package cz.zcu.kiv.eegdatabase.wui.ui.search;

import cz.zcu.kiv.eegdatabase.logic.controller.searchsolr.FulltextSearchService;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;

import java.util.*;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class SearchPage extends MenuPage {
    
    private static final long serialVersionUID = -1967810037377960414L;

    @SpringBean
    private FulltextSearchService searchService;

    public SearchPage() throws SolrServerException {
        
        setPageTitle(ResourceUtils.getModel("title.page.search"));

        //add(new Label("searchResult", String.valueOf(searchService.getAllDocuments().size())));

        Form<Void> searchForm = new Form<Void>("searchForm");
        add(searchForm);

        final AutoCompleteTextField<String> searchField = new AutoCompleteTextField<String>(
                "autocomplete", new Model<String>("")) {
            @Override
            protected Iterator<String> getChoices(String input) {
                return getAutoCompleteChoices(input);
            }
        };
        searchForm.add(searchField);

        /*
        searchField.add(new AjaxFormSubmitBehavior(searchForm, "onchange") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                super.onError(target);
            }
        });
        */

    }

    private Iterator<String> getAutoCompleteChoices(String input) {
        if(Strings.isEmpty(input)) {
            List<String> emptyList = Collections.emptyList();
            return emptyList.iterator();
        }

        Set<String> autocompletePhrases = new HashSet<String>(10);
        try {
            autocompletePhrases = searchService.getTextToAutocomplete(input);
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return autocompletePhrases.iterator();
    }
}
