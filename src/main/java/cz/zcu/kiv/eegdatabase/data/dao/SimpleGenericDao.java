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
    public List<T> readByParameter(String parameterName, Object parameterValue) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(type);
        criteria.add(Restrictions.eq(parameterName, parameterValue));
        return criteria.list();
    }

	@Override
	public List<T> readByParameter(Map<String, Object> paramMap) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(type);
        Iterator<Map.Entry<String, Object>> it = paramMap.entrySet().iterator();
		
		
		Map.Entry<String, Object> tmp;
		while(it.hasNext()) {
			tmp = it.next();
			criteria.add(Restrictions.eq(tmp.getKey(), tmp.getValue()));
		}
        return criteria.list();
	}

    /**
     * Method update data in database.
     *
     * @param transientObject - updating object
     */
    public void update(T transientObject) {
        getHibernateTemplate().update(transientObject);
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

    /**
     * Gets all records with all of their properties set.
     * The method enforces eager loading of all properties that have their getter methods.
     * @return List of records with eagerly loaded properties.
     */
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
        return null;
    }

    @Override
    public List<T> findByExample(T example) {
        return getHibernateTemplate().findByExample(example);
    }
    
    protected void initializeProperty(Object property) {
        if(!Hibernate.isInitialized(property)) {
            getHibernateTemplate().initialize(property);
        }
    }
}

