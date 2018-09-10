package cz.zcu.kiv.eegdatabase.webservices.rest.metadata;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.nosql.ElasticSynchronizationInterceptor;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.webservices.rest.metadata.wrappers.Metadata;
import cz.zcu.kiv.eegdatabase.webservices.rest.metadata.wrappers.MetadataList;
import odml.core.Property;
import odml.core.Section;
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
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

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

    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;

    private ApplicationContext context;

    @Override
    @Transactional(readOnly = true)
    public MetadataList getOdml(int from, int count) {
        ElasticSynchronizationInterceptor elasticSynchronizationInterceptor = context.getBean(ElasticSynchronizationInterceptor.class);
        elasticSynchronizationInterceptor.setLoadSemantic(true);
        List<Experiment> experiments = experimentDao.getAllExperimentsForUser(personDao.getLoggedPerson(), from, count);
        List<Metadata> result = new LinkedList<Metadata>();
        for(Experiment experiment : experiments) {
            result.add(new Metadata(fill(experiment.getElasticExperiment().getMetadata()), experiment.getExperimentId()));
        }
        elasticSynchronizationInterceptor.setLoadSemantic(false);
        return new MetadataList(result);

    }

    protected String fill(odml.core.Section sec)  {
        /*Working with copy - copy is created as tmp file on drive
        The original is used when any I/O error occurs
        * */
        Section copy = sec;
        try {
            copy = sec.copy();
        } catch (Exception e) {
            log.warn("Creating odml copy failed. Using current one");
            log.warn(e);
        }

        modify(copy);
        Writer writer = new Writer(copy);
        ByteArrayOutputStream file = new ByteArrayOutputStream();
        boolean result = writer.write(file);
        log.debug("ODML writter created: " + result);
        byte[] bytes = file.toByteArray();
        try {
            file.close();
        } catch (IOException e) {
            log.warn(e);
        }
        return new String(bytes);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    /**
     * Modify odml values to return only the selected one
     * @param root
     */
    private  void modify(Section root) {
        if (root != null) {
            Vector<Property> properties = root.getProperties();
            if (properties != null) {
                for (Property property : properties) {
                    Object value = property.getValue();
                    if (value != null && property.getValues().size() > 1) {
                        for (Object val : property.getValues()) {
                            property.removeValue(val);
                        }
                        property.addValue(value);
                    }
                }
            }
            Vector<Section> sections = root.getSections();
            if (sections != null) {
                for (Section section : sections) {
                    modify(section);
                }
            }
        }
    }
}
