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
 *   TemplateFacadeImpl.java, 2015/02/26 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.experiments.metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import odml.core.Property;
import odml.core.Reader;
import odml.core.Section;
import odml.core.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Template;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsService;

public class TemplateFacadeImpl implements TemplateFacade {

    protected Log log = LogFactory.getLog(getClass());

    private TemplateService service;

    private ExperimentsService expService;

    // default location in resources, configurations via project.properties
    private String odmlSectionsPath = "odML/odMLSections";

    @Required
    public void setService(TemplateService service) {
        this.service = service;
    }

    @Required
    public void setExpService(ExperimentsService expService) {
        this.expService = expService;
    }

    public void setOdmlSectionsPath(String odmlSectionsPath) {
        this.odmlSectionsPath = odmlSectionsPath;
    }

    @Override
    public List<Template> getTemplatesByPerson(int personId) {
        return service.getTemplatesByPerson(personId);
    }

    @Override
    public List<Template> getDefaultTemplates() {
        return service.getDefaultTemplates();
    }

    /**
     * Finds all default and user's templates
     * 
     * @param personId
     *            id of a user
     * @return default + user's templates
     */
    @Override
    public List<Template> getUsableTemplates(int personId) {
        return service.getUsableTemplates(personId);
    }

    @Override
    public Template getTemplateByPersonAndName(int personId, String name) {
        return service.getTemplateByPersonAndName(personId, name);
    }

    @Override
    public boolean isDefault(int id) {
        return service.isDefault(id);
    }

    @Override
    public boolean canSaveName(String name, int personId) {
        return service.canSaveName(name, personId);
    }

    @Override
    public Integer create(Template newInstance) {
        return service.create(newInstance);
    }

    @Override
    public Template read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<Template> readByParameter(String parameterName, Object parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(Template transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(Template persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<Template> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<Template> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<Template> getUnique(Template example) {
        return service.getUnique(example);
    }

    @Override
    public List<Section> getListOfAvailableODMLSections() {

        List<File> filteredFiles = new ArrayList<File>();
        File[] paths;
        try {
            paths = new ClassPathResource(odmlSectionsPath).getFile().listFiles();
            for (File path : paths) {
                if (path.getName().endsWith(".xml")) {
                    filteredFiles.add(path);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        final List<Section> sections = new ArrayList<Section>();
        Reader reader = new Reader();
        for (File file : filteredFiles) {
            Section section = null;
            try {
                section = reader.load(file.getAbsolutePath());
                sections.addAll(section.getSections());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        try {
            Section empty = new Section(ResourceUtils.getString("text.template.empty.section"), "new");
            Property emptyProp = new Property(ResourceUtils.getString("text.template.empty.propertyName"), ResourceUtils.getString("text.template.empty.propertyValue"));
            empty.add(emptyProp);
            sections.add(0, empty);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return sections;
    }

    @Override
    @Transactional
    public boolean migrateSQLToES() {

        log.trace("recreate index...");
        if (!expService.deleteAndCreateExperimentIndexInES()) {
            return false;
        }
        log.trace("Index recreated...");

        log.trace("migrate experiments");
        List<Experiment> allRecords = expService.getAllRecords();
        for (Experiment tmp : allRecords) {
            log.trace("migrate experiment " + tmp.getExperimentId());

            Experiment experiment = expService.getExperimentForDetail(tmp.getExperimentId());
            Section section = ExperimentToODMLMapper.convertExperimentToSection(experiment);

            experiment.getElasticExperiment().setMetadata(section);

            expService.simpleUpdate(experiment);
        }

        log.trace("migration done...");

        return true;
    }

    @Override
    public boolean createSystemTemplate(Section section) {
        try {

            Writer writer = new Writer(section, true, true);
            FileOutputStream fostream = new FileOutputStream(new File(new ClassPathResource(odmlSectionsPath).getFile(), section.getName() + ".xml"));
            writer.write(fostream);
            fostream.close();

            return true;
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            return false;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}
