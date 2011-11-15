package cz.zcu.kiv.eegdatabase.logic.controller.service;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ServiceResult;
import cz.zcu.kiv.eegdsp.common.ISignalProcessingResult;
import cz.zcu.kiv.eegdsp.common.ISignalProcessor;
import cz.zcu.kiv.eegdsp.main.SignalProcessingFactory;
import org.hibernate.Hibernate;
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
import java.io.ByteArrayOutputStream;
import java.sql.Blob;
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
        ServiceResult service = new ServiceResult();
        service.setOwner(personDao.getLoggedPerson());
        service.setStatus("running");
        service.setTitle(super.fileName + "_Fast_Fourier");
        service.setFilename(super.fileName + "_FFT.png");
        resultDao.create(service);
        double signal[] = transformer.readBinaryData(data, cmd.getChannel());
        new FFTProcess(service, signal).start();

        return mav;
    }
     private class FFTProcess extends Thread {
        private ServiceResult service;
        private double[] signal;

        public FFTProcess(ServiceResult result, double[] signal) {
            this.service = result;
            this.signal = signal;
        }

        public void run() {
             ISignalProcessor fft = SignalProcessingFactory.getInstance().getFastFourier();
        ISignalProcessingResult res;
        res = fft.processSignal(signal);
        Map<String, Double[][]> map = res.toHashMap();
        Double[][] result = map.get("coefficients");
        double[] real = new double[result.length];
        double[] imag = new double[result.length];
        for (int i = 0; i < result.length; i++) {
            real[i] = result[i][0];
            imag[i] = result[i][1];
        }
        XYSeries XYreal = new XYSeries("Real part");
        XYSeries XYimag = new XYSeries("Imaginary part");
        for (int i = 200; i < real.length - 200; i++) {
            XYreal.add(i, real[i]);
            XYimag.add(i, imag[i]);
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(XYreal);
        dataset.addSeries(XYimag);
        //         Generate the graph
        JFreeChart chart = ChartFactory.createXYLineChart("Fast Fourier Transformation", // Title
                "time", // x-axis Label
                "value", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                ChartUtilities.writeChartAsPNG(out, chart, 800, 500);
            } catch (Exception e) {

            }
            service.setFigure(Hibernate.createBlob(out.toByteArray()));
            service.setStatus("finished");
            resultDao.update(service);

        }
     }
}
