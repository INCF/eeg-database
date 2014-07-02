package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
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
        String hqlQuery = "from Template t where t.owner.personId = :personId";
        List<Template> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("personId", personId).list();

        return list;
    }

    @Override
    public List<Template> getTemplatesByExperiment(int experimentId) {
        String hqlQuery = "from Template t where t.experiment.experimentId = :experimentId";
        List<Template> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("experimentId", experimentId).list();

        return list;
    }

    @Override
    public Person getOwner(int templateId) {
        String hqlQuery = "select Person from Template t where t.templateId = :templateId";
        Person owner = (Person)getSessionFactory().getCurrentSession().createQuery(hqlQuery);

        return owner;
    }

    @Override
    public Experiment getExperiment(int templateId) {
        String hqlQuery = "select Experiment from Template t where t.templateId = :templateId";
        Experiment experiment = (Experiment)getSessionFactory().getCurrentSession().createQuery(hqlQuery);

        return experiment;
    }
}
