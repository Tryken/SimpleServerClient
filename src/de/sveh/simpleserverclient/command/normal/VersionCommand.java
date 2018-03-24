package de.sveh.simpleserverclient.command.normal;

import de.sveh.simpleserverclient.command.ICommandHandler;
import de.sveh.simpleserverclient.command.CommandResponse;
import de.sveh.simpleserverclient.sender.Sender;

public class VersionCommand implements ICommandHandler {

    @Override
    public void onInit() {

    }

    @Override
    public CommandResponse onCommand(Sender sender, String prefix, String[] args) {

        if (args.length != 0)
            return CommandResponse.SYNTAX_ERROR;

        sender.sendMessage("SimpleServerClient 0.0.0.1-Alpha");

        return CommandResponse.SUCCESS;
    }

}
