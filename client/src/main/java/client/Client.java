package client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.NoSuchElementException;

import commands.ClientCommandManager;
import common.auth.User;
import common.connection.Request;
import common.connection.Response;
import common.connection.SenderReceiver;
import common.exceptions.*;

import static common.io.OutputManager.print;
import static common.io.OutputManager.printErr;

/**
 * Класс клиента.
 */

public class Client extends Thread implements SenderReceiver {
    private SocketAddress address;
    private DatagramSocket socket;
    public final int MAX_TIME_OUT = 1000;
    public final int MAX_ATTEMPTS = 3;
    private User user = null;
    private boolean running;
    private ClientCommandManager commandManager;

    /**
     * Инициализация клиента.
     */

    private void init(String addr, int p) throws ConnectionException {
        connect(addr, p);
        running = true;
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

    public Response receive() throws ConnectionException, InvalidDataException {

        ByteBuffer bytes = ByteBuffer.allocate(BUFFER_SIZE);
        DatagramPacket receivePacket = new DatagramPacket(bytes.array(), bytes.array().length);
        try {
            socket.receive(receivePacket);
        } catch (SocketTimeoutException e) {
            for (int attempts = MAX_ATTEMPTS; attempts > 0; attempts--) {
                printErr("Превышен тайм-аут ответа сервера." + attempts + " попытки всё...");
                try {
                    socket.receive(receivePacket);
                    break;
                } catch (IOException ignored) {

                }
            }

            throw new ConnectionTimeoutException();
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
        commandManager.consoleMode();
        close();
    }

    /**
     * Закрывает клиента.
     */

    public void close() {
        running = false;
        commandManager.close();
        socket.close();
    }
}