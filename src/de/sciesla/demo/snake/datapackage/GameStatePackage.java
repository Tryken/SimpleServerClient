package de.sciesla.demo.snake.datapackage;

import de.sciesla.datapackage.DataPackage;
import de.sciesla.demo.snake.client.Game;
import de.sciesla.demo.snake.server.SnakeServer;
import de.sciesla.sender.Sender;
import de.sciesla.server.Server;

public class GameStatePackage extends DataPackage {

	public GameStatePackage(String state) {
		super(state);
	}

	@Override
	public void onServer(Sender sender) {
		
		if (getLength() == 1) {
			
			Server.getInstance().broadcastDataPackage(new GameStatePackage(getString(0)));

			if (getString(0).equalsIgnoreCase("paused"))
				SnakeServer.getInstance().setPaused(true);
			
			if (getString(0).equalsIgnoreCase("resume"))
				SnakeServer.getInstance().setPaused(false);
		}
	}

	@Override
	public void onClient(Sender sender) {

		if (getLength() == 1) {
			
			if (getString(0).equalsIgnoreCase("paused"))
				Game.getInstance().setPaused(true);
			
			if (getString(0).equalsIgnoreCase("resume"))
				Game.getInstance().setPaused(false);
		}
	}
}
