package cz.zcu.kiv.eegdatabase.logic.controller.searchsolr;

import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import cz.zcu.kiv.eegdatabase.data.indexing.IndexField;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 12.3.13
 * Time: 20:53
 * To change this template use File | Settings | File Templates.
 */
@Service
public class FulltextSearchService {

    @Autowired
    private SolrServer solrServer;
    @Autowired
    private HardwareDao hardwareFacade;

    public List<FullTextResult> getResultsForQuery(String inputQuery) {
        SolrQuery query = new SolrQuery();
        query.set("df", "text_all");
        query.setQuery(inputQuery);
        query.setRows(200);
        QueryResponse response = null;
        try {
            response = solrServer.query(query);
        } catch (SolrServerException e) {
            //e.printStackTrace();
            return new ArrayList<FullTextResult>();
        }
        List<SolrDocument> foundDocuments = response.getResults();
        List<FullTextResult> results = new ArrayList<FullTextResult>();
        for(SolrDocument document : foundDocuments) {
            FullTextResult result = new FullTextResult();
            String uuid = (String) document.getFieldValue(IndexField.UUID.getValue());
            int id = (Integer) document.getFieldValue(IndexField.ID.getValue());
            String className = (String) document.getFieldValue(IndexField.CLASS.getValue());
            String title = (String) document.getFieldValue(IndexField.TITLE.getValue());
            String description = (String) document.getFieldValue(IndexField.TEXT.getValue());
            /*
            Object descriptionField = document.getFieldValue(IndexField.TEXT.getValue());
            if(descriptionField != null) {
                description = descriptionField.toString();
            }
            */

            Class<?> instance = null;
            try {
                if(className != null) {
                    instance = Class.forName(className);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            result.setUuid(uuid);
            result.setId(id);
            result.setInstance(FullTextSearchUtils.getTargetPage(instance));
            result.setType(FullTextSearchUtils.getDocumentType(result.getInstance()));
            System.out.println(result.getClass().getName());
            if(title != null) {
                result.setTitle(title);
            }
            if(description != null) {
                result.setText(description);
            }

            results.add(result);
            /*
            if (result.getInstance().equals(Hardware.class)) {
                List<FullTextResult> nextResults = expandResults(result);
                results.addAll(nextResults);
            }
            else {
                results.add(result);
            }
            */
        }

        return results;
    }

    public List<FullTextResult> getAllResults() {
        return getResultsForQuery("*");
    }

    /**
     * Gets values to autocomplete for the input string.
     * The autocomplete feature works for multivalued fields and is based on a highlighting trick.
     * See http://solr.pl/en/2013/02/25/autocomplete-on-multivalued-fields-using-highlighting/
     * @param keywordStart
     * @return
     * @throws SolrServerException
     */
    public Set<String> getTextToAutocomplete(String keywordStart) throws SolrServerException {
        SolrQuery query = new SolrQuery();
        query.setQuery(IndexField.AUTOCOMPLETE.getValue()+":"+keywordStart);
        query.setFields(IndexField.AUTOCOMPLETE.getValue());
        query.setHighlight(true);
        query.setParam("hl.fl", IndexField.AUTOCOMPLETE.getValue());
        query.setHighlightSimplePre("");
        query.setHighlightSimplePost("");
        query.setRows(FullTextSearchUtils.AUTOCOMPLETE_ROWS);

        Set<String> autocompleteList = new HashSet<String>();

        QueryResponse response = solrServer.query(query);
        Set<String> foundIds = response.getHighlighting().keySet();
        for(String id : foundIds) {
            List<String> resultsPerDocument = response.getHighlighting().get(id).get(IndexField.AUTOCOMPLETE.getValue());
            for (String result : resultsPerDocument) {
                autocompleteList.add(result.toLowerCase());
            }

            if(autocompleteList.size() == FullTextSearchUtils.AUTOCOMPLETE_ROWS) {
                break;
            }
        }

        return autocompleteList;
    }

    public void cleanupIndex() throws IOException, SolrServerException {
        solrServer.deleteByQuery("*:*");
    }

    private List<FullTextResult> expandResults(FullTextResult result) {

        List<FullTextResult> expandedResults = new ArrayList<FullTextResult>();

        if(result.getInstance().equals(Hardware.class)) {
            int id = result.getId();
            Set<Experiment> experiments = hardwareFacade.read(id).getExperiments();
            Set<ResearchGroup> researchGroups = hardwareFacade.read(id).getResearchGroups();

            for(Experiment experiment : experiments) {
                FullTextResult expandedResult = new FullTextResult();
                expandedResult.setId(experiment.getExperimentId());
                expandedResult.setInstance(FullTextSearchUtils.getTargetPage(Experiment.class));
                expandedResult.setUuid(result.getUuid());
                expandedResult.setTitle(result.getTitle());
                expandedResult.setText(result.getText());

                expandedResults.add(expandedResult);
            }
            for(ResearchGroup researchGroup : researchGroups) {
                FullTextResult expandedResult = new FullTextResult();
                expandedResult.setId(researchGroup.getResearchGroupId());
                expandedResult.setInstance(FullTextSearchUtils.getTargetPage(ResearchGroup.class));
                expandedResult.setUuid(result.getUuid());
                expandedResult.setTitle(result.getTitle());
                expandedResult.setText(result.getText());

                expandedResults.add(expandedResult);
            }
        }

        return expandedResults;
    }
}
