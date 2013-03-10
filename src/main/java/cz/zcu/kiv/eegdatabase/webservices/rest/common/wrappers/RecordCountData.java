package cz.zcu.kiv.eegdatabase.webservices.rest.common.wrappers;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
@XmlType(name="recordCount", propOrder = {"myRecords", "publicRecords"})
@XmlRootElement(name = "recordCount")
public class RecordCountData {

    private int myRecords;
    private int publicRecords;

    public int getMyRecords() {
        return myRecords;
    }

    public void setMyRecords(int myRecords) {
        this.myRecords = myRecords;
    }

    public int getPublicRecords() {
        return publicRecords;
    }

    public void setPublicRecords(int publicRecords) {
        this.publicRecords = publicRecords;
    }
}
