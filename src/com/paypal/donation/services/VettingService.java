package com.paypal.donation.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.paypal.donation.models.Widget;
import com.paypal.donation.utils.Constants;
import com.paypal.donation.utils.SHA1Hash;
import com.paypal.donation.utils.Utility;

public class VettingService {
	
	private static final Log LOG = LogFactory.getLog(VettingService.class);
	
	/**
	 * Method to read the vetting response
	 * @param request - ServletRequest
	 * @return - Map with Key-Value pair of vetting response attributes
	 */
	public static Map<String, String> readVettingResponse(HttpServletRequest request) {
		
		LOG.debug("Starting VettingService.readVettingResponse()");
		Map<String, String> vettingResponseMap = new HashMap<String, String> ();
		String responseJSONOutput = null;
		StringBuilder responseBuilder = new StringBuilder();
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
			while ((responseJSONOutput = bufferedReader.readLine()) != null) {
				responseBuilder.append(responseJSONOutput);
			}
			responseJSONOutput = responseBuilder.toString();
			LOG.debug("Vetting Response from stream:"+responseJSONOutput);
			if(responseJSONOutput != null && responseJSONOutput.length() > 0) {
				JSONObject obj = (JSONObject)JSONSerializer.toJSON(responseJSONOutput);
				vettingResponseMap.put(Constants.VETTING_RESPONSE_RESPONSE, obj.getString(Constants.VETTING_RESPONSE_RESPONSE));
				vettingResponseMap.put(Constants.VETTING_RESPONSE_APPLICATION_ID, obj.getString(Constants.VETTING_RESPONSE_APPLICATION_ID));
				vettingResponseMap.put(Constants.VETTING_RESPONSE_REQUEST_ID, obj.getString(Constants.VETTING_RESPONSE_REQUEST_ID));
			}
		} catch (Exception exc) {
			LOG.error("Vetting Response:"+responseJSONOutput);
			LOG.error(Utility.getStackTrace(exc));
		}
		LOG.debug("Ending VettingService.readVettingResponse()");
		return vettingResponseMap;
	}
	
	/**
	 * Method to send Vetting Request
	 * @param vettingDetails - Map containing the vetting details
	 * @return - Returns "1", if request was sent successfully, otherwise, returns "0".
	 */
	public static String sendVettingRequest(Widget widget, String payerEmailId) {

		LOG.debug("Starting VettingService.sendVettingRequest()");
		LOG.debug("Input parameters for sendVettingRequest: "+widget);
		
		String requestValidity = "0";
		String responseJSONOutput = null;
		
		if ( widget != null && widget.getEin() != null &&
			widget.getEin().trim().length() > 0 &&
			widget.getOrgName() != null &&
			widget.getOrgName().trim().length() > 0) {
			
			int timeOut = 10000;
			try {
				timeOut = Integer.parseInt(ApplicationProperties.getProperty(Constants.VETTING_REQUEST_TIME_OUT));
			} catch (NumberFormatException exc) {
			}
			
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(timeOut));
			httpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(timeOut));
			
			HttpPost httpPost = new HttpPost(ApplicationProperties.getProperty(Constants.VETTING_REQUEST_URL));
			String jsonVettingRequest = "";
			List<NameValuePair> nameValuePairsList = new ArrayList<NameValuePair> ();
			Map<String, String> inputMap = new HashMap<String, String> ();
			String vettingRequestId = SHA1Hash.SHA1_32("Vetting:"+System.currentTimeMillis());
			// Setting Vetting Request Id to the widget to udpate in DB
			widget.setVettingRequestId(vettingRequestId); 
			
			inputMap.put(Constants.VETTING_REQUEST_APPLICATION_ID, ApplicationProperties.getProperty(Constants.VETTING_REQUEST_APPLICATION_ID_VALUE));
			inputMap.put(Constants.VETTING_REQUEST_REQUEST_ID, vettingRequestId);
			//response url
			inputMap.put(Constants.VETTING_REQUEST_RESPONSE_URL, Utility.getServerUrl()+"/"
					+ ApplicationProperties.getProperty(Constants.VETTING_REQUEST_RESPONSE_URL_VALUE));
			inputMap.put(Constants.VETTING_REQUEST_EIN, widget.getEin());
			inputMap.put(Constants.VETTING_REQUEST_NAME, widget.getOrgName());
			inputMap.put(Constants.VETTING_REQUEST_EMAIL, payerEmailId);
			inputMap.put(Constants.VETTING_REQUEST_REQUEST_ORIGIN, ApplicationProperties.getProperty(Constants.VETTING_REQUEST_REQUEST_ORIGIN_VALUE));
			
			JSON json = JSONSerializer.toJSON(inputMap);
			jsonVettingRequest = json.toString();
			nameValuePairsList.add(new BasicNameValuePair(ApplicationProperties.getProperty(Constants.VETTING_REQUEST_KEY), jsonVettingRequest));
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairsList));
				HttpResponse httpResponse = httpClient.execute(httpPost);
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
						httpResponse.getEntity().getContent()));
				while ((responseJSONOutput = bufferedReader.readLine()) != null) {
					break;
				}
				if(responseJSONOutput != null) {
					JSONObject obj = (JSONObject)JSONSerializer.toJSON(responseJSONOutput);
					requestValidity = obj.getString(ApplicationProperties.getProperty(Constants.VETTING_REQUEST_SUCCESS_KEY));
				}
			} catch (Exception exc) {
				LOG.error("Vetting Request Response: " + responseJSONOutput);
				LOG.error(Utility.getStackTrace(exc));
			}
		}
		LOG.debug("Ending VettingService.sendVettingRequest()");
		return requestValidity;
	}
}
