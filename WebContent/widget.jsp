<%@page import="com.paypal.donation.utils.Utility"%>
<%@page import="com.paypal.donation.services.WidgetService"%>
<%@page import="com.paypal.donation.services.ApplicationProperties"%>
<%@page import="com.paypal.donation.models.Currency"%>
<%@page import="com.paypal.donation.models.Widget"%>
<%@page import="com.paypal.donation.models.Widgetui"%>
<%@page import="com.paypal.donation.utils.Constants"%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title><%=ApplicationProperties.getProperty(Constants.WIDGET_TITLE) %></title>

<!-- CSS -->
<link rel="stylesheet" href="assets/css/skeleton/stylesheets/base.css">
<link rel="stylesheet" href="assets/css/skeleton/stylesheets/skeleton.css">
<link rel="stylesheet" href="assets/css/skeleton/stylesheets/layout.css">

<style type="text/css">
	hr{border:solid #000;border-width:1px 0 0;clear:both;margin:5px 0 5px;height:0}
	#progressBarBorder,#amountsList{-webkit-border-radius:3px;-moz-border-radius:3px;border-radius:3px}
	.container{padding:5px;margin:5px;background:white;-webkit-border-radius:5px;-moz-border-radius:5px;border-radius:5px;opacity:.95}
	.outerContainer{background-image:url("assets/images/themes/blue.jpg");margin:5px;border:solid #000;border-width:1px;-webkit-border-radius:5px;-moz-border-radius:5px;border-radius:5px;-webkit-box-shadow:2px 3px 2px 3px #d3d3d3;-moz-box-shadow:2px 3px 2px 3px #d3d3d3;box-shadow:2px 3px 2px 3px #d3d3d3}
	#iconsDiv{display:inline;float:right}
	#coverImage{max-width:100%;width:100%}
</style>
<script type="text/javascript" src="assets/js/jquery/jquery.js"></script>
<%
	Widgetui widgetui = null;
	Widget widget = null;
	long amountCollected = 0;
	int amountPercent = 50;
	boolean displayWidget = true;
	String displayMsg = null;
	String themeOrColor = "blue";
	Currency currency = null;

	String widgetExternalId = request.getParameter("id");
	if(!widgetExternalId.equals("null")) {
		try{
			widgetui = WidgetService.getInstance().findWidgetuiByExternalId(widgetExternalId);
			widget = WidgetService.getInstance().findWidgetByExternalId(widgetExternalId);
		}catch(Exception exc){
			response.sendRedirect("/error");
		}
	}
	

	if (widget != null) {
		widgetExternalId = widget.getWidgetexternalid();
		if (widget.getStatus() == 'D') {
			displayMsg = ApplicationProperties.getProperty(Constants.DELETE_MESSAGE);
		} else if (widget.getStatus() == 'I') {
			displayMsg = ApplicationProperties.getProperty(Constants.INACTIVE_MESSAGE);
		}
	}

	try {
		if (widgetui != null) {
			amountCollected = WidgetService.getInstance().findTotalGoalAmountCollected(widgetExternalId);
			if (widgetui.getGoal() > 0) {
				amountPercent = Math.round(amountCollected * 100 / widgetui.getGoal());
			}
			currency = ApplicationProperties.findCurrencyById(widgetui.getGoalcurrency());
		}
	} catch (Exception exc) {
		displayMsg = ApplicationProperties.getProperty(Constants.TECHNICAL_PROBLEM_MESSAGE);
	}
%>

<script type="text/javascript">

$(document).ready(function() {
<%if (displayMsg != null && (displayMsg.length() > 0)) {%>
		$('#innerContainer').html("<%=displayMsg%><br><div style='position:static;height:15px;'>"+ $('#logoDiv').html() +"</div>");
<%} else {%>
	$('#progressBar').css('width', '50%');
	
	<%if (widgetui != null && currency != null && widgetui.getGoal() > 0) {%>
			$('#goalTarget').html('<%=widgetui.getGoal()%>&nbsp;<%=currency.getUnits()%>');
	<%}%>
	$('#goalTotalAmount').html('<%=amountCollected%>&nbsp;<%=(amountCollected > 0 && currency != null ? currency.getUnits() : "")%>');
	
	$('#aboutClick').click(function(){
		$('#widgetDiv').hide();
		$('#progressDiv').hide();
		$('#about').show();
		$('#aboutClick').hide();
		$('#mainPage').show();
	});
	
	$('#mainPage').click(function(){
		$('#about').hide();
		$('#widgetDiv').show();
		$('#progressDiv').show();
		$('#aboutClick').show();
		$('#mainPage').hide();
	});

	<%if (widgetui != null) {
		if(widgetui.getTitlecolor() != null && widgetui.getTitlecolor().trim().length() > 0) {%>
			var color = '<%=widgetui.getTitlecolor()%>';
			$('#title').css('color', color);
		<%}
		if(widgetui.getTitle() != null && widgetui.getTitle().trim().length() > 0) {%>
			$('#title').html('<%=widgetui.getTitle().replaceAll("\"", "\\\\\"").replaceAll("'", "\\\\\'")%>');
		<%}
		if (widgetui.getWeburl() != null && widgetui.getWeburl().trim().length() > 0) {%>
			$('#imageURLId').show();
			$('#imageURLId').attr('href', '<%=widgetui.getWeburl()%>');
		<%} else {%>
			$('#imageURLId').hide();
		<%}
		if (widgetui.getCause() != null && widgetui.getCause().trim().length() > 0) {%>
			$('#aboutDescription').html('<%=widgetui.getCause().replaceAll("\"", "\\\\\"").replaceAll("'", "\\\\\'")%>');
		<%}
		if (widgetui.getGoalprogbcolor() != null && widgetui.getGoalprogbcolor().trim().length() > 0) {%>
			$('#progressBar').css('background', '<%=widgetui.getGoalprogbcolor()%>');		
		<%}
		if (widgetui.getCoverimageurl() != null && widgetui.getCoverimageurl().trim().length() > 0) {%>
			$('#coverImage').attr('src','<%=widgetui.getCoverimageurl()%>');
		<%} 
		if (widgetui.getThemetype() != null && widgetui.getThemevalue() != null) {
			if (widgetui.getThemetype() == 'C') {%>
				var imageURL = 'assets/images/themes/<%=Constants.COLOR_THEMES[Integer.parseInt(widgetui.getThemevalue())-1]%>.jpg';
				$('#widgetOuterBorder').css('backgroundImage','url('+imageURL+')');
			<%} else if (widgetui.getThemetype() == 'T') {%>
				var imageURL = 'assets/images/themes/'+(<%=widgetui.getThemevalue()%><10?'0<%=widgetui.getThemevalue()%>': '<%=widgetui.getThemevalue()%>')+'.jpg';
				$('#widgetOuterBorder').css('backgroundImage','url('+imageURL+')');
			<%}
	 	 }
	}
}%>
});

</script>
</head>
<body style="background-color: #F5F5F5;">
	<div class="outerContainer" id="widgetOuterBorder">
		<div class="container" id="innerContainer">
			<div class="thirteen columns" id="title" style="font-weight: bold; text-align: center; margin: 1px; color: #322415; width:89%;height:45px;"><%=ApplicationProperties.getProperty(Constants.TITLE)%></div>
			<div class="three columns" id="iconsDiv" style="width:8%; height:45px; margin:1px;">
				<a href="#" id="aboutClick"><img
					src="assets/images/i_icon.gif" /></a> <a href="#"
					style="display: none;" id="mainPage"><img
					src="assets/images/home.gif" /></a>
			</div>
			<div class="sixteen columns" id="widgetDiv">
				<img id="coverImage" />
			</div>
			<div class="sixteen columns" id="progressDiv"
				style="height: 30px; font-weight: bold;">
				<div
					style="background: white; height: 20px; border: solid #000000; border-width: 1px; margin: 5px 6px 5px 0;"
					id="progressBarBorder">
					<div style="display: inline; float: right; margin-right: 3px;"
						id="goalTarget">&nbsp;</div>
					<div style="display: inline; float: left; margin-left: 3px;"
						id="goalTotalAmount">&nbsp;0</div>
					<div style="background: #4D84B2; width: 10%; height: 100%;"
						id="progressBar"></div>
				</div>
			</div>
			<div class="sixteen columns" id="about"
				style="display: none; margin-top: 5px;">
				<p align="center">
					<textarea id="aboutDescription" style="resize: none; width: 85%;"
						rows="5" maxlength="1000" readonly></textarea>
				</p>
				<div style="margin-top: -15px;" id="urlDiv">
					<a id="imageURLId"
						style="display: none; font-size: 12px; margin-left: 8px;"
						target="_blank"><%=ApplicationProperties.getProperty(Constants.GO_TO_HOMEPAGE)%></a>
				</div>
			</div>
			<div class="three columns">
				<!-- <select id="amountsList" name="amountsList">
				</select> -->
				<input type="number" id="amountsList" name="amountsList" placeholder="Enter Amount" readonly="readonly" style="width: 100%; text-align:center;"/>
			</div>
			<div class="thirteen columns">
				<img id="donateImgDiv"
					src="https://www.paypal.com/en_US/i/btn/btn_donate_LG.gif"
					alt="PayPal - The safer, easier way to pay online" />
			</div>
			<div class="sixteen columns" id="logoDiv">
				<img alt="Paypal" src="assets/images/paypal_logo.png"
					style="float: right; width: 50px; height: 13px;" />
			</div>
		</div>
	</div>
</body>
</html>