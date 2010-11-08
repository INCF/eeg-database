/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

import org.apache.lucene.search.Query;
import org.hibernate.search.FullTextQuery;

/**
 *
 * @author Honza
 */
public class Queries {

  private Query luceneQuery;
  private FullTextQuery fullTextQuery;

  public Queries(Query luceneQuery, FullTextQuery fullTextQuery) {
    this.luceneQuery = luceneQuery;
    this.fullTextQuery = fullTextQuery;
  }

  public FullTextQuery getFullTextQuery() {
    return fullTextQuery;
  }

  public void setFullTextQuery(FullTextQuery fullTextQuery) {
    this.fullTextQuery = fullTextQuery;
  }

  public Query getLuceneQuery() {
    return luceneQuery;
  }

  public void setLuceneQuery(Query luceneQuery) {
    this.luceneQuery = luceneQuery;
  }
}
