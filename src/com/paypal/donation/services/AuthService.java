package com.paypal.donation.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paypal.donation.models.Location;
import com.paypal.donation.models.LocationHome;
import com.paypal.donation.models.User;
import com.paypal.donation.models.UserHome;
import com.paypal.donation.utils.Constants;
import com.paypal.donation.utils.Utility;
import com.paypal.sdk.core.nvp.NVPDecoder;
import com.paypal.services.AuthenticateService;
import com.paypal.services.PermissionService;

/**
 * @author hjodiawalla
 */
public class AuthService {
	
	private static final Log LOG = LogFactory.getLog(AuthService.class);
	
	private static AuthService singletonObj = new AuthService();

	public static AuthService getInstance() {
		return singletonObj;
	}

	
	// Method to authenticate the user
	public void doAuthenticate(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		LOG.debug("Starting AuthService.doAuthenticate()");
		NVPDecoder resp = new NVPDecoder();
		Map<String, String> ecData = new HashMap<String, String>();		
		StringBuilder returnUrl = new StringBuilder();
		StringBuilder cancelUrl = new StringBuilder();
		StringBuilder logoutUrl = new StringBuilder();
		final String serverUrl = Utility.getServerUrl().toString();
		try {
			returnUrl.append(Utility.getServerUrl()).append("/ac?method=authSuccess");
			cancelUrl.append(Utility.getServerUrl()).append("/ac?method=authCancel");
			logoutUrl.append(serverUrl).append("/ac?method=authLogoutSuccess");
			
			ecData.put(Constants.RETURNURL, returnUrl.toString());
			ecData.put(Constants.CANCELURL, cancelUrl.toString());
			ecData.put(Constants.LOGOUTURL, logoutUrl.toString());
			ecData.put(Constants.HDRIMG, serverUrl + "/assets/images/labslogo.png");
			ecData.put(Constants.HDRBORDERCOLOR, "#ffffff");
			
			resp.decode(AuthenticateService.getInstance().doSetAuthFlowParamCall(ecData));
			
			if ("Success".equals(resp.get(Constants.ACK))) {
				response.sendRedirect(ApplicationProperties.getProperty(Constants.AUTH_LOGIN_URL)
						+ resp.get(Constants.TOKEN));
			} else {
				LOG.error("Error -- AuthenticateService.doSetAuthFlowParamCall()");
				final String errorCode = resp.get("L_ERRORCODE0").toString();
				final String errorLongMsg = resp.get("L_LONGMESSAGE0").toString();
				LOG.error(errorCode + "|" + errorLongMsg);
				response.sendRedirect(Utility.buildReturnUrl(request).toString());
			}
		} catch (Exception e) {
			LOG.info("Error -- AuthService.doAuthenticate()");
			throw e;
		} finally {
			resp = null;
			ecData = null;
			returnUrl = null;
			cancelUrl = null;
			logoutUrl = null;
		}
		LOG.debug("Ending AuthService.doAuthenticate()");
	}
	
