package cz.zcu.kiv.eegdatabase.webservices.rest.metadata;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentPackageConnectionDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.nosql.MetadataUtil;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageConnection;
import cz.zcu.kiv.eegdatabase.webservices.rest.metadata.wrappers.Metadata;
import cz.zcu.kiv.eegdatabase.webservices.rest.metadata.wrappers.MetadataList;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import odml.core.Property;
import odml.core.Section;
import odml.core.Writer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
public class MetadataServiceImpl implements MetadataService {

    protected Log log = LogFactory.getLog(getClass());

    @Autowired
    MetadataUtil metadataUtil;

    @Autowired
    @Qualifier("experimentDao")
    private ExperimentDao experimentDao;

    @Autowired
    @Qualifier("experimentPackageConnectionDao")
    private ExperimentPackageConnectionDao experimentPackageConnectionDao;

    @Autowired
    private ExperimentPackageFacade experimentPackageFacade;

    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;

    private ApplicationContext context;

    @Override
    @Transactional(readOnly = true)
    public MetadataList getOdml(int from, int count) {
        List<Experiment> experiments = new LinkedList<Experiment>();
        //Firstly read experiments with a package then experiments without package
        List<ExperimentPackage> packages = experimentPackageFacade.listVisiblePackages(personDao.getLoggedPerson());
        for(ExperimentPackage pack : packages) {
            Set<ExperimentPackageConnection> connectionSet =  pack.getExperimentPackageConnections();
            for(ExperimentPackageConnection connection : connectionSet) {
                experiments.add(connection.getExperiment());
            }
        }
        experiments.addAll(experimentPackageConnectionDao.listExperimentsWithoutPackage());

        //check bounds
        if(from < 0) {
            from = 0;
        }
        if(count >= experiments.size()) {
            count = experiments.size() - 1;
        }
        List<Metadata> result = new LinkedList<Metadata>();
        log.debug("I got " + experiments.size() + " experiments");
        //There is no dao with from, count parameters, I must read everything and then return a sublist
        for(Experiment experiment : experiments.subList(from, count)) {
            metadataUtil.loadMetadata(experiment);
            result.add(new Metadata(fill(experiment.getElasticExperiment().getMetadata()), experiment.getExperimentId()));
        }
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
