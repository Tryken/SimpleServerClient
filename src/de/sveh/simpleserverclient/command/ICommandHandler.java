package de.sveh.simpleserverclient.command;

import de.sveh.simpleserverclient.sender.Sender;

public interface ICommandHandler {
	
	void onInit();
	CommandResponse onCommand(Sender sender, String prefix, String[] args);
}
