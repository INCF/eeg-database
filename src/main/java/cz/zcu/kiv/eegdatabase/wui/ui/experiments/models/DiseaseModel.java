package cz.zcu.kiv.eegdatabase.wui.ui.experiments.models;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 11.4.13
 * Time: 12:40
 */
public class DiseaseModel implements Serializable {
    private String name;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
