package cz.zcu.kiv.eegdatabase.logic.controller.service;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.signal.ChannelInfo;
import cz.zcu.kiv.eegdatabase.logic.signal.DataTransformer;
import cz.zcu.kiv.eegdatabase.logic.signal.VhdrReader;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FastFourierController extends AbstractProcessingController {

    public FastFourierController() {
        setCommandClass(FastFourierCommand.class);
        setCommandName("fastFourier");
    }

    protected Map referenceData(HttpServletRequest request) throws Exception {

        return super.referenceData(request);
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        FastFourierCommand cmd = (FastFourierCommand) command;
        return new ModelAndView(getSuccessView());
    }
}
