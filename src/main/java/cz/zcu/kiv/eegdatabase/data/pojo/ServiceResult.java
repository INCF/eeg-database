package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;
import java.sql.Blob;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 9.11.11
 * Time: 12:20
 * To change this template use File | Settings | File Templates.
 */
public class ServiceResult implements Serializable {

    private int serviceResultId;
    private Blob figure;
    private String filename;
    private String status;
    private String title;
    private Person owner;

    public int getServiceResultId() {
        return serviceResultId;
    }

    public void setServiceResultId(int serviceResultId) {
        this.serviceResultId = serviceResultId;
    }

    public Blob getFigure() {
        return figure;
    }

    public void setFigure(Blob figure) {
        this.figure = figure;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
