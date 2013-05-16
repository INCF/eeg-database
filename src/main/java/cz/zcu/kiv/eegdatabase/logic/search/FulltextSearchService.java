package cz.zcu.kiv.eegdatabase.logic.search;

import cz.zcu.kiv.eegdatabase.logic.indexing.IndexField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * The full text search service. Provides search and faceting methods.
 * User: Jan Koren
 * Date: 12.3.13
 */
@Service
public class FulltextSearchService {

    @Autowired
    private SolrServer solrServer;

    protected Log log = LogFactory.getLog(getClass());

    /**
     * Gets results for a given query string.
     * @param inputQuery The input query.
     * @param start The index of the first returned result.
     * @param count Number of retrieved results.
     * @return List of full text results.
     */
    public List<FullTextResult> getResultsForQuery(String inputQuery, ResultCategory category, int start, int count) {

        SolrQuery query = configureQuery(inputQuery, category, start, count);
        // fetches a response to the query
        QueryResponse response = null;
        try {
            response = solrServer.query(query);
        } catch (SolrServerException e) {
            return new ArrayList<FullTextResult>();
        }
        List<SolrDocument> foundDocuments = response.getResults();
        List<FullTextResult> results = new ArrayList<FullTextResult>();
        for(SolrDocument document : foundDocuments) {
            FullTextResult result = new FullTextResult();
            String uuid = (String) document.getFieldValue(IndexField.UUID.getValue());
            int id = (Integer) document.getFieldValue(IndexField.ID.getValue());
            String type = (String) document.getFieldValue(IndexField.CLASS.getValue());
            Date timestamp = (Date) document.getFieldValue(IndexField.TIMESTAMP.getValue());
            String source = (String) document.getFieldValue(IndexField.SOURCE.getValue());
            if(source == null) {
                source = "";
            }

            // return text with highlighted words.
            String title = getHighlightedText(response, uuid, IndexField.TITLE);
            List<String> textFragments = new ArrayList<String>();
            String description = getHighlightedText(response, uuid, IndexField.TEXT);
            addNotEmpty(description, textFragments);
            String name = getHighlightedText(response, uuid, IndexField.NAME);
            addNotEmpty(name, textFragments);
            addNotEmpty(getHighlightedText(response, uuid, IndexField.CHILD_TITLE), textFragments);
            addNotEmpty(getHighlightedText(response, uuid, IndexField.CHILD_TEXT), textFragments);

            result.setUuid(uuid);
            result.setId(id);
            result.setTargetPage(FullTextSearchUtils.getTargetPage(type));
            result.setTimestamp(timestamp);
            result.setType(type);
            result.setSource(source);

            result.setTitle(title);
            result.setTextFragments(textFragments);

            results.add(result);
        }

        return results;
    }

    /**
     * Adds a string element to the list only and if only the element is not empty.
     * @param text
     * @param list
     */
    private void addNotEmpty(String text, List<String> list) {
        if(!text.isEmpty()) {
            list.add(text);
        }
    }

    /**
     * Configures the query to be processed.
     * @param inputQuery The query string.
     * @param start Index of the first returned result.
     * @param count Number of results we wish to display.
     * @return Configured query.
     */
    private SolrQuery configureQuery(String inputQuery, ResultCategory category, int start, int count) {
        SolrQuery query = new SolrQuery();
        query.setQuery(inputQuery);
        if(category != null && !category.equals(ResultCategory.ALL)) {
            query.addFilterQuery(IndexField.CLASS.getValue() + ":\"" + category.getValue() + "\"");
        }
        query.setStart(start);
        query.setRows(count);
        query.setHighlightSimplePre(FullTextSearchUtils.HIGHLIGHTED_TEXT_BEGIN);
        query.setHighlightSimplePost(FullTextSearchUtils.HIGHLIGHTED_TEXT_END);
        return query;
    }

