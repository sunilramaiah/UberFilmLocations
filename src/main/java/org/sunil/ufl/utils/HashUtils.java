package org.sunil.ufl.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This code is used from stackoverflow.
 * 
 * @author sunil
 *
 */
public class HashUtils {
	
	/**
	 * This will generate a SHA1 hash of the given string.
	 * 
	 * @param input
	 * @return String
	 * @throws NoSuchAlgorithmException
	 */
	public static String generateHash(String input)
            throws NoSuchAlgorithmException
    {
		String key = "SHA1";
		MessageDigest md = MessageDigest.getInstance(key);
		md.reset();
		byte[] buffer = input.getBytes();
		md.update(buffer);
		byte[] digest = md.digest();
		
		String hexStr = "";
		for (int i = 0; i < digest.length; i++) {
		    hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return hexStr;
    }	
	
}
