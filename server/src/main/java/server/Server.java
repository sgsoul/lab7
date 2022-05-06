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
import common.data.*;

import exceptions.ServerOnlyCommandException;
import log.Log;
import common.exceptions.*;

import static log.Log.logger;

/**
 * Класс сервера.
 */

public class Server extends Thread implements SenderReceiver {
    public final int MAX_CLIENTS = 10;
    private HumanManager collectionManager;
    private ServerCommandManager commandManager;
    private int port;
    private DatagramChannel channel;
    private User hostUser;
    private Selector selector;
    private volatile boolean running;
    // private DatabaseHandler databaseHandler; // todo sql
    private UserManager userManager;
    private Thread receiverThread;
    private Thread senderThread;
    private ExecutorService requestHandlerFixedThreadPool;
    private final Lock locker = new ReentrantLock();

    private void init(int p, Properties properties) throws ConnectionException, DatabaseException {
        running = true;
        port = p;
        hostUser = null;

        requestHandlerFixedThreadPool = Executors.newFixedThreadPool(MAX_CLIENTS);

        //   databaseHandler = new DatabaseHandler(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));
        //   userManager = new UserDatabaseManager(databaseHandler);
        //  collectionManager = new HumanDatabaseManager(databaseHandler, userManager);
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
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
        } catch (AlreadyBoundException e) {
            throw new PortAlreadyInUseException();
        } catch (IllegalArgumentException e) {
            throw new InvalidPortException();
        } catch (IOException e) {
            throw new ConnectionException("Что-то пошло не так во время инициализации сервера.");
        }
    }

    public Server(int p, Properties properties) throws ConnectionException {
        init(p, properties);
    }

    /**
     * Получение запроса от клиента.
     */

    public void receive() throws ConnectionException, InvalidDataException {
        Runnable task = () -> {
            ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
            InetSocketAddress clientAddress = null;
            Request request = null;
            try {
                clientAddress = (InetSocketAddress) channel.receive(buf);
                if (clientAddress == null) return;
                Log.logger.trace("Получение запроса от " + clientAddress.toString());
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
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buf.array()));
                request = (Request) objectInputStream.readObject();
            } catch (ClassNotFoundException | ClassCastException | IOException e) {
                try {
                    throw new InvalidReceivedDataException();
                } catch (InvalidReceivedDataException ex) {
                    ex.printStackTrace();
                }
            }
        };
        Thread readThread = new Thread(task);
        readThread.start();
    }

    /**
     * Отправление ответа.
     */

    public void send(InetSocketAddress address, Response response) throws ConnectionException {

        if (address == null) throw new InvalidAddressException("Адрес клиента не найден.");
        Runnable task = () -> {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            ObjectOutputStream objectOutputStream = null;
            try {
                objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                objectOutputStream.writeObject(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                channel.send(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()), address);
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.trace("Ответ отправлен на " + address.toString());
        };
        Thread sendThread = new Thread(task);
        sendThread.start();
    }

    /**
     * Обработка запроса.
     */

    private void handleRequest(InetSocketAddress address, Request request) {
        ExecutorService service = Executors.newFixedThreadPool(2);
        Runnable task = () -> {
            AnswerMsg answerMsg = new AnswerMsg();
            try {

                HumanBeing human = request.getHuman();

                if (human != null) {
                    human.setCreationDate(new Date());
                }
                request.setStatus(Request.Status.RECEIVED_BY_SERVER);

                if (commandManager.getCommand(request).getType() == CommandType.SERVER_ONLY) {
                    throw new ServerOnlyCommandException();
                }
                answerMsg = (AnswerMsg) commandManager.runCommand(request);

                if (answerMsg.getStatus() == Response.Status.EXIT) {
                    close();
                }
            } catch (CommandException e) {
                answerMsg.error(e.getMessage());
                Log.logger.error(e.getMessage());
            }
        };
        service.execute(task);

    }


    /**
     * Запуск сервера.
     */

    public void run() {
        while (running) {
            try {
                selector.select();
            } catch (IOException e) {
                continue;
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
            receiverThread.interrupt();
            requestHandlerFixedThreadPool.shutdown();
            senderThread.interrupt();
            // databaseHandler.closeConnection();
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


    private class RequestHandler implements Runnable {
        private final Request request;
        private final InetSocketAddress address;

        public RequestHandler(Map.Entry<InetSocketAddress, Request> requestEntry) {
            request = requestEntry.getValue();
            address = requestEntry.getKey();
        }

        public void run() {
            handleRequest(address, request);
        }
    }


}