	// Method to the user details, once authenticated
	public void getAuthDetails(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		LOG.debug("Starting AuthService.doGetAuthDetails()");
		NVPDecoder decoder = new NVPDecoder();
		Location loc = new Location();
		Map<String, String> ecData = new HashMap<String, String>();
		StringBuilder returnUrl = new StringBuilder();
		StringBuilder cancelUrl = new StringBuilder();
		final String serverUrl = Utility.getServerUrl();
		
		try {
			final String token = request.getParameter("token");
			returnUrl.append(serverUrl).append("/dashboard");
			cancelUrl.append(serverUrl);
			
			ecData.put(Constants.RETURNURL, returnUrl.toString());
			ecData.put(Constants.CANCELURL, cancelUrl.toString());
			ecData.put(Constants.TOKEN, token);
			
			decoder.decode(AuthenticateService.getInstance().doGetAuthDetails(ecData));
			User user = UserService.getInstance().findUserByEmail(decoder.get(Constants.EMAIL));
			
			// User already exists
			if (user != null) {
				user.setUpdatedDt(new Date());
				UserHome.getInstance().merge(user);
			} else {
				user = new User();
				Date now = new Date();
				user.setFirstName(decoder.get("FIRSTNAME"));
				user.setLastName(decoder.get("LASTNAME"));
				user.setEmailId(decoder.get(Constants.EMAIL));
				user.setUpdatedDt(now);
				user.setCreatedDt(now);
				user.setPayerId(decoder.get(Constants.PAYERID));
				user.setBuild(decoder.get("BUILD"));
				user.setCorrelationId(decoder.get("CORRELATIONID"));
				user.setVersion(decoder.get("VERSION"));
				user.setGrantedPermission('N');
				UserService.getInstance().upsertUser(user);
			}
			
			HttpSession session = request.getSession();
			session.setAttribute("FNAME", decoder.get("FIRSTNAME"));
			session.setAttribute("PAYER_ID", decoder.get(Constants.PAYERID));
			session.setAttribute("PAYER_EMAIL_ID", decoder.get(Constants.EMAIL));
			session.setAttribute(Constants.TOKEN, token);
			
			// Update the location table
			loc.setCountry(request.getLocale().getDisplayCountry());
			loc.setHost(request.getRemoteHost());
			loc.setIpaddress(request.getRemoteAddr());
			loc.setLocale(request.getLocale().toString());
			loc.setLanguage(request.getLocale().getDisplayLanguage());
			loc.setPayerid(decoder.get(Constants.PAYERID));
			LocationHome.getInstance().persist(loc);
			
			//TODO: update permission api
			if (user.getGrantedPermission() == 'N') {
				ecData.clear();
				StringBuilder callbackUrl = new StringBuilder();
				callbackUrl.append(Utility.getServerUrl()).
					append("/ac?method=permissionSuccess&payerId=").append(user.getPayerId());
				ecData.put(Constants.CALLBACK, callbackUrl.toString());
				ecData.put(Constants.REQENVELOPEERRORLANG, ApplicationProperties.getProperty("REQENVELOPEERRORLANG"));
				ecData.put(Constants.SCOPE0, ApplicationProperties.getProperty("SCOPE0"));
				ecData.put(Constants.SCOPE1, ApplicationProperties.getProperty("SCOPE1"));
				decoder.decode(PermissionService.getInstance().requestPermission(ecData));
				
				if("SUCCESS".equalsIgnoreCase(decoder.get("responseEnvelope.ack"))){
					String redirectURL = ApplicationProperties.getProperty("PERMISSION_URL") + decoder.get("token");
					LOG.debug(redirectURL);
					response.sendRedirect(redirectURL);
				}else{					
					//LOG.error(getDecodedString(decoder));
					LOG.error("Error -- PermissionService.requestPermission()");
					final String errorCode = decoder.get("L_ERRORCODE0").toString();
					final String errorLongMsg = decoder.get("L_LONGMESSAGE0").toString();
					LOG.error(errorCode + "|" + errorLongMsg);
				}
			} else {
				response.sendRedirect(serverUrl + "/dashboard");
			}
		} catch (Exception exc) {
			//String error = ApplicationProperties.getProperty("PPAPI_ERROR");
			LOG.error("Error in AuthService.doGetAuthDetails()");
			LOG.error(Utility.getStackTrace(exc));
		} finally {
			loc = null;
			ecData = null;
			returnUrl = null;
			cancelUrl = null;
			decoder = null;		
		}
		LOG.debug("Ending AuthService.doGetAuthDetails()");
	}
	
	// Logging out the user. Removing session information
	public void doLogout(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		LOG.debug("Starting AuthService.doLogout()");
		String action = null;
		HttpSession session = null;
		try {
			action = request.getParameter("method");
			session = request.getSession(false);
			if (session == null || "authLogoutSuccess".equals(action)) {
				response.sendRedirect(Utility.getServerUrl());
				return;
			}
			final String token = (String) session.getAttribute(Constants.TOKEN);
			if (token != null) {
				response.sendRedirect(ApplicationProperties
						.getProperty(Constants.AUTH_LOGOUT_URL) + token);
			}
		} catch (Exception exc) {
			LOG.error("Error in AuthService.doLogout()");
			throw exc;
		} finally {
			if (session != null && "logout".equals(action)) {
				session.invalidate();
			}
		}
		LOG.debug("Ending AuthService.doLogout()");
	}

	// If Cancel button pressed. User does not login. 
	public void doAuthCancel(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		LOG.debug("Starting AuthService.doAuthCancel()");
		StringBuilder returnURL = new StringBuilder();
		returnURL.append(Utility.getServerUrl());
		response.sendRedirect(returnURL.toString());
		LOG.debug(returnURL.toString());
		LOG.debug("Ending AuthService.doAuthCancel()");
	}

	public static void getPermissionToken(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		LOG.debug("Starting AuthService.doGetAccessToken()");
		Map<String, String> ecData = new HashMap<String, String>();
		try {
			NVPDecoder decoder = new NVPDecoder();
			String payerId = request.getParameter("payerId");
			ecData.put(Constants.REQENVELOPEERRORLANG, ApplicationProperties.getProperty("REQENVELOPEERRORLANG"));
			ecData.put(Constants.TOKEN, request.getParameter("request_token"));
			ecData.put(Constants.VERIFIER, request.getParameter("verification_code"));
			String resp = PermissionService.getInstance().getAccessToken(ecData);
			decoder.decode(resp);
			
			LOG.debug(Utility.getDecodedString(decoder));		
			PermissionService.getInstance().getAuthHeader(decoder.get("token"), decoder.get("tokenSecret"));
			
			if(null != decoder.get("scope(0)") || decoder.get("scope(0)") != ""){
				User user = UserService.getInstance().findUserByPayerId(payerId);
				if(user != null) {
					user.setGrantedPermission('Y');
					UserHome.getInstance().merge(user);
				}
			}
		} catch (Exception e){
			LOG.error(Utility.getStackTrace(e));
		} finally {
			response.sendRedirect(Utility.getServerUrl() + "/dashboard" );
		}		
		LOG.debug("Ending AuthService.doGetAccessToken()");
	}
}
