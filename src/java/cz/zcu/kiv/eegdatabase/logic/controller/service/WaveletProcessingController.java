package cz.zcu.kiv.eegdatabase.logic.controller.service;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.signal.ChannelInfo;
import cz.zcu.kiv.eegdatabase.logic.signal.DataTransformer;
import cz.zcu.kiv.eegdatabase.logic.signal.VhdrReader;
import cz.zcu.kiv.eegdsp.common.ISignalProcessor;
import cz.zcu.kiv.eegdsp.main.SignalProcessingFactory;
import cz.zcu.kiv.eegdsp.wavelet.continuous.WaveletTransformationContinuous;
import cz.zcu.kiv.eegdsp.wavelet.discrete.WaveletTransformationDiscrete;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WaveletProcessingController extends AbstractProcessingController {


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

        return map;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        WaveletCommand cmd = (WaveletCommand) command;
        return new ModelAndView(getSuccessView());
    }
}
