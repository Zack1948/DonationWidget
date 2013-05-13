package com.paypal.donation.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paypal.donation.exceptions.CustomAPIException;
import com.paypal.donation.services.AuthService;
import com.paypal.donation.services.PaymentService;
import com.paypal.donation.utils.Utility;

/**
 * Servlet implementation class APIController
 */
public class APIController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(APIController.class);

	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		try {
			handleRequest(request, response);
			return;
		} catch (Exception e) {
			handleException(request, response, e);
		}
	}

	protected void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		try {
			handleRequest(request, response);
			return;
		} catch (Exception e) {
			handleException(request, response, e);
		}
	}

	private void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			final String action = request.getParameter("method");
			LOG.debug("Action Performed: " + action);
			
			if ("create".equals(action)) {
				AuthService.getInstance().doAuthenticate(request, response);
			} else if ("authSuccess".equals(action)) {
				AuthService.getInstance().getAuthDetails(request, response);
			} else if ("pay".equals(action)) {
				PaymentService.getInstance().doPay(request, response);
			} else if ("ipnMsg".equals(action)) {
				PaymentService.getIPN(request, response);
			} else if ("paymentSuccess".equals(action)) {
				PaymentService.getInstance().getPaymentDetails(request, response);
			} else if ("logout".equals(action) || "authLogoutSuccess".equals(action)) {
				AuthService.getInstance().doLogout(request, response);
			} else if ("authCancel".equals(action)) {
				AuthService.getInstance().doAuthCancel(request, response);
			} else if("permissionSuccess".equals(action)) {
				AuthService.getPermissionToken(request, response);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void handleException(final HttpServletRequest request,
			final HttpServletResponse response, Exception e) throws IOException {
		if (e instanceof CustomAPIException) {
			//RenderWidget error will be captured here.
			//Error will not be displayed to the user
			//Exception is already logged in respective methods before throwing new CustomAPIException.
			response.sendRedirect(Utility.getServerUrl() + "/error");
		} else {
			//Admin errors are captured here.
			//The request is redirected to home page.
			LOG.error(Utility.getStackTrace(e));
			response.sendRedirect(Utility.buildReturnUrl(request).toString());
		}
	}
}
