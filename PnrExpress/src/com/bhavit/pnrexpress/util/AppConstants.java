package com.bhavit.pnrexpress.util;

import java.util.HashMap;

public class AppConstants {

	public static HashMap<String, String> quotas = new HashMap<String, String>();
	
	public static HashMap<String, String> getQuotas() {
		
		quotas.put("TATKAL", "CK");
		quotas.put("GENERAL", "GN");
		
		return quotas;
	}
	
	
	public static String getQuotaValue(String key){

		return quotas.get(key);
		
	}
	
}
