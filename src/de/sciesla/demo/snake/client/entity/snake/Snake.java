package de.sciesla.demo.snake.client.entity.snake;

import java.awt.Color;
import java.awt.Graphics;

import de.sciesla.demo.snake.client.direction.Direction;
import de.sciesla.demo.snake.client.entity.Entity;

public class Snake extends Entity {

	private SnakePart mainpart;

	private Color color;
	private Direction direction;

	private int speed = 5;
	private int count = 0;

	public Snake(Direction direction, int posX, int posY, int length, Color color) {

		this.color = color;
		this.direction = direction;

		this.mainpart = new SnakePart(posX, posY);

		SnakePart nextPart = null;

		for (int l = length; l > 0; l--) {

			int pX = posX;
			int pY = posY;

			switch (direction) {

			case RIGHT:
				pX -= l;
				break;
			case LEFT:
				pX += l;
				break;
			case UP:
				pY += l;
				break;
			case DOWN:
				pY -= l;
				break;
			case NONE:
				break;
			default:
				break;
			}

			SnakePart tempPart = new SnakePart(pX, pY);
			if (nextPart == null) {
				nextPart = tempPart;
			} else {
				tempPart.setNextpart(nextPart);
				nextPart = tempPart;
			}
		}

		this.mainpart.setNextpart(nextPart);
	}

	@Override
	public void onInit() {

	}

	@Override
	public void onUpdate(float dt) {

		count += dt;
		if (count >= 1000 / speed) {

			count = 1000 - count;
			mainpart.move(getDirection(), false);
		}
	}

	@Override
	public void onRender(Graphics g, float dt) {

		g.setColor(this.color);
		mainpart.onRender(g, dt);
	}

	/**
	 * @return the mainpart
	 */
	public SnakePart getMainpart() {
		return mainpart;
	}

	/**
	 * @param mainpart
	 *            the mainpart to set
	 */
	public void setMainpart(SnakePart mainpart) {
		this.mainpart = mainpart;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return the direction
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
