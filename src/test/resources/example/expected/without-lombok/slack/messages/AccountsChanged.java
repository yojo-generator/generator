package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.AccountsChangedType;

@Generated("Yojo")
/**
* The list of accounts a user is signed into has changed.
*/
public class AccountsChanged {


    private AccountsChangedType type;
    public void setType(AccountsChangedType type) {
        this.type = type;
    }
    public AccountsChangedType getType() {
        return type;
    }
}