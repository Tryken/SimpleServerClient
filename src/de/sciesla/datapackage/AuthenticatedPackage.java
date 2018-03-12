package de.sciesla.datapackage;

import de.sciesla.client.Client;
import de.sciesla.sender.Sender;

public class AuthenticatedPackage extends DataPackage {
	public void onServer(Sender sender) {
	}

	public void onClient(Sender sender) {
		final Client client = Client.getInstance();
		client.setAuthenticated(true);

		new Thread() {
			public void run() {
				client.onAuthenticated();
			}
		}.start();
	}
}
