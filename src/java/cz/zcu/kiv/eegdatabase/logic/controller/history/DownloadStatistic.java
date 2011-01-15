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
