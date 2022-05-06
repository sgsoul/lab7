package common.connection;

import java.io.Serializable;

public interface Response extends Serializable{
    enum Status {
        ERROR,
        FINE,
        EXIT,
        AUTH_SUCCESS
    }

    public String getMessage();
    public Status getStatus();
}
