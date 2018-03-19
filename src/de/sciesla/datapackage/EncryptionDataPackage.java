package de.sciesla.datapackage;

import de.sciesla.client.Client;
import de.sciesla.encoding.RSAEncoding;
import de.sciesla.sender.Sender;
import de.sciesla.server.Server;
import de.sciesla.server.connection.Connection;
import de.sciesla.server.logger.LogType;
import de.sciesla.server.logger.Logger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class EncryptionDataPackage extends DataPackage {
	
	public EncryptionDataPackage(String key) {
		
		super(key);
	}

	public EncryptionDataPackage(String key, int size) {
		
		super(key, String.valueOf(size));
	}

	public void onServer(Sender sender) {
		
		if (getLength() != 1) {
			return;
		}
		
		Connection connection = sender.getConnection();
		String key = connection.getRsaEncoding().decrypt(getString(0));
		connection.getAesEncoding().setSecretKey(key);
		
		if (connection.isAuthenticated()) {
			connection.setAuthenticated(true);
		} else {
			
			connection.sendDataPackage(new AuthenticationPackage());
		}
	}

	public void onClient(Sender sender) {
		
		if (getLength() != 2) {
			
			return;
		}
		
		Client client = Client.getInstance();
		client.getAesEncoding().generateSecretKey(getInt(1));
		
		try {
			
			byte[] decodedKey = Base64.getDecoder().decode(getString(0));
			X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			RSAPublicKey generatePublic = (RSAPublicKey) kf.generatePublic(spec);
			String key = client.getAesEncoding().getSecretKey();
			String encryptedkey = RSAEncoding.encrypt(key, generatePublic);
			sender.sendDataPackage(new EncryptionDataPackage(encryptedkey));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		}
	}
}
