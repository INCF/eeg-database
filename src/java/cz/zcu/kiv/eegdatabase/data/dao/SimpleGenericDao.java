package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import org.apache.lucene.search.highlight.Encoder;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLEncoder;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
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

  public List<T> getFullTextResult(String fullTextQuery, String[] fields) throws ParseException {

    Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

    MultiFieldQueryParser parser = null;
    parser = new MultiFieldQueryParser(fields, new StandardAnalyzer(new HashSet()));


    FullTextSession fts = org.hibernate.search.Search.getFullTextSession(session);
//    List<Object> entities = fts.createCriteria(type).list();
//
//    for (Object entity : entities) {
//        fts.index(entity);  //manually index an item instance
//    }

    org.apache.lucene.search.Query luceneQuery;
    try {
      luceneQuery = parser.parse(fullTextQuery);
    } catch (ParseException e) {
      throw new RuntimeException("Unable to parse query: " + fullTextQuery, e);
    }

    FullTextQuery query = fts.createFullTextQuery(luceneQuery, type);  //return matching Items
    query.setFirstResult(0).setMaxResults(20);                 //Use pagination

    //gets directory with indexes
    DirectoryProvider[] providers = fts.getSearchFactory().getDirectoryProviders(type);

    Directory d = providers[0].getDirectory();

    IndexSearcher searcher;
    try {
      searcher = new IndexSearcher(d);
      luceneQuery = searcher.rewrite(luceneQuery);
    } catch (CorruptIndexException ex) {
      throw new RuntimeException("Unable to index.");
    } catch (IOException ex) {
      throw new RuntimeException("Unable to read index directory");
    }



    Fragmenter fragmenter = new SimpleFragmenter(100);
    QueryScorer scorer = new QueryScorer(luceneQuery);
    Encoder encoder = new SimpleHTMLEncoder();
    Formatter formatter = new SimpleHTMLFormatter("<b>", "</b>");

    Analyzer analyzer = new StandardAnalyzer();
    Highlighter ht = new Highlighter(formatter, encoder, scorer);
    ht.setTextFragmenter(fragmenter);
    List<T> result = query.list();
//    try {

    if (!result.isEmpty()) {

      for (T t : result) {
        try {
          //    String highlightedText = ht.getBestFragment(analyzer, "WEATHERNOTE", "");
          //   System.out.println(hl.getBestFragment());
          if (t instanceof Scenario) {
            Scenario scenario = (Scenario) t;
            String title = ht.getBestFragment(analyzer, "title", scenario.getTitle());
            String description = ht.getBestFragment(analyzer, "description", scenario.getDescription());
////
           // System.out.println(title + " " + description);
          }
////          if (t instanceof Experiment) {
////            experiment = (Experiment) t;
////            String weatherNote = ht.getBestFragment(analyzer, "WEATHERNOTE", experiment.getWeathernote());
////
////            //System.out.println(weatherNote);
////          }
////          if (t instanceof Person) {
////            person = (Person) t;
////            String note = ht.getBestFragment(analyzer, "NOTE", person.getNote());
////            // System.out.println(note);
////          }
////
////          if (t instanceof Scenario) {
////            scenario = (Scenario) t;
////            String title = ht.getBestFragment(analyzer, "TITLE", scenario.getTitle());
////            String description = ht.getBestFragment(analyzer, "DESCRIPTION", scenario.getDescription());
////
////            //System.out.println(title + " " + description);
////          }
////          if (t instanceof Experiment) {
////            experiment = (Experiment) t;
////            String weatherNote = ht.getBestFragment(analyzer, "WEATHERNOTE", experiment.getWeathernote());
////
////            //System.out.println(weatherNote);
////          }
////          if (t instanceof Person) {
////            person = (Person) t;
////            String note = ht.getBestFragment(analyzer, "NOTE", person.getNote());
////            // System.out.println(note);
////          }
        } catch (IOException ex) {
          throw new RuntimeException("Unable to highlight");
        }
       
      }
    }

//      } catch  (IOException ex) {
//      log.debug(ex);
//    }


    //execute the query

    return result;
  }
}

