package cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

/**
 * Data container for list of person data.
 * Used in XML marshaling.
 *
 * @author Petr Miko
 */
@XmlRootElement(name = "people")
public class PersonDataList {

    @XmlElement(name = "person")
    public List<PersonData> people;

    public PersonDataList() {
        this.people = Collections.emptyList();
    }

    public PersonDataList(List<PersonData> people) {
        this.people = people;
    }
}
