package client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.ByteBuffer;

import commands.ClientCommandManager;
import common.auth.User;
import common.connection.CommandMsg;
import common.connection.Request;
import common.connection.Response;
import common.connection.SenderReceiver;
import common.exceptions.*;
import common.io.OutputManager;

import static common.io.ConsoleOutputter.print;
import static common.io.ConsoleOutputter.printErr;


/**
 * Класс клиента.
 */

public class Client extends Thread implements SenderReceiver {
    private SocketAddress address;
    private DatagramSocket socket;
    public final int MAX_TIME_OUT = 500;
    public final int MAX_ATTEMPTS = 3;
    private User user;
    private User attempt;
    private OutputManager outputManager;
    private boolean running;
    private volatile boolean receivedRequest;
    private volatile boolean authSuccess;
    private ClientCommandManager commandManager;

    private boolean connected;

    //private HumanObservableManager collectionManager;
    public boolean isReceivedRequest() {
        return receivedRequest;
    }

    /**
     * Инициализация клиента.
     */

    private void init(String addr, int p) throws ConnectionException {
        connect(addr, p);
        running = true;
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

    public User getAttemptUser() {
        return attempt;
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
        try {
            socket.setSoTimeout(MAX_TIME_OUT);
        } catch (SocketException ignored) {

        }
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
//        while (running) {
        //           try {
/*                      receivedRequest = false;
        try {
            Response response = receiveWithoutTimeLimits();
            print(response.getMessage());
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }
        receivedRequest = true;*/
        //         } catch (InvalidDataException | ConnectionException e) {
        //          e.printStackTrace();
        commandManager.consoleMode();
        close();
    }


    /**
     * Процесс идентификации
     *
     * @param login
     * @param password
     * @param register
     */

    public void processAuthentication(String login, String password, boolean register) {
        attempt = new User(login, password);
        CommandMsg msg = new CommandMsg();
        if (register) {
            msg = new CommandMsg("register").setStatus(Request.Status.DEFAULT).setUser(attempt);
        } else {
            msg = new CommandMsg("login").setStatus(Request.Status.DEFAULT).setUser(attempt);
        }
        try {
            send(msg);
            Response answer = receiveWithoutTimeLimits();
            connected = true;
            authSuccess = (answer.getStatus() == Response.Status.AUTH_SUCCESS);
            if (authSuccess) {
                user = attempt;
            } else {
                outputManager.error("Неверный пароль.");
            }
        } catch (ConnectionTimeoutException e) {
            outputManager.error("Тайм-аут соединения.");
            connected = false;
        } catch (ConnectionException | InvalidDataException e) {
            connected = false;
        }
    }

    public void consoleMode() {
        commandManager.consoleMode();
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isAuthSuccess() {
        return authSuccess;
    }

    public ClientCommandManager getCommandManager() {
        return commandManager;
    }

    public OutputManager getOutputManager() {
        return outputManager;
    }

    public void setOutputManager(OutputManager out) {
        outputManager = out;
    }

    /**
     * Закрывает клиента.
     */

    public void close() {
        try {
            send(new CommandMsg().setStatus(Request.Status.EXIT));
        } catch (ConnectionException ignored) {

        }
        running = false;
        commandManager.close();
        socket.close();
    }

}