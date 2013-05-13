package com.paypal.services;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paypal.donation.utils.Constants;
import com.paypal.sdk.core.nvp.NVPDecoder;
import com.paypal.sdk.core.nvp.NVPEncoder;
import com.paypal.sdk.services.NVPCallerServices;

public final class AuthenticateService {

	private final NVPCallerServices caller = new NVPCallerServices();
	private static final Log LOG = LogFactory.getLog(AuthenticateService.class);
	
	private AuthenticateService() {

	}

	public NVPCallerServices getCaller() {
		return caller;
	}

	private static AuthenticateService authSvcSingleton = new AuthenticateService();

	public static AuthenticateService getInstance() {
		return authSvcSingleton;
	}

	public String doSetAuthFlowParamCall(final Map<String, String> ecData)
			throws Exception {
		LOG.debug("Start API Call - SetAuthFlowParam");
		final NVPEncoder encoder = new NVPEncoder();
		final NVPDecoder decoder = new NVPDecoder();
		// caller = new NVPCallerServices();
		caller.setAPIProfile(PayPalProfile.getAPIProfile());
		encoder.add(Constants.VERSION, "63.0");
		encoder.add(Constants.METHOD, "SetAuthFlowParam");
		encoder.add(Constants.RETURNURL, ecData.get(Constants.RETURNURL));
		encoder.add(Constants.CANCELURL, ecData.get(Constants.CANCELURL));
		encoder.add(Constants.LOGOUTURL, ecData.get(Constants.LOGOUTURL));
		encoder.add("SERVICENAME1", "Name");
		encoder.add("SERVICEDEFREQ1", "Required");
		encoder.add("SERVICENAME2", "Email");
		encoder.add("SERVICEDEFREQ2", "Required");
		encoder.add(Constants.HDRIMG, ecData.get(Constants.HDRIMG));
		encoder.add(Constants.HDRBORDERCOLOR, ecData.get(Constants.HDRBORDERCOLOR));
		final String NVPRequest = encoder.encode();
		final String NVPResponse = caller.call(NVPRequest);
		LOG.debug("Response on SetAuthFlowParam \n\n" + NVPResponse);
		decoder.decode(NVPResponse);
		LOG.debug("End API Call - SetAuthFlowParam");
		return NVPResponse;
	}

	public String doGetAuthDetails(final Map<String, String> ecData) throws Exception {
		final NVPEncoder encoder = new NVPEncoder();
		final NVPDecoder decoder = new NVPDecoder();
		caller.setAPIProfile(PayPalProfile.getAPIProfile());
		encoder.add(Constants.VERSION, "63.0");
		encoder.add(Constants.METHOD, "GetAuthDetails");
		encoder.add(Constants.RETURNURL, ecData.get(Constants.RETURNURL));
		encoder.add(Constants.CANCELURL, ecData.get(Constants.CANCELURL));
		encoder.add(Constants.TOKEN, ecData.get(Constants.TOKEN));
		final String NVPRequest = encoder.encode();
		final String NVPResponse = caller.call(NVPRequest);
		LOG.debug("Response on GetAuthDetails \n\n" + NVPResponse);
		decoder.decode(NVPResponse);		
		LOG.debug("End API Call - GetAuthDetails");
		return NVPResponse;
	}
}
