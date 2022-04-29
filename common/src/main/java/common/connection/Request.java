package common.connection;

import java.io.Serializable;

import common.data.HumanBeing;

public interface Request extends Serializable{
    public String getStringArg();
    public HumanBeing getHuman();
    public String getCommandName();
}
