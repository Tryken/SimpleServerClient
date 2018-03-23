package de.sveh.simpleserverclient.datapackage;

import de.sveh.simpleserverclient.sender.Sender;

public class PingDataPackage extends DataPackage {
	
	public void onServer(Sender sender) {
		
		sender.sendDataPackage(new PongDataPackage(System.currentTimeMillis()));
	}

	public void onClient(Sender sender) {
		
		sender.sendDataPackage(new PongDataPackage(System.currentTimeMillis()));
	}
}
