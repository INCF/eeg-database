/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.indexing;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.io.Serializable;

/**
 *
 * @author pbruha
 */
public class IndexingData<T, PK extends Serializable> extends HibernateDaoSupport {

  public void init() {
    HibernateTemplate hb = createHibernateTemplate(getSessionFactory());
    Session session = hb.getSessionFactory().openSession();
    FullTextSession fts = org.hibernate.search.Search.getFullTextSession(session);
    fts.getTransaction().begin();
    String[] types = {Experiment.class.getName(), Scenario.class.getName(), 
                      Person.class.getName(),Article.class.getName(),
                      Hardware.class.getName()
                      , Weather.class.getName(),
                      ExperimentOptParamDef.class.getName(), DataFile.class.getName(),
                      ArticleComment.class.getName()};
    for (int i = 0; i < types.length; i++) {
//      List<T> entities = fts.createCriteria(types[i]).list();
//
//      for (T entity : entities) {
//        fts.index(entity);  //manually index an item instance
//      }
   }
    //System.out.println(type);
    fts.getTransaction().commit(); //index are written at commit time
  }
}
