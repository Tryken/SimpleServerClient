package de.sveh.simpleserverclient.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import de.sveh.simpleserverclient.command.ICommandHandler;
import de.sveh.simpleserverclient.command.CommandManager;
import de.sveh.simpleserverclient.command.CommandRegister;
import de.sveh.simpleserverclient.config.IConfiguration;
import de.sveh.simpleserverclient.config.PropertyConfiguration;
import de.sveh.simpleserverclient.datapackage.AbstractDataPackage;
import de.sveh.simpleserverclient.datapackage.KickDataPackage;
import de.sveh.simpleserverclient.datapackage.MessageDataPackage;
import de.sveh.simpleserverclient.event.Event;
import de.sveh.simpleserverclient.event.IEventHandler;
import de.sveh.simpleserverclient.event.EventManager;
import de.sveh.simpleserverclient.server.connection.Connection;
import de.sveh.simpleserverclient.server.logger.LogType;
import de.sveh.simpleserverclient.server.logger.ILogger;

public abstract class Server {

    private static Server instance;

    private int port;
    private int maxClients;
    private String password;

    private ServerState serverState;

    private ServerSocket listener;
    private ArrayList<Connection> connections;

    private EventManager eventManager;
    private CommandManager commandManager;

    private int maxTps = 20;
    private int tps;
    private long tick;
    private float deltaTime;
    private Thread updateThread;

    private IConfiguration properties;

    public abstract void onInit();

    public abstract void onStart();

    public abstract void onStop();

    public abstract void onUpdate(float deltaTime);

    public Server(int port, int maxClients, String password) {
        createServer(port, maxClients, password);
    }

    public Server(int port, int maxClients, String password, String propertyFilename) {
        properties = new PropertyConfiguration(propertyFilename);
        createServer(port, maxClients, password);
    }

    public Server(String propertyFilename) {
        properties = new PropertyConfiguration(propertyFilename);

        String portProp = properties.getProperty("port");
        String clientProp = properties.getProperty("max_clients");
        String passwordProp = properties.getProperty("password");

        if (portProp == null) {
            ILogger.log(LogType.ERROR, "No port property found");
            return;
        } else if (clientProp == null) {
            ILogger.log(LogType.ERROR, "No max_clients property found");
            return;
        } else if (passwordProp == null) {
            ILogger.log(LogType.ERROR, "No password property found");
            return;
        }

        try {
            int portNo = Integer.parseInt(portProp);
            int clientsNo = Integer.parseInt(clientProp);
            createServer(portNo, clientsNo, passwordProp);
        } catch (Exception e) {
            ILogger.log(LogType.ERROR, "One property is not a number but was expected to be a number");
        }


    }

    private void createServer(int port, int maxClients, String password) {
        instance = this;

        this.port = port;
        this.maxClients = maxClients;
        this.password = password;

        serverState = ServerState.STOPPED;

        init();
    }

    public String getProperty(String key) {
        if (properties == null) return null;
        return properties.getProperty(key);
    }


    private void init() {
        ILogger.log(LogType.INFO, "Server Init");

        connections = new ArrayList<>();

        eventManager = new EventManager();
        commandManager = new CommandManager();
        new CommandRegister();

        onInit();
    }

