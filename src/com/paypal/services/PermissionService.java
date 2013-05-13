package com.paypal.services;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paypal.donation.utils.Constants;
import com.paypal.donation.utils.Utility;
import com.paypal.platform.sdk.core.CallerServices;
import com.paypal.platform.sdk.core.Decoder;
import com.paypal.platform.sdk.core.Encoder;
import com.paypal.platform.sdk.core.SerializeFactory;
import com.paypal.sdk.util.OAuthSignature;
import com.paypal.sdk.util.OAuthSignature.HTTPMethod;

public class PermissionService {
  
	private static Log LOG = LogFactory.getLog(PermissionService.class);
	private static Map<String, String> credentials = null;
	private static PermissionService psSingleton = new PermissionService();

	private PermissionService() {
		try {
			credentials = PayPalProfile.getAPICredentials();
		} catch (Exception e) {
			LOG.error(Utility.getStackTrace(e));
		}
	}
	
	public static PermissionService getInstance() {
		return psSingleton;
	}

	public String requestPermission(final Map<String, String> apData) throws Exception{
		
		LOG.debug("Starting PermissionService.requestPermission()");
		Encoder encoder= SerializeFactory.getInstance().getEncoder();
		Decoder decoder= SerializeFactory.getInstance().getDecoder();
		String apiName = "/Permissions/RequestPermissions";
		String response="";		
		encoder.add(Constants.REQENVELOPEERRORLANG, apData.get(Constants.REQENVELOPEERRORLANG));
		encoder.add(Constants.CALLBACK, apData.get(Constants.CALLBACK));
		encoder.add(Constants.SCOPE0, apData.get(Constants.SCOPE0));
		encoder.add(Constants.SCOPE1, apData.get(Constants.SCOPE1));
		String request = encoder.encode();
		response = CallerServices.call(request, PayPalProfile.getEndPointUrl(apiName), credentials);
		decoder.decode(response);
		LOG.debug("Ending PermissionService.requestPermission()");
		return response;
	}

	public String getAccessToken(final Map<String, String> apData) throws Exception{
		
		LOG.debug("Starting PermissionService.getAccessToken()");
		Encoder encoder= SerializeFactory.getInstance().getEncoder();
		String apiName = "/Permissions/GetAccessToken";
		String response = "";
		encoder.add(Constants.REQENVELOPEERRORLANG, apData.get(Constants.REQENVELOPEERRORLANG));			
		encoder.add("token", apData.get(Constants.TOKEN));
		encoder.add("verifier", apData.get(Constants.VERIFIER));
		Decoder decoder= SerializeFactory.getInstance().getDecoder();
		String request = encoder.encode();
		response = CallerServices.call(request, PayPalProfile.getEndPointUrl(apiName), credentials);
		decoder.decode(response);
		LOG.debug(decoder);
		LOG.debug("Ending PermissionService.getAccessToken()");
		return response;
	}
	
	@SuppressWarnings("rawtypes")
	public void getAuthHeader(String token, String tokenSecret) throws Exception{
		HTTPMethod httpMethod = OAuthSignature.HTTPMethod.POST; 
		String scriptURI = "https://api.sandbox.paypal.com/nvp"; 
		Map queryParams = null; 
		Map map = OAuthSignature.getAuthHeader(credentials.get(Constants.X_PAYPAL_SECURITY_USERID), credentials.get(Constants.X_PAYPAL_SECURITY_PASSWORD), 
				token,tokenSecret, httpMethod, scriptURI, queryParams); 
		Iterator itr = map.entrySet().iterator(); 
		while(itr.hasNext()){ 
			Map.Entry entry = (Map.Entry)itr.next(); 
			LOG.debug(entry.getKey() + ": " + entry.getValue()); 
		}
	}
}
