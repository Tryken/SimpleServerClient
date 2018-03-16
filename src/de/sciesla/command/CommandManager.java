package de.sciesla.command;

import java.util.HashMap;

import de.sciesla.sender.Sender;
import de.sciesla.sender.SenderType;
import de.sciesla.server.logger.LogType;
import de.sciesla.server.logger.Logger;

public class CommandManager {

	private String prefix = "/";
	
	private HashMap<String, CommandHandler> commandHandlers;
	
	public CommandManager() {
		
		commandHandlers = new HashMap<>();
	}
	
	public void registerCommandHandler(CommandHandler commandHandler, String... labels) {
		
		commandHandler.onInit();
		for(String label : labels)
			this.commandHandlers.put(label, commandHandler);
	}
	
	public boolean callCommand(Sender sender, String message) {
		
		if(!message.startsWith(getPrefix()))
			return false;
		
		String[] split = message.split(" ");
		String label = split[0].replace(prefix, "");
		String[] args = new String[split.length - 1];
		
		for(int i = 1; i < split.length; i++)
			args[i - 1] = split[i];

		CommandHandler commandHandler = commandHandlers.get(label);
		if(commandHandler == null) {
			
			if(sender.getType() == SenderType.SERVER)
				Logger.log(LogType.INFO, "Unknown command!");
			else
				sender.sendMessage("Unknown command!");
			return true;
		}
		
		if(sender.getType() == SenderType.CLIENT)
			Logger.log(LogType.INFO, sender.getUserName() + " use " + message);
			
		new Thread() {	
			public void run() {
				CommandResponse response = commandHandler.onCommand(sender, label, args);
				
				switch (response) {
				case NO_PERMISSIONS:
					if(sender.getType() == SenderType.SERVER)
						Logger.log(LogType.INFO, "This command is not available for the console!");
					else
						sender.sendMessage("You have no authority to use this command!");			
					break;
				case SYNTAX_ERROR:
					if(sender.getType() == SenderType.SERVER)
						Logger.log(LogType.INFO, "Syntaxerror use " + getPrefix() + "help " + label);
					else
						sender.sendMessage("Syntaxerror use " + getPrefix() + "help " + label);
					break;
				}
			}
		}.start();
		
		return true;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
