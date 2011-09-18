package cz.zcu.kiv.eegdatabase.logic.controller.service;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 16.9.11
 * Time: 15:46
 * To change this template use File | Settings | File Templates.
 */
public class ChooseCommand {

    String headerName;

    String service;

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
