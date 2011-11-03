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
import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


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
              int id = Integer.parseInt(request.getParameter("experimentId"));
        Experiment ex = experimentDao.read(id);
        byte[] data = null;
        for (DataFile dataFile : ex.getDataFiles()) {
            int index = dataFile.getFilename().lastIndexOf(".");
            if (dataFile.getFilename().substring(0, index).equals(super.fileName)) {
                if ((dataFile.getFilename().endsWith(".avg"))||(dataFile.getFilename().endsWith(".eeg"))) {
                    data = dataFile.getFileContent().getBytes(1, (int) dataFile.getFileContent().length());
                    break;
                }
            }
        }
        double signal[] = transformer.readBinaryData(data, cmd.getChannel());
        ISignalProcessingResult res = null;
        try {
        ISignalProcessor mp = SignalProcessingFactory.getInstance().getMatchingPursuit();
        ((MatchingPursuit) mp).setIterationCount(cmd.getAtom());
         res = mp.processSignal(signal);
        } catch(OutOfMemoryError e) {
            return new ModelAndView("services/outOfMemory");
        }
        Map<String, Double[][]> result = res.toHashMap();
        Double[][] atoms = result.get("atoms");
        Double[][] rec = result.get("reconstruction");
        XYSeries XYatom = new XYSeries("Gabor atom");
        XYSeries XYrec = new XYSeries("Reconstructed signal");
        for (int i = 0; i < signal.length; i++) {
            XYatom.add(i, atoms[0][i]);
            XYrec.add(i, rec[0][i]);
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(XYatom);
        dataset.addSeries(XYrec);
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
