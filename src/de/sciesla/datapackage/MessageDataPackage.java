package de.sciesla.datapackage;

import de.sciesla.annotations.Authorized;
import de.sciesla.annotations.Encoding;
import de.sciesla.command.CommandManager;
import de.sciesla.event.server.OnChatMessageEvent;
import de.sciesla.sender.Sender;
import de.sciesla.server.Server;
import de.sciesla.server.logger.LogType;
import de.sciesla.server.logger.Logger;

@Encoding
@Authorized
public class MessageDataPackage extends DataPackage {
	public MessageDataPackage(String msg) {
		super(msg);
	}

	public void onServer(Sender sender) {
		
		String message = getString(0);

		if (message.equalsIgnoreCase(""))
			return;
		
		Server server = Server.getInstance();
		CommandManager commandManager = server.getCommandManager();
		
		if(commandManager.callCommand(sender, message))
			return;
	
		Server.callEvent(new OnChatMessageEvent(message), new Runnable() {
			public void run() {

				String formattedMessage = sender.getUserName() + ": " + getString(0);
				Server.getInstance().broadcastDataPackage(new MessageDataPackage(formattedMessage));
				Logger.log(LogType.INFO, formattedMessage);
			}
		});
	}

	public void onClient(Sender sender) {
		System.out.println(getString(0));
	}
}
