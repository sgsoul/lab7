package common.connection;

import java.io.Serializable;

import common.data.HumanBeing;

/**
 * Сообщение с командой и аргументами.
 */

public class CommandMsg implements Request {
    private String commandName;
    private String commandStringArgument;
    private Serializable commandObjectArgument;

    public CommandMsg(String commandNm, String commandSA, Serializable commandOA) {
        commandName = commandNm;
        commandStringArgument = commandSA;
        commandObjectArgument = commandOA;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getStringArg() {
        return commandStringArgument;
    }

    public HumanBeing getHuman() {
        return (HumanBeing) commandObjectArgument;
    }
}