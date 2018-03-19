package de.sciesla.command;

import de.sciesla.sender.Sender;

public interface CommandHandler {
	
	void onInit();
	CommandResponse onCommand(Sender sender, String prefix, String[] args);
}
