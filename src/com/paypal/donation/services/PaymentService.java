package com.paypal.donation.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paypal.donation.exceptions.CustomAPIException;
import com.paypal.donation.models.Currency;
import com.paypal.donation.models.Event;
import com.paypal.donation.models.EventHome;
import com.paypal.donation.models.RenderLocation;
import com.paypal.donation.models.RenderLocationHome;
import com.paypal.donation.models.Transaction;
import com.paypal.donation.models.TransactionHome;
import com.paypal.donation.models.User;
import com.paypal.donation.models.Widgetui;
import com.paypal.donation.utils.Constants;
import com.paypal.donation.utils.Utility;
import com.paypal.sdk.core.nvp.NVPDecoder;
import com.paypal.services.DoExpressCheckout;
import com.paypal.services.GetExpressCheckout;
import com.paypal.services.GetTransactionDetails;
import com.paypal.services.SetExpressCheckout;

public class PaymentService {
	
	private static final Log LOG = LogFactory.getLog(PaymentService.class);
	private static final Log APILOG = LogFactory.getLog("apiErrors");
	
	private static PaymentService singletonObj = new PaymentService();

	public static PaymentService getInstance() {
		return singletonObj;
	}

	public void doPay(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		LOG.debug("Starting PaymentService.doPay()");
		Map<String, String> data = new HashMap<String, String>();
		
		final String amount = request.getParameter("amountsList");
		final String widgetExternalId = request.getParameter("widgetId");
		//final String action = request.getParameter("method");
		Widgetui widgetui = null;
		User user = null;
		Currency currency = null;
		String serverUrl = Utility.getServerUrl();
		
		try {
			widgetui = WidgetService.getInstance().findWidgetuiByExternalId(widgetExternalId);
			currency = ApplicationProperties.findCurrencyById(widgetui.getGoalcurrency());
			user = UserService.getInstance().findUserByPayerId(widgetui.getPayerid());
			
			data.put(Constants.CURRENCYCODE, currency.getUnits());
			data.put(Constants.IPN_URL, serverUrl + "/ac?method=ipnMsg");
			data.put(Constants.RECEIVEREMAIL, user.getEmailId());
			
			// Update the render_location table with payment history
			RenderLocation renderLoc = new RenderLocation();
			renderLoc.setCountry(request.getLocale().getDisplayCountry());
			renderLoc.setHost(request.getRemoteHost());
			renderLoc.setIpaddress(request.getRemoteAddr());
			renderLoc.setLocale(request.getLocale().toString());
			renderLoc.setLanguage(request.getLocale().getDisplayLanguage());
			renderLoc.setAmount(new Double(amount));
			renderLoc.setDonateflag('Y');
			renderLoc.setPayerid(widgetui.getPayerid());
			renderLoc.setCreatedDt(new Date());
			renderLoc.setWidgetexternalid(widgetExternalId);
			RenderLocationHome.getInstance().persist(renderLoc);
			
			data.put(Constants.NOTE, ApplicationProperties.getProperty("PAYMENT_MEMO")+" "+widgetui.getTitle());
			NVPDecoder respDecoder = new NVPDecoder();
			data.put(Constants.AMT, amount);
			StringBuilder strBuf = new StringBuilder();
			strBuf.append(serverUrl).append("/ac?method=paymentSuccess&widgetId=");
			strBuf.append(widgetExternalId);
			strBuf.append("&payerId=");
			strBuf.append(widgetui.getPayerid());
			strBuf.append('&');
			strBuf.append(Constants.APP_RETURNURL);
			strBuf.append('=');
			if(widgetui.getWeburl() != null && widgetui.getWeburl().trim().length() > 0) {
				strBuf.append(widgetui.getWeburl());
			} else {
				strBuf.append(ApplicationProperties.getProperty(Constants.DEFAULT_AFTER_DONATION_RETURN_PAGE));
			}
			data.put(Constants.RETURNURL, strBuf.toString());
			data.put(Constants.CANCELURL, data.get(Constants.RETURNURL));
			respDecoder.decode(SetExpressCheckout.getInstance().setExpressCheckoutCall(data));
			
			if ("Success".equals(respDecoder.get(Constants.ACK))) {
				response.sendRedirect(ApplicationProperties.getProperty(Constants.MEC_URL)
						+ respDecoder.get(Constants.TOKEN).toString());
			} else {
				// Display a user friendly Error on the page using any of
				// the following error information returned by PayPal
				final String errorCode = respDecoder.get("L_ERRORCODE0").toString();
				final String errorLongMsg = respDecoder.get("L_LONGMESSAGE0").toString();
				LOG.error(errorCode + "|" + errorLongMsg);
				response.sendRedirect(Utility.getServerUrl()+"/error");
			}
		} catch (Exception exc) {
			LOG.error("Error - PaymentService.doPay()");
			LOG.error(Utility.getStackTrace(exc));
			throw new CustomAPIException();
		} finally {
			data = null;
			widgetui = null;
			user = null;
			currency = null;
		}
		LOG.debug("Ending PaymentService.doPay()");
	}

