package cz.zcu.kiv.eegdatabase.logic.controller.service;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.signal.DataTransformer;
import cz.zcu.kiv.eegdatabase.logic.signal.EEGDataTransformer;
import cz.zcu.kiv.eegdatabase.logic.signal.VhdrReader;
import cz.zcu.kiv.eegdatabase.logic.util.SignalProcessingUtils;
import cz.zcu.kiv.eegdsp.common.ISignalProcessingResult;
import cz.zcu.kiv.eegdsp.common.ISignalProcessor;
import cz.zcu.kiv.eegdsp.main.SignalProcessingFactory;
import cz.zcu.kiv.eegdsp.matchingpursuit.MatchingPursuit;
import cz.zcu.kiv.eegdsp.wavelet.continuous.WaveletTransformationContinuous;
import cz.zcu.kiv.eegdsp.wavelet.continuous.algorithm.wavelets.WaveletCWT;
import cz.zcu.kiv.eegdsp.wavelet.discrete.WaveletTransformationDiscrete;
import cz.zcu.kiv.eegdsp.wavelet.discrete.algorithm.wavelets.WaveletDWT;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class SignalProcessingController extends AbstractController {


    private GenericDao<Experiment, Integer> experimentDao;
    private DataTransformer transformer;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws Exception {

        int id = Integer.parseInt(httpServletRequest.getParameter("experimentId"));
        Experiment exp = experimentDao.read(id);
        ModelAndView mav = new ModelAndView("services/notSuitable");
        transformer = new EEGDataTransformer();
        if (SignalProcessingUtils.getHeaders(exp).size() > 0) {
            mav = new ModelAndView("services/index");
            mav.addObject("experimentId", id);
        }

        return mav;
    }

    public GenericDao<Experiment, Integer> getExperimentDao() {
        return experimentDao;
    }

    public void setExperimentDao(GenericDao<Experiment, Integer> experimentDao) {
        this.experimentDao = experimentDao;
    }

    public DataTransformer getTransformer() {
        return transformer;
    }

    public void setTransformer(DataTransformer transformer) {
        this.transformer = transformer;
    }
}
