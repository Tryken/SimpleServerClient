package de.sveh.simpleserverclient.command;

import de.sveh.simpleserverclient.server.Server;
import de.sveh.simpleserverclient.command.normal.VersionCommand;

public class CommandRegister {

    public CommandRegister() {
        Server server = Server.getInstance();
        server.registerCommandHandler(new VersionCommand(), "v", "version");
    }
}
