package de.sveh.simpleserverclient.demo.snake.client.entity.snake;

import java.awt.Graphics;

import de.sveh.simpleserverclient.demo.snake.client.direction.Direction;
import de.sveh.simpleserverclient.demo.snake.client.frame.Frame;

public class SnakePart {

	private int posX;
	private int posY;

	private SnakePart nextpart;

	public SnakePart(int posX, int posY) {

		this.posX = posX;
		this.posY = posY;
	}

	public void onRender(Graphics g, float dt) {

		g.fillRect(posX * 10, posY * 10, 10, 10);

		if (getNextpart() != null)
			getNextpart().onRender(g, dt);
	}

	public void move(Direction direction, boolean createNewPart) {

		if (getNextpart() != null) {
			
			if (getNextpart().getNextpart() == null && createNewPart) {
				
				getNextpart().setNextpart(new SnakePart(getNextpart().getPosX(), getNextpart().getPosY()));
				getNextpart().setPosX(getPosX());
				getNextpart().setPosY(getPosY());
			} else {
				
				getNextpart().move(Direction.NONE, createNewPart);
				getNextpart().setPosX(getPosX());
				getNextpart().setPosY(getPosY());
			}

		}

		switch (direction) {

		case RIGHT:
			setPosX(getPosX() + 1);
			break;
		case LEFT:
			setPosX(getPosX() - 1);
			break;
		case UP:
			setPosY(getPosY() - 1);
			break;
		case DOWN:
			setPosY(getPosY() + 1);
			break;
		case NONE:
			break;
		default:
			break;
		}

		if (getPosX() * 10 + 10 > Frame.getInstance().getWidth())
			setPosX(0);
	}

	/**
	 * @return the posX
	 */
	public int getPosX() {
		return posX;
	}

	/**
	 * @param posX
	 *            the posX to set
	 */
	public void setPosX(int posX) {
		this.posX = posX;
	}

	/**
	 * @return the posY
	 */
	public int getPosY() {
		return posY;
	}

	/**
	 * @param posY
	 *            the posY to set
	 */
	public void setPosY(int posY) {
		this.posY = posY;
	}

	/**
	 * @return the nextpart
	 */
	public SnakePart getNextpart() {
		return nextpart;
	}

	/**
	 * @param nextpart
	 *            the nextpart to set
	 */
	public void setNextpart(SnakePart nextpart) {
		this.nextpart = nextpart;
	}
}
