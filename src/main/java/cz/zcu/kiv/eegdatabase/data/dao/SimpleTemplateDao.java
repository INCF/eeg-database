package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Template;

import java.util.List;

/**
 * ********************************************************************************************************************
 * <p/>
 * This file is part of the eegdatabase project
 * <p/>
 * ==========================================
 * <p/>
 * Copyright (C) 2014 by University of West Bohemia (http://www.zcu.cz/en/)
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * SimpleTemplateDao, 2014/07/02 11:34 Prokop
 * <p/>
 * ********************************************************************************************************************
 */
public class SimpleTemplateDao extends SimpleGenericDao<Template, Integer> implements TemplateDao {

    public SimpleTemplateDao(){
        super(Template.class);
    }

    @Override
    public List<Template> getTemplatesByPerson(int personId) {
        String hqlQuery = "from Template t where t.personByPersonId.personId = :personId";
        List<Template> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("personId", personId).list();

        return list;
    }

    @Override
    public List<Template> getDefaultTemplates() {
        String hqlQuery = "from Template t where t.personByPersonId is null";
        List<Template> list = getHibernateTemplate().find(hqlQuery);

        return list;
    }

    @Override
    public Template getTemplateByPersonAndName(int personId, String name) {
        String hqlQuery = "from Template t where t.personByPersonId.personId = :personId and t.name=:name";
        List<Template> list = getSessionFactory().getCurrentSession().
                createQuery(hqlQuery).
                setParameter("personId", personId).
                setParameter("name", name).list();

        if (list != null && !list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    @Override
    public boolean canSaveName(String name, int personId) {
        String hqlQuery = "from Template t where t.personByPersonId.personId = :personId and t.name=:name";
        List<Template> list = getSessionFactory().getCurrentSession().
                createQuery(hqlQuery).
                setParameter("personId", personId).
                setParameter("name", name).list();
        return (list.size() == 0);
    }
}
