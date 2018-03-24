package de.sveh.simpleserverclient.client;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

import de.sveh.simpleserverclient.annotations.Authorized;
import de.sveh.simpleserverclient.datapackage.AbstractDataPackage;
import de.sveh.simpleserverclient.datapackage.MessageDataPackage;
import de.sveh.simpleserverclient.encoding.AESEncoding;
import de.sveh.simpleserverclient.sender.Sender;
import de.sveh.simpleserverclient.sender.SenderType;
import de.sveh.simpleserverclient.server.logger.LogType;
import de.sveh.simpleserverclient.server.logger.ILogger;

public abstract class Client {

    private static Client instance;

    private String host;
    private int port;

    private boolean authenticated = false;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private ClientState clientState;
    private AESEncoding aesEncoding;

    private Properties properties;

    public Client(String host, int port) {
        createClient(host, port);
    }

    public Client(String host, int port, String propertyFilename) {
        boolean read = readProperties(propertyFilename);
        if (!read)
            ILogger.log(LogType.ERROR, "Could not read the property file");

        createClient(host, port);
    }

    public Client(String propertyFilename) {
        boolean read = readProperties(propertyFilename);
        if (read) {
            String hostProp = properties.getProperty("host");
            String portProp = properties.getProperty("port");

            if (hostProp == null) {
                ILogger.log(LogType.ERROR, "No host property found");
                return;
            } else if (portProp == null) {
                ILogger.log(LogType.ERROR, "No port property found");
                return;
            }

            try {
                int portNo = Integer.parseInt(portProp);
                createClient(hostProp, portNo);
            } catch (Exception e) {
                ILogger.log(LogType.ERROR, "Port property is not a number");
            }

        }
    }

    private void createClient(String host, int port) {
        instance = this;

        this.host = host;
        this.port = port;

        init();
    }


    private boolean readProperties(String propertyFilename) {
        properties = new Properties();
        File propertyFile = new File(propertyFilename);
        if (!propertyFile.exists() || propertyFile.isDirectory()) return false;

        try {
            properties.load(new FileInputStream(propertyFile));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }


    public abstract void onConnected();

    public abstract void onDisconnected();

    public abstract String onPasswordRequired();

    public abstract void onAuthenticated();

    private void init() {
        clientState = ClientState.DISCONNECTED;

        this.aesEncoding = new AESEncoding();
    }

    public void connect() {
        if (clientState != ClientState.DISCONNECTED) {
            System.out.println("Client is already connected to a server!");
            return;
        }

        new Thread(() -> {
            try {
                socket = new Socket(host, port);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                new Thread(() -> onConnected()).start();

                String line;
                while ((line = in.readLine()) != null) {
                    AbstractDataPackage dataPackage = AbstractDataPackage.fromString(line, getAesEncoding());
                    if (dataPackage != null) // TODO errorhandling
                        dataPackage.onClient(new Sender(SenderType.SERVER));
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    socket = null;
                    System.out.println("Connection lost!");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    System.out.println("Could not connect to the server!");
                }

                new Thread(() -> onDisconnected()).start();
            }
        }).start();

    }

    public void sendDataPackage(AbstractDataPackage datapackage) {

        if ((datapackage.getClass().isAnnotationPresent(Authorized.class)) && (!isAuthenticated())) {
            return;
        }

        if (this.out != null) {
            this.out.println(datapackage.toString(getAesEncoding()));
        }
    }

    public void sendMessage(String message) {
        sendDataPackage(new MessageDataPackage(message));
    }

    /**
     * @return the authentificated
     */
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * @param authenticated the authentificated to set
     */
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public AESEncoding getAesEncoding() {
        return this.aesEncoding;
    }

    public static Client getInstance() {
        return instance;
    }
}
