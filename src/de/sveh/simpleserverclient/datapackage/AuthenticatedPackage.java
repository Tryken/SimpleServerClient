package de.sveh.simpleserverclient.datapackage;

import de.sveh.simpleserverclient.client.Client;
import de.sveh.simpleserverclient.sender.Sender;

public class AuthenticatedPackage extends DataPackage {
	
	public void onServer(Sender sender) {}

	public void onClient(Sender sender) {
		
		Client client = Client.getInstance();
		client.setAuthenticated(true);

		new Thread(() -> client.onAuthenticated()).start();
	}
}
