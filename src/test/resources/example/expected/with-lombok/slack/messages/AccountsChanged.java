package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.AccountsChangedType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* The list of accounts a user is signed into has changed.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class AccountsChanged {

    private AccountsChangedType type;

}