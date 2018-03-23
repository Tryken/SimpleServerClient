package de.sveh.simpleserverclient.sender;

import de.sveh.simpleserverclient.client.Client;
import de.sveh.simpleserverclient.datapackage.DataPackage;
import de.sveh.simpleserverclient.server.Server;
import de.sveh.simpleserverclient.server.connection.Connection;

public class Sender {

	private SenderType senderType;
	private Connection connection;

	public Sender(SenderType senderType) {
		this.senderType = senderType;
	}

	public Sender(Connection connection, SenderType senderType) {
		this.senderType = senderType;
		this.connection = connection;
	}

	public void sendDataPackage(DataPackage datapackage) {
		
		switch (senderType) {
		case CLIENT:
			Server.getInstance().sendDataPackage(getConnection(), datapackage);
			break;
		case SERVER:
			Client.getInstance().sendDataPackage(datapackage);
			break;
		}
	}
	
	public void sendMessage(String message) {
		
		switch (senderType) {
		case CLIENT:
			Server.getInstance().sendMessage(getConnection(), message);
			break;
		case SERVER:
			Client.getInstance().sendMessage(message);
			break;
		}
	}
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		
		switch (senderType) {
		case CLIENT:
			return connection.getUserName();
		case SERVER:
			return "Console";
		}
		return "";
	}

	/**
	 * @return the userName
	 */
	public Connection getConnection() {
		
		switch (senderType) {
		case CLIENT:
			return connection;
		case SERVER:
			return null;
		}
		return null;
	}

	public SenderType getType() {
		return senderType;
	}
}
