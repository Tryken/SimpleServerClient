package de.sciesla.event.server;

import de.sciesla.event.Event;

public class OnChatMessageEvent extends Event{

	private String message;
	
	public OnChatMessageEvent(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
