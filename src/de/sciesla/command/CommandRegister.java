package de.sciesla.command;

import de.sciesla.command.normal.VersionCommand;
import de.sciesla.server.Server;

public class CommandRegister {

	public CommandRegister() {
		
		Server server = Server.getInstance();
		
		server.registerCommandHandler(new VersionCommand(), "v", "version");
	}
}
