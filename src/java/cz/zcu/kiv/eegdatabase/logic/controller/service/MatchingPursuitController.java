package cz.zcu.kiv.eegdatabase.logic.controller.service;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdsp.common.ISignalProcessingResult;
import cz.zcu.kiv.eegdsp.common.ISignalProcessor;
import cz.zcu.kiv.eegdsp.main.SignalProcessingFactory;
import cz.zcu.kiv.eegdsp.matchingpursuit.MatchingPursuit;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


public class MatchingPursuitController extends AbstractProcessingController {


    public MatchingPursuitController() {
        setCommandClass(MatchingPursuitCommand.class);
        setCommandName("matchingPursuit");
    }

    protected Map referenceData(HttpServletRequest request) throws Exception {
        return super.referenceData(request);
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        MatchingPursuitCommand cmd = (MatchingPursuitCommand) command;
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
        double[] binaryData = transformer.readBinaryData(bytes, cmd.getChannel());
        ISignalProcessor mp = SignalProcessingFactory.getInstance().getMatchingPursuit();
		((MatchingPursuit)mp).setIterationCount(cmd.getAtom());
		ISignalProcessingResult res = mp.processSignal(binaryData);
        Map<String, Double[][]> result = res.toHashMap();

        return mav;
    }
}
