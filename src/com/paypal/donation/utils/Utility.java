package com.paypal.donation.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.paypal.donation.models.Currency;
import com.paypal.donation.services.ApplicationProperties;
import com.paypal.sdk.core.nvp.NVPDecoder;

public class Utility {
	private static final Log LOG = LogFactory.getLog(Utility.class);	
	
	public static String getServerUrl() {
		return ApplicationProperties.getProperty(Constants.APP_URL);
	}
	
	public static String getStackTrace(Exception exc) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exc.printStackTrace(pw);
		return sw.toString();
	}
	
	public static void writeMessageToResponse(HttpServletResponse response, String message){
		try {
			response.setContentType("text/plain");
			response.getWriter().write("Error:"+message);
			response.flushBuffer();
		} catch (IOException exc) {
			LOG.error(getStackTrace(exc));
		}
	}
	
	public static void writeSuccessMessageToResponse(HttpServletResponse response, String message){
		try {
			response.setContentType("text/plain");
			response.getWriter().write("Success:-"+message);
			response.flushBuffer();
		} catch (IOException exc) {
			LOG.error(getStackTrace(exc));
		}
	}
	
	public static String getValidCurrencySymbol(Currency currency) {
		if(currency != null) {
			if(currency.getSymbol() != null) {
				if ("EUR".equalsIgnoreCase(currency.getUnits())) {
					return Constants.EURO_HTML_NOTATION;
				} else if ("PHP".equalsIgnoreCase(currency.getUnits())) {
					return Constants.PHP_HTML_NOTATION;
				} else if ("THB".equalsIgnoreCase(currency.getUnits())) {
					return Constants.THB_HTML_NOTATION;
				} else {
					return String.valueOf(currency.getSymbol());
				}
				
			} else return "";
		} 
		return "$";
	}
	
	/**
	 * Method to shorten the widget URL - used for Twitter/Facebook
	 * @param longUrl - String containing URL to be shortened
	 * @return - Returns the short URL String
	 * @throws Exception 
	 */
	public static String shortURL(String longUrl) throws Exception{
		LOG.debug("Starting shortURL");
		String shortUrl = null;
		String inputUrl = ApplicationProperties.getProperty(Constants.SHORTEN_URL)+URLEncoder.encode(longUrl.trim(), "UTF-8");
		LOG.debug("Shortening api call:"+inputUrl);
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(inputUrl);
		try {
			HttpResponse response = client.execute(get);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			
			String line = "";
			while ((line = rd.readLine()) != null) {
				break;
			}
			LOG.debug("Shortening api response:"+line);
			// Sample response for accessible URL
			// { "status_code": 200, "status_txt": "OK", "data": { "long_url": "http:\/\/www.google.com\/", "url": "http:\/\/bit.ly\/z3fudr", "hash": "z3fudr", "global_hash": "2V6CFi", "new_hash": 0 } }
			// Sample response for non accessible URL
			//{ "data": [ ], "status_code": 500, "status_txt": "INVALID_URI" }
			JSONObject obj = (JSONObject)JSONSerializer.toJSON(line);
			if(obj.get("data") instanceof JSONArray) {
				LOG.error("Error in shortening the url:"+longUrl+" with response from api:"+line);
			} 
			if(obj.get("data") instanceof JSONObject) {
				shortUrl = obj.getJSONObject("data").getString("url");
			}
		} catch (Exception exc) {
			throw exc;
		} finally {
			LOG.debug("Shortened URL for:"+longUrl+" is:"+shortUrl);
		}
		LOG.debug("End of shortURL");
		return shortUrl;
	}
	
/*	public static boolean isMobileRequest(HttpServletRequest request) {
		boolean mobileRequest = false; 
		final String userAgent = request.getHeader(Constants.USERAGENT_HEADER).toLowerCase();
		final String[] userAgentKeywords = ApplicationProperties.getProperty(Constants.USERAGENT).split(",");
		for(String keyword : userAgentKeywords) {
			if(userAgent.contains(keyword)) {
				mobileRequest = true;
				break;
			}
		}
		return mobileRequest;
	}*/
	
	public static String getDecodedString(NVPDecoder decoder) throws Exception {

		@SuppressWarnings("unchecked")
		final HashMap<String, String> apiResponseMap = 
			(HashMap<String, String>) decoder.getMap();
		
		StringBuilder apiResponseBuffer = new StringBuilder();
		final int mapSize = apiResponseMap.size();
		int bufferSize = 0;
		for (String keyStr : apiResponseMap.keySet()) {
			bufferSize++;
			apiResponseBuffer.append(keyStr).append('=').append(apiResponseMap.get(keyStr));
			if (bufferSize == mapSize)
				break;
			apiResponseBuffer.append('&');
		}
		return apiResponseBuffer.toString();
	}
	
	public static synchronized long getTrackingId() {
		return System.currentTimeMillis();
	}
	
	public static StringBuilder buildReturnUrl(HttpServletRequest request) {
		StringBuilder redirectUrl = new StringBuilder();
		try {
			redirectUrl.append(Utility.getServerUrl());
		} catch (Exception e) {
			LOG.error(Utility.getStackTrace(e));
		}
		redirectUrl.append("/?errorCode=101");
		return redirectUrl;
	}
}
