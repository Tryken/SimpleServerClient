package de.sciesla.server.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

import de.sciesla.annotations.Authorized;
import de.sciesla.datapackage.AuthenticatedPackage;
import de.sciesla.datapackage.DataPackage;
import de.sciesla.datapackage.EncryptionDataPackage;
import de.sciesla.datapackage.MessageDataPackage;
import de.sciesla.encoding.AESEncoding;
import de.sciesla.encoding.RSAEncoding;
import de.sciesla.sender.Sender;
import de.sciesla.sender.SenderType;
import de.sciesla.server.Server;
import de.sciesla.server.logger.LogType;
import de.sciesla.server.logger.Logger;

public class Connection extends Thread {

	private Socket socket;
	private String userName;
	private UUID uuid;
	private boolean authenticated;

	private BufferedReader in;
	private PrintWriter out;

	private RSAEncoding rsaEncoding;
	private AESEncoding aesEncoding;

	public Connection(Socket socket, boolean authenticated) {
		this.socket = socket;
		this.userName = "User-" + String.format("%04d", (int) (Math.random() * 9999));
		this.uuid = UUID.randomUUID();
		this.authenticated = authenticated;

		this.rsaEncoding = new RSAEncoding();
		this.rsaEncoding.generateKeyPair();

		this.aesEncoding = new AESEncoding();

		Logger.log(LogType.INFO, userName + socket.getInetAddress().toString() + " versucht sich zu verbinden!");
	}

	@Override
	public void run() {

		try {

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			Server server = Server.getInstance();
			if (server.isConnectionLimitReached()) {
				server.removeConnection(this, "Kick: The Server is full!");
				return;
			}

			server.sendDataPackage(this, new EncryptionDataPackage(getRsaEncoding().getPublicKey(), 256));

			String line;
			while ((line = in.readLine()) != null) {
				DataPackage dataPackage = DataPackage.fromString(line, getAesEncoding());
				if (dataPackage != null) // ERRORhandling kick?
					dataPackage.onServer(new Sender(this, SenderType.CLIENT));
			}

		} catch (IOException e) {
			// e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}

			Logger.log(LogType.INFO, userName + " hat den Server verlassen!");

			if (authenticated) {
				Server.getInstance()
						.broadcastDataPackage(new MessageDataPackage(userName + " hat den Server verlassen!"));
			}

			Server.getInstance().removeConnection(this);
		}
	}

	public void sendDataPackage(DataPackage datapackage) {
	    if ((datapackage.getClass().isAnnotationPresent(Authorized.class)) && 
	    	      (!isAuthenticated())) {
	    	      return;
	    	    }
	    	    if (this.out != null) {
	    	      this.out.println(datapackage.toString(getAesEncoding()));
	    	    }
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * @return the authentificated
	 */
	public boolean isAuthenticated() {
		return authenticated;
	}

	/**
	 * @param authentificated
	 *            the authentificated to set
	 */
	public void setAuthenticated(boolean authenticated) {

		this.authenticated = authenticated;

		if (authenticated) {
			Server server = Server.getInstance();
			sendDataPackage(new AuthenticatedPackage());
			Logger.log(LogType.INFO, userName + " hat den Server betreten!");
			server.broadcastDataPackage(new MessageDataPackage(userName + " hat den Server betreten!"));
		}
	}

	public RSAEncoding getRsaEncoding() {
		return this.rsaEncoding;
	}

	public AESEncoding getAesEncoding() {
		return this.aesEncoding;
	}
}
