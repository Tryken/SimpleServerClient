package de.sveh.simpleserverclient.event.server;

import de.sveh.simpleserverclient.event.Event;

public class OnChatMessageEvent extends Event{

	private String message;
	
	public OnChatMessageEvent(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
