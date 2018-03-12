package de.sciesla;

import de.sciesla.annotations.Event;
import de.sciesla.event.EventHandler;
import de.sciesla.event.server.OnClientJoinEvent;
import de.sciesla.event.server.OnClientLeaveEvent;
import de.sciesla.server.Server;
import de.sciesla.server.connection.Connection;

public class ChatServer extends Server implements EventHandler{

	public ChatServer(int port, String password, int maxClients) {
		super(port, maxClients, password);
	}

	@Override
	public void onInit() {

		registerEventHandler(this);
	}

	@Override
	public void onStart() {

	}

	@Override
	public void onStop() {

	}

	@Override
	public void onUpdate(float deltaTime) {

	}
	
	@Event
	public void onClientJoinEvent(OnClientJoinEvent event) {
		
		Connection connection = event.getConnection();
		
		brodcastMessage(connection.getUserName() + " has joined the server! (custom join message)");
		event.setCancelled(true);
	}
	
	@Event
	public void onClientLeaveEvent(OnClientLeaveEvent event) {
		
		Connection connection = event.getConnection();
		
		brodcastMessage(connection.getUserName() + " left the server! (custom leave message)");
		event.setCancelled(true);
	}
}
