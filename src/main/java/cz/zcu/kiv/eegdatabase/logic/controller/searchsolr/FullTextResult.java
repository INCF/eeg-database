package cz.zcu.kiv.eegdatabase.logic.controller.searchsolr;

import org.apache.wicket.Page;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 22.3.13
 * Time: 0:59
 * To change this template use File | Settings | File Templates.
 */
public class FullTextResult implements Serializable {

    private String uuid;
    private String title;
    private String text;
    private String type;
    private Class<? extends Page> instance;
    private int id;

    private static final long serialVersionUID = 5458851218415845861L;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Class<? extends Page> getInstance() {
        return instance;
    }

    public void setInstance(Class<? extends  Page> instance) {
        this.instance = instance;
    }
}
