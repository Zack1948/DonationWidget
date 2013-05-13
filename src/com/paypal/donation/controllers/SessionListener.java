package com.paypal.donation.controllers;

import java.io.IOException;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.paypal.donation.services.ApplicationProperties;
import com.paypal.donation.utils.Constants;
import com.paypal.donation.utils.Utility;

/**
 * Application Lifecycle Listener implementation class SessionListener
 *
 */
@WebListener
public class SessionListener implements HttpSessionListener {
	private static final Log LOG = LogFactory.getLog(SessionListener.class);

    public SessionListener() {       
    }

    public void sessionCreated(HttpSessionEvent arg0) {           	
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
    	String token = (String) session.getAttribute("TOKEN");
		if (token != null) {
			LOG.debug("Session Listener calling AUTH LOGOUT");
			HttpClient client = new DefaultHttpClient();			
			HttpPost post = new HttpPost((ApplicationProperties.getProperty(Constants.AUTH_LOGOUT_URL) + token));
			try {
				HttpResponse response = client.execute(post);
				LOG.debug(response.getStatusLine());
			} catch (IOException e) {
				LOG.error(Utility.getStackTrace(e));
			}
		}
    }
	
}
