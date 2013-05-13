package com.paypal.services;

/*
 * Copyright 2005, 2008 PayPal, Inc. All Rights Reserved.
 *
 * SetExpressCheckout NVP example; last modified 08MAY23. 
 *
 * Initiate an Express Checkout transaction.  
 */

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paypal.donation.utils.Constants;
import com.paypal.sdk.core.nvp.NVPEncoder;
import com.paypal.sdk.services.NVPCallerServices;

/**
 * PayPal Java SDK sample code
 */
public final class SetExpressCheckout {

	private static SetExpressCheckout setECSingleton = new SetExpressCheckout();
	private static final Log LOG = LogFactory.getLog(SetExpressCheckout.class);

	private SetExpressCheckout() {

	}

	public static SetExpressCheckout getInstance() {
		return setECSingleton;
	}

	public String setExpressCheckoutCall(final Map<String, String> ecData)
			throws Exception {
		LOG.debug("Start API Call - SetExpressCheckout");
		final NVPEncoder encoder = new NVPEncoder();
		final NVPCallerServices caller = new NVPCallerServices();
		caller.setAPIProfile(PayPalProfile.getAPIProfile(ecData.get(Constants.RECEIVEREMAIL)));
		encoder.add(Constants.METHOD, "SetExpressCheckout");
		// Add request-specific fields to the request string.
		encoder.add(Constants.RETURNURL, ecData.get(Constants.RETURNURL));
		encoder.add(Constants.CANCELURL, ecData.get(Constants.CANCELURL));
		encoder.add(Constants.CURRENCYCODE, ecData.get(Constants.CURRENCYCODE));
		encoder.add(Constants.AMT, ecData.get(Constants.AMT));
		encoder.add(Constants.NOSHIPPING, "1");
		encoder.add("NOTETEXT", ecData.get(Constants.NOTE));
		// 1 - Buyer enters the NOTE; 0 - Buyer does not see the NOTE
		encoder.add(Constants.ALLOWNOTE, "1");
		encoder.add(Constants.NOTIFYURL, ecData.get(Constants.IPN_URL));
		// Execute the API operation and obtain the response.
		final String NVPRequest = encoder.encode();
		final String nvpResponse = caller.call(NVPRequest);
		LOG.info(nvpResponse);
		LOG.debug("End API Call - SetExpressCheckout");
		return nvpResponse;
	}
}
