package com.paypal.donation.services;

import javax.servlet.http.HttpServletRequest;

import com.paypal.donation.exceptions.CustomUIException;
import com.paypal.donation.models.Widget;
import com.paypal.donation.models.Widgetui;
import com.paypal.donation.utils.Constants;

public final class ValidationService {
	
	private static ValidationService singleton = new ValidationService();
	
	public static ValidationService getInstance() {
		return singleton;
	}
	
	private ValidationService () {
	}
	
	public void validateAndPopulateWidgetInput(HttpServletRequest request, Widget widget, Widgetui widgetUI, String purpose) throws Exception {
		
		//Validate Widget Title
		String widgetTitle = request.getParameter("widgetTitle");
		if (widgetTitle == null || "".equals(widgetTitle.trim())) {
			throw new CustomUIException(ApplicationProperties.getProperty(Constants.TITLE_MANDATORY_ERROR));
		}else {
			widgetTitle = widgetTitle.trim();
			if (widgetTitle.length() > 50){ // Length check
				throw new CustomUIException("Title "+ApplicationProperties.getProperty(Constants.LENGTH_ERROR));
			}else if(!widgetTitle.matches(ApplicationProperties.getProperty(Constants.TITLE_EXPRESSION))){
				throw new CustomUIException(ApplicationProperties.getProperty(Constants.TITLE_EXPRESSION_ERROR));
			}else {
				widgetUI.setTitle(widgetTitle);
			}
		}
		
		//Validate Widget Title Color
		String widgetTitleColor = request.getParameter("widgetTitleColor");
		if (widgetTitleColor == null || widgetTitleColor.trim().length() == 0) {
			widgetUI.setTitlecolor(ApplicationProperties.getProperty(Constants.TITLE_COLOR));
		} else { 
			widgetTitleColor = widgetTitleColor.trim();
			if(widgetTitleColor.length() > 7){ // Length check
				throw new CustomUIException("TitleColor "+ApplicationProperties.getProperty(Constants.LENGTH_ERROR));
			} else if(!widgetTitleColor.matches(ApplicationProperties.getProperty(Constants.COLOR_EXPRESSION))){
				throw new CustomUIException(ApplicationProperties.getProperty(Constants.COLOR_EXPRESSION_ERROR));
			} else {
				widgetUI.setTitlecolor(widgetTitleColor);
			}
		}
		
		//Validate Widget Image URL
		String widgetImageUrl = request.getParameter("widgetImageUrl");
		if (widgetImageUrl == null || "".equals(widgetImageUrl.trim())) {
			widgetUI.setCoverimageurl("");
		}else { 
			widgetImageUrl = widgetImageUrl.trim();
			if(widgetImageUrl.length() > 250){
				throw new CustomUIException("Campaign Image "+ApplicationProperties.getProperty(Constants.LENGTH_ERROR));
			} else {
				if(!widgetImageUrl.matches(ApplicationProperties.getProperty(Constants.URL_EXPRESSION))){
					throw new CustomUIException(ApplicationProperties.getProperty(Constants.IMAGE_URL_ERROR));
				}
				widgetUI.setCoverimageurl(widgetImageUrl);
			}
		}
		
		//Validate Widget Goal Amount
		String widgetGoal = request.getParameter("widgetGoal");
		if (widgetGoal == null || "".equals(widgetGoal.trim())) {
			widgetUI.setGoal(0L);
		} else {
			widgetGoal = widgetGoal.trim();
			try {
				long goalVal = Long.parseLong(widgetGoal);
				if (goalVal > 0) {
					widgetUI.setGoal(goalVal);
				} else {
					throw new CustomUIException(ApplicationProperties.getProperty(Constants.GOAL_AMOUNT_ERROR));
				}
			} catch (NumberFormatException exc) {
				throw new CustomUIException(ApplicationProperties.getProperty(Constants.GOAL_AMOUNT_VALIDATION));
			}
		}
		
		//Validate Widget Currency
		String currency = request.getParameter("currency");
		if (currency == null || currency.trim().length() == 0) {
			widgetUI.setGoalcurrency(1);
		} else {
			currency = currency.trim();
			try {
				widgetUI.setGoalcurrency(Integer.parseInt(currency));
			} catch (NumberFormatException exc) {
				throw new CustomUIException(ApplicationProperties.getProperty(Constants.INVALID_CURRENCY));
			}
		}
		
		//Validate Goal Progress Bar Color
		String progressBarColor = request.getParameter("goalProgressBarColor");
		if (progressBarColor == null || progressBarColor.trim().length() == 0) {
			widgetUI.setGoalprogbcolor(ApplicationProperties.getProperty(Constants.PROGRESS_BAR_COLOR));
		} else {
			progressBarColor = progressBarColor.trim();
			if(progressBarColor.length() > 7) {
				throw new CustomUIException("Progress Bar Color "+ApplicationProperties.getProperty(Constants.LENGTH_ERROR));
			} else if(!progressBarColor.matches(ApplicationProperties.getProperty(Constants.COLOR_EXPRESSION))){
				throw new CustomUIException(ApplicationProperties.getProperty(Constants.COLOR_EXPRESSION_ERROR));
			} else {
				widgetUI.setGoalprogbcolor(progressBarColor);
			}
		}
		
		//Validate Donation Contribution Amounts
		// Contribution Amounts not used anymore
		// So, setting a dummy value of 1
		widgetUI.setContribamts("1");
		/*
		String contribAmts = request.getParameter("contributionAmounts");
		if (contribAmts == null || contribAmts.trim().length() == 0) {
			widgetUI.setContribamts(ApplicationProperties.getProperty(Constants.DONATION_AMOUNTS));
		} else {
			contribAmts = contribAmts.trim();
			if(contribAmts.length() > 200) {
				throw new CustomUIException(ApplicationProperties.getProperty(Constants.LENGTH_ERROR));
			} else {
				widgetUI.setContribamts(contribAmts);
			}
		}*/
		
		//Validate Theme Selection Type
		String themeType = request.getParameter("themeType");
		if (themeType == null || "".equals(themeType.trim())) {
			widgetUI.setThemetype(ApplicationProperties.getProperty(Constants.THEME_TYPE).charAt(0));
		} else {
			themeType = themeType.trim();
			if("T".equals(themeType) || "C".equals(themeType)) {
				widgetUI.setThemetype(themeType.charAt(0));
			} else {
				throw new CustomUIException(ApplicationProperties.getProperty(Constants.THEME_TYPE_ERROR));
			}
		}
		
		//Validate Theme Selected Value
		String themeValue = request.getParameter("themeValue");
		if (themeValue == null || "".equals(themeValue.trim())) {
			widgetUI.setThemevalue(ApplicationProperties.getProperty(Constants.THEME_VALUE));
		} else {
			boolean invalidTheme = false;
			themeValue = themeValue.trim();
			int themeVal = 0;
			try {
				themeVal = Integer.parseInt(themeValue);
				int patternCount = Integer.parseInt(ApplicationProperties.getProperty(Constants.THEME_PATTERN_COUNT)) - 1;
				if(("T".equals(themeType) && themeVal > patternCount) || ("C".equals(themeType) && themeVal > Constants.COLOR_THEMES.length - 1)){
					invalidTheme = true;
				}
			} catch (Exception exc) {
				invalidTheme = true;
			}
			if(invalidTheme) {
				throw new CustomUIException(ApplicationProperties.getProperty(Constants.THEME_VALUE_ERROR));
			} else {
				widgetUI.setThemevalue(themeValue);
			}
		}
		
		//Validate Cause Description
		String aboutCause = request.getParameter("aboutCauseDesc");
		if (aboutCause == null || aboutCause.trim().length() == 0) {
			widgetUI.setCause("");
		} else {
			aboutCause = aboutCause.trim();
			if (aboutCause.length() > 1000) {
				throw new CustomUIException("Description "+ApplicationProperties.getProperty(Constants.LENGTH_ERROR));
			} else if(!aboutCause.matches(ApplicationProperties.getProperty(Constants.DESCRIPTION_EXPRESSION))){
				throw new CustomUIException(ApplicationProperties.getProperty(Constants.DESCRIPTION_EXPRESSION_ERROR));
			} else {
				widgetUI.setCause(aboutCause);
			}
		}
		
		//Validate Widget Web site Url
		String widgetWebpageURL = request.getParameter("widgetWebpageURL");
		if (widgetWebpageURL == null || widgetWebpageURL.trim().length() == 0) {
			widgetUI.setWeburl("");
		} else {
			widgetWebpageURL = widgetWebpageURL.trim();
			if(widgetWebpageURL.length() > 250) {
				throw new CustomUIException("Website URL "+ApplicationProperties.getProperty(Constants.LENGTH_ERROR));
			} else 
			if(!widgetWebpageURL.matches(ApplicationProperties.getProperty(Constants.URL_EXPRESSION))){
				throw new CustomUIException(ApplicationProperties.getProperty(Constants.HOMEPAGE_URL_ERROR));
			} else {
				widgetUI.setWeburl(widgetWebpageURL);
			}
		}
		
		// Validate EIN
		String ein = request.getParameter("ein");
		if(ein != null && ein.trim().length() > 0) {
			ein = ein.trim();
			if(ein.length() > 50) {
				throw new CustomUIException("EIN "+ApplicationProperties.getProperty(Constants.LENGTH_ERROR));
			} else if (!ein.matches(ApplicationProperties.getProperty(Constants.EIN_EXPRESSION))){
				throw new CustomUIException(ApplicationProperties.getProperty(Constants.EIN_EXPRESSION_ERROR));
			}
		} else {
			ein = "";
		}
		
		//Validate OrgName
		String orgName = request.getParameter("orgName");
		if(orgName != null && orgName.trim().length() > 0) {
			orgName = orgName.trim();
			if(orgName.length() > 260) {
				throw new CustomUIException("Non-Profit Name "+ApplicationProperties.getProperty(Constants.LENGTH_ERROR));
			} else if(!orgName.matches(ApplicationProperties.getProperty(Constants.TITLE_EXPRESSION))){
				throw new CustomUIException(ApplicationProperties.getProperty(Constants.ORGNAME_EXPRESSION_ERROR));
			}
		} else {
			orgName = "";
		}
		
		if("Save".equals(purpose)) { // New Widget
			widget.setEin(ein);
			widget.setOrgName(orgName);
		} else if("Update".equals(purpose)) {
			if (ein.length() > 0 && (widget.getEin() == null || "null".equalsIgnoreCase(widget.getEin()) || !widget.getEin().equalsIgnoreCase(ein))) { // Current EIN is different than older saved EIN
				// Then, update to the new EIN and set the status of EIN verification as not started
				widget.setEin(ein); 
				widget.setEinVerified('N');
			}
			// Update with the new Org Name.
			widget.setOrgName(orgName); 
		}
	}
}
