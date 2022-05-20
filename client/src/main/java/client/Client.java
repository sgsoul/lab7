package client;

import commands.ClientCommandManager;
import common.auth.User;
import common.connection.CommandMsg;
import common.connection.Request;
import common.connection.Response;
import common.connection.SenderReceiver;
import common.exceptions.*;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;


/**
 * Класс клиента.
 */

public class Client extends Thread implements SenderReceiver {
    private SocketAddress address;
    private DatagramSocket socket;
    public final int MAX_TIME_OUT = 500;
    private User user;
    private User attempt;
    private volatile boolean authSuccess;
    private ClientCommandManager commandManager;
    private boolean connected;

    /**
     * Инициализация клиента.
     */

    private void init(String addr, int p) throws ConnectionException {
        connect(addr, p);
        connected = false;
        authSuccess = false;
        commandManager = new ClientCommandManager(this);
        setName("Клиентский поток.");
    }

    public Client(String addr, int p) throws ConnectionException {
        init(addr, p);
    }

    public void setUser(User usr) {
        user = usr;
    }

    public User getUser() {
        return user;
    }

    public void setAttemptUser(User u) {
        attempt = u;
    }

    /**
     * Соединение клиента с сервером.
     */

    public void connect(String addr, int p) throws ConnectionException {
        try {
            address = new InetSocketAddress(InetAddress.getByName(addr), p);
        } catch (UnknownHostException e) {
            throw new InvalidAddressException();
        } catch (IllegalArgumentException e) {
            throw new InvalidPortException();
        }
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(MAX_TIME_OUT);
        } catch (IOException e) {
            throw new ConnectionException("Не удается открыть сокет.");
        }
    }

    /**
     * Отправление запроса на сервер.
     */

    public void send(Request request) throws ConnectionException {
        try {
            request.setStatus(Request.Status.SENT_FROM_CLIENT);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            ObjectOutputStream objOutput = new ObjectOutputStream(byteArrayOutputStream);
            objOutput.writeObject(request);
            DatagramPacket requestPacket = new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.size(), address);
            socket.send(requestPacket);
            byteArrayOutputStream.close();
        } catch (IOException e) {
            throw new ConnectionException("Что-то пошло не так при отправке запроса.");
        }

    }

    /**
     * Получение сообщения от сервера.
     */

    public Response receiveWithoutTimeLimits() throws ConnectionException, InvalidDataException {
        try {
            socket.setSoTimeout(0);
        } catch (SocketException ignored) {

        }
        ByteBuffer bytes = ByteBuffer.allocate(BUFFER_SIZE);
        DatagramPacket receivePacket = new DatagramPacket(bytes.array(), bytes.array().length);
        try {
            socket.receive(receivePacket);
        } catch (IOException e) {
            throw new ConnectionException("Что-то пошло не так при получении ответа.");
        }

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes.array()));
            return (Response) objectInputStream.readObject();
        } catch (ClassNotFoundException | ClassCastException | IOException e) {
            throw new InvalidReceivedDataException();
        }
    }

    /**
     * Запускает клиента.
     */

    @Override
    public void run() {
        Request hello = new CommandMsg();
        hello.setStatus(Request.Status.HELLO);
        commandManager.consoleMode();
        close();
    }


    /**
     * Закрывает клиента.
     */


    public void close() {
        try {
            send(new CommandMsg().setStatus(Request.Status.EXIT));
        } catch (ConnectionException ignored) {

        }
        commandManager.close();
        socket.close();
    }
}

