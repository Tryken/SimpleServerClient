package de.sveh.simpleserverclient.event.server;

import de.sveh.simpleserverclient.event.Event;
import de.sveh.simpleserverclient.server.connection.Connection;

public class OnClientLeaveEvent extends Event {

    private Connection connection;

    public OnClientLeaveEvent(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
