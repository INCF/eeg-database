package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.logic.indexing.PojoIndexer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.util.Version;
import org.apache.solr.client.solrj.SolrServerException;
import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Class implements interface for connecting logic and data layer.
 * This class makes create, showing, delete,
 * read and update data possible.
 *
 * @author Pavel Bořík, A06208
 */
public class SimpleGenericDao<T, PK extends Serializable>
        extends HibernateDaoSupport implements GenericDao<T, PK> {

    @Autowired
    PojoIndexer indexer;

    protected final static Version LUCENE_COMPATIBILITY_VERSION = Version.LUCENE_31;

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
        // first save the instance
        PK primaryKey = (PK) getHibernateTemplate().save(newInstance);
        // then add marked fields to the solr index
        // currently DISABLED, because when the solr server is down,
        // an exception is thrown

        try {
            indexer.index(newInstance);
        } catch (IOException e) {
            log.error(e);
        } catch (SolrServerException e) {
            log.error(e);
        } catch (IllegalAccessException e) {
            log.error(e);
        }

        return primaryKey;
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
     * Method read record (row) in database based on column and it's value.
     *
     * @param parameterName  - hibernate name of the parameter (column)
     * @param parameterValue - value of the parameter
     * @return object that was selected in database
     */
    public List<T> readByParameter(String parameterName, int parameterValue) {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(type);
        criteria.add(Restrictions.eq(parameterName, parameterValue));
        return criteria.list();
    }

    /**
     * Method read record (row) in database based on column and it's value.
     *
     * @param parameterName  - hibernate name of the parameter (column)
     * @param parameterValue - value of the parameter
     * @return object that was selected in database
     */
    public List<T> readByParameter(String parameterName, String parameterValue) {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(type);
        criteria.add(Restrictions.eq(parameterName, parameterValue));
        return criteria.list();
    }

    /**
     * Method update data in database.
     *
     * @param transientObject - updating object
     */
    public void update(T transientObject) {
        getHibernateTemplate().update(transientObject);
        // currently DISABLED, because when the solr server is down,
        // an exception is thrown
        try {
            indexer.index(transientObject);
        } catch (IllegalAccessException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        } catch (SolrServerException e) {
            log.error(e);
        }
    }

    /**
     * Delete data from database.
     * Method doesn't called by logic layer yet.
     *
     * @param persistentObject - Object that will be deleted from database
     */
    public void delete(T persistentObject) {
        getHibernateTemplate().delete(persistentObject);

        try {
            indexer.unindex(persistentObject);
        } catch (IOException e) {
            log.error(e);
        } catch (SolrServerException e) {
            log.error(e);
        }
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

    public List<T> getAllRecordsFull() {
        List<T> records = getAllRecords();
        for(T record : records) {
            Method[] methods = record.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if(method.getName().startsWith("get") && !method.getReturnType().isPrimitive()) {
                    try {
                        initializeProperty(method.invoke(record));
                    } catch (IllegalAccessException e) {
                        log.error(e);
                    } catch (InvocationTargetException e) {
                        log.error(e);
                    } catch (Exception e) {
                        log.error(e);
                    }
                }
            }
        }
        return records;
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
        /*
        List<String> fieldsList = new ArrayList<String>();
        Field[] an = type.getDeclaredFields();
        for (int i = 0; i < an.length; i++) {
            if (an[i].isAnnotationPresent(Fields.class)) {
                fieldsList.add(an[i].getName());
            }
        }
        String[] fields = new String[fieldsList.size()];
        fields = fieldsList.toArray(fields);

        MultiFieldQueryParser parser = new MultiFieldQueryParser(LUCENE_COMPATIBILITY_VERSION, fields, new StandardAnalyzer(LUCENE_COMPATIBILITY_VERSION));
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
        */

        return null;
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

    protected void initializeProperty(Object property) {
        if(!Hibernate.isInitialized(property)) {
            getHibernateTemplate().initialize(property);
        }
    }
}

