package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

import java.util.LinkedList;
import java.util.List;

/**
 * @author František Liška
 */
public class WeatherInfo {
    private int weatherId;
    private String description;
    private String title;
    private int defaultNumber;
    private List<Integer> researchGroupIdList = new LinkedList<Integer>();

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
    }

    public List<Integer> getResearchGroupIdList() {
        return researchGroupIdList;
    }

    public void setResearchGroupIdList(List<Integer> researchGroupIdList) {
        this.researchGroupIdList = researchGroupIdList;
    }
}