	public void getPaymentDetails(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		LOG.debug("Starting PaymentService.doGetPaymentDetails()");
		NVPDecoder decoder = new NVPDecoder();
		NVPDecoder transactionDecoder = new NVPDecoder();
		Transaction transactionData = new Transaction();
		final String payerId = request.getParameter("payerId");
		final String widgetId = request.getParameter("widgetId");
		final String token = request.getParameter("token");
		Map<String, String> ecData = null;
		try {
			User user = UserService.getInstance().findUserByPayerId(payerId);
			decoder.decode(GetExpressCheckout.getInstance().getExpressCheckoutCall(token, user.getEmailId()));
			
			if ("Success".equals(decoder.get(Constants.ACK))) {
				transactionData.setEmailid(decoder.get(Constants.EMAIL));
				// PayerId of the Receiver.
				transactionData.setPayerid(payerId);
				ecData = new HashMap<String, String>();
				ecData.put(Constants.TOKEN, token);
				ecData.put(Constants.PAYERID, decoder.get(Constants.PAYERID));
				ecData.put(Constants.AMT, decoder.get(Constants.AMT));
				ecData.put(Constants.RECEIVEREMAIL, user.getEmailId());
				ecData.put(Constants.CURRENCYCODE, decoder.get(Constants.CURRENCYCODE));
				ecData.put(Constants.IPN_URL, Utility.getServerUrl() + "/ac?method=ipnMsg");
				
				decoder.decode(DoExpressCheckout.getInstance().doExpressCheckoutCall(ecData));
				
				if ("Success".equals(decoder.get(Constants.ACK))) {
					transactionDecoder.decode(GetTransactionDetails.getInstance().getTransactionDetails(decoder
							.get(Constants.TRANSACTIONID), user.getEmailId()));
					
					final Date now = new Date();
					transactionData.setAmount(new Double(transactionDecoder.get(Constants.AMT)));
					transactionData.setCreatedDt(now);
					transactionData.setUpdatedDt(now);
					transactionData.setPaypaltransid(transactionDecoder.get(Constants.TRANSACTIONID));
					transactionData.setStatus(transactionDecoder.get(Constants.PAYMENTSTATUS));
					transactionData.setTransresponse(Utility.getDecodedString(transactionDecoder));
					transactionData.setWidgetexternalid(widgetId);
					transactionData.setTransactiontype("EC");
					TransactionHome.getInstance().persist(transactionData);
				} else {
					APILOG.error("Error in PaymentService.doGetPaymentDetails()");
					APILOG.error(Utility.getDecodedString(decoder));
				}
			} else {
				APILOG.error("Error in PaymentService.doGetPaymentDetails()");
				APILOG.error(Utility.getDecodedString(decoder));
			}
			if (request.getParameter(Constants.APP_RETURNURL) != null) {
				response.sendRedirect(request.getParameter(Constants.APP_RETURNURL));
			} else {
				response.sendRedirect(ApplicationProperties.getProperty(Constants.DEFAULT_AFTER_DONATION_RETURN_PAGE));
			}
			LOG.debug("Ending PaymentService.doGetPaymentDetails()");
		} catch (Exception exc) {
			APILOG.error("Error in PaymentService.doGetPaymentDetails()");
			APILOG.error(Utility.getDecodedString(decoder));
			APILOG.error(Utility.getStackTrace(exc));
			throw new CustomAPIException();
		} finally {
			decoder = null;
			transactionData = null;
			ecData = null;
		}
	}

