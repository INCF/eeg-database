package cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Data container for user information.
 */

@XmlType(propOrder = {"name","surname","rights"})
@XmlRootElement(name = "user")
@XStreamAlias("user")
public class UserInfo {
    private String name;
    private String surname;
    private String rights;

    public UserInfo(){}

    public UserInfo(String name, String surname, String rights){
        this.name = name;
        this.surname = surname;
        this.rights = rights;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }
}
