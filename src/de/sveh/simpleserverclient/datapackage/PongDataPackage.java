package de.sveh.simpleserverclient.datapackage;

import de.sveh.simpleserverclient.sender.Sender;

public class PongDataPackage extends DataPackage {
	
	public PongDataPackage(long time) {
		
		super(String.valueOf(time));
	}

	public void onServer(Sender sender) {
		
		System.out.println("Pong");
	}

	public void onClient(Sender sender) {
		
		System.out.println("Pong");
	}
}
