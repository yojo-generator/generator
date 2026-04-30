package example.testGenerate.test.messages;

import javax.validation.constraints.Email;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class UserSignedUp {

    /**
     * Name of the user
     */
    private String displayName;

    /**
     * Email of the user
     */
    @Email
    private String email;

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
}