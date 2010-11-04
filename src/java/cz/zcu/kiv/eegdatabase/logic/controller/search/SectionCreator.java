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
public abstract class SectionCreator {

  protected final int MAIN_OBJECT = 0;
  protected final int ONE_REL = 1;
  protected final int SET = 2;
  protected Highlighter ht;
  protected Analyzer analyzer = new StandardAnalyzer();
  protected Encoder encoder = new SimpleHTMLEncoder();
  protected Formatter formatter = new SimpleHTMLFormatter("<span class=\"highlightText\">", "</span>");
  protected Fragmenter fragmenter = new SimpleFragmenter(150);

  public abstract Set<FulltextResult> createSection(Queries queries, Class type, String[] fields);

  protected List<Object> getFulltextResults(Queries queries, Class type) {

    Query query = queries.getLuceneQuery();
    QueryScorer scorer = new QueryScorer(query);
    ht = new Highlighter(formatter, encoder, scorer);
    ht.setTextFragmenter(fragmenter);
    return queries.getFullTextQuery().list();
  }

  protected String getHighlightedText(String[] fields, Object t) throws Exception {
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
