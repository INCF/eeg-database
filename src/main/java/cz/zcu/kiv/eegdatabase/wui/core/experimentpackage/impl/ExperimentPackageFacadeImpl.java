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
 *   ExperimentPackageFacadeImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.impl;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacadeImpl;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageConnectionService;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author Jakub Danek
 */
public class ExperimentPackageFacadeImpl extends GenericFacadeImpl<ExperimentPackage, Integer> implements ExperimentPackageFacade {

    private ExperimentPackageService experimentPackageService;
    private ExperimentPackageConnectionService experimentPackageConnectionService;

    public ExperimentPackageFacadeImpl() {
    }

    public ExperimentPackageFacadeImpl(GenericService<ExperimentPackage, Integer> service) {
	super(service);
    }

    @Required
    public void setExperimentPackageService(ExperimentPackageService experimentPackageService) {
	this.experimentPackageService = experimentPackageService;
    }

    @Required
    public void setExperimentPackageConnectionService(ExperimentPackageConnectionService experimentPackageConnectionService) {
	this.experimentPackageConnectionService = experimentPackageConnectionService;
    }

    @Override
    public List<ExperimentPackage> listExperimentPackagesByGroup(ResearchGroup researchGroup) {
		if(researchGroup != null) {
			return experimentPackageService.listExperimentPackagesByGroup(researchGroup.getResearchGroupId());
		} else {
			return new ArrayList<ExperimentPackage>();
		}
    }

    @Override
    public boolean addExperimentToPackage(Experiment exp, ExperimentPackage pckg) {
		return this.experimentPackageConnectionService.addExperimentToPackage(exp, pckg);
    }

    @Override
    public int addExperimentsToPackage(List<Experiment> exp, ExperimentPackage pckg) {
		int i = 0;
		for(Experiment e : exp) {
			if(this.addExperimentToPackage(e, pckg)) {
			i++;
			}
		}
			return i;
		}

    @Override
    public boolean removeExperimentFromPackage(Experiment exp, ExperimentPackage pckg) {
	return experimentPackageConnectionService.removeExperimentFromPackage(exp, pckg);
    }

    @Override
    public int removeExperimentsFromPackage(List<Experiment> exp, ExperimentPackage pckg) {
	int i = 0;
	for(Experiment e : exp) {
	    if(this.removeExperimentFromPackage(e, pckg)) {
		i++;
	    }
	}
	return i;
    }

	@Override
	public void removeExperimentPackage(ExperimentPackage pckg) {
		this.experimentPackageService.delete(pckg);
	}

	@Override
	public List<ExperimentPackage> listVisiblePackages(Person person) {
		return this.experimentPackageService.listVisiblePackages(person);
	}

}
