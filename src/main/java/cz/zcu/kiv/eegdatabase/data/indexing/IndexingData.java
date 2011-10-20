/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.indexing;

import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

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
                      Hardware.class.getName(),HearingImpairment.class.getName(),
                      VisualImpairment.class.getName(), Weather.class.getName(),
                      ExperimentOptParamDef.class.getName(), DataFile.class.getName(),
                      ArticleComment.class.getName()};
    for (int i = 0; i < types.length; i++) {
      List<T> entities = fts.createCriteria(types[i]).list();

      for (T entity : entities) {
        fts.index(entity);  //manually index an item instance
      }
    }
    //System.out.println(type);
    fts.getTransaction().commit(); //index are written at commit time
  }
}
