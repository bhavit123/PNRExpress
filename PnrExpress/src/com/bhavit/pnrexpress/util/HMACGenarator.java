package com.bhavit.pnrexpress.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMACGenarator {

	
	public static String getSignature(String privateKey, String param){
		String mykey = privateKey;
		String test = param;	
		String response = "";
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec secret = new SecretKeySpec(mykey.getBytes(),
					"HmacSHA1");
			mac.init(secret);
			byte[] digest = mac.doFinal(test.getBytes());
			for (byte b : digest) {
				response = response + String.format("%02x", b);
				//System.out.format("%02x", b);
			}
			System.out.println();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return response;
	}
} 