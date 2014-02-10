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
 *   WaveletProcessingController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.service;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ServiceResult;
import cz.zcu.kiv.eegdsp.common.ISignalProcessor;
import cz.zcu.kiv.eegdsp.main.SignalProcessingFactory;
import cz.zcu.kiv.eegdsp.wavelet.continuous.WaveletTransformationContinuous;
import cz.zcu.kiv.eegdsp.wavelet.continuous.algorithm.wavelets.WaveletCWT;
import cz.zcu.kiv.eegdsp.wavelet.discrete.WaveletTransformationDiscrete;
import cz.zcu.kiv.eegdsp.wavelet.discrete.algorithm.wavelets.WaveletDWT;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
                if ((dataFile.getFilename().endsWith(".avg")) || (dataFile.getFilename().endsWith(".eeg"))) {
                    data = dataFile.getFileContent();
                    break;
                }
            }
        }
        ServiceResult service = new ServiceResult();
        service.setOwner(personDao.getLoggedPerson());
        service.setStatus("running");
        service.setTitle(super.fileName + "_Wavelet");
        service.setFilename(super.fileName + "_WT.png");
        resultDao.create(service);
        double signal[] = transformer.readBinaryData(data, cmd.getChannel());
        new WTProcess(type, service, signal, cmd.getWavelet()).start();


        return mav;
    }

    private class WTProcess extends Thread {

        private String type;
        private ServiceResult service;
        private double[] signal;
        private String wavelet;
        private final int MAX_LENGTH = 10240;

        public WTProcess(String type, ServiceResult service, double[] signal, String wavelet) {
            this.type = type;
            this.service = service;
            this.signal = signal;
            this.wavelet = wavelet;
        }

        public void run() {
            ISignalProcessor wt = null;
            if (type.equals("CWT")) {
                if (signal.length > MAX_LENGTH) {
                    service.setStatus("failed");
                    service.setFilename("errorLog.txt");
                    String errorText = "Not enough memory for analysed signal.";
                    service.setContent(errorText.getBytes());
                    resultDao.update(service);
                    return;
                }
                wt = SignalProcessingFactory.getInstance().getWaveletContinuous();
                WaveletCWT wavelet = ((WaveletTransformationContinuous) wt).getWaveletGenerator().getWaveletByName(this.wavelet);
                ((WaveletTransformationContinuous) wt).setWavelet(wavelet);
            } else {
                wt = SignalProcessingFactory.getInstance().getWaveletDiscrete();
                WaveletDWT wavelet = ((WaveletTransformationDiscrete) wt).getWaveletGenerator().getWaveletByName(this.wavelet);
                ((WaveletTransformationDiscrete) wt).setWavelet(wavelet);
            }
            byte[] image = createImage(wt.processSignal(signal).toHashMap());
            if (image == null) {
                service.setStatus("failed");
                service.setFilename("errorLog.txt");
                String errorText = "Error while creating an image.";
                service.setContent(errorText.getBytes());
                resultDao.update(service);
                return;
            }
            service.setContent(image);
            service.setStatus("finished");
            resultDao.update(service);
        }

        private byte[] createImage(Map<String, Double[][]> result) {
            int width = signal.length;
            if (type.equals("DWT")) {
                width = 800;
            }
            int height = 400;
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setPaint(Color.BLACK);
            g2.fill(new Rectangle2D.Float(0, 0, width, height));
            if (type.equals("DWT")) {
                Double[] highest = result.get("highestCoefficients")[0];
                Double[] DWTCoefficients = result.get("dwtCoefficients")[0];
                float xstep;
                float ystep = (float) (height / highest.length);
                float x;
                float y = ystep * highest.length;
                int start = DWTCoefficients.length / 2;
                int end = DWTCoefficients.length;
                int level = 2;
                for (int i = highest.length - 1; i >= 0; i--) {
                    xstep = (float) (width / (DWTCoefficients.length / level));
                    x = 0;
                    for (int j = start; j < end; j++) {

                        int color = (int) Math.abs(255 * DWTCoefficients[j] / highest[i]);
                        g2.setPaint(new Color(color, color, color));
                        g2.fill(new Rectangle2D.Float(x, y - ystep, xstep, ystep));
                        x += xstep;
                    }
                    start /= 2;
                    end /= 2;
                    level *= 2;
                    y -= ystep;
                }
            } else {
                Double[] highest = result.get("highestCoeficients")[0];
                Double[][] CWTReal = result.get("real");
                float xstep;
                float ystep = (float) (height / highest.length);
                float x;
                float y = ystep * highest.length + 1;
                for (int i = 0; i < highest.length; i++) {
                    xstep = (float) (width / CWTReal[i].length);
                    x = 0;
                    for (int index = 0; index < CWTReal[i].length; index++) {
                        double base = Math.abs(CWTReal[i][index] / highest[i]);
                        int color = (int) (255 * base);
                        g2.setPaint(new Color(color, color, color));
                        g2.fill(new Rectangle2D.Float(x, y - ystep, xstep, ystep));
                        x += xstep;
                    }
                    y -= ystep;
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                ImageIO.write(img, "PNG", out);
            } catch (IOException e) {
                return null;
            }
            return out.toByteArray();
        }
    }
}
