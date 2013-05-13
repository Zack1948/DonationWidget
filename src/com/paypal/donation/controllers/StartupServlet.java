package com.paypal.donation.controllers;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paypal.donation.models.CurrencyHome;
import com.paypal.donation.services.ApplicationProperties;
import com.paypal.donation.utils.Utility;
import com.paypal.services.PayPalProfile;

/**
 * Servlet implementation class StartupServlet
 */

public class StartupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	private static final Log LOGGER = LogFactory
			.getLog(StartupServlet.class);
	
	public void init(ServletConfig config) throws ServletException {
		LOGGER.debug("Loading properties in Startup Servlet");
		
		// Loading application.properties
		try {
			ApplicationProperties.getAppProperties().load(StartupServlet.class.getClassLoader().getResourceAsStream("application.properties"));
			PayPalProfile.getAPIProperties().load(StartupServlet.class.getClassLoader().getResourceAsStream("paypalAPI.properties"));
			PayPalProfile.setAPICredentials();
			PayPalProfile.setAPIProfile();
		} catch (IOException exc) {
			LOGGER.error(Utility.getStackTrace(exc));
		} catch (Exception exc) {
			LOGGER.error(Utility.getStackTrace(exc));
		}
		
		// Loading Currencies 
		try {
			ApplicationProperties.setCurrencyList(CurrencyHome.getInstance().getAllCurrencies());
		} catch (Exception exc) {
			LOGGER.error(Utility.getStackTrace(exc));
		}
		LOGGER.debug("End of Startup Servlet");
	}
}
