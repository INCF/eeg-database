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
 *   ChartUtils.java, 2014/05/13 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.utils;

import java.awt.image.BufferedImage;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

import cz.zcu.kiv.eegdatabase.logic.controller.history.ChoiceHistory;
import cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic;

public class ChartUtils {

    public static BufferedImage gererateChartForTopDownloadHistory(ChoiceHistory choiceHistory, boolean generateEmptyGraph,
            List<DownloadStatistic> topDownloadedFilesList, long countOfFilesHistory) {

        long countFile = 0;

        DefaultPieDataset dataset = new DefaultPieDataset();
        if (!generateEmptyGraph && topDownloadedFilesList != null) {
            for (int i = 0; i < topDownloadedFilesList.size(); i++) {
                dataset.setValue(topDownloadedFilesList.get(i).getFileName(), new Long(topDownloadedFilesList.get(i).getCount()));
                countFile = countFile + topDownloadedFilesList.get(i).getCount();
            }

            if (countOfFilesHistory > countFile) {
                dataset.setValue("Other", countOfFilesHistory - countFile);
            }
        }

        String chartTitle = ResourceUtils.getString("title.dailyStatistic");
        if (choiceHistory == ChoiceHistory.WEEKLY) {
            chartTitle = ResourceUtils.getString("title.weeklyStatistic");

        } else if (choiceHistory == ChoiceHistory.MONTHLY) {
            chartTitle = ResourceUtils.getString("title.monthlyStatistic");
        }

        JFreeChart chart = ChartFactory.createPieChart3D(chartTitle, // chart
                                                                     // title
                dataset, // data
                true, // include legend
                true, false);

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        plot.setNoDataMessage(ResourceUtils.getString("label.noDataMessage"));

        return chart.createBufferedImage(600, 400);
    }

}
