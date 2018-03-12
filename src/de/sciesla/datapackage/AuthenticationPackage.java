package de.sciesla.datapackage;

import de.sciesla.annotations.Encoding;
import de.sciesla.client.Client;
import de.sciesla.sender.Sender;
import de.sciesla.server.Server;
import de.sciesla.server.connection.Connection;

@Encoding
public class AuthenticationPackage extends DataPackage {
	public AuthenticationPackage() {
	}

	public AuthenticationPackage(String password) {
		super(new String[] { password });
	}

	public void onServer(Sender sender) {
		if (getLength() == 1) {
			Server server = Server.getInstance();
			if (server.isConnectionLimitReached()) {
				server.removeConnection(sender.getConnection(), "Kick: The Server is full!");
				return;
			}
			if (server.isPasswordCorrect(getString(0))) {
				sender.getConnection().setAuthenticated(true);
			} else {
				sender.sendDataPackage(new AuthenticationPackage());
			}
		} else {
			sender.sendDataPackage(new AuthenticationPackage());
		}
	}

	public void onClient(Sender sender) {
		Client client = Client.getInstance();
		String password = client.onPasswordRequired();
		sender.sendDataPackage(new AuthenticationPackage(password));
	}
}
