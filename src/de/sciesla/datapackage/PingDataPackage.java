package de.sciesla.datapackage;

import de.sciesla.sender.Sender;

public class PingDataPackage extends DataPackage {
	
	public void onServer(Sender sender) {
		
		sender.sendDataPackage(new PongDataPackage(System.currentTimeMillis()));
	}

	public void onClient(Sender sender) {
		
		sender.sendDataPackage(new PongDataPackage(System.currentTimeMillis()));
	}
}
