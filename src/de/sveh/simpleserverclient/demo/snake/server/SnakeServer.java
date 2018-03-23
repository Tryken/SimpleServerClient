package de.sveh.simpleserverclient.demo.snake.server;

import de.sveh.simpleserverclient.demo.snake.datapackage.GameStatePackage;
import de.sveh.simpleserverclient.server.Server;

public class SnakeServer extends Server {

	private static SnakeServer instance;

	private boolean start = false;
	private boolean paused = true;

	public SnakeServer() {

		super(25565, 2, "");
	}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdate(float deltaTime) {

		if (!start && getConnectionAmount() == 2) {

			paused = false;
			start = true;
			broadcastDataPackage(new GameStatePackage("resume"));
		}

		if (start && getConnectionAmount() <= 1) {
			paused = true;
			start = false;
			broadcastDataPackage(new GameStatePackage("paused"));
		}
	}

	public static SnakeServer getInstance() {
		return instance;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
}
