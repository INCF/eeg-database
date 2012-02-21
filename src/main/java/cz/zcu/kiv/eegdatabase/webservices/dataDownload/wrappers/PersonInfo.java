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
    private long scn;
    private boolean changed;
    private boolean added;

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

    /**
     * Getter of revision number (oracle scn).
     * @return revision number
     */
    public long getScn() {
        return scn;
    }

    /**
     * Setter of revision number (oracle scn).
     *
     * @param scn revision number
     */
    public void setScn(long scn) {
        this.scn = scn;
    }
    
    /**
     * Object is meant to create new record.
     * @return new record
     */
    public boolean isAdded() {
        return added;
    }

    /**
     * Mark object to create new record.
     * @param added new record
     */
    public void setAdded(boolean added) {
        this.added = added;
    }

    /**
     * Object is meant to update existing record.
     * @return updated record
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Mark object to update an existing object.
     * @param changed updated record
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