	public static void getIPN(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
	NVPDecoder decoder = new NVPDecoder();
	try {
		Enumeration<String> en = request.getParameterNames();
		StringBuilder ipnMsg = new StringBuilder();
		ipnMsg.append("cmd=_notify-validate");
		while (en.hasMoreElements()) {
			String paramName = (String) en.nextElement();
			String paramValue = request.getParameter(paramName);
			if("method".equals(paramName)){
				continue;
			}
			ipnMsg.append('&').append(paramName).append('=').append(URLEncoder.encode(paramValue, "UTF-8"));
		}
		LOG.debug("Resending IPN message to paypal - " +ipnMsg.toString());
		// post back to PayPal system to validate
		// NOTE: change http: to https: in the following URL to verify using
		// SSL (for increased security).
		// using HTTPS requires either Java 1.4 or greater, or Java Secure
		// Socket Extension (JSSE)
		// and configured for older versions.
		URL u = new URL(ApplicationProperties.getProperty(Constants.IPN_VALIDATE_URL));
		URLConnection uc = u.openConnection();
		uc.setDoOutput(true);
		uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		PrintWriter pw = new PrintWriter(uc.getOutputStream());
		pw.println(ipnMsg.toString());
		pw.close();
		BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		String res = in.readLine();
		in.close();
		LOG.debug("IPN response from paypal - " + res);
		decoder.decode(ipnMsg.toString());
		
		// check notification validation
		if (res.equals("VERIFIED")) {
			Transaction transaction = null;
			final Date now = new Date();				
			final Event eventData = new Event();
			boolean expressCheckoutTransaction = false; 
			String status = decoder.get(ApplicationProperties.getProperty(Constants.IPN_AP_STATUS));
			if (status == null){
				status = decoder.get(ApplicationProperties.getProperty(Constants.IPN_EC_STATUS));
				expressCheckoutTransaction = true;
			}
			eventData.setResponse(Utility.getDecodedString(decoder));
			eventData.setCreatedDt(now);
			eventData.setUpdatedDt(now);
			eventData.setEventType("IPN");
			eventData.setStatus(status);
			EventHome.getInstance().persist(eventData);
			String transactionId = null;
			String senderEmail = null;
			String amount = null;
			String transactionType = null;
			
			if (expressCheckoutTransaction) {
				//Express Checkout
				if ("Refunded".equals(status)){
					transactionId = decoder.get(ApplicationProperties.getProperty(Constants.IPN_EC_REFUND_TXNID));
				} else {
					transactionId = decoder.get(ApplicationProperties.getProperty(Constants.IPN_EC_TXNID));
				}
				senderEmail = decoder.get(ApplicationProperties.getProperty(Constants.IPN_EC_SENDEREMAIL));
				transactionType = "EC";
				amount = decoder.get(ApplicationProperties.getProperty(Constants.IPN_EC_TXNAMT));					
				if(amount == null)
					amount = "0" ;					
			} /*else {
				//Adaptive Payments - Amount is concatenated with Units. Ex:USD 5.00
				transactionId = decoder.get(ApplicationProperties.getProperty(Constants.IPN_AP_TXNID));					
				senderEmail = decoder.get(ApplicationProperties.getProperty(Constants.IPN_AP_SENDEREMAIL));
				transactionType = "AP";
				if ("Refunded".equals(status) || "Partially_Refunded".equals(status)) {
					amount = decoder.get(ApplicationProperties
							.getProperty(Constants.IPN_AP_TXN_REFUND_AMT));
				} else {
					amount = decoder.get(ApplicationProperties
							.getProperty(Constants.IPN_AP_TXNAMT));
				}
				if (amount != null) {
					String[] amountArray = amount.split(" ");
					if(amountArray.length > 1) {
						amount = amountArray[1];
					} 
				} else {
					amount = "0";
				}					
			}*/
			if (transactionId != null || "".equals(transactionId)) {
				transaction = TransactionHome.getInstance().findByTransactionId(transactionId);
				if(transaction == null){
					LOG.error("Transaction not found -"+ Utility.getDecodedString(decoder));
				} else if ("Refunded".equals(status) || "Partially_Refunded".equals(status) || (!Constants.COMPLETED.equalsIgnoreCase(transaction
								.getStatus()) && Constants.COMPLETED.equalsIgnoreCase(status))) {
					//Insert record in transaction table when there is a refund or transaction status change from Pending to Completed.
					Transaction refTransaction = new Transaction();
					refTransaction.setAmount(new Double(amount));
					refTransaction.setCreatedDt(now);
					refTransaction.setUpdatedDt(now);
					refTransaction.setEmailid(senderEmail);
					refTransaction.setPayerid(transaction.getPayerid());
					refTransaction.setPaypaltransid(transactionId);
					refTransaction.setTransresponse(Utility.getDecodedString(decoder));
					refTransaction.setStatus(status);
					refTransaction.setTransactiontype(transactionType);
					refTransaction.setWidgetexternalid(transaction.getWidgetexternalid());	
					TransactionHome.getInstance().persist(refTransaction);
				} 
			}
		} else if (res.equals("INVALID")) {
			LOG.error("Invalid IPN Received -"+ Utility.getDecodedString(decoder));
		} else {
			LOG.error("Error in IPN Status -"+ Utility.getDecodedString(decoder));
		}

	} catch (Exception exc) {
		APILOG.error("Error in doIPNNotificationMsg");
		APILOG.error(Utility.getDecodedString(decoder));
		APILOG.error(Utility.getStackTrace(exc));
		throw new CustomAPIException();
	} finally {
		decoder = null;
	}

}
}
