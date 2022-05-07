package common.connection;

import java.io.Serializable;

import common.auth.User;
import common.data.HumanBeing;


public interface Request extends Serializable {
    String getStringArg();

    HumanBeing getHuman();

    String getCommandName();

    User getUser();

    void setUser(User user);

    Status getStatus();

    void setStatus(Status status);

    enum Status {
        DEFAULT,
        SENT_FROM_CLIENT,
        RECEIVED_BY_SERVER
    }
}
