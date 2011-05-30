package cz.zcu.kiv.eegdatabase.logic.controller.service;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.signal.ChannelInfo;
import cz.zcu.kiv.eegdatabase.logic.signal.DataTransformer;
import cz.zcu.kiv.eegdatabase.logic.signal.VhdrReader;
import cz.zcu.kiv.eegdsp.common.ISignalProcessingResult;
import cz.zcu.kiv.eegdsp.common.ISignalProcessor;
import cz.zcu.kiv.eegdsp.main.SignalProcessingFactory;
import cz.zcu.kiv.eegdsp.wavelet.continuous.WaveletTransformationContinuous;
import cz.zcu.kiv.eegdsp.wavelet.continuous.algorithm.wavelets.WaveletCWT;
import cz.zcu.kiv.eegdsp.wavelet.discrete.WaveletTransformationDiscrete;
import cz.zcu.kiv.eegdsp.wavelet.discrete.algorithm.wavelets.WaveletDWT;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WaveletProcessingController extends AbstractProcessingController {

    private String type;

    public WaveletProcessingController() {
        setCommandClass(WaveletCommand.class);
        setCommandName("wavelet");
    }

    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map map = super.referenceData(request);
        String[] names;
        if (request.getParameter("type").equals("DWT")) {
            ISignalProcessor dwt = SignalProcessingFactory.getInstance().getWaveletDiscrete();
            names = ((WaveletTransformationDiscrete) dwt).getWaveletGenerator().getWaveletNames();
        } else {
            ISignalProcessor dwt = SignalProcessingFactory.getInstance().getWaveletContinuous();
            names = ((WaveletTransformationContinuous) dwt).getWaveletGenerator().getWaveletNames();
        }
        map.put("wavelets", names);
        map.put("type", request.getParameter("type"));
        type = request.getParameter("type");

        return map;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        WaveletCommand cmd = (WaveletCommand) command;
        ModelAndView mav = new ModelAndView(getSuccessView());
        Experiment experiment = experimentDao.read(Integer.parseInt(request.getParameter("experimentId")));
        DataFile binaryFile = null;
        for (DataFile file : experiment.getDataFiles()) {
            if (file.getFilename().equals(transformer.getProperties().get("CI").get("DataFile"))) {
                binaryFile = file;
                break;
            }
        }
        byte[] bytes = binaryFile.getFileContent().getBytes(1, (int) binaryFile.getFileContent().length());
        double signal[] = transformer.readBinaryData(bytes, cmd.getChannel());
        ISignalProcessor wt = null;
        if (type.equals("CWT")) {
            wt = SignalProcessingFactory.getInstance().getWaveletContinuous();
            String name = cmd.getWavelet();
            WaveletCWT wavelet = ((WaveletTransformationContinuous) wt).getWaveletGenerator().getWaveletByName(name);
            ((WaveletTransformationContinuous) wt).setWavelet(wavelet);
            mav.addObject("coefs", false);
            mav.addObject("title", "Continuous Wavelet Transformation");
        } else {
            wt = SignalProcessingFactory.getInstance().getWaveletDiscrete();
            String name = cmd.getWavelet();
            WaveletDWT wavelet = ((WaveletTransformationDiscrete) wt).getWaveletGenerator().getWaveletByName(name);
            ((WaveletTransformationDiscrete) wt).setWavelet(wavelet);
            mav.addObject("coefs", true);
            mav.addObject("title", "Discrete Wavelet Transformation");

        }
        ISignalProcessingResult res;
        try {
         res = wt.processSignal(signal);
        } catch(OutOfMemoryError e) {
            return new ModelAndView("services/outOfMemory");
        }
        Map<String, Double[][]> map = res.toHashMap();
         if (map.containsKey("highestCoefficients")) {
            mav.addObject("name", "highest Coefficients");
            mav.addObject("values", map.get("highestCoefficients")[0]);
        }
        return mav;
    }
}
