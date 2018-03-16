package de.sciesla.command;

import de.sciesla.sender.Sender;

public interface CommandHandler {
	
	public abstract void onInit();
	public abstract CommandResponse onCommand(Sender sender, String prefix, String[] args);
}
