package cz.zcu.kiv.eegdatabase.webservices.rest.metadata;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.nosql.ElasticSynchronizationInterceptor;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.webservices.rest.metadata.wrappers.OdmlWrapper;
import odml.core.Writer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;

/***********************************************************************************************************************
 *
 * This file is part of the eegdatabase project

 * ==========================================
 *
 * Copyright (C) 2018 by University of West Bohemia (http://www.zcu.cz/en/)
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
 * MetadataServiceImpl, 2018/09/04 11:11 petr-jezek
 *
 **********************************************************************************************************************/
@Service
public class MetadataServiceImpl implements MetadataService, ApplicationContextAware {

    protected Log log = LogFactory.getLog(getClass());

    @Autowired
    @Qualifier("experimentDao")
    private ExperimentDao experimentDao;

    private ApplicationContext context;

    @Override
    @Transactional(readOnly = true)
    public OdmlWrapper getOdml(int experimentId) {
        ElasticSynchronizationInterceptor elasticSynchronizationInterceptor = context.getBean(ElasticSynchronizationInterceptor.class);
        elasticSynchronizationInterceptor.setLoadSemantic(true);
        Experiment experiment = experimentDao.read(experimentId);
        odml.core.Section section = experiment.getElasticExperiment().getMetadata();
        elasticSynchronizationInterceptor.setLoadSemantic(false);
        return fill(section);

    }

    protected OdmlWrapper fill(odml.core.Section sec) {
        OdmlWrapper wrapper = new OdmlWrapper();
        Writer writer = new Writer(sec);
        ByteArrayOutputStream file = new ByteArrayOutputStream();
        boolean result = writer.write(file);
        log.debug("ODML Writter: " + result);
        byte[] bytes = file.toByteArray();
        wrapper.setData(new String(bytes));

        return wrapper;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
