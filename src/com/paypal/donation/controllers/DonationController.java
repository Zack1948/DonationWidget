package com.paypal.donation.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paypal.donation.exceptions.CustomUIException;
import com.paypal.donation.models.Widget;
import com.paypal.donation.models.WidgetHome;
import com.paypal.donation.models.Widgetui;
import com.paypal.donation.services.ApplicationProperties;
import com.paypal.donation.services.ValidationService;
import com.paypal.donation.services.VettingService;
import com.paypal.donation.services.WidgetService;
import com.paypal.donation.utils.Constants;
import com.paypal.donation.utils.Utility;

public class DonationController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(DonationController.class);

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			handleRequest(request, response);
		} catch (Exception exc) {
			Utility.writeMessageToResponse(response, exc.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			handleRequest(request, response);
		} catch (Exception exc) {
			Utility.writeMessageToResponse(response, exc.getMessage());
		}
	}

	protected void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final String action = request.getParameter("method");
		if ("saveWidget".equals(action)) {
			saveOrUpdateWidget(request, response, "Save");
		} else if ("updateWidget".equals(action)) {
			saveOrUpdateWidget(request, response, "Update");
		} else if ("delete".equals(action)) {
			deleteWidget(request, response);
		}
	}

	private void deleteWidget(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LOG.debug("Start function - deleteORInactivateWidget");
		try {
			final String widgetId = request.getParameter("widget");
			if (widgetId != null) {
				Widget widget = WidgetHome.getInstance().findByWidgetExternalId(widgetId);
				if (widget == null) {
					throw new CustomUIException(ApplicationProperties.getProperty(Constants.WIDGET_NOT_FOUND));
				}
				widget.setStatus('D');
				widget.setUpdatedDt(new Date());
				WidgetHome.getInstance().merge(widget);
				response.sendRedirect(Utility.getServerUrl() + "/dashboard");
			}
		} catch (Exception exc) {
			LOG.error("Error in deleteWidget");
			LOG.error(Utility.getStackTrace(exc));
			throw new Exception("Could not delete the widget");
		}
		LOG.debug("End function - deleteORInactivateWidget");
	}

	public void saveOrUpdateWidget(HttpServletRequest request,
			HttpServletResponse response, String purpose) throws Exception {
		LOG.debug("Start function - saveOrUpdateWidget");
		String widgetExternalId = null;
		Widget widget = null;
		Widgetui widgetUI = null;
		try {
			logInputParameters(request);
			LOG.debug("Start "+purpose+" widget");
			
			HttpSession session = request.getSession(false);
			String payerId = (String) session.getAttribute("PAYER_ID");
			String payerEmailId = (String)session.getAttribute("PAYER_EMAIL_ID");
			String widgetExtId = request.getParameter("widget");
			
			if ("Save".equals(purpose)) {
				//Initializing Widget
				widget = new Widget();
				widget.setWidgetexternalid("xyz123"); // Dummy Id which will get replaced in WidgetHome persist() method call.
				widget.setEinVerified('N');
				widget.setStatus('A'); // Widget Active during Save
				widget.setPayerid(payerId);
				
				// Initializing WidgetUI
				widgetUI = new Widgetui ();
				widgetUI.setPayerid(payerId);
				
			} else if ("Update".equals(purpose) && widgetExtId != null) {
				widget = WidgetService.getInstance().findWidgetByExternalId(widgetExtId);
				if (widget == null) {
					throw new CustomUIException(ApplicationProperties.getProperty(Constants.WIDGET_NOT_FOUND));
				}
				widgetUI = WidgetService.getInstance().findWidgetUIByWidgetId(widget.getId());
			}
			ValidationService.getInstance().validateAndPopulateWidgetInput(request, widget, widgetUI, purpose);
			widgetExternalId = WidgetService.getInstance().saveOrUpdateWidget(purpose, widget, widgetUI);
			if ('N' == widget.getEinVerified() || 'D' == widget.getEinVerified()) {
				 String requestStatus = VettingService.sendVettingRequest(widget,payerEmailId);//NGO Paypal email
				 WidgetHome.getInstance().updateEinStatus(requestStatus, widget);
			 }
			response.setContentType("text/plain");
			response.getWriter().write("Success:"+widgetExternalId);
			response.flushBuffer();
		} catch (Exception exc) {
			LOG.error(Utility.getStackTrace(exc));
			if(exc instanceof CustomUIException) {
				throw new Exception("Could not " + purpose + " widget.<br>"+exc.getMessage());
			} else {
				throw new Exception("Could not " + purpose + " widget.");
			}
		}
		LOG.debug("End "+purpose+" widget");
		LOG.debug("End function - saveOrUpdateWidget");
	}

	private void logInputParameters(HttpServletRequest request) {
		LOG.debug("Start function - logInputParameters");
		Enumeration<String> paramEnum = request.getParameterNames();
		while(paramEnum.hasMoreElements()) {
			String paramName = paramEnum.nextElement();
			String paramValue = request.getParameter(paramName);
			LOG.debug(paramName+":"+paramValue);
		}
		LOG.debug("End function - logInputParameters");
	}
}
