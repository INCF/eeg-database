package cz.zcu.kiv.eegdatabase.wui.core.person;

import org.springframework.beans.factory.annotation.Required;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;

public class PersonFacadeImpl implements PersonFacade {

    PersonService personService;

    @Required
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public void createPerson(Person user) {
        personService.createPerson(user);
    }

    @Override
    public Person getPersonByHash(String hashCode) {
        return personService.getPersonByHash(hashCode);
    }

    @Override
    public void deletePerson(Person user) {
        personService.deletePerson(user);
    }

    @Override
    public void updatePerson(Person user) {
        personService.updatePerson(user);
    }

    @Override
    public boolean usernameExists(String userName) {
        return personService.usernameExists(userName);
    }

    @Override
    public Person getPersonByUserName(String userName) {
        return personService.getPersonByUserName(userName);
    }

    @Override
    public void changeUserPassword(String userName, String password) {
        personService.changeUserPassword(userName, password);
    }

    @Override
    public boolean isPasswordEquals(String userName, String password) {
        return personService.isPasswordEquals(userName, password);
    }
    
    @Override
    public void forgottenPassword(Person person) {
        personService.forgottenPassword(person);
    }

}