    /**
     * Gets text that contains highlighted search words.
     * Neighbouring highlighted words are merged into one highlighted phrase.
     * @param response The response to the search query, contains search results.
     * @param uuid id of the found document.
     * @param field searched field.
     * @return Text with or without highlighted words, depending whether the text contains any of the searched words.
     *
     */
    private String getHighlightedText(QueryResponse response, String uuid, IndexField field) {
        List<String> highlightedTextList = response.getHighlighting().get(uuid).get(field.getValue());
        if(highlightedTextList == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer("");
        for(String highlightedText : highlightedTextList) {
            sb.append(highlightedText.replace(FullTextSearchUtils.HIGHLIGHT_MERGE_SEQUENCE, " "));
        }
        return sb.toString();
    }

    /**
     * Fetches all indexed documents.
     * @return All indexed documents.
     * @throws SolrServerException
     */
    public List<FullTextResult> getAllResults() throws SolrServerException {
        int resultsFound = getTotalNumberOfDocumentsForQuery("*", ResultCategory.ALL);
        return getResultsForQuery("*", ResultCategory.ALL,  0, resultsFound);
    }

    /**
     * Gets values to autocomplete for the input string.
     * The autocomplete feature works for multivalued fields and is based on a highlighting trick.
     * See http://solr.pl/en/2013/02/25/autocomplete-on-multivalued-fields-using-highlighting/
     * @param keywordStart
     * @return Autocomplete values.
     * @throws SolrServerException
     */
    public Set<String> getTextToAutocomplete(String keywordStart) throws SolrServerException {
        SolrQuery query = new SolrQuery();
        query.setQuery(IndexField.AUTOCOMPLETE.getValue() + ":" + keywordStart);
        query.setFields(IndexField.AUTOCOMPLETE.getValue());
        query.setHighlight(true);
        query.setParam("hl.fl", IndexField.AUTOCOMPLETE.getValue());
        query.setHighlightSimplePre("");
        query.setHighlightSimplePost("");
        query.setRows(FullTextSearchUtils.AUTOCOMPLETE_ROWS);

        Map<String, Integer> map = new TreeMap<String, Integer>();
        QueryResponse response = solrServer.query(query);

        Set<String> foundIds = response.getHighlighting().keySet();
        for(String id : foundIds) {

            List<String> resultsPerDocument = response.getHighlighting().get(id).get(IndexField.AUTOCOMPLETE.getValue());
            for (String result : resultsPerDocument) {
                String resultValue;
                int resultFrequency;
                int delimiterPosition = result.lastIndexOf('#');
                if (delimiterPosition == -1) { // autocomplete phrase was copied from title
                    resultValue = result;
                    resultFrequency = 0;
                }
                else {
                    resultValue = result.substring(0, delimiterPosition);
                    try {
                        resultFrequency = Integer.valueOf(result.substring(delimiterPosition + 1, result.length()));
                    } catch (NumberFormatException e) {
                        resultFrequency = 0;
                    }
                }

                map.put(resultValue.toLowerCase(), resultFrequency);
            }

            if(map.size() == FullTextSearchUtils.AUTOCOMPLETE_ROWS) {
                break;
            }
        }

        map = sortByValue(map);
        return map.keySet();
    }

    /**
     * Deletes all documents form the index.
     * @throws IOException
     * @throws SolrServerException
     */
    public void cleanupIndex() throws IOException, SolrServerException {
        solrServer.deleteByQuery("*:*");
    }

    /**
     * Gets the number of all documents matching the query.
     * @param queryString The query string.
     * @return The number of all documents matching the query.
     */
    public int getTotalNumberOfDocumentsForQuery(String queryString, ResultCategory category) {
        SolrQuery q = new SolrQuery();
        q.setQuery(queryString);
        if(category != null && !category.equals(ResultCategory.ALL)) {
            q.setFilterQueries(IndexField.CLASS.getValue() + ":\"" + category.getValue() + "\"") ;
        }
        q.setRows(0); // don't actually request any data
        try {
            return (int) solrServer.query(q).getResults().getNumFound();
        } catch (SolrServerException e) {
            log.error(e);
            e.getCause().printStackTrace();
        }
        return 0;
    }

    /**
     * Helper method for sorting a map by its map values.
     * @param map The map to be sorted.
     * @param <K> the map key type
     * @param <V> the map value type
     * @return Sorted map.
     */
    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

    /**
     * Given the input query, finds out the total count of full text results for each full text result type.
     * @param solrQuery The search query.
     * @return Map containg category-count pairs.
     */
    public Map<String, Long> getCategoryFacets(String solrQuery) {
        SolrQuery query = new SolrQuery(solrQuery);
        query.setParam("fl", IndexField.UUID.getValue());
        query.setHighlight(false);
        query.setFacet(true);
        query.addFacetField(IndexField.CLASS.getValue());

        Map<String, Long> results = new HashMap<String, Long>();
        QueryResponse response = null;
        try {
            response = solrServer.query(query);
        } catch (SolrServerException e) {
            log.error(e);
        }
        long totalCount = 0;
        List<FacetField> facets = response.getFacetFields();
        for(FacetField field : facets) {
            log.info("count: " + field.getValueCount());
            List<FacetField.Count> facetEntries = field.getValues();
            for (FacetField.Count count : facetEntries) {
                long countValue = count.getCount();
                results.put(count.getName(), countValue);
                log.info(count.getName() + ", " + countValue);
                totalCount += countValue;
            }
        }

        // add a "facet" for all results
        results.put(ResultCategory.ALL.getValue(), totalCount);

        return results;
    }
}


