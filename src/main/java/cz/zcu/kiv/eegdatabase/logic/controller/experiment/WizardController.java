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
