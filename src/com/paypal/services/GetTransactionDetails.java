/*
 * Copyright 2005, 2008 PayPal, Inc. All Rights Reserved.
 *
 * GetTransactionDetails NVP example; last modified 08MAY23. 
 *
 * Get detailed information about a single transaction.  
 */
package com.paypal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paypal.donation.utils.Constants;
import com.paypal.sdk.core.nvp.NVPEncoder;
import com.paypal.sdk.services.NVPCallerServices;

/**
 * PayPal Java SDK sample code
 */
public final class GetTransactionDetails {

	private static GetTransactionDetails singleton = new GetTransactionDetails();
	private static final Log LOG = LogFactory.getLog(GetTransactionDetails.class);

	private GetTransactionDetails() { }

	public static GetTransactionDetails getInstance() {
		return singleton;
	}

	public String getTransactionDetails(String transactionId, String email)
			throws Exception {

		LOG.debug("Start API Call - GetTransactionDetails");
		NVPEncoder encoder = new NVPEncoder();
		final NVPCallerServices caller = new NVPCallerServices();
		caller.setAPIProfile(PayPalProfile.getAPIProfile(email));
		encoder.add(Constants.VERSION, "63.0");
		encoder.add(Constants.METHOD, "GetTransactionDetails");
		encoder.add(Constants.TRANSACTIONID, transactionId);
		String nvpRequest = encoder.encode();
		LOG.debug(nvpRequest);
		String nvpResponse = caller.call(nvpRequest);
		LOG.debug(nvpResponse);
		LOG.info("End API Call - GetTransactionDetails");
		return nvpResponse;
	}
}
