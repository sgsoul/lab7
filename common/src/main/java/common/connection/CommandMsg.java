package common.connection;


import common.auth.User;
import common.data.HumanBeing;

/**
 * Сообщение с командой и аргументами.
 */

public class CommandMsg implements Request {
    private final String commandName;
    private String commandStringArgument;
    private HumanBeing human;
    private User user;
    private Request.Status status;

    public CommandMsg(String commandNm, String commandSA, HumanBeing h) {
        commandName = commandNm;
        commandStringArgument = commandSA;
        human = h;
        user = null;
        status = Status.DEFAULT;
    }

    public CommandMsg() {
        commandName = null;
        commandStringArgument = null;
        human = null;
        status = Status.DEFAULT;
    }

    public CommandMsg(String s) {
        commandName = s;
        commandStringArgument = null;
        human = null;
        status = Status.DEFAULT;
    }

    public CommandMsg(String commandNm, String commandSA, HumanBeing h, User usr) {
        commandName = commandNm;
        commandStringArgument = commandSA;
        human = h;
        user = usr;
        status = Status.DEFAULT;
    }

    public Status getStatus() {
        return status;
    }

    public CommandMsg setStatus(Status s) {
        status = s;
        return this;
    }

    public CommandMsg setUser(User usr) {
        user = usr;
        return this;
    }

    public CommandMsg setHuman(HumanBeing h) {
        human = h;
        return this;
    }

    public CommandMsg setArgument(String s) {
        commandStringArgument = s;
        return this;
    }


    /**
     * @return Название команды.
     */

    public String getCommandName() {
        return commandName;
    }

    /**
     * @return Строковый аргумент команды.
     */

    public String getStringArg() {
        return commandStringArgument;
    }

    /**
     * @return Аргумент в виде объекта команды.
     */

    public HumanBeing getHuman() {
        return human;
    }

    public User getUser() {
        return user;
    }
}