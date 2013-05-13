package com.paypal.donation.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.paypal.donation.models.Currency;

final public class ApplicationProperties {

	private static final java.util.Properties APP_PROPERTIES = new Properties();
	private static List<Currency> currencyList = null;
	private static Map<Integer,Currency> currencyMap = null;
	
	private ApplicationProperties() {
	}
	
	public static Properties getAppProperties() {
		return APP_PROPERTIES;
	}
	
	public static final String getProperty(String key) {
		return (APP_PROPERTIES.getProperty(key) != null ? APP_PROPERTIES.getProperty(key).trim() : "");
	}
	
	public static void setCurrencyList (List<Currency> cList) {
		currencyList = cList;
		currencyMap = new HashMap<Integer,Currency> ();
		if(currencyList != null) {
			for(Currency currency: currencyList) {
				currencyMap.put(currency.getId(), currency);
			}
		}
	}
	
	public static List<Currency> getCurrencyList () {
		return currencyList;
	}
	
	public static Currency findCurrencyById (final int currencyId) {
		return currencyMap.get(currencyId);
	}
	
}
