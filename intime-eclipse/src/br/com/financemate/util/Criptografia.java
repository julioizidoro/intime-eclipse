package br.com.financemate.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

/**
 *
 * @author Wolverine
 */
public class Criptografia {
    
    private static MessageDigest messageDigest;
    private static BASE64Encoder encoder;

	public static String encript(String value) throws NoSuchAlgorithmException{
		if (messageDigest == null || messageDigest.getAlgorithm() != "SHA-256") {
			messageDigest = MessageDigest.getInstance("SHA-256");
		}

		if (encoder == null) {
			encoder = new BASE64Encoder();
		}
		
	    byte[] hash = messageDigest.digest(value.getBytes());
	    return encoder.encode(hash);
	}

    
}