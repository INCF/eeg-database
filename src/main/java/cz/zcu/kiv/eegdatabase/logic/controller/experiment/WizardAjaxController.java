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
