package example.testGenerate.test.messages;

import javax.annotation.processing.Generated;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
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
}