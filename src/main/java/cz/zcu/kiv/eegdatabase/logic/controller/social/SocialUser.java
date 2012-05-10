
package cz.zcu.kiv.eegdatabase.logic.controller.social;

/**
 * Simple object for storing user information from social networks.
 * @author Michal Patočka
 */
public class SocialUser {

    protected String email;
    protected String firstName;
    protected String lastName;

    public SocialUser(String email, String firstName, String lastName) {
        if (email != null) {
            this.email = email;
        } else {
            this.email = firstName + "@" + lastName + ".com";
        }
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
