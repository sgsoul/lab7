﻿package main;


import java.io.PrintStream;

import client.Client;
import common.exceptions.ConnectionException;
import common.exceptions.EndOfInputException;
import common.exceptions.InvalidPortException;

import static common.io.OutputManager.*;


public class Main {
    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        String address = "localhost";
        String strPort = "4445";
        int port = 0;
        try {
            if (args.length == 2) {
                address = args[0];
                strPort = args[1];
            }
            if (args.length == 1) {
                strPort = args[0];
                print("Нет адреса, передаваемого аргументами, установка по умолчанию " + address);
            }
            if (args.length == 0) {
                print("Нет порта и адреса, передаваемых аргументами, установка по умолчанию :" + address + "/" + strPort);
            }
            try {
                port = Integer.parseInt(strPort);
            } catch (NumberFormatException e) {
                throw new InvalidPortException();
            }
            Client client = new Client(address, port);
            try {
                client.start();
            } catch (EndOfInputException e) {
                printErr(e.getMessage());
            }
        } catch (ConnectionException e) {
            print(e.getMessage());
        }
    }
}
