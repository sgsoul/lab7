package server;

import java.io.*;
import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import auth.UserManager;


import collection.HumanManager;
import commands.ServerCommandManager;
import common.auth.User;
import common.commands.*;
import common.connection.*;

import database.DBManager;
import database.HumanDBManager;
import database.UserDBManager;
import exceptions.DataBaseException;
import exceptions.ServerOnlyCommandException;
import log.Log;
import common.exceptions.*;

import static log.Log.logger;

/**
 * Класс сервера.
 */

public class Server extends Thread implements SenderReceiver {

    private HumanManager collectionManager;
    private ServerCommandManager commandManager;
    private DBManager dbManager;
    private int port;
    private DatagramChannel channel;
    private volatile boolean running;
    private User hostUser;
    private InetSocketAddress clientAddress;

    private UserManager userManager;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final Lock lock = new ReentrantLock();

    private void init(int p, Properties properties) throws ConnectionException, DatabaseException {
        port = p;
        hostUser = null;
        running = true;
        setDaemon(true);
        dbManager = new DBManager(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));
        userManager = new UserDBManager(dbManager);
        collectionManager = new HumanDBManager(dbManager, userManager);
        commandManager = new ServerCommandManager(this);
        try {
            collectionManager.deserializeCollection("");
        } catch (CollectionException e) {
            Log.logger.error(e.getMessage());
        }
        host(port);
        setName("Поток сервера.");
        Log.logger.trace("Запуск сервера!");
    }

    private void host(int p) throws ConnectionException {
        try {
            if (channel != null && channel.isOpen()) channel.close();
            port = p;
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));
            Selector selector = Selector.open();
            channel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
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

    public void requestHandle() throws ConnectionException, InvalidDataException {
        ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE * 2);
        buf.clear();
        try {
            clientAddress = (InetSocketAddress) channel.receive(buf);
            logger.trace("Получение запроса от " + clientAddress.toString());
        } catch (ClosedChannelException e) {
            try {
                throw new ClosedConnectionException();
            } catch (ClosedConnectionException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            try {
                throw new ConnectionException("Что-то пошло не так во время получения запроса.");
            } catch (ConnectionException ex) {
                ex.printStackTrace();
            }
        }

        Runnable task = () -> {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buf.array()));
                Request req = (Request) objectInputStream.readObject();
                lock.lock();
                commandEx(req);
                lock.unlock();
            } catch (ClassNotFoundException | ClassCastException | IOException e) {
                try {
                    throw new InvalidReceivedDataException();
                } catch (InvalidReceivedDataException ex) {
                    ex.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    private void commandEx(Request commandMsg) {
        Runnable task = () -> {
            AnswerMsg answerMsg = new AnswerMsg();
            try {
                try {
                    if (commandMsg.getHuman() != null) {
                        commandMsg.getHuman().setCreationDate(new Date());
                    }
                    if (commandManager.getCommand(commandMsg).getType() == CommandType.SERVER_ONLY) {
                        throw new ServerOnlyCommandException();
                    }
                    answerMsg = (AnswerMsg) commandManager.runCommand(commandMsg);
                    if (answerMsg.getStatus() == Response.Status.EXIT) {
                        close();
                    }
                } catch (CommandException e) {
                    answerMsg.error(e.getMessage());
                    logger.error(e.getMessage());
                }
                send(answerMsg);
            } catch (ConnectionException e) {
                logger.error(e.getMessage());
            }
        };
        executorService.execute(task);
    }

    /**
     * Отправление ответа.
     */

    public void send(Response response) throws ConnectionException {

        if (clientAddress == null) throw new InvalidAddressException("Адрес клиента не найден.");
        Runnable task = () -> {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(response);
                channel.send(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()), clientAddress);
                logger.trace("Ответ отправлен на " + clientAddress.toString());
            } catch (IOException e) {
                try {
                    throw new ConnectionException("Что-то пошло не так во время отправки ответа.");
                } catch (ConnectionException ex) {
                    ex.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    /**
     * Запуск сервера.
     */

    public void run() {
        while (running) {
            try {
                requestHandle();
            } catch (ConnectionException | InvalidDataException e) {
                e.printStackTrace();
            }
        }
    }

    public void consoleMode() {
        commandManager.consoleMode();
    }

    /**
     * Закрытие сервера и соединения.
     */

    public void close() {
        try {
            running = false;
            dbManager.closeConnection();
            channel.close();
        } catch (IOException e) {
            Log.logger.error("Не удалось закрыть канал.");
        }
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

    public Commandable getCommandManager() {
        return commandManager;
    }

    public HumanManager getCollectionManager() {
        return collectionManager;
    }


}
