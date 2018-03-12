package de.sciesla.event.server;

import de.sciesla.event.Event;
import de.sciesla.server.connection.Connection;

public class OnClientLeaveEvent extends Event{

	private Connection connection;
	
	public OnClientLeaveEvent(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
	}
}
