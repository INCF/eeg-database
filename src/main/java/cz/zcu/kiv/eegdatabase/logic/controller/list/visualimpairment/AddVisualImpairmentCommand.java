package cz.zcu.kiv.eegdatabase.logic.controller.list.visualimpairment;

/**
 * @author Jindra
 */
public class AddVisualImpairmentCommand {

    private int visualImpairmentId;
    private String description;

    public int getVisualImpairmentId() {
        return visualImpairmentId;
    }

    public void setVisualImpairmentId(int visualImpairmentId) {
        this.visualImpairmentId = visualImpairmentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
