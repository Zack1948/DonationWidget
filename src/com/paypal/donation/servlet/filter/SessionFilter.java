package com.paypal.donation.servlet.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.paypal.donation.utils.Utility;

/**
 * Servlet Filter implementation class SessionFilter
 */
@WebFilter("/SessionFilter")
public class SessionFilter implements Filter {

	private ArrayList<String> urlList;
	/**
     * Default constructor. 
     */
    public SessionFilter() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String url = request.getServletPath();
        boolean allowedRequest = false;
        if(url.startsWith("/")) {
        	url = url.substring(1);
        }
        if(urlList != null) {
	        if(urlList.contains(url)) {
	            allowedRequest = true;
	        }
	 
	        if (!allowedRequest) {
	            HttpSession session = request.getSession(false);
	            if (null == session || session.getAttribute("PAYER_ID") == null) {
	                try {
						response.sendRedirect(Utility.getServerUrl());
					} catch (Exception e) {
						Utility.getStackTrace(e);
					}
	                return;
	            }
	        }
        }
        chain.doFilter(req, res);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		String urls = fConfig.getInitParameter("avoid-urls");
		if(urls != null) {
	        StringTokenizer token = new StringTokenizer(urls, ",");
	        urlList = new ArrayList<String>();
	        while (token.hasMoreTokens()) {
	            urlList.add(token.nextToken().trim());
	 
	        }
        } else {
        	urlList = new ArrayList<String>();
        }
	}

}
