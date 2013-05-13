package com.paypal.donation.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public final class SHA1Hash {

	private static final Logger LOGGER = Logger.getLogger(SHA1Hash.class.getName());

	private static String convertToHex(final byte[] data) {
		final StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	public static String SHA1(final String text) {
		String toReturn = null;
		try {
			final MessageDigest msgDigest =  MessageDigest.getInstance("SHA-1");
			byte[] sha1hash = new byte[40];
			msgDigest.update(text.getBytes("iso-8859-1"), 0, text.length());
			sha1hash = msgDigest.digest();
			toReturn = convertToHex(sha1hash);
		} catch (NoSuchAlgorithmException ne) {
			LOGGER.fine(ne.getMessage());
		} catch (UnsupportedEncodingException ue) {
			LOGGER.fine(ue.getMessage());
		}
		return toReturn;
	}
	
	public static String SHA1_32(final String text) {
		String toReturn = null;
		try {
			final MessageDigest msgDigest =  MessageDigest.getInstance("MD5");
			byte[] sha1hash = new byte[32];
			msgDigest.update(text.getBytes("iso-8859-1"), 0, text.length());
			sha1hash = msgDigest.digest();
			toReturn = convertToHex(sha1hash);
		} catch (NoSuchAlgorithmException ne) {
			LOGGER.fine(ne.getMessage());
		} catch (UnsupportedEncodingException ue) {
			LOGGER.fine(ue.getMessage());
		}
		return toReturn;
	}
}
