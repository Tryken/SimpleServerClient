package de.sveh.simpleserverclient.datapackage;

import de.sveh.simpleserverclient.annotations.Authorized;
import de.sveh.simpleserverclient.annotations.Encoding;
import de.sveh.simpleserverclient.command.CommandManager;
import de.sveh.simpleserverclient.event.server.OnChatMessageEvent;
import de.sveh.simpleserverclient.sender.Sender;
import de.sveh.simpleserverclient.server.Server;
import de.sveh.simpleserverclient.server.logger.LogType;
import de.sveh.simpleserverclient.server.logger.ILogger;

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
	
		Server.callEvent(new OnChatMessageEvent(message), () -> {

            String formattedMessage = sender.getUserName() + ": " + getString(0);
            Server.getInstance().broadcastDataPackage(new MessageDataPackage(formattedMessage));
            ILogger.log(LogType.INFO, formattedMessage);
        });
	}

	public void onClient(Sender sender) {
		System.out.println(getString(0));
	}
}
