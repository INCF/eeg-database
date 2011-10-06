package cz.zcu.kiv.eegdatabase.logic.controller.list.weather;

/**
 * @author Jindra
 */
public class AddWeatherCommand {

    private int id;
    private String title;
    private String description;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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
}
