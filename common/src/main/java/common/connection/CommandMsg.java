package common.connection;

import java.io.Serializable;

import common.data.HumanBeing;
import common.utils.User;

/**
 * Сообщение с командой и аргументами.
 */

public class CommandMsg implements Request {
    private String commandName;
    private String commandStringArgument;
    private Serializable commandObjectArgument;
    private HumanBeing human;
    private User user;
    private Request.Status status;

    public CommandMsg(String commandName, String commandSA, Serializable commandOA) {
        commandName = commandName;
        commandStringArgument = commandSA;
        commandObjectArgument = commandOA;
    }

    public CommandMsg(String commandName, String commandSA, HumanBeing humanBeing, User usr) {
        commandName = commandName;
        commandStringArgument = commandSA;
        human = humanBeing;
        user = usr;
        status = Status.DEFAULT;
    }

    public String getCommandName() {
        return commandName;
    }

    @Override
    public User getUser() {
        return user;
    }

    public String getStringArg() {
        return commandStringArgument;
    }

    public HumanBeing getHuman() {
        return (HumanBeing) commandObjectArgument;
    }

    public void setStatus(Status status) {
        status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setUser(User user) {
        user = user;
    }

}