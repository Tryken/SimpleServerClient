package de.sveh.simpleserverclient.demo.chat;

import java.util.Scanner;

import de.sveh.simpleserverclient.client.Client;

public class ChatClient extends Client {

	public ChatClient(String host, int port) {
		super(host, port);
	}

	@Override
	public void onConnected() {

	}

	@Override
	public void onDisconnected() {

	}

	@SuppressWarnings("resource")
	@Override
	public String onPasswordRequired() {

		System.out.print("Server Kennwort: ");
		return new Scanner(System.in).nextLine();
	}

	@Override
	public void onAuthenticated() {

		while (true) {

			@SuppressWarnings("resource")
			String message = new Scanner(System.in).nextLine();
			sendMessage(message);
		}
	}
}
