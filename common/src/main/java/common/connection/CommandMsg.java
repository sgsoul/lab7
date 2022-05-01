package common.connection;

import java.io.Serializable;

import common.auth.User;
import common.data.HumanBeing;

/**
 * Сообщение с командой и аргументами.
 */

public class CommandMsg implements Request {
    private final String commandName;
    private final String commandStringArgument;
    private final HumanBeing human;
    private User user;
    private Request.Status status;

    public CommandMsg(String commandNm, String commandSA, HumanBeing h) {
        commandName = commandNm;
        commandStringArgument = commandSA;
        human = h;
        user = null;
        status = Status.DEFAULT;
    }

    public CommandMsg(String commandNm, String commandSA, HumanBeing h, User usr) {
        commandName = commandNm;
        commandStringArgument = commandSA;
        human = h;
        user = usr;
        status = Status.DEFAULT;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getStringArg() {
        return commandStringArgument;
    }

    public HumanBeing getHuman() {
        return human;
    }

    public void setUser(User usr) {
        user = usr;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status s) {
        status = s;
    }

    public User getUser() {
        return user;
    }
}