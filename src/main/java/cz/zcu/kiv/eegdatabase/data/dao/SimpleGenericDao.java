package cz.zcu.kiv.eegdatabase.data.dao;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.store.DirectoryProvider;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Class implements interface for connecting logic and data layer.
 * This class makes create, showing, delete,
 * read and update data possible.
 *
 * @author Pavel Bořík, A06208
 */
public class SimpleGenericDao<T, PK extends Serializable>
        extends HibernateDaoSupport implements GenericDao<T, PK> {

    protected final static Version LUCENE_COMPATIBILITY_VERSION = Version.LUCENE_34;

    protected Class<T> type;
    protected Log log = LogFactory.getLog(getClass());

    public SimpleGenericDao(Class<T> type) {
        this.type = type;
    }

    public SimpleGenericDao() {
    }

    /*
    * Create new record (row) in database.
    * @param newInstance - Object that will be created in database
    * @return record (row) saving in database
    */
    public PK create(T newInstance) {
        return (PK) getHibernateTemplate().save(newInstance);
    }

    /**
     * Method read record (row) in database.
     * Record select in agreement with Primary Key (id).
     *
     * @param id - Id selecting (searching) record
     * @return object that was selected in database in
     *         agreement with PK
     */
    public T read(PK id) {
        return (T) getHibernateTemplate().get(type, id);
    }

    /**
     * Method update data in database.
     *
     * @param transientObject - updating object
     */
    public void update(T transientObject) {
        getHibernateTemplate().update(transientObject);
    }

    /**
     * Delete data from database.
     * Method doesn't called by logic layer yet.
     *
     * @param persistentObject - Object that will be deleted from database
     */
    public void delete(T persistentObject) {
        getHibernateTemplate().delete(persistentObject);
    }

    /**
     * Method gets all records from database.
     *
     * @return list that includes all records
     */
    public List<T> getAllRecords() {
       return getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(type));
        //return getHibernateTemplate().loadAll(type);
    }

    /**
     * Get specific count of records from database.
     * Count is given values of params.
     * Method doesn't called by logic layer yet.
     *
     * @param first - first select records (start from grant zero)
     * @param max   - count of records
     * @return list that includes specific count of records
     */
    public List<T> getRecordsAtSides(int first, int max) {
        return getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(type), first, max);
    }

    /**
     * Method gets count of records in database.
     * Method doesn't called by logic layer yet.
     *
     * @return count of records in database
     */
    public int getCountRecords() {
        return getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(type)).size();
        //return getHibernateTemplate().loadAll(type).size();
    }

    public Map<T, String> getFulltextResults(String fullTextQuery) throws ParseException {
        List<String> fieldsList = new ArrayList<String>();
        Field[] an = type.getDeclaredFields();
        for (int i = 0; i < an.length; i++) {
            if (an[i].isAnnotationPresent(Fields.class)) {
                fieldsList.add(an[i].getName());
            }
        }
        String[] fields = new String[fieldsList.size()];
        fields = fieldsList.toArray(fields);

        MultiFieldQueryParser parser = new MultiFieldQueryParser(LUCENE_COMPATIBILITY_VERSION,fields, new StandardAnalyzer(LUCENE_COMPATIBILITY_VERSION));
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        FullTextSession fts = org.hibernate.search.Search.getFullTextSession(session);

        DirectoryProvider[] providers = fts.getSearchFactory().getDirectoryProviders(type);

        Directory d = providers[0].getDirectory();
        Query luceneQuery;
        try {
            luceneQuery = parser.parse(fullTextQuery);
        } catch (ParseException e) {
            throw new RuntimeException("Unable to parse query: " + fullTextQuery, e);
        }
        FullTextQuery fQuery = fts.createFullTextQuery(luceneQuery, type);
        IndexSearcher searcher;
        Map<T, String> map = new HashMap<T, String>();
        try {
            searcher = new IndexSearcher(d);
            Analyzer analyzer = new StandardAnalyzer(LUCENE_COMPATIBILITY_VERSION);
            Encoder encoder = new SimpleHTMLEncoder();
            Formatter formatter = new SimpleHTMLFormatter("<span class=\"highlightText\">", "</span>");
            Fragmenter fragmenter = new SimpleFragmenter(150);
            luceneQuery = searcher.rewrite(luceneQuery);
            QueryScorer scorer = new QueryScorer(luceneQuery);
            Highlighter ht = new Highlighter(formatter, encoder, scorer);
            ht.setTextFragmenter(fragmenter);

            List<T> list = fQuery.list();
            for (T t : list) {
                try {
                    map.put(t, getHighlightedText(fields, t, ht, analyzer));
                } catch (NoSuchFieldException e) {

                }
            }
        } catch (CorruptIndexException ex) {
            throw new RuntimeException("Unable to index.");
        } catch (IOException ex) {
            throw new RuntimeException("Unable to read index directory");
        } catch (Exception ex) {
            Logger.getLogger(SimpleGenericDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    protected String getHighlightedText(String[] fields, T t, Highlighter ht, Analyzer analyzer) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, IOException, InvalidTokenOffsetsException {
        Field[] field = new Field[fields.length];
        String text = "";
        for (int i = 0; i < field.length; i++) {
            field[i] = t.getClass().getDeclaredField(fields[i]);

            field[i].setAccessible(true);
            if (field[i].get(t) != null) {
                String fragment = ht.getBestFragment(analyzer, field[i].getName(), "" + field[i].get(t));
                if (fragment == null) {
                    text += field[i].get(t) + " ";
                } else {
                    text += fragment + " ";
                }
            }
        }
        return text;
    }
}

