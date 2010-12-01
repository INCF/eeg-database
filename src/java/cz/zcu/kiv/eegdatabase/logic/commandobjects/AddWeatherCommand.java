package cz.zcu.kiv.eegdatabase.logic.commandobjects;

/**
 * @author Jindra
 */
public class AddWeatherCommand {

    private String title;
    private String description;

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
