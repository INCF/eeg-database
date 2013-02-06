package cz.zcu.kiv.eegdatabase.wui.core.person;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;

public interface PersonFacade {

    void createPerson(Person user);

    void deletePerson(Person user);

    void updatePerson(Person user);

    Person getPersonByHash(String hashCode);

    Person getPersonByUserName(String userName);

    public boolean usernameExists(String userName);

    void changeUserPassword(String userName, String newPass);

    boolean isPasswordEquals(String userName, String password);

    void forgottenPassword(Person person);

}
