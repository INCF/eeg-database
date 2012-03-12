package cz.zcu.kiv.eegdatabase.data.service;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.springframework.mail.MailException;

import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Jiri Novotny
 * Date: 12.3.12
 * Time: 2:42
 */
public interface MailService {

    /**
     * Sends activation link to confirm user registration
     */
    void sendRegistrationConfirmMail(Person user, Locale locale) throws MailException;
}
