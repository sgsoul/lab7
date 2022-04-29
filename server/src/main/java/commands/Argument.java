package commands;

import common.data.HumanBeing;

public class Argument implements common.connection.Request {
    private String arg;
    private HumanBeing human;

    public Argument(String s, HumanBeing h) {
        arg = s;
        human = h;
    }

    public String getStringArg() {
        return arg;
    }

    public HumanBeing getHuman() {
        return human;
    }

    public String getCommandName() {
        return "";
    }
}
