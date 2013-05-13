package com.paypal.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paypal.donation.utils.Constants;
import com.paypal.donation.utils.Utility;
import com.paypal.platform.sdk.exception.FatalException;
import com.paypal.sdk.profiles.APIProfile;
import com.paypal.sdk.profiles.ProfileFactory;
import com.paypal.sdk.profiles.SignatureAPIProfile;

public final class PayPalProfile {
	private static final Log LOG = LogFactory.getLog(PayPalProfile.class);
	private static Map<String, String> credentials;
	private static final java.util.Properties ppAPIProperties = new Properties();
	private static APIProfile profile = null;

	private PayPalProfile() {

	}

	public static final Properties getAPIProperties() {
		return ppAPIProperties;
	}
	
	public static void setAPICredentials() throws Exception {
		try {
			credentials = new HashMap<String, String>();
			credentials.put(Constants.X_PAYPAL_SECURITY_USERID,	ppAPIProperties.getProperty(Constants.X_PAYPAL_SECURITY_USERID));
			credentials.put(Constants.X_PAYPAL_SECURITY_PASSWORD, ppAPIProperties.getProperty(Constants.X_PAYPAL_SECURITY_PASSWORD));
			credentials.put(Constants.X_PAYPAL_SECURITY_SIGNATURE, ppAPIProperties.getProperty(Constants.X_PAYPAL_SECURITY_SIGNATURE));
			credentials.put(Constants.X_PAYPAL_APPLICATION_ID, ppAPIProperties.getProperty(Constants.X_PAYPAL_APPLICATION_ID));
			// credentials.put("X-PAYPAL-DEVICE-IPADDRESS", "10.244.173.201");
			credentials.put(Constants.API_BASE_ENDPOINT, ppAPIProperties.getProperty(Constants.API_BASE_ENDPOINT));
			//credentials.put("TRUST_ALL_CONNECTION", "true");
			//credentials.put(Constants.X_PAYPAL_SERVICE_VERSION, ppAPIProperties.getProperty(Constants.X_PAYPAL_SERVICE_VERSION));
			//credentials.put("JSSE_PROVIDER", "SunJSSE");
			credentials.put(Constants.X_PAYPAL_REQUEST_DATA_FORMAT, ppAPIProperties.getProperty(Constants.X_PAYPAL_REQUEST_DATA_FORMAT));
			credentials.put(Constants.X_PAYPAL_RESPONSE_DATA_FORMAT, ppAPIProperties.getProperty(Constants.X_PAYPAL_RESPONSE_DATA_FORMAT));
		} catch (Exception e) {
			LOG.error(Utility.getStackTrace(e));
			throw e;
		}
	}

	public static void setAPIProfile() throws Exception {
		LOG.debug("Start function - getAPIProfile");
		try {
			profile = new SignatureAPIProfile();
			profile = ProfileFactory.createSignatureAPIProfile();
			profile.setAPIUsername(ppAPIProperties.getProperty(Constants.API_USER_NAME));
			profile.setAPIPassword(ppAPIProperties.getProperty(Constants.API_PASSWORD));
			profile.setSignature(ppAPIProperties.getProperty(Constants.API_SIGNATURE));
			profile.setEnvironment(ppAPIProperties.getProperty(Constants.API_ENVIRONMENT));
		} catch (Exception e) {
			LOG.error(Utility.getStackTrace(e));
			throw e;
		}
		LOG.debug("End function - getAPIProfile");
	}
	
	public static APIProfile getAPIProfile() throws Exception {
		profile.setSubject("");
		return profile;
	}
	
	public static APIProfile getAPIProfile(String email) throws Exception {
		profile.setSubject(email);
		return profile;
	}

	public static Map<String, String> getAPICredentials() throws Exception {
		return credentials;
	}
	
	public static String getEndPointUrl(final String apiName) throws Exception {
		LOG.debug("Start function - getEndPointUrl");
		if (credentials.containsKey(Constants.API_BASE_ENDPOINT)) {
			final String endPoint = (String) credentials.get(
					Constants.API_BASE_ENDPOINT);
			if (endPoint == null || endPoint.length() == 0) {
				throw new FatalException("End point value is blank.");
			}
			LOG.debug("End function - getEndPointUrl");
			return endPoint + apiName;
		} else {
			LOG.debug("End function - getEndPointUrl");
			throw new FatalException("End point is not provided.");
		}
	}
}
