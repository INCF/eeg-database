package cz.zcu.kiv.eegdatabase.logic.controller.service;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.signal.DataTransformer;
import cz.zcu.kiv.eegdatabase.logic.signal.EEGDataTransformer;
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


    GenericDao<Experiment, Integer> experimentDao;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws Exception {

        //Experiment exp = experimentDao.read(156);   //tested experiment - eeg data
        Experiment exp = experimentDao.read(159);    //avg data
        ModelAndView mav = new ModelAndView("redirect:/home.html");
        DataTransformer transformer = new EEGDataTransformer();
        if (transformer.isSuitableExperiment(exp)) {
            double[] experimentalData = transformer.transformExperimentalData(exp);
            //for (int i = 0; i < 1000; i = i+50)
                //System.out.println(experimentalData[i]);
           ISignalProcessor cwt = SignalProcessingFactory.getInstance().getWaveletContinuous();

		String[] names = ((WaveletTransformationContinuous)cwt).getWaveletGenerator().getWaveletNames();
		System.out.println(Arrays.toString(names));

	    String name= names[0];
			System.out.println("\t" + name + "...");

			WaveletCWT wavelet = null;
			try {
				wavelet = ((WaveletTransformationContinuous)cwt).getWaveletGenerator().getWaveletByName(name);

			} catch (Exception e) {
				System.out.println("Exception loading wavelet " + name + ".");
			}


            ((WaveletTransformationContinuous)cwt).setWavelet(wavelet);

			ISignalProcessingResult res = cwt.processSignal(experimentalData);

            Map<String, Double[][]> result = res.toHashMap();
             for (Map.Entry<String, Double[][]> e: result.entrySet())
                System.out.println(e. getKey() + " " + e.getValue().length + " " + e.getValue()[0].length);
		}


           // Double[] x = result.get("reconstruction")[0];
          //  for (double d: x)
           //     System.out.println(d);
        

        return mav;
    }

    public GenericDao<Experiment, Integer> getExperimentDao() {
        return experimentDao;
    }

    public void setExperimentDao(GenericDao<Experiment, Integer> experimentDao) {
        this.experimentDao = experimentDao;
    }
}
