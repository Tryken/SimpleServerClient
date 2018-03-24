package de.sveh.simpleserverclient.datapackage;

import de.sveh.simpleserverclient.annotations.Encoding;
import de.sveh.simpleserverclient.client.Client;
import de.sveh.simpleserverclient.sender.Sender;
import de.sveh.simpleserverclient.server.Server;

@Encoding
public class AuthenticationPackage extends AbstractDataPackage {

    public AuthenticationPackage() {
    }

    public AuthenticationPackage(String password) {
        super(password);
    }

    public void onServer(Sender sender) {
        if (getLength() == 1) {

            Server server = Server.getInstance();

            if (server.isConnectionLimitReached()) {
                server.removeConnection(sender.getConnection(), "Kick: The Server is full!");
                return;
            }

            if (server.isPasswordCorrect(getString(0)))
                sender.getConnection().setAuthenticated(true);
            else
                sender.sendDataPackage(new AuthenticationPackage());

        } else
            sender.sendDataPackage(new AuthenticationPackage());

    }

    public void onClient(Sender sender) {
        Client client = Client.getInstance();
        String password = client.onPasswordRequired();
        sender.sendDataPackage(new AuthenticationPackage(password));
    }
}
