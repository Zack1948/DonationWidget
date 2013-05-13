<%@page import="com.paypal.donation.models.Currency"%>
<%@page import="com.paypal.donation.models.Widget"%>
<%@page import="com.paypal.donation.models.Widgetui"%>
<%@page import="com.paypal.donation.services.ApplicationProperties"%>
<%@page import="com.paypal.donation.services.WidgetService" %>
<%@page import="com.paypal.donation.utils.Utility" %>
<%@page import="com.paypal.donation.utils.Constants" %>
<%
	String externalId = request.getParameter("id");
	Widgetui widgetui = null;
	Widget widget = null;
	
	if(externalId != null) {
		try{
			widgetui = WidgetService.getInstance().findWidgetuiByExternalId(externalId);
			widget = WidgetService.getInstance().findWidgetByExternalId(externalId);
		}catch(Exception exc){
			response.sendRedirect("/error");
		}
	}
%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title><%=ApplicationProperties.getProperty(Constants.WIDGET_TITLE) %></title>
	
	<link rel="stylesheet" media="screen" href="assets/css/bootstrap.css" >
	<link rel="stylesheet" media="screen" href="assets/css/colorpicker.css" >
	<link rel="shortcut icon" href="https://www.paypalobjects.com/en_US/i/icon/pp_favicon_x.ico">
	
	<style type="text/css">
		.donateform{background-color:#fff;border-radius:5px 5px 5px 5px;border-style:solid;border-width:1px;border-color:#e5e5e5 #d9d9d9 #ccc;padding:19px 37px 27px;box-shadow:0 2px 2px -1px rgba(0,0,0,0.1)}
		#description{resize:none}
		.amts{font-size:14px}
	</style>
</head>
<body>
	<!-- Header -->
	<%@ include file="header.html" %>
	<!-- End Header -->
	
	<div style="background-color: #F5F5F5;">
		<div class="container-fluid">
			
			<!-- Back Button & User Logout -->
			<div class="row-fluid">
				<div class="span5 offset1" style="padding-top: 10px; padding-bottom: 10px;">
					<a class="previous" target="_self" href="dashboard">&larr; Back</a>
				</div>
				<div class="span5 content text-right">
					<span><small>Hi, <i><%=session.getAttribute("FNAME") %></i>!<br />
						<span>Not <i><%=session.getAttribute("FNAME") %></i>? <a href="ac?method=logout">Sign Out</a></span>
					</small></span>
				</div>
			</div>
			<!-- Back Button & User Logout Ends-->
			
			<!-- Modal Window for showing ERROR message thrown by server-->
			<div id="errorContent" class="modal hide fade in" style="display: none;">
				<div class="modal-header"><h4>Error</h4>
				</div>  
				<div id="errorbody" class="modal-body text-error">  
				</div>  
				<div class="modal-footer">  
					<a href="#" class="btn btn-warning" data-dismiss="modal">Ok</a>  
				</div>  
			</div>
			
			<!-- Modal Window for SUCCESS if saving widget-->
			<div id="successContent" class="modal hide fade in" style="display: none;">
				<div class="modal-header"><h4>Success</h4>
				</div>  
				<div id="successbody" class="modal-body text-success">Widget Saved Successfully...:)
				</div>  
				<div class="modal-footer">  
					<a href="dashboard" class="btn btn-success">Go To Dashboard</a>  
				</div>  
			</div>
			
			<!-- Saving/Updating Widget -->
			<div class="row-fluid">
				<div class="span7 donateform offset1">
					<input type="hidden" name="method" id="method"/>
					<legend class="lead"><%=ApplicationProperties.getProperty(Constants.BUILD_WIDGET) %></legend>
					
				<!-- Title -->
					<div class="controls controls-row">
						<h5><%=ApplicationProperties.getProperty(Constants.TITLE)%></h5>
							<input type="text" class="input-block-level span8" placeholder="Mandatory Field" 
								maxlength="50" required name="widgetTitle" id="widgetTitle">
						<a id="causeColor" class="btn btn-info btn-block span4" data-color="rgb(255, 255, 255)" data-color-format="hex" href="#" value="">Title color</a>
					</div>
					<div>
						<label id="titleLengthVal" class="alert alert-error" style="display: none;">Title <%=ApplicationProperties.getProperty(Constants.LENGTH_ERROR) %></label>
						<label id="titleMandatoryVal" class="alert alert-error" style="display: none;"><%=ApplicationProperties.getProperty(Constants.TITLE_MANDATORY_ERROR) %></label>
						<label id="titleTextVal" class="alert alert-error" style="display: none;"><%=ApplicationProperties.getProperty(Constants.TITLE_EXPRESSION_ERROR) %></label>
					</div>
				<!-- Image -->
					<div>
						<h5><%=ApplicationProperties.getProperty(Constants.IMAGE) %></h5>
						<input type="text" name="widgetImage" id="widgetImage" maxlength="250" class="input-block-level"
						       placeholder="<%=ApplicationProperties.getProperty(Constants.IMAGE_PH) %>"/> 
					</div>
				<!-- Goal -->
					<div class="controls controls-row">
						<h5><%=ApplicationProperties.getProperty(Constants.SET_GOAL) %></h5>
						<input type="number" name="widgetGoal" id="widgetGoal"  class="input-block-level span4" min="1"
								placeholder="Goal Amount (<%=ApplicationProperties.getProperty(Constants.GOAL_OPTIONAL) %>"/>
						<select name="currency" id="currency" class="input-block-level span4"></select>
						<a id=progressBarColor class="btn btn-info btn-block span4" data-color="rgb(255, 255, 255)" 
							data-color-format="hex" href="#" value="">Progress Bar Color</a>
					</div>
					<div>
						<label id="goalValidation" class="alert alert-error" style="display: none;">
							<%=ApplicationProperties.getProperty(Constants.GOAL_AMOUNT_ERROR) %></label>
					</div>
				<!-- Theme/Color -->
					<h5><%=ApplicationProperties.getProperty(Constants.SET_COLOR_THEME) %></h5>
					<div id="themeTypeSelect">
						<label class="radio"><%=ApplicationProperties.getProperty(Constants.THEME) %> 
							<input type="radio" name="themes" id="theme" value="T" 
								<%=(widgetui != null && widgetui.getThemetype() != null && widgetui.getThemetype() == 'T' ? "checked" : "")%>/>
						</label>
						<label class="radio"><%=ApplicationProperties.getProperty(Constants.COLOR) %> 
							<input type="radio" name="themes" id="themeColor" value="C" 
								<%=(widgetui == null ? "checked" : (widgetui != null  && widgetui.getThemetype() == 'C' ? "checked" : ""))%>/>
						</label>
					</div>
					<br/>
					<div>
				<!-- Data set to select the Themes -->
						<div id="themeFieldSelect"></div>
				<!-- Data Set to select the Colors -->
						<div id="colorFieldSelect"></div>
					</div>
				<!-- Description -->
					<div>
						<h5><%=ApplicationProperties.getProperty(Constants.DESCRIBE_CAUSE) %></h5>
						<textarea name="description" id="description" maxlength="1000" class="input-block-level"
							placeholder="<%=ApplicationProperties.getProperty(Constants.ABOUT_PH) %>"></textarea>
						<label id="aboutvalidation" class="alert alert-error" style="display: none;">Description <%=ApplicationProperties.getProperty(Constants.LENGTH_ERROR) %></label>	
						<h5><%=ApplicationProperties.getProperty(Constants.HOMEPAGE_URL) %></h5> 
						<input type="text" id="url" name="url" maxlength="250" class="input-block-level"
							placeholder="<%=ApplicationProperties.getProperty(Constants.OPTIONAL_PH) %>"/>
							<h5><%=ApplicationProperties.getProperty(Constants.EIN) %></h5>
							<input type="text" id="ein" name="ein" maxlength="12" class="input-block-level" 
								placeholder="<%=ApplicationProperties.getProperty(Constants.OPTIONAL_PH) %>"/>
							<label id="einVal" class="alert alert-error" style="display: none;"><%=ApplicationProperties.getProperty(Constants.EIN_ERROR) %></label>
						<h5><%=ApplicationProperties.getProperty(Constants.ORGNAME) %></h5>
						<input type="text" id="orgName" name="orgName" maxlength="75" class="input-block-level" 
							placeholder="<%=ApplicationProperties.getProperty(Constants.OPTIONAL_PH) %>"/>
						<label id="orgNameTextVal" class="alert alert-error" style="display: none;"><%=ApplicationProperties.getProperty(Constants.ORGNAME_EXPRESSION_ERROR) %></label>
					</div>
					<input type="button" value="<%if(widgetui != null) {%>Update<%}else{ %>Save<%} %> Widget" id="saveWidget" class="btn btn-block btn-primary btn-success btn-large" onClick="saveForm()"/>
				</div>
			<!-- Preview Widget -->
				<div class="span3">
					<iframe src="widget?id=<%=externalId %>" id="wiframe" style="width: 225px; min-height:350px; border:0px;"></iframe>
				</div>
			</div>
			<br/>
		</div>
	</div>
	<!-- Footer -->
	<%@ include file="footer.html" %>
	<!-- End Footer -->
	<!-- JavaScript Files -->
	<script type="text/javascript" src="assets/js/jquery/jquery.js"></script>
    <script type="text/javascript" src="assets/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="assets/js/bootstrap-colorpicker.js"></script>

    <script type="text/javascript">
	var colorThemes = ["beige","blue","bright_orange","bright_red","brown","dark_brown","dark_green",
	                   "dark_red","green","grey","light_blue","light_orange","light_purple","light_yellow","maroon",
	                   "navy","orange","pink","purple","red","teal","violet","white"];
	var titleValidation = '';
	$(document).ready(function(){
		titleValidation = '<%=ApplicationProperties.getProperty(Constants.TITLE_JAVASCRIPT_EXPRESSION)%>';
		<%  	
		String themeValue = "0";
		if(widgetui != null && widgetui.getThemetype() == 'T') {
			if(widgetui.getThemevalue() != null) {
				themeValue = widgetui.getThemevalue();
			}
		} %>

		for(var i=1; i<=25; i++){
			$('#themeFieldSelect').append('<label class="radio inline" style="margin-left: 0px;"><input type="radio" name="themeSelect" id="T'+i+'" value="'+i+'" '+(i==<%=themeValue%> ? 'checked' : '')+'/><img id="IMGT'+i+'" style="height:40px;width:35px;position:relative;left:-25px;padding-bottom:2px;'+(i==<%=themeValue%> ? 'background:blue;' : '')+'" src="assets/images/themes/swatches/'+(i<10?"0"+i:i)+'.jpg"/></label>');
			$('#themeFieldSelect').trigger('create');
		}
	
		<%  themeValue="2";
		if(widgetui != null && widgetui.getThemetype() == 'C') {
			if(widgetui.getThemevalue() != null) {
				themeValue = widgetui.getThemevalue();
			}
		} %>

		for(var i=1; i<=colorThemes.length; i++){
			$('#colorFieldSelect').append('<label class="radio inline" style="margin-left: 0px;"><input type="radio" name="colorSelect" id="C'+i+'" value="'+i+'" '+(i==<%=(widgetui == null ? "2" :themeValue)%> ? 'checked' : '')+'/><img id="IMGC'+i+'" style="height:40px;width:35px;position:relative;left:-25px;padding-bottom:2px;'+(i==<%=(widgetui == null ? "2" :themeValue)%> ? 'background:blue;' : '')+'" src="assets/images/themes/swatches/'+colorThemes[i-1]+'.jpg"/></label>');
			$('#colorFieldSelect').trigger('create');
		}
		
		$('#themeColor').click(function() {
			$('#themeFieldSelect').hide();
			$('#colorFieldSelect').show();
		});
	
		$('#theme').click(function() {
			$('#colorFieldSelect').hide();
			$('#themeFieldSelect').show();
		});

		<% if(widgetui == null) { %>
				$('#themeColor').trigger('click');
		<% } else if(widgetui.getThemetype() == 'T'){%>
				$('#theme').trigger('click');
		<% } else if (widgetui.getThemetype() == 'C') {%>
				$('#themeColor').trigger('click');
		<% } 
		
		java.util.List<Currency> currencyList = ApplicationProperties.getCurrencyList();
		if(currencyList != null) {
		for(Currency currency: currencyList) {
			if(widgetui != null) {
				if (widgetui.getGoalcurrency() == currency.getId()) { %>
					$('#currency').append('<option value="<%=currency.getUnits()+"^"+currency.getId()%>" selected><%=currency.getName()%></option>');
		<%
				break;
				} else {
					continue;
				}
			} %>
			$('#currency').append('<option value="<%=currency.getUnits()+"^"+currency.getId()%>"><%=currency.getName()%></option>');
		<% } %>	
	<% } %>

	<% if(widgetui != null) { %>

		requestURL = 'dc?method=updateWidget&widget=<%=externalId%>';
		$('#method').val("updateWidget");
		einDisplay = <%=((widget != null && (widget.getEinVerified() == 'N' || widget.getEinVerified() == 'D')) ? "true" : "false")%>
	
		$('#widgetTitle').val("<%=widgetui.getTitle().replaceAll("\"", "\\\\\"").replaceAll("'", "\\\\\'")%>");
		$('#widgetImage').val("<%=(widgetui.getCoverimageurl() != null ? widgetui.getCoverimageurl() : "")%>");
		
		<%if(widgetui.getGoal() > 0) {	%>
			$('#widgetGoal').val("<%=widgetui.getGoal()%>");
		<% } %>
		$('#description').val("<%=widgetui.getCause().replaceAll("\"", "\\\\\"").replaceAll("'", "\\\\\'")%>");
		$('#ein').val("<%=widget.getEin()%>");
		$('#orgName').val("<%=widget.getOrgName()%>");
		$('#url').val("<%=widgetui.getWeburl()%>");

		// Enable or Disable EIN/Org
		if(!einDisplay) {
			$('#ein').addClass('uneditable-input');
			$('#ein').css('width', '100%');
			$('#orgName').addClass('uneditable-input');
			$('#orgName').css('width', '100%');
		} else {
			$('#ein').removeClass('uneditable-input');
			$('#orgName').removeClass('uneditable-input');
		}
    <% } else {%>
    	$('#method').val("saveWidget");
    <%}%>
	
	});

	// function for client side validation
	function saveForm(){
		if(!validateForm()){
			return false;
		}else{
			save();
		}
	}

	var requestURL = 'dc?method=saveWidget';
	function save() {

		var donateObj = new Object();
		
		// selecting the radio button
		donateObj.themeType = $('input[name=themes]:checked').val();
		if(donateObj.themeType == 'T')
			donateObj.themeValue = $('input[name=themeSelect]:checked').val();
		else if(donateObj.themeType == 'C')
			donateObj.themeValue = $('input[name=colorSelect]:checked').val();

		//selecting title and title properties
		donateObj.widgetTitle = $('#widgetTitle').val();
		donateObj.widgetTitleColor = $('#causeColor').attr('value');
		if($('input[name=widgetImage]').val() != '') {
			donateObj.widgetImageUrl = $('input[name=widgetImage]').val();
		}
		
		// widget goal and contribution amount
		donateObj.widgetGoal = $('input[name=widgetGoal]').val();
		var currencySelectedVal = $('#currency').val();
		var currencySplitArray = currencySelectedVal.split('^');
		donateObj.currency = currencySplitArray[1];
		donateObj.goalProgressBarColor = $('#progressBarColor').attr('value');
		
		// cause and web page URL
		donateObj.aboutCauseDesc = $('textarea[name=description]').val();
		donateObj.widgetWebpageURL = $('input[name=url]').val();
		
		donateObj.ein = $('input[name=ein]').val();
		donateObj.orgName = $('input[name=orgName]').val();
		
		$.ajax({
			url : requestURL,
			type : "POST",
			data : donateObj,
			dataType: "text",
			success : function(data, textStatus, jqXHR) {
				if(data.indexOf("Error:") == -1) {
					if(data.indexOf("Success:") == -1) {
						window.location = 'error';
					} else {
						var externalId = data.split(":");
						jsWidgetExternalId = externalId[1] ;
						requestURL = 'dc?method=updateWidget&widget='+jsWidgetExternalId;
						$('#errorContent').modal('hide');
						$('#successContent').modal('show');
					};
				} else {
					$('#errorbody').html(data);
					$('#errorContent').modal('show');
				};
			},
			error : function(jqXHR, textStatus, errorThrown) { }
		});
	};
	</script>
	<script type="text/javascript" src="assets/js/widget.js"></script>
    <script type="text/javascript" src="assets/js/validations.js"></script>
</body>
</html>
