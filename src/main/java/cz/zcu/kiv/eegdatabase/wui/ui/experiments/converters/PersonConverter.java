package cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;

import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 12.5.13
 * Time: 12:14
 */
public class PersonConverter implements IConverter<Person> {
    private PersonFacade personFacade;

    public PersonConverter(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }

    @Override
    public Person convertToObject(String s, Locale locale) {
        if(Strings.isEmpty(s)){
            return null;
        }

        String[] datas = s.split(",");
        String username = datas[datas.length -1];

        Person person = new Person();
        person.setUsername(username);
        List<Person> persons = personFacade.getUnique(person);
        return (persons != null && persons.size() > 0) ? persons.get(0) : person;
    }

    @Override
    public String convertToString(Person person, Locale locale) {
        return person.getAutoCompleteData();
    }
}
