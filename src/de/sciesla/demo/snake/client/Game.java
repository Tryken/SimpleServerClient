package de.sciesla.demo.snake.client;

import java.awt.Color;
import java.awt.Graphics;

import de.sciesla.demo.snake.client.direction.Direction;
import de.sciesla.demo.snake.client.entity.snake.Snake;
import de.sciesla.demo.snake.client.frame.Frame;

public class Game extends Frame {

	private static final long serialVersionUID = -2234079094945346566L;

	private static Game instance;

	private SnakeClient client;
	private boolean paused = true;

	public Snake snake;

	public Game() {
		super("Pong");
	}

	@Override
	public void onInit() {

		instance = this;

		client = new SnakeClient("localhost", 25565);
		client.connect();

		snake = new Snake(Direction.RIGHT, 5, 5, 5, Color.red);
	}

	@Override
	public void onUpdate(float dt) {

		if (isPaused())
			return;

		snake.onUpdate(dt);
	}

	@Override
	public void onRender(Graphics g, float dt) {

		snake.onRender(g, dt);

		g.setColor(Color.WHITE);

		if (isPaused())
			g.drawString("Paused", getWidth() / 2 - 5, getHeight() / 2);

		g.drawString("Fps: " + getFps(), 5, 15);
	}

	public static Game getInstance() {
		return instance;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
}
