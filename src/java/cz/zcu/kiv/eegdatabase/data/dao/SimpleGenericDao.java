package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.logic.controller.search.Queries;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import org.apache.lucene.store.Directory;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.store.DirectoryProvider;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Class implements interface for connecting logic and data layer.
 * This class makes create, showing, delete,
 * read and update data possible.
 * @author Pavel Bořík, A06208
 */
public class SimpleGenericDao<T, PK extends Serializable>
        extends HibernateDaoSupport implements GenericDao<T, PK> {

  protected Class<T> type;
  protected Log log = LogFactory.getLog(getClass());

  public SimpleGenericDao(Class<T> type) {
    this.type = type;
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
   * @param id - Id selecting (searching) record
   * @return object that was selected in database in
   * agreement with PK
   */
  public T read(PK id) {
    return (T) getHibernateTemplate().get(type, id);
  }

  /**
   * Method update data in database.
   * @param transientObject - updating object
   */
  public void update(T transientObject) {
    getHibernateTemplate().update(transientObject);
  }

  /**
   * Delete data from database.
   * Method doesn't called by logic layer yet.
   * @param persistObject - Object that will be deleted from database
   */
  public void delete(T persistentObject) {
    getHibernateTemplate().delete(persistentObject);
  }

  /**
   * Method gets all records from database.
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
   * @param first - first select records (start from grant zero)
   * @param max - count of records
   * @return list that includes specific count of records
   */
  public List<T> getRecordsAtSides(int first, int max) {
    return getHibernateTemplate().findByCriteria(
            DetachedCriteria.forClass(type), first, max);
  }

  /**
   * Method gets count of records in database.
   * Method doesn't called by logic layer yet.
   * @return count of records in database
   */
  public int getCountRecords() {
    return getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(type)).size();
    //return getHibernateTemplate().loadAll(type).size();
  }

  public Queries getLuceneQuery(String fullTextQuery, String[] fields) throws ParseException {


    MultiFieldQueryParser parser = null;
    parser = new MultiFieldQueryParser(fields, new StandardAnalyzer(new HashSet()));
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
    
    try {
      searcher = new IndexSearcher(d);
      
      luceneQuery = searcher.rewrite(luceneQuery);
    } catch (CorruptIndexException ex) {
      throw new RuntimeException("Unable to index.");
    } catch (IOException ex) {
      throw new RuntimeException("Unable to read index directory");
    }
    return new Queries(luceneQuery, fQuery);

  //  return luceneQuery;
  }
}

