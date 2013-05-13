package com.paypal.donation.controllers;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paypal.donation.models.Widget;
import com.paypal.donation.models.WidgetHome;
import com.paypal.donation.services.ApplicationProperties;
import com.paypal.donation.services.VettingService;
import com.paypal.donation.utils.Constants;
import com.paypal.donation.utils.Utility;

/**
 * Servlet implementation class VettingController
 */
public class VettingController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(VettingController.class);

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			handleRequest(request, response);
		} catch (Exception exc) {
			LOG.error(Utility.getStackTrace(exc));
		}
	}

	protected void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LOG.debug("In handleRequest - START handling Vetting Response");
		Map<String, String> vettingResponseMap = null;
		vettingResponseMap = VettingService.readVettingResponse(request);
		LOG.debug("Response to the Vetting Controller:"+vettingResponseMap);
		final String vettingRequestId = vettingResponseMap.get(Constants.VETTING_RESPONSE_REQUEST_ID);
		final String vettingAppId = vettingResponseMap.get(Constants.VETTING_RESPONSE_APPLICATION_ID);
		final String vettingResponse = vettingResponseMap.get(Constants.VETTING_RESPONSE_RESPONSE);
		
		if(vettingAppId != null && ApplicationProperties.getProperty(Constants.VETTING_REQUEST_APPLICATION_ID_VALUE).equals(vettingAppId)) {
			WidgetHome widgetHome = WidgetHome.getInstance();
			Widget widget = widgetHome.findByVettingRequestId(vettingRequestId);
			if(widget != null) {
				char einVerified = 'N';
				if((vettingResponse.equals(ApplicationProperties.getProperty(Constants.VETTING_APPROVED)))) {
					einVerified = ApplicationProperties.getProperty(Constants.VETTING_APPROVED_CHAR).charAt(0);
				} else if (vettingResponse.equals(ApplicationProperties.getProperty(Constants.VETTING_DENIED))) {
					einVerified = ApplicationProperties.getProperty(Constants.VETTING_DENIED_CHAR).charAt(0);
				} 
				/*else if (vettingResponse.equals(ApplicationProperties.getProperty(Constants.VETTING_OPEN))) {
					einVerified = ApplicationProperties.getProperty(Constants.VETTING_OPEN_CHAR).charAt(0);
				} else if (vettingResponse.equals(ApplicationProperties.getProperty(Constants.VETTING_REOPENED))) {
					einVerified = ApplicationProperties.getProperty(Constants.VETTING_REOPENED_CHAR).charAt(0);
				} */
				if(einVerified != 'N') {
					widget.setEinVerified(einVerified);
					widgetHome.merge(widget);
				}
				response.setContentType("text/plain");
				response.getWriter().write("{\"r\":1}");
				response.flushBuffer();
			}
		}
		LOG.debug("In handleRequest - END handling Vetting Response");
	}
}
