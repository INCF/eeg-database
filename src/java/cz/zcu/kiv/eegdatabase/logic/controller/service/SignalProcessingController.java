package cz.zcu.kiv.eegdatabase.logic.controller.service;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.signal.DataTransformer;
import cz.zcu.kiv.eegdatabase.logic.signal.EEGDataTransformer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SignalProcessingController extends AbstractController {


    GenericDao<Experiment, Integer> experimentDao;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws Exception {

        Experiment exp = experimentDao.read(156);   //tested experiment
        ModelAndView mav = new ModelAndView("redirect:/home.html");
        DataTransformer transformer = new EEGDataTransformer();
        if (transformer.isSuitableExperiment(exp)) {
            double[] experimentalData = transformer.transformExperimentalData(exp);
        }

        return mav;
    }

     public GenericDao<Experiment, Integer> getExperimentDao() {
        return experimentDao;
    }

    public void setExperimentDao(GenericDao<Experiment, Integer> experimentDao) {
        this.experimentDao = experimentDao;
    }
}
