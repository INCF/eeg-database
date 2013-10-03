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
 *   WizardAjaxController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 19.3.13
 * Time: 15:33
 */
@Controller
public class WizardAjaxController {
    @RequestMapping(value="/experiments/wizard/json/arm")
    public ModelAndView arm(){
        ModelAndView mav = new ModelAndView("experiments/json/arm");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/json/disease")
    public ModelAndView disease(){
        ModelAndView mav = new ModelAndView("experiments/json/disease");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/json/experimentators")
    public ModelAndView experimentators(){
        ModelAndView mav = new ModelAndView("experiments/json/experimentators");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/json/group")
    public ModelAndView group(){
        ModelAndView mav = new ModelAndView("experiments/json/group");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/json/hardware")
    public ModelAndView hardware(){
        ModelAndView mav = new ModelAndView("experiments/json/hardware");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/json/person")
    public ModelAndView person(){
        ModelAndView mav = new ModelAndView("experiments/json/person");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/json/pharmaceutical")
    public ModelAndView pharmaceutical(){
        ModelAndView mav = new ModelAndView("experiments/json/pharmaceutical");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/json/project")
    public ModelAndView project(){
        ModelAndView mav = new ModelAndView("experiments/json/project");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/json/scenario")
    public ModelAndView scenario(){
        ModelAndView mav = new ModelAndView("experiments/json/scenario");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/json/software")
    public ModelAndView software(){
        ModelAndView mav = new ModelAndView("experiments/json/software");
        return mav;
    }

    @RequestMapping(value="/experiments/wizard/json/weather")
    public ModelAndView weather(){
        ModelAndView mav = new ModelAndView("experiments/json/weather");
        return mav;
    }
}
