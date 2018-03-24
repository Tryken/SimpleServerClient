package de.sveh.simpleserverclient.client;

public enum ClientState {

    CONNECTING("Connecting"),
    CONNECTED("Connected"),
    DISCONNECTING("Disconnecting"),
    DISCONNECTED("Disconnected"),
    RECONNECTING("Reconnecting");

    private String name;

    ClientState(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}
