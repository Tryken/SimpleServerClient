package de.sveh.simpleserverclient.server.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

import de.sveh.simpleserverclient.annotations.Authorized;
import de.sveh.simpleserverclient.datapackage.AuthenticatedPackage;
import de.sveh.simpleserverclient.datapackage.AbstractDataPackage;
import de.sveh.simpleserverclient.datapackage.EncryptionDataPackage;
import de.sveh.simpleserverclient.datapackage.MessageDataPackage;
import de.sveh.simpleserverclient.encoding.AESEncoding;
import de.sveh.simpleserverclient.encoding.RSAEncoding;
import de.sveh.simpleserverclient.event.server.OnClientJoinEvent;
import de.sveh.simpleserverclient.event.server.OnClientLeaveEvent;
import de.sveh.simpleserverclient.sender.Sender;
import de.sveh.simpleserverclient.sender.SenderType;
import de.sveh.simpleserverclient.server.Server;
import de.sveh.simpleserverclient.server.logger.LogType;
import de.sveh.simpleserverclient.server.logger.ILogger;

public class Connection extends Thread {

    private Socket socket;
    private String userName;
    private UUID uuid;
    private boolean authenticated;

    private BufferedReader in;
    private PrintWriter out;

    private RSAEncoding rsaEncoding;
    private AESEncoding aesEncoding;

    public Connection(Socket socket, boolean authenticated) {
        this.socket = socket;
        this.userName = "User-" + String.format("%04d", (int) (Math.random() * 9999));
        this.uuid = UUID.randomUUID();
        this.authenticated = authenticated;

        this.rsaEncoding = new RSAEncoding();
        this.rsaEncoding.generateKeyPair();

        this.aesEncoding = new AESEncoding();

        ILogger.log(LogType.INFO, userName + socket.getInetAddress().toString() + " tries to connect!");
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            Server server = Server.getInstance();
            if (server.isConnectionLimitReached()) {
                server.removeConnection(this, "Kick: The Server is full!");
                return;
            }

            server.sendDataPackage(this, new EncryptionDataPackage(getRsaEncoding().getPublicKey(), 256));

            String line;
            while (((line = in.readLine()) != null)) {
                AbstractDataPackage dataPackage = AbstractDataPackage.fromString(line, getAesEncoding());
                if (dataPackage != null) // ERRORhandling kick?
                    dataPackage.onServer(new Sender(this, SenderType.CLIENT));
            }

        } catch (IOException e) {
            try {
                socket.close();
                in.close();
                out.close();;
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            if (authenticated)
                Server.callEvent(new OnClientLeaveEvent(this), () -> Server.brodcastMessage(getUserName() + " left the server!"));
            else
                ILogger.log(LogType.INFO, getUserName() + " canceled the connection!");

            Server.getInstance().removeConnection(this);
        }
    }

    public void sendDataPackage(AbstractDataPackage datapackage) {
        if ((datapackage.getClass().isAnnotationPresent(Authorized.class)) && (!isAuthenticated()))
            return;

        if (this.out != null)
            this.out.println(datapackage.toString(getAesEncoding()));
    }

    public void sendMessage(String message) {
        sendDataPackage(new MessageDataPackage(message));
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return the uuid
     */
    public UUID getUuid() {
        return uuid;
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

        if (authenticated) {
            sendDataPackage(new AuthenticatedPackage());
            Server.callEvent(new OnClientJoinEvent(this), () -> Server.brodcastMessage(getUserName() + " has joined the server!"));
        }
    }

    public RSAEncoding getRsaEncoding() {
        return this.rsaEncoding;
    }

    public AESEncoding getAesEncoding() {
        return this.aesEncoding;
    }
}
