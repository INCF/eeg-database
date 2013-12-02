/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   MatchingPursuitController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.service;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ServiceResult;
import cz.zcu.kiv.eegdsp.common.ISignalProcessingResult;
import cz.zcu.kiv.eegdsp.common.ISignalProcessor;
import cz.zcu.kiv.eegdsp.main.SignalProcessingFactory;
import cz.zcu.kiv.eegdsp.matchingpursuit.MatchingPursuit;
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

import java.util.Map;



public class MatchingPursuitController extends AbstractProcessingController {

    private final int MAX_SIGNAL_LENGTH = 10240;

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
                if ((dataFile.getFilename().endsWith(".avg")) || (dataFile.getFilename().endsWith(".eeg"))) {
                    data = dataFile.getFileContent();
                    break;
                }
            }
        }
        ServiceResult service = new ServiceResult();
        service.setOwner(personDao.getLoggedPerson());
        service.setStatus("running");
        service.setTitle(super.fileName + "_Matching_Pursuit");
        service.setFilename(super.fileName + "_MP.png");
        resultDao.create(service);
        double signal[] = transformer.readBinaryData(data, cmd.getChannel());

        new MPProcess(service, signal, cmd.getAtom()).start();

        return mav;
    }

    private class MPProcess extends Thread {
        private ServiceResult service;
        private double[] signal;
        private int atoms;

        public MPProcess(ServiceResult result, double[] signal, int atoms) {
            this.service = result;
            this.atoms = atoms;
            this.signal = signal;
        }

        public void run() {
            if (signal.length > MAX_SIGNAL_LENGTH) {
                service.setStatus("failed");
                service.setFilename("errorLog.txt");
                String errorText = "Not enough memory for analysed signal.";
                service.setFigure(errorText.getBytes());
                resultDao.update(service);
                return;
            }
            ISignalProcessingResult res = null;
            ISignalProcessor mp = SignalProcessingFactory.getInstance().getMatchingPursuit();
            ((MatchingPursuit) mp).setIterationCount(atoms);
            res = mp.processSignal(signal);
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
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                ChartUtilities.writeChartAsPNG(out, chart, 800, 500);
            } catch (Exception e) {

            }
            service.setFigure(out.toByteArray());
            service.setStatus("finished");
            resultDao.update(service);

        }
    }
}
