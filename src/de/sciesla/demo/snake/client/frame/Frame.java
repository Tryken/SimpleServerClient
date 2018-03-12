package de.sciesla.demo.snake.client.frame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public abstract class Frame extends JFrame {

	private static final long serialVersionUID = -1930862773226161150L;

	public abstract void onInit();

	public abstract void onUpdate(float dt);

	public abstract void onRender(Graphics g, float deltaTime2);

	private int fps = 0;
	private int maxFps = 64;
	private float deltaTime = 0f;
	private long tick = 0l;

	private Canvas canvas;

	private static Frame instance;

	public Frame(String title) {

		instance = this;

		setTitle(title);
		setSize(1000, 1, 1);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		canvas = new Canvas();
		add(canvas);
		setVisible(true);

		canvas.createBufferStrategy(2);

		init();
	}

	private void setSize(int width, int rw, int rh) {

		setBounds(new Rectangle(width, (int) (width / ((float) rw / (float) rh))));
		setLocationRelativeTo(null);
	}

	private void init() {

		onInit();

		new Thread() {
			@Override
			public void run() {

				BufferStrategy bufferStrategy = canvas.getBufferStrategy();

				long lastTime = System.nanoTime();
				Graphics g = bufferStrategy.getDrawGraphics();
				while (true) {

					g.setColor(Color.BLACK);
					g.fillRect(0, 0, getWidth(), getHeight());

					onUpdate(deltaTime);
					onRender(g, deltaTime);

					bufferStrategy.show();

					tick++;
					deltaTime = (int) (System.nanoTime() - lastTime) / 1000000;
					if (deltaTime < (float) (1000 / maxFps)) {
						try {
							Thread.sleep((long) ((float) (1000 / maxFps) - deltaTime));
							deltaTime = (int) (System.nanoTime() - lastTime) / 1000000;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					fps = (int) (1000 / deltaTime);
					lastTime = System.nanoTime();
				}
			}
		}.start();

	}

	public static Frame getInstance() {
		return instance;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	@Override
	public int getWidth() {
		return getInstance().getCanvas().getWidth() - 1;
	}

	@Override
	public int getHeight() {
		return getInstance().getCanvas().getHeight() - 1;
	}

	public void setName(String tile) {
		getInstance().setTitle(tile);
	}

	/**
	 * @return the maxFps
	 */
	public int getMaxFps() {
		return maxFps;
	}

	/**
	 * @param maxFps
	 *            the maxFps to set
	 */
	public void setMaxFps(int maxFps) {
		this.maxFps = maxFps;
	}

	/**
	 * @return the fps
	 */
	public int getFps() {
		return fps;
	}

	/**
	 * @return the deltaTime
	 */
	public float getDeltaTime() {
		return deltaTime;
	}

	/**
	 * @return the tick
	 */
	public long getTick() {
		return tick;
	}
}
