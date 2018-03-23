package de.sveh.simpleserverclient.demo.snake.datapackage;

import java.util.UUID;

import de.sveh.simpleserverclient.datapackage.DataPackage;
import de.sveh.simpleserverclient.sender.Sender;
import de.sveh.simpleserverclient.server.Server;

public class SnakePackage extends DataPackage {

	public SnakePackage(UUID uuid, String snake) {
		super(snake);
	}

	@Override
	public void onServer(Sender sender) {

		Server.getInstance().broadcastDataPackage(new SnakePackage(UUID.fromString(getString(0)), getString(1)));
	}

	@Override
	public void onClient(Sender sender) {}
}
