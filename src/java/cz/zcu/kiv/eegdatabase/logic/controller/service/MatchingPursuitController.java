package cz.zcu.kiv.eegdatabase.logic.controller.service;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdsp.common.ISignalProcessingResult;
import cz.zcu.kiv.eegdsp.common.ISignalProcessor;
import cz.zcu.kiv.eegdsp.main.SignalProcessingFactory;
import cz.zcu.kiv.eegdsp.matchingpursuit.MatchingPursuit;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
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
        ((MatchingPursuit) mp).setIterationCount(cmd.getAtom());
        ISignalProcessingResult res = mp.processSignal(binaryData);
        Map<String, Double[][]> result = res.toHashMap();
        Map<String, Double[][]> map = res.toHashMap();
        for (Map.Entry<String, Double[][]> e : map.entrySet()) {
            System.out.println(e.getKey() + " " + e.getValue()[0].length + " " + e.getValue().length);
        }
        Double[][] atoms = result.get("atoms");
        XYSeries XYatom = new XYSeries("Gabor atom");
        XYSeries XYimag = new XYSeries("Imaginary part");
        for (int i = 0; i < atoms[0].length; i++) {
            XYatom.add(i, atoms[0][i]);
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(XYatom);
        //         Generate the graph
        JFreeChart chart = ChartFactory.createXYLineChart("Matching Pursuit", // Title
                "time", // x-axis Label
                "value", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );
        ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 800, 500);
        response.getOutputStream().close();
        mav.addObject("coefs", false);
        mav.addObject("title", "Matching Pursuit");
        return mav;
    }
}
