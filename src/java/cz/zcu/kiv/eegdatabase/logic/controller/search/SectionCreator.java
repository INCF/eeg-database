/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.logic.wrapper.IWrapper;
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

/**
 *
 * @author Honza
 */
public abstract class SectionCreator {

  protected Highlighter ht;
  protected Analyzer analyzer = new StandardAnalyzer();
  protected Encoder encoder = new SimpleHTMLEncoder();
  protected Formatter formatter = new SimpleHTMLFormatter("<span class=\"highlightText\">", "</span>");
  protected Fragmenter fragmenter = new SimpleFragmenter(150);

  public abstract Set<FulltextResult> createSection(Queries queries, String[] fields, IWrapper type);

  protected List<Object> getFulltextResults(Queries queries) {

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
