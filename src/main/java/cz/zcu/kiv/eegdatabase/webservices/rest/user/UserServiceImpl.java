package cz.zcu.kiv.eegdatabase.webservices.rest.user;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Implementation of user service
 *
 * @author Petr Miko
 */
public class UserServiceImpl implements UserService {

    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;

    @Override
    public UserInfo login() {
        Person user = personDao.getLoggedPerson();
        return new UserInfo(user.getGivenname(), user.getSurname(), user.getAuthority());
    }
}
