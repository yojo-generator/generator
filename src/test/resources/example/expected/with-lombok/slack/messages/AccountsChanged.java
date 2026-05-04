package slack.messages;

import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.AccountsChangedType;

@Generated("Yojo")
/**
* The list of accounts a user is signed into has changed.
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class AccountsChanged {


    private AccountsChangedType type;
}