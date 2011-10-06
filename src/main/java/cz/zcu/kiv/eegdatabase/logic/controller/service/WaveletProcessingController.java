package cz.zcu.kiv.eegdatabase.logic.controller.service;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdsp.common.ISignalProcessingResult;
import cz.zcu.kiv.eegdsp.common.ISignalProcessor;
import cz.zcu.kiv.eegdsp.main.SignalProcessingFactory;
import cz.zcu.kiv.eegdsp.wavelet.continuous.WaveletTransformationContinuous;
import cz.zcu.kiv.eegdsp.wavelet.continuous.algorithm.wavelets.WaveletCWT;
import cz.zcu.kiv.eegdsp.wavelet.discrete.WaveletTransformationDiscrete;
import cz.zcu.kiv.eegdsp.wavelet.discrete.algorithm.wavelets.WaveletDWT;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Blob;
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
        int id = Integer.parseInt(request.getParameter("experimentId"));
        Experiment ex = experimentDao.read(id);
        byte[] data = null;
        for (DataFile dataFile : ex.getDataFiles()) {
            int index = dataFile.getFilename().lastIndexOf(".");
            if (dataFile.getFilename().substring(0, index).equals(super.fileName)) {
                if ((dataFile.getFilename().endsWith(".avg"))||(dataFile.getFilename().endsWith(".eeg"))) {
                    Blob blob = dataFile.getFileContent();
                    data = blob.getBytes(1, (int) blob.length());
                    break;
                }
            }
        }
        double signal[] = transformer.readBinaryData(data, cmd.getChannel());
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
