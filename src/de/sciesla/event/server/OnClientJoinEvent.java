package de.sciesla.event.server;

import de.sciesla.event.Event;
import de.sciesla.server.connection.Connection;

public class OnClientJoinEvent extends Event{

	private Connection connection;
	
	public OnClientJoinEvent(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
	}
}
