package de.sciesla.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import de.sciesla.annotations.Authorized;
import de.sciesla.datapackage.DataPackage;
import de.sciesla.datapackage.MessageDataPackage;
import de.sciesla.encoding.AESEncoding;
import de.sciesla.sender.Sender;
import de.sciesla.sender.SenderType;

public abstract class Client {

	private static Client instance;

	private String host;
	private int port;

	private boolean authenticated = false;

	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	private ClientState clientState;
	private AESEncoding aesEncoding;

	public Client(String host, int port) {

		Client.instance = this;

		this.host = host;
		this.port = port;

		init();
	}

	public abstract void onConnected();

	public abstract void onDisconnected();

	public abstract String onPasswordRequired();

	public abstract void onAuthenticated();

	private void init() {
		clientState = ClientState.DISCONNECTED;

		this.aesEncoding = new AESEncoding();
	}

	public void connect() {
		if (clientState != ClientState.DISCONNECTED) {
			System.out.println("Client ist bereits verbunden!");
			return;
		}

		new Thread() {
			@Override
			public void run() {

				try {
					socket = new Socket(host, port);
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out = new PrintWriter(socket.getOutputStream(), true);

					new Thread() {
						@Override
						public void run() {
							onConnected();
						}
					}.start();

					String line;
					while ((line = in.readLine()) != null) {
						DataPackage dataPackage = DataPackage.fromString(line, getAesEncoding());
						if (dataPackage != null) // ERRORhandling server verlassen?
							dataPackage.onClient(new Sender(SenderType.SERVER));
					}

				} catch (IOException e) {

				} finally {
					try {
						socket.close();
						socket = null;
						System.out.println("Verbindung verloren!");
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (NullPointerException e2) {
						System.out.println("Konnte keine Verbindung zum Server herstellen!");
					}

					new Thread() {
						@Override
						public void run() {
							onDisconnected();
						}
					}.start();
				}
			}
		}.start();

	}

	public void sendDataPackage(DataPackage datapackage) {
		
		if ((datapackage.getClass().isAnnotationPresent(Authorized.class)) && (!isAuthenticated())) {
			return;
		}
		
		if (this.out != null) {
			this.out.println(datapackage.toString(getAesEncoding()));
		}
	}
	
	public void sendMessage(String message) {
		sendDataPackage(new MessageDataPackage(message));
	}

	/**
	 * @return the authentificated
	 */
	public boolean isAuthenticated() {
		return authenticated;
	}

	/**
	 * @param authenticated the authentificated to set
	 */
	public void setAuthenticated(boolean authentificated) {
		this.authenticated = authentificated;
	}

	public AESEncoding getAesEncoding() {
		return this.aesEncoding;
	}

	public static Client getInstance() {
		return instance;
	}
}
