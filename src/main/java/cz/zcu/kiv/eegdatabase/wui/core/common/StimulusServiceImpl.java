/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   StimulusServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.dao.SimpleStimulusDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Stimulus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 16.4.13
 * Time: 19:48
 * To change this template use File | Settings | File Templates.
 */
public class StimulusServiceImpl implements StimulusService {

    protected Log log = LogFactory.getLog(getClass());

    SimpleStimulusDao stimulusDao;

    @Required
    public void setStimulusDao(SimpleStimulusDao stimulusDao) {
        this.stimulusDao = stimulusDao;
    }


    @Override
    @Transactional
    public Integer create(Stimulus newInstance) {
        return stimulusDao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public Stimulus read(Integer id) {
        return stimulusDao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stimulus> readByParameter(String parameterName, Object parameterValue) {
        return stimulusDao.readByParameter(parameterName, parameterValue);
    }


    @Override
    @Transactional
    public void update(Stimulus transientObject) {
         stimulusDao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(Stimulus persistentObject) {
         stimulusDao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stimulus> getAllRecords() {
        return stimulusDao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stimulus> getRecordsAtSides(int first, int max) {
        return stimulusDao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return stimulusDao.getCountRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stimulus> getUnique(Stimulus example) {
        return stimulusDao.findByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveDescription(String description) {
        return stimulusDao.canSaveDescription(description);
    }
}
