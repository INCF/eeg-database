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
 *   DownloadStatistic.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.controller.history;

/**
 * Using for saving download statistic
 *
 * @author pbruha
 */
public class DownloadStatistic implements Comparable<DownloadStatistic> {
    private int scenarioId;
    private int experimentId;
    private String fileName;

    private String title;
    private long count;

    public DownloadStatistic(long count) {
        this.count = count;
    }

    public DownloadStatistic(int scenarioId, String title, long count) {
        this.scenarioId = scenarioId;
        this.title = title;
        this.count = count;
        this.fileName = title + "-" + scenarioId + ".xml";
    }

    public DownloadStatistic(int scenarioId, String title, String fileName, long count) {
        this.scenarioId = scenarioId;
        this.title = title;
        this.count = count;
        this.fileName = fileName;
    }

    public DownloadStatistic(int scenarioId, int experimentId, String title, long count) {
        this.scenarioId = scenarioId;
        this.title = title;
        this.count = count;
        this.fileName = title + ".zip";
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }


    public int getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int compareTo(DownloadStatistic o) {
        if (this.count < o.count) return +1;
        if (this.count > o.count) return -1;
        return 0;
    }

    public int getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
