package common.connection;

import java.io.Serializable;

public interface Response extends Serializable {

    String getMessage();

    Status getStatus();

    enum Status {
        ERROR,
        FINE,
        EXIT,
        AUTH_SUCCESS
    }

}
