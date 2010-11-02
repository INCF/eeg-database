/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.controller.search;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
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
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.store.DirectoryProvider;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author Honza
 */
public abstract class SectionCreator extends HibernateDaoSupport {

  protected final int MAIN_OBJECT = 0;
  protected final int ONE_REL = 1;
  protected final int SET = 2;
  protected Highlighter ht;
  protected Analyzer analyzer = new StandardAnalyzer();
  protected Encoder encoder = new SimpleHTMLEncoder();
  protected Formatter formatter = new SimpleHTMLFormatter("<strong>", "</strong>");
  protected Fragmenter fragmenter = new SimpleFragmenter(150);

  public abstract Set<FulltextResult> createSection(Query query, Class type, String[] fields);

  
  protected List<Object> getFulltextResults(Query query, Class type) {
    Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
    FullTextSession fts = org.hibernate.search.Search.getFullTextSession(session);
    FullTextQuery fQuery = fts.createFullTextQuery(query, type);
    DirectoryProvider[] providers = fts.getSearchFactory().getDirectoryProviders(type);

    Directory d = providers[0].getDirectory();

    IndexSearcher searcher;
    try {
      searcher = new IndexSearcher(d);
      query = searcher.rewrite(query);
    } catch (CorruptIndexException ex) {
      throw new RuntimeException("Unable to index.");
    } catch (IOException ex) {
      throw new RuntimeException("Unable to read index directory");
    }
    QueryScorer scorer = new QueryScorer(query);
    ht = new Highlighter(formatter, encoder, scorer);
    ht.setTextFragmenter(fragmenter);
    return fQuery.list();
  }

  protected String getHighlightedText(String[] fields, Object t) throws Exception{
    Field[] field = new Field[fields.length];
     String text = "";
          for (int i = 0; i < field.length; i++) {
            field[i] = t.getClass().getDeclaredField(fields[i]);
            field[i].setAccessible(true);
            if (field[i].get(t) != null) {
              String fragment = ht.getBestFragment(analyzer, field[i].getName(), "" + field[i].get(t));
              if (fragment == null) {
                text += field[i].get(t)+ " ";
              } else {
                text += fragment + " ";
              }
            }
          }
    return text;
  }

}
