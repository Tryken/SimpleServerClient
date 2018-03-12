package de.sciesla.demo.snake;

import javax.swing.JOptionPane;

import de.sciesla.demo.snake.client.Game;
import de.sciesla.demo.snake.server.SnakeServer;

public class Main {

	public static void main(String[] args) {

		Object[] options = { "Server", "Client" };
		int n = JOptionPane.showOptionDialog(null, "Type:", "Pong", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

		if (n == -1)
			System.exit(0);

		if (n == 1)
			new Game();
		
		if (n == 0)
			new SnakeServer().start();
	}
}
