package common.connection;

import java.io.Serializable;

import common.auth.User;
import common.data.HumanBeing;

public interface Request extends Serializable {
    String getStringArg();

    HumanBeing getHuman();

    String getCommandName();

    User getUser();

    void setUser(User usr);

    Status getStatus();

    void setStatus(Status s);

    enum Status {
        DEFAULT,
        SENT_FROM_CLIENT,
        RECEIVED_BY_SERVER
    }
}
