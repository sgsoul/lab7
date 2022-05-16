package common.connection;

import java.io.Serializable;

import common.auth.User;
import common.data.HumanBeing;


public interface Request extends Serializable {
    String getStringArg();

    HumanBeing getHuman();

    String getCommandName();

    User getUser();

    Request setUser(User usr);

    Status getStatus();

    Request setStatus(Status s);

    enum Status {
        HELLO,
        DEFAULT,
        SENT_FROM_CLIENT,
        RECEIVED_BY_SERVER,
        EXIT
    }
}
