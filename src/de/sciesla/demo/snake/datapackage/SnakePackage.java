package de.sciesla.demo.snake.datapackage;

import java.util.UUID;

import de.sciesla.datapackage.DataPackage;
import de.sciesla.demo.snake.client.Game;
import de.sciesla.demo.snake.server.SnakeServer;
import de.sciesla.sender.Sender;
import de.sciesla.server.Server;

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
