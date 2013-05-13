/*
 * Copyright 2005, 2008 PayPal, Inc. All Rights Reserved.
 *
 * DoExpressCheckoutPayment NVP example; last modified 08MAY23. 
 *
 * Complete an Express Checkout transaction.  
 */
package com.paypal.services;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paypal.donation.utils.Constants;
import com.paypal.sdk.core.nvp.NVPEncoder;
import com.paypal.sdk.services.NVPCallerServices;

/**
 * PayPal Java SDK sample code
 */
public final class DoExpressCheckout {
	private static DoExpressCheckout doECsingleton = new DoExpressCheckout();
	private static final Log LOG = LogFactory.getLog(DoExpressCheckout.class);

	private DoExpressCheckout() {

	}

	public static DoExpressCheckout getInstance() {
		return doECsingleton;
	}

	private final NVPCallerServices caller = new NVPCallerServices();

	public String doExpressCheckoutCall(final Map<String, String> ecData)
			throws Exception {
		LOG.debug("Start API Call - DoExpressCheckoutPayment");
		final NVPEncoder encoder = new NVPEncoder();
		caller.setAPIProfile(PayPalProfile.getAPIProfile(ecData.get(Constants.RECEIVEREMAIL)));
		//encoder.add(Constants.VERSION, "78");
		encoder.add(Constants.METHOD, "DoExpressCheckoutPayment");
		encoder.add(Constants.TOKEN, ecData.get(Constants.TOKEN));
		encoder.add(Constants.PAYERID, ecData.get(Constants.PAYERID));
		encoder.add(Constants.AMT, ecData.get(Constants.AMT));
		encoder.add(Constants.PAYMENTACTION, "Sale");
		encoder.add(Constants.CURRENCYCODE, ecData.get(Constants.CURRENCYCODE));
		encoder.add(Constants.NOTIFYURL, ecData.get(Constants.IPN_URL));
		// Execute the API operation and obtain the response.
		final String NVPRequest = encoder.encode();
		final String nvpResponse = caller.call(NVPRequest);
		LOG.debug("response - " + nvpResponse);
		LOG.debug("End API Call - DoExpressCheckoutPayment");
		return nvpResponse;
	}
}
