package de.sveh.simpleserverclient.demo.snake.client;

import de.sveh.simpleserverclient.client.Client;

public class SnakeClient extends Client {

	public SnakeClient(String host, int port) {
		super(host, port);
	}

	@Override
	public void onConnected() {

	}

	@Override
	public void onDisconnected() {

	}

	@Override
	public String onPasswordRequired() {

		return null;
	}

	@Override
	public void onAuthenticated() {

	}
}
