package de.sveh.simpleserverclient.encoding;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAEncoding {
    private KeyPair keyPair = null;

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom rand = new SecureRandom();
            keyPairGenerator.initialize(1024, rand);
            this.keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String arg, PublicKey pk) {
        Cipher cipher;

        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(1, pk);
            byte[] bytes = cipher.doFinal(arg.getBytes("ISO-8859-1"));
            return new String(Base64.getEncoder().encode(bytes), "ISO-8859-1");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String decrypt(String arg) {
        return decrypt(arg, getKeyPair().getPrivate());
    }

    public static String decrypt(String arg, PrivateKey sk) {
        Cipher cipher;

        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(2, sk);
            byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(arg));
            return new String(bytes, "ISO-8859-1");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KeyPair getKeyPair() {
        return this.keyPair;
    }

    public String getPublicKey() {
        try {
            return new String(Base64.getEncoder().encode(getKeyPair().getPublic().getEncoded()), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
