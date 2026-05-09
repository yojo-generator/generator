package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.AccountsChangedType;

/**
* The list of accounts a user is signed into has changed.
*/
@Generated("Yojo")
public class AccountsChanged {


    private AccountsChangedType type;
    public void setType(AccountsChangedType type) {
        this.type = type;
    }
    public AccountsChangedType getType() {
        return type;
    }
}