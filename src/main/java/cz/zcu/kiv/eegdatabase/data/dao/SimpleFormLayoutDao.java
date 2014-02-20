/***********************************************************************************************************************
 *
 * This file is part of the EEG-database project
 *
 * =============================================
 *
 * Copyright (C) 2014 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * SimpleFormLayoutDao.java, 18. 2. 2014 18:02:31, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import java.io.InputStream;
import java.sql.Blob;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import cz.zcu.kiv.eegdatabase.data.pojo.FormLayout;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;


/**
 * Implements the {@link FormLayoutDao} using Hibernate ORM.
 *
 * @author Jakub Krauz
 */
public class SimpleFormLayoutDao extends SimpleGenericDao<FormLayout,Integer> implements FormLayoutDao {
	
	
	public SimpleFormLayoutDao() {
        super(FormLayout.class);
    }
	
	
	@Override
	public int getAllFormsCount() {
		return getFormsCount(null);
	}


	@Override
	public int getFormsCount(Person owner) {
		DetachedCriteria criteria = DetachedCriteria.forClass(type);
		criteria.setProjection(Projections.rowCount());
		if (owner != null)
			criteria.add(Restrictions.eq("person.personId", owner.getPersonId()));
		
		return DataAccessUtils.intResult(getHibernateTemplate().findByCriteria(criteria));
	}


	@Override
	public List<String> getAllFormNames() {
		return getFormNames(null);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<String> getFormNames(Person owner) {
		DetachedCriteria criteria = DetachedCriteria.forClass(type);
		criteria.setProjection(Projections.distinct(Projections.property("formName")));
		if (owner != null)
			criteria.add(Restrictions.eq("person.personId", owner.getPersonId()));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}

	
	@Override
	public int getAllLayoutsCount() {
		return getCountRecords();
	}
	
	
	@Override
	public int getLayoutsCount(Person owner) {
		return getLayoutsCount(owner, null);
	}
	
	
	@Override
	public int getLayoutsCount(String formName) {
		return getLayoutsCount(null, formName);
	}
	
	
	@Override
	public int getLayoutsCount(Person owner, String formName) {
		DetachedCriteria criteria = DetachedCriteria.forClass(type);
		criteria.setProjection(Projections.rowCount());
		if (owner != null)
			criteria.add(Restrictions.eq("person.personId", owner.getPersonId()));
		if (formName != null)
			criteria.add(Restrictions.eq("formName", formName));
		
		return DataAccessUtils.intResult(getHibernateTemplate().findByCriteria(criteria));
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public FormLayout getLayout(String formName, String layoutName) {
		DetachedCriteria criteria = DetachedCriteria.forClass(type);
		criteria.add(Restrictions.eq("formName", formName));
		criteria.add(Restrictions.eq("layoutName", layoutName));
		
		return DataAccessUtils.singleResult((List<FormLayout>) getHibernateTemplate().findByCriteria(criteria));
	}
	
	
	@Override
	public List<FormLayout> getAllLayouts() {
		return getAllRecords();
	}
	
	
	@Override
	public List<FormLayout> getLayouts(Person owner) {
		return getLayouts(owner, null);
	}
	
	
	@Override
	public List<FormLayout> getLayouts(String formName) {
		return getLayouts(null, formName);
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<FormLayout> getLayouts(Person owner, String formName) {
		DetachedCriteria criteria = DetachedCriteria.forClass(type);
		if (owner != null)
			criteria.add(Restrictions.eq("person.personId", owner.getPersonId()));
		if (formName != null)
			criteria.add(Restrictions.eq("formName", formName));
		
        return getHibernateTemplate().findByCriteria(criteria);
	}
	
	
	@Override
    public Blob createBlob(byte[] input) {
        return getHibernateTemplate().getSessionFactory().getCurrentSession().getLobHelper().createBlob(input);
    }

	
	@Override
    public Blob createBlob(InputStream input, int length) {
        return getHibernateTemplate().getSessionFactory().getCurrentSession().getLobHelper().createBlob(input,length);
    }


    
}
