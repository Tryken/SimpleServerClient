package de.sveh.simpleserverclient.demo.chat;

import de.sveh.simpleserverclient.annotations.Event;
import de.sveh.simpleserverclient.event.IEventHandler;
import de.sveh.simpleserverclient.event.server.OnClientJoinEvent;
import de.sveh.simpleserverclient.event.server.OnClientLeaveEvent;
import de.sveh.simpleserverclient.server.Server;
import de.sveh.simpleserverclient.server.connection.Connection;

public class ChatServer extends Server implements IEventHandler {

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
