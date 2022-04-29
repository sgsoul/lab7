package main;


import java.io.PrintStream;
import java.util.NoSuchElementException;

import client.Client;
import common.exceptions.ConnectionException;
import common.exceptions.InvalidPortException;
import common.exceptions.InvalidProgramArgumentsException;

import static common.io.OutputManager.*;


public class Main {
    //public static Logger logger = LogManager.getLogger("logger");
    //static final Logger logger = LogManager.getRootLogger();
    public static void main(String[] args) throws Exception {
        //System.setOut(new PrintStream(System.out, false, "UTF-8"));
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        args = new String[]{"localhost", "4445"};
        String addr = "";
        int port = 0;
        try {
            if (args.length != 2) throw new InvalidProgramArgumentsException("Адрес не найден.");
            addr = args[0];
            try {
                port = Integer.parseInt(args[1]);

//            catch (NoSuchElementException e) {
//                print("хватит баловться >:(");
//            }
            Client client = new Client(addr, port);
            client.start();
              } catch (NumberFormatException |NoSuchElementException e) {
                throw new InvalidPortException();
            }
//            catch (NoSuchElementException e) {
//                print("хватит баловться >:(");
//            }
        } catch (InvalidProgramArgumentsException | ConnectionException e) {
            print(e.getMessage());
        }

        //System.out.println(res.getMessage());
    }
}
