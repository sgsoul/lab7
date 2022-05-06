package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;

import java.sql.DatabaseMetaData;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import collection.CollectionManager;
import collection.HumanCollectionManager;


import commands.ServerCommandManager;
import common.commands.*;
import common.connection.*;
import common.connection.Status;
import common.data.*;
import common.file.FileManager;
import common.file.ReaderWriter;

import common.utils.User;
import database.DBManager;
import database.HumanDBManager;
import database.UserDBManager;
import database.UserManager;
import exceptions.DataBaseException;
import exceptions.ServerOnlyCommandException;
import log.Log;
import common.exceptions.*;

import static log.Log.logger;

/**
 * Класс сервера.
 */

public class Server extends Thread implements SenderReceiver {

    private CollectionManager<HumanBeing> collectionManager;
    private ReaderWriter fileManager;
    private ServerCommandManager commandManager;
    private int port;
    private InetSocketAddress clientAddress;
    private DatagramChannel channel;
    private DBManager dbManager;
    private UserDBManager userManager;
    private User hostUser;
    private Queue<Map.Entry<InetSocketAddress, Request>> requestQueue;
    private Queue<Map.Entry<InetSocketAddress, Response>> responseQueue;

    private volatile boolean running;

    private void init(int port, Properties properties) throws ConnectionException, DataBaseException {
        running = true;
        port = port;

        hostUser = null;

        requestQueue = new ConcurrentLinkedQueue<>();
        responseQueue = new ConcurrentLinkedQueue<>();

        dbManager = new DBManager(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));
        userManager = new UserDBManager(dbManager);
        collectionManager = new HumanDBManager(dbManager, userManager);
        commandManager = new ServerCommandManager(this);


        try {
            collectionManager.deserializeCollection("");
        } catch (RuntimeException e) {
            Log.logger.error(e.getMessage());
        }
        host(port);
        setName("Поток сервера.");
        logger.trace("Запуск сервера!");
    }

    private void host(int p) throws ConnectionException {
        try {
            if (channel != null && channel.isOpen()) channel.close();
            channel = DatagramChannel.open();
            channel.bind(new InetSocketAddress(port));
        } catch (AlreadyBoundException e) {
            throw new PortAlreadyInUseException();
        } catch (IllegalArgumentException e) {
            throw new InvalidPortException();
        } catch (IOException e) {
            throw new ConnectionException("Что-то пошло не так во время инициализации сервера.");
        }
    }

    public Server(int port, Properties properties) throws ConnectionException, DataBaseException {
        init(port, properties);
    }

    /**
     * Получение запроса от клиента.
     */

    public Request receive() throws ConnectionException, InvalidDataException {
        ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
        try {
            clientAddress = (InetSocketAddress) channel.receive(buf);
            logger.trace("Получение запроса от " + clientAddress.toString());
        } catch (ClosedChannelException e) {
            throw new ClosedConnectionException();
        } catch (IOException e) {
            throw new ConnectionException("Что-то пошло не так во время получения запроса.");
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buf.array()));
            Request req = (Request) objectInputStream.readObject();
            return req;
        } catch (ClassNotFoundException | ClassCastException | IOException e) {
            throw new InvalidReceivedDataException();
        }

    }

    /**
     * Отправление ответа.
     */

    public void send(Response response) throws ConnectionException {
        if (clientAddress == null) throw new InvalidAddressException("Адрес клиента не найден.");
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(response);
            channel.send(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()), clientAddress);
            logger.trace("Ответ отправлен на " + clientAddress.toString());
        } catch (IOException e) {
            throw new ConnectionException("Что-то пошло не так во время отправки ответа.");
        }
    }

    /**
     * Запуск сервера.
     */

    public void run() {
        while (running) {
            AnswerMsg answerMsg = new AnswerMsg();
            try {
                try {
                    Request commandMsg = receive();
                    if (commandMsg.getHuman() != null) {
                        commandMsg.getHuman().setCreationDate(new Date());
                    }
                    if (commandManager.getCommand(commandMsg).getType() == CommandType.SERVER_ONLY) {
                        throw new ServerOnlyCommandException();
                    }
                    answerMsg = (AnswerMsg) commandManager.runCommand(commandMsg);
                    if (answerMsg.getStatus().equals(Status.EXIT)) {
                        close();
                    }
                } catch (CommandException e) {
                    answerMsg.error(e.getMessage());
                    logger.error(e.getMessage());
                }
                send(answerMsg);
            } catch (ConnectionException | InvalidDataException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void consoleMode() throws FileException, InvalidDataException, ConnectionException {
        commandManager.consoleMode();
    }

    /**
     * Закрытие сервера и соединения.
     */

    public void close() {
        try {
            running = false;
            channel.close();
        } catch (IOException e) {
            logger.error("Не удаётся закрыть канал.");
        }
    }

    public Commandable getCommandManager() {
        return commandManager;
    }

    public ReaderWriter getFileManager() {
        return fileManager;
    }

    public CollectionManager<HumanBeing> getCollectionManager() {
        return collectionManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public User getHostUser() {
        return hostUser;
    }

    public void setHostUser(User usr) {
        hostUser = usr;
    }
}