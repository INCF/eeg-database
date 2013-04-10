package cz.zcu.kiv.eegdatabase.logic.controller.searchsolr;

import org.apache.wicket.Page;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    private List<String> textFragments;
    private String type;
    private Class<? extends Page> targetPage;
    private int id;
    private Date timestamp;

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

    public List<String> getTextFragments() {
        return textFragments;
    }

    public void setTextFragments(List<String> textFragments) {
        this.textFragments = textFragments;
    }

    public Class<? extends Page> getTargetPage() {
        return targetPage;
    }

    public void setTargetPage(Class<? extends Page> targetPage) {
        this.targetPage = targetPage;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
