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
 *   WizardController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 19.3.13
 * Time: 15:20
 */
@Controller
public class WizardController {

    @RequestMapping(value="/experiments/wizard")
    public ModelAndView scenario(){
        ModelAndView mav = new ModelAndView("experiments/scenario");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/addARM")
    public ModelAndView addARM(){
        ModelAndView mav = new ModelAndView("experiments/addARM");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/addArtifact")
    public ModelAndView addArtifact(){
        ModelAndView mav = new ModelAndView("experiments/addArtifact");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/addDisease")
    public ModelAndView addDisease(){
        ModelAndView mav = new ModelAndView("experiments/addDisease");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/addGroup")
    public ModelAndView addGroup(){
        ModelAndView mav = new ModelAndView("experiments/addGroup");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/addHW")
    public ModelAndView addHW(){
        ModelAndView mav = new ModelAndView("experiments/addHW");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/addPerson")
    public ModelAndView addPerson(){
        ModelAndView mav = new ModelAndView("experiments/addPerson");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/addPharmaceutical")
    public ModelAndView addPharmaceutical(){
        ModelAndView mav = new ModelAndView("experiments/addPharmaceutical");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/addProjectType")
    public ModelAndView addProjectType(){
        ModelAndView mav = new ModelAndView("experiments/addProjectType");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/addScenario")
    public ModelAndView addScenario(){
        ModelAndView mav = new ModelAndView("experiments/addScenario");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/addStimulus")
    public ModelAndView addStimulus(){
        ModelAndView mav = new ModelAndView("experiments/addStimulus");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/addSW")
    public ModelAndView addSW(){
        ModelAndView mav = new ModelAndView("experiments/addSW");
        return mav;
    }
}
