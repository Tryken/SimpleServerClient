package de.sciesla.demo.snake.client.entity;

import java.awt.Graphics;

public abstract class Entity {

	public Entity() {
		this.onInit();
	}

	public abstract void onInit();

	public abstract void onUpdate(float dt);

	public abstract void onRender(Graphics g, float dt);
}