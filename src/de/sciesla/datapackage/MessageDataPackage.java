package de.sciesla.datapackage;

import de.sciesla.annotations.Authorized;
import de.sciesla.annotations.Encoding;
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
		if (getString(0).equalsIgnoreCase("")) {
			return;
		}
		String msg = sender.getUserName() + ": " + getString(0);
		Server.getInstance().broadcastDataPackage(new MessageDataPackage(msg));
		Logger.log(LogType.INFO, msg);
	}

	public void onClient(Sender sender) {
		System.out.println(getString(0));
	}
}
