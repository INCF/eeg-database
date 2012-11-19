package cz.zcu.kiv.eegdatabase.wui.core.person;

import org.springframework.beans.factory.annotation.Required;

import cz.zcu.kiv.eegdatabase.wui.core.dto.FullPersonDTO;

public class PersonFacadeImpl implements PersonFacade {

    PersonService personService;

    @Required
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public void createPerson(FullPersonDTO user) {
        personService.createPerson(user);
    }

    @Override
    public FullPersonDTO getPersonByHash(String hashCode) {
        return personService.getPersonByHash(hashCode);
    }

    @Override
    public void deletePerson(FullPersonDTO user) {
        personService.deletePerson(user);
    }

    @Override
    public void updatePerson(FullPersonDTO user) {
        personService.updatePerson(user);
    }

    @Override
    public boolean usernameExists(String userName) {
        return personService.usernameExists(userName);
    }

    @Override
    public FullPersonDTO getPersonByUserName(String userName) {
        return personService.getPersonByUserName(userName);
    }

}
