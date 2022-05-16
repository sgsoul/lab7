package main;


import java.io.PrintStream;

import client.Client;
import common.exceptions.ConnectionException;
import common.exceptions.EndOfInputException;
import common.exceptions.InvalidPortException;

import static common.io.ConsoleOutputter.print;


public class Main {
    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        String address = "localhost";
        String strPort = "5432";
        int port;
        try {
            if (args.length == 2) {
                address = args[0];
                strPort = args[1];
            }
            if (args.length == 1) {
                strPort = args[0];
                print("��� ������, ������������� �����������, ��������� �� ��������� " + address);
            }
            if (args.length == 0) {
                print("��� ����� � ������, ������������ �����������, ��������� �� ��������� :" + address + "/" + strPort);
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
                print(e.getMessage());
            }
        } catch (ConnectionException e) {
            print(e.getMessage());
        }
    }
}

 class Client1 {
    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        String address = "localhost";
        String strPort = "5432";
        int port;
        try {
            if (args.length == 2) {
                address = args[0];
                strPort = args[1];
            }
            if (args.length == 1) {
                strPort = args[0];
                print("��� ������, ������������� �����������, ��������� �� ��������� " + address);
            }
            if (args.length == 0) {
                print("��� ����� � ������, ������������ �����������, ��������� �� ��������� :" + address + "/" + strPort);
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
                print(e.getMessage());
            }
        } catch (ConnectionException e) {
            print(e.getMessage());
        }
    }
}
