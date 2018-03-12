package de.sciesla.encoding;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESEncoding {
	private SecretKey sk = null;

	public void generateSecretKey(int size) {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			SecureRandom rand = new SecureRandom();
			keyGenerator.init(size, rand);
			this.sk = keyGenerator.generateKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public String encrypt(String arg) {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(1, this.sk);
			byte[] chiffrat = cipher.doFinal(arg.getBytes("ISO-8859-1"));
			return new String(Base64.getEncoder().encode(chiffrat), "ISO-8859-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String decrypt(String arg) {
		Cipher cipher = null;
		try {
			arg = new String(Base64.getDecoder().decode(arg), "ISO-8859-1");
			cipher = Cipher.getInstance("AES");
			cipher.init(2, this.sk);
			byte[] chiffrat = cipher.doFinal(arg.getBytes("ISO-8859-1"));
			return new String(chiffrat, "ISO-8859-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getSecretKey() {
		try {
			return new String(Base64.getEncoder().encode(this.sk.getEncoded()), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setSecretKey(String sk) {
		byte[] decodedKey = Base64.getDecoder().decode(sk);
		this.sk = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
	}
}
