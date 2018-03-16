package de.sciesla.command.normal;

import de.sciesla.command.CommandHandler;
import de.sciesla.command.CommandResponse;
import de.sciesla.sender.Sender;

public class VersionCommand implements CommandHandler{

	@Override
	public void onInit() {
		
	}

	@Override
	public CommandResponse onCommand(Sender sender, String prefix, String[] args) {

		if(args.length != 0)
			return CommandResponse.SYNTAX_ERROR;
		
		sender.sendMessage("SimpleServerClient 0.0.0.1-Alpha");	
		
		return CommandResponse.SUCCESS;
	}

}
