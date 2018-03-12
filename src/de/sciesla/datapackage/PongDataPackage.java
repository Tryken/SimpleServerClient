package de.sciesla.datapackage;

import de.sciesla.sender.Sender;

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
