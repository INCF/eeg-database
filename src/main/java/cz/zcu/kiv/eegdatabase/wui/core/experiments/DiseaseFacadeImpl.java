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
 *   DiseaseFacadeImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 16.4.13
 * Time: 10:28
 */
public class DiseaseFacadeImpl implements DiseaseFacade {
    DiseaseService diseaseService;

    @Required
    public void setDiseaseService(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
    }

    @Override
    public Integer create(Disease newInstance) {
        return diseaseService.create(newInstance);
    }

    @Override
    public Disease read(Integer id) {
        return diseaseService.read(id);
    }

    @Override
    public List<Disease> readByParameter(String parameterName, Object parameterValue) {
        return diseaseService.readByParameter(parameterName, parameterValue);  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public void update(Disease transientObject) {
        diseaseService.update(transientObject);//To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(Disease persistentObject) {
        diseaseService.delete(persistentObject);//To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Disease> getAllRecords() {
        return diseaseService.getAllRecords();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Disease> getRecordsAtSides(int first, int max) {
        return diseaseService.getRecordsAtSides(first, max);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCountRecords() {
        return diseaseService.getCountRecords();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Disease> getUnique(Disease example) {
        return diseaseService.getUnique(example);
    }

    @Override
    public boolean existsDisease(String name) {
        return diseaseService.existsDisease(name);
    }
}
