package cz.zcu.kiv.eegdatabase.webservices.dataDownload.wrappers;

/**
 * Data wrapper for shared person information.
 *
 * @author: Petr Miko (miko.petr at gmail.com)
 */
public class PersonInfo {

    private int personId;
    private int defaultGroupId;
    private String givenName;
    private String surname;
    private char gender;

    /**
     * Getter of person's identifier.
     *
     * @return person's identifier
     */
    public int getPersonId() {
        return personId;
    }

    /**
     * Setter of person's identifier.
     *
     * @param personId person's identifier
     */
    public void setPersonId(int personId) {
        this.personId = personId;
    }

    /**
     * Getter of default group's identifier.
     *
     * @return default group identifier
     */
    public int getDefaultGroupId() {
        return defaultGroupId;
    }

    /**
     * Setter of default group's identifier.
     *
     * @param defaultGroupId default group identifier
     */
    public void setDefaultGroupId(int defaultGroupId) {
        this.defaultGroupId = defaultGroupId;
    }

    /**
     * Getter of person's given name.
     *
     * @return given name
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * Setter of person's given name.
     *
     * @param givenName given name
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * Getter of person's surname
     *
     * @return surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Setter of person's surname.
     *
     * @param surname surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Getter of person's gender.
     *
     * @return M/F as male/female
     */
    public char getGender() {
        return gender;
    }

    /**
     * Setter of person's gender
     *
     * @param gender M/F as male/female
     */
    public void setGender(char gender) {
        this.gender = gender;
    }
}
