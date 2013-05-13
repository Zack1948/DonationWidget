package com.paypal.services;

/*
 * Copyright 2005, 2008 PayPal, Inc. All Rights Reserved.
 *
 * GetExpressCheckoutDetails NVP example; last modified 08MAY23. 
 *
 * Get information about an Express Checkout transaction.  
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paypal.donation.utils.Constants;
import com.paypal.sdk.core.nvp.NVPEncoder;
import com.paypal.sdk.services.NVPCallerServices;

public final class GetExpressCheckout {
	private static GetExpressCheckout getECsingleton = new GetExpressCheckout();
	private static final Log LOG = LogFactory.getLog(GetExpressCheckout.class);

	private GetExpressCheckout() {

	}

	public static GetExpressCheckout getInstance() {
		return getECsingleton;
	}

	public String getExpressCheckoutCall(final String token, String email) throws Exception {
		LOG.debug("Start API Call - GetExpressCheckout");
		final NVPCallerServices caller = new NVPCallerServices();
		final NVPEncoder encoder = new NVPEncoder();
		caller.setAPIProfile(PayPalProfile.getAPIProfile(email));
		encoder.add(Constants.VERSION, "51.0");
		encoder.add(Constants.METHOD, "GetExpressCheckoutDetails");
		encoder.add(Constants.TOKEN, token);
		// Execute the API operation and obtain the response.
		final String nvpRequest = encoder.encode();
		final String nvpResponse = caller.call(nvpRequest);
		LOG.debug("End API Call - GetExpressCheckout");
		return nvpResponse;
	}

}
