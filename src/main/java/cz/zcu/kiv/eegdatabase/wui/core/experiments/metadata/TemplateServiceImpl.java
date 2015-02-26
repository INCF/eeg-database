package cz.zcu.kiv.eegdatabase.wui.core.experiments.metadata;

import cz.zcu.kiv.eegdatabase.data.dao.SimpleTemplateDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Template;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

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
 * TemplateServiceImpl, 2014/07/08 14:52 Prokop
 * <p/>
 * ********************************************************************************************************************
 */
public class TemplateServiceImpl implements TemplateService {

    SimpleTemplateDao templateDao;

    @Required
    public void setTemplateDao(SimpleTemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    @Override
    @Transactional
    public List<Template> getTemplatesByPerson(int personId) {
        return templateDao.getTemplatesByPerson(personId);
    }

    @Override
    @Transactional
    public List<Template> getDefaultTemplates() {
        return templateDao.getDefaultTemplates();
    }

    /**
     * Finds all default and user's templates
     *
     * @param personId id of a user
     * @return default + user's templates
     */
    @Override
    @Transactional
    public List<Template> getUsableTemplates(int personId) {
        return templateDao.getUsableTemplates(personId);
    }

    @Override
    @Transactional
    public Template getTemplateByPersonAndName(int personId, String name) {
        return templateDao.getTemplateByPersonAndName(personId, name);
    }

    @Override
    @Transactional
    public boolean isDefault(int id) {
        return templateDao.isDefault(id);
    }

    @Override
    @Transactional
    public boolean canSaveName(String name, int personId){
        return templateDao.canSaveName(name, personId);
    }

    @Override
    @Transactional
    public Integer create(Template newInstance) {
        return templateDao.create(newInstance);
    }

    @Override
    @Transactional
    public Template read(Integer id) {
        return templateDao.read(id);
    }

    @Override
    @Transactional
    public List<Template> readByParameter(String parameterName, Object parameterValue) {
        return templateDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(Template transientObject) {
        templateDao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(Template persistentObject) {
        templateDao.delete(persistentObject);
    }

    @Override
    @Transactional
    public List<Template> getAllRecords() {
        return templateDao.getAllRecords();
    }

    @Override
    @Transactional
    public List<Template> getRecordsAtSides(int first, int max) {
        return templateDao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional
    public int getCountRecords() {
        return templateDao.getCountRecords();
    }

    @Override
    @Transactional
    public List<Template> getUnique(Template example) {
        return templateDao.findByExample(example);
    }
}