    public void start() {
        if (serverState != ServerState.STOPPED) {
            ILogger.log(LogType.ERROR, "Server läuft bereits!");
            return;
        }

        setServerState(ServerState.STARTING);

        setServerState(ServerState.STARTED);
        new Thread(() -> {
            try {
                listener = new ServerSocket(port);
                ILogger.log(LogType.INFO, "Listening on Port " + port);

                while (true) {

                    Socket socket = listener.accept();
                    Connection connection = new Connection(socket, password.equalsIgnoreCase(""));
                    connections.add(connection);
                    connection.start();

                }
            } catch (IOException e) {

                ILogger.log(LogType.ERROR, "Ein Server läuft bereits unter diesem");
                Server.getInstance().stop();
            }

        }).start();

        onStart();

        updateThread = new Thread(() -> {

            long lastTime = System.nanoTime();

            while (true) {
                onUpdate(deltaTime);

                tick++;
                deltaTime = (int) (System.nanoTime() - lastTime) / 1000000;
                if (deltaTime < (float) (1000 / maxTps)) {
                    try {
                        Thread.sleep((long) ((float) (1000 / maxTps) - deltaTime));
                        deltaTime = (int) (System.nanoTime() - lastTime) / 1000000;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                tps = (int) (1000 / deltaTime);
                lastTime = System.nanoTime();
            }
        });
        updateThread.start();
    }

    public void stop() {
        if (serverState != ServerState.STARTED && serverState != ServerState.RESTARTING) {
            ILogger.log(LogType.ERROR, "Server läuft nicht!");
            return;
        }

        setServerState(ServerState.STOPPING);

        try {
            listener.close();
            listener = null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            //e.printStackTrace();
        }

        setServerState(ServerState.STOPPED);

        onStop();
    }

    public void restart() {
        if (serverState != ServerState.STARTED && serverState != ServerState.RESTARTING) {
            ILogger.log(LogType.ERROR, "Server läuft nicht!");
            return;
        }

        setServerState(ServerState.RESTARTING);

        init();
        stop();
        start();
    }

    public void sendDataPackage(Connection connection, AbstractDataPackage datapackage) {
        connection.sendDataPackage(datapackage);
    }

    public void sendMessage(Connection connection, String message) {
        connection.sendMessage(message);
    }

    public void broadcastDataPackage(AbstractDataPackage datapackage) {
        for (Connection connection : connections)
            if (connection.isAuthenticated())
                connection.sendDataPackage(datapackage);
    }

    public static void brodcastMessage(String message) {

        ILogger.log(LogType.INFO, message);
        getInstance().broadcastDataPackage(new MessageDataPackage(message));
    }

    public static void callEvent(Event event, Runnable runnable) {
        getInstance().getEventManager().callEvent(event, runnable);
    }

    private void setServerState(ServerState state) {
        this.serverState = state;
        ILogger.log(LogType.INFO, "Server " + state.getName());
    }

    public void removeConnection(Connection connection) {
        connections.remove(connection);
    }

    public void removeConnection(Connection connection, String reason) {

        ILogger.log(LogType.INFO, "Kick " + connection.getUserName() + " reason: " + reason);
        connection.setAuthenticated(false);
        sendDataPackage(connection, new KickDataPackage(reason));
        removeConnection(connection);
    }

    public int getConnectionAmount() {

        int i = 0;

        for (Connection connection : connections)
            if (connection.isAuthenticated())
                i++;

        return i;
    }

    public boolean isConnectionLimitReached() {
        return (getMaxClients() != -1 && getConnectionAmount() > getMaxClients());
    }

    public void registerEventHandler(IEventHandler eventHandler) {
        getEventManager().registerEventHandler(eventHandler);
    }

    public void registerCommandHandler(ICommandHandler commandHandler, String... labels) {
        getCommandManager().registerCommandHandler(commandHandler, labels);
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * @return the maxClients
     */
    public int getMaxClients() {
        return maxClients;
    }

    /**
     * @param maxClients the maxClients to set
     */
    public void setMaxClients(int maxClients) {
        this.maxClients = maxClients;
    }

    public boolean isPasswordCorrect(String password) {
        return (this.password.equalsIgnoreCase("") || password.equalsIgnoreCase(this.password));
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the maxUps
     */
    public int getMaxTps() {
        return maxTps;
    }

    /**
     * @param maxTps the maxUps to set
     */
    public void setMaxTps(int maxTps) {
        this.maxTps = maxTps;
    }

    /**
     * @return the ups
     */
    public int getTps() {
        return tps;
    }

    /**
     * @return the tick
     */
    public long getTick() {
        return tick;
    }

    /**
     * @return the deltaTime
     */
    public float getDeltaTime() {
        return deltaTime;
    }

    public static Server getInstance() {
        return instance;
    }
}
