<%@page import="java.net.URLEncoder"%>
<%@page import="com.paypal.donation.utils.Utility"%>
<%@page import="com.paypal.donation.services.WidgetService"%>
<%@page import="com.paypal.donation.models.Currency"%>
<%@page import="com.paypal.donation.models.Widget"%>
<%@page import="com.paypal.donation.models.Widgetui"%>
<%@page import="com.paypal.donation.models.RenderLocation"%>
<%@page import="com.paypal.donation.models.RenderLocationHome"%>
<%@page import="com.paypal.donation.services.ApplicationProperties"%>
<%@page import="com.paypal.donation.utils.Constants"%>
<%@page import="java.util.Date"%>
<%@page import="org.apache.commons.logging.Log" %>
<%@page import="org.apache.commons.logging.LogFactory" %>
<!DOCTYPE html>
<!--[if lt IE 7 ]><html class="ie ie6" lang="en"> <![endif]-->
<!--[if IE 7 ]><html class="ie ie7" lang="en"> <![endif]-->
<!--[if IE 8 ]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!-->
<html lang="en">
<!--<![endif]-->
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title><%=ApplicationProperties.getProperty(Constants.WIDGET_TITLE) %></title>
<!--[if lt IE 9]>
		<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->

<!-- CSS -->
<link rel="stylesheet" href="assets/css/skeleton/stylesheets/base.css">
<link rel="stylesheet" href="assets/css/skeleton/stylesheets/skeleton.css">
<link rel="stylesheet" href="assets/css/skeleton/stylesheets/layout.css">
<link rel="shortcut icon" href="https://www.paypalobjects.com/en_US/i/icon/pp_favicon_x.ico">

<style type="text/css">
	hr{border:solid #000;border-width:1px 0 0;clear:both;margin:5px 0 5px;height:0}
	#progressBarBorder, #amountsList{-webkit-border-radius:3px;-moz-border-radius:3px;border-radius:3px}
	.container{padding:5px;margin:5px;background:white;-webkit-border-radius:5px;-moz-border-radius:5px;border-radius:5px;opacity:.95}
	.outerContainer{background-image:url("assets/images/themes/blue.jpg");margin:5px auto;border:solid #000;border-width:1px;-webkit-border-radius:5px;-moz-border-radius:5px;border-radius:5px;-webkit-box-shadow:2px 3px 2px 3px #d3d3d3;-moz-box-shadow:2px 3px 2px 3px #d3d3d3;box-shadow:2px 3px 2px 3px #d3d3d3}
	#iconsDiv{display:inline;float:right}
	#coverImage{max-width:100%;width:100%}
</style>
<script type="text/javascript" src="assets/js/jquery/jquery.js"></script>
<%
	Log LOG = LogFactory.getLog(Widget.class);
	String widgetExternalId = request.getParameter("i");
	if(widgetExternalId == null) {
		widgetExternalId = request.getParameter("widgetId");
	}
	String widgetURL = "";
	Widgetui widgetui = null;
	Widget widget = null;
	long amountCollected = 0;
	int amountPercent = 100;
	boolean displayWidget = true;
	String displayMsg = null;
	String themeOrColor = "blue";
	Currency currency = null;

	if (widgetExternalId != null && !"".equals(widgetExternalId)) {
		widgetURL = URLEncoder.encode(Utility.getServerUrl()+"/r?i="+widgetExternalId,"UTF-8");
		try {
			widget = WidgetService.getInstance().findWidgetByExternalId(widgetExternalId);
			LOG.debug("RenderWidget - widget found"+widget);
			if (widget != null) {
				if (widget.getStatus() == 'D') {
					displayMsg = ApplicationProperties.getProperty(Constants.DELETE_MESSAGE);
				} else if (widget.getStatus() == 'I') {
					displayMsg = ApplicationProperties.getProperty(Constants.INACTIVE_MESSAGE);
				} else {
					LOG.debug("RenderWidget - find widgetui for widgetid"+widget.getId());
					widgetui = WidgetService.getInstance().findWidgetUIByWidgetId(widget.getId());
					if (widgetui == null) {
						LOG.debug("RenderWidget - error in getting widgetui for widgetid"+widget.getId());
						displayMsg = ApplicationProperties.getProperty(Constants.TECHNICAL_PROBLEM_MESSAGE);
					} else {
						LOG.debug("RenderWidget - find amount collected for widgetexternalid"+widgetExternalId);
						amountCollected = WidgetService.getInstance().findTotalGoalAmountCollected(widgetExternalId);
						LOG.debug("RenderWidget - amountCollected|"+amountCollected+"|for widget external id|"+widgetExternalId);
						if (widgetui.getGoal() > 0) {
							amountPercent = Math.round(amountCollected* 100 / widgetui.getGoal());
						}
						currency = ApplicationProperties.findCurrencyById(widgetui.getGoalcurrency());
						LOG.debug("RenderWidget - currency queried |"+currency);
					}
				}
				LOG.debug("RenderWidget - updating the render_location table for widgetexternalid -"+widgetExternalId);
				//update the render location 
				RenderLocation renderLoc = new RenderLocation();
				renderLoc.setCountry(request.getLocale().getDisplayCountry());
				renderLoc.setHost(request.getRemoteHost());
				renderLoc.setIpaddress(request.getRemoteAddr());
				renderLoc.setLocale(request.getLocale().toString());
				renderLoc.setLanguage(request.getLocale().getDisplayLanguage());
				renderLoc.setAmount(0);
				renderLoc.setDonateflag('N');
				renderLoc.setPayerid(widget.getPayerid());
				renderLoc.setCreatedDt(new Date());
				renderLoc.setWidgetexternalid(widgetExternalId);
				RenderLocationHome.getInstance().persist(renderLoc);
				LOG.debug("RenderWidget - updating the render_location table complete for widgetexternalid -"+widgetExternalId);
			} else {
				displayMsg = ApplicationProperties.getProperty(Constants.INVALID_WIDGET);
			}
		} catch (Exception exc) {
			displayMsg = ApplicationProperties.getProperty(Constants.TECHNICAL_PROBLEM_MESSAGE);
		}
	} else {
		displayMsg = ApplicationProperties.getProperty(Constants.WIDGET_ID_EMPTY_MESSAGE);
	}
%>
<script type="text/javascript">

$(document).ready(function() {
	<%if (displayMsg != null && (displayMsg.length() > 0)) { %>
		$('#innerContainer').html("<%=displayMsg%><br><div style='position:static;height:15px;'>"+ $('#logoDiv').html() +"</div>");
	<%} else { %>
		var temp =  '<%=(amountPercent > 100 ? 100 : amountPercent)%>%'; 
		$('#progressBar').css('width', temp);
	
	<%if (widgetui != null && currency != null && widgetui.getGoal() > 0) {%>
		$('#goalTarget').html('<%=widgetui.getGoal()%>&nbsp;<%=currency.getUnits()%>');
	<%}%>
	
	$('#goalTotalAmount').html('<%=amountCollected%>&nbsp;<%=(amountCollected > 0 && currency != null ? currency.getUnits() : "USD")%>');
	
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
		if(widgetui.getTitlecolor() != null && widgetui.getTitlecolor().trim().length() > 0) { %>
			var color = '<%=widgetui.getTitlecolor()%>';
			$('#title').css('color', color);
	<%} 
		if(widgetui.getTitle() != null && widgetui.getTitle().trim().length() > 0) { %>
			$('#title').html('<%=widgetui.getTitle().replaceAll("\"", "\\\\\"").replaceAll("'", "\\\\\'")%>');
	<%} 
		if(widgetui.getWeburl() != null && widgetui.getWeburl().trim().length() > 0) { %>
			$('#imageURLId').show();
			$('#imageURLId').attr('href', '<%=widgetui.getWeburl()%>');
	<%} 
		else{ %>
			$('#imageURLId').hide();
	<%} 
		if(widgetui.getCause() != null && widgetui.getCause().trim().length() > 0) {%>
			$('#aboutDescription').html('<%=widgetui.getCause().replaceAll("\"", "\\\\\"").replaceAll("'", "\\\\\'")%>');
	<%} 
		if(widgetui.getGoalprogbcolor() != null && widgetui.getGoalprogbcolor().trim().length() > 0) {%>
			$('#progressBar').css('background', '<%=widgetui.getGoalprogbcolor()%>');		
	<%} 
		if(widgetui.getCoverimageurl() != null && widgetui.getCoverimageurl().trim().length() > 0) {%>
			$('#coverImage').attr('src','<%=widgetui.getCoverimageurl()%>');
	<%} 
		if(widgetui.getThemetype() != null && widgetui.getThemevalue() != null) {
			if (widgetui.getThemetype() == 'C') { %>
				var imageURL = 'assets/images/themes/<%=Constants.COLOR_THEMES[Integer.parseInt(widgetui.getThemevalue())-1]%>.jpg';
				$('#widgetOuterBorder').css('backgroundImage','url('+imageURL+')');
		<%} 
			else if(widgetui.getThemetype() == 'T') { %>
				var imageURL = 'assets/images/themes/'+(<%=widgetui.getThemevalue()%><10?'0<%=widgetui.getThemevalue()%>': '<%=widgetui.getThemevalue()%>')+'.jpg';
				$('#widgetOuterBorder').css('backgroundImage', 'url(' + imageURL + ')'); <%}
		} 
		%>
	  <%}
	}%>
});

function performDonate() {
	if($('#amountsList').val().trim()=='' ){
		window.location = 'cerror?c=1';
	}else if(parseFloat($('#amountsList').val()) == 'NaN' || isNaN($('#amountsList').val().trim())){
		if(parseFloat($('#amountsList').val()) <= 0){
			window.location = 'cerror?c=2';
		}
		window.location = 'cerror?c=3';
	}else {
		document.donateForm.action = "ac?method=pay&widgetId=<%=widgetExternalId%>";
		document.donateForm.submit();
	};
}
</script>
</head>
<body>
<div id="fb-root"></div>
	<form name="donateForm" method="post" target="_blank">
		<div class="outerContainer" id="widgetOuterBorder">
			<div class="container" id="innerContainer">
				<div class="sixteen columns" style="text-align: center;">
					<div>
						<div id="title"><%=ApplicationProperties.getProperty(Constants.TITLE)%></div>
						<div id="iconsDiv">
							<a href="#" id="aboutClick"><img src="assets/images/i_icon.gif" /></a> <a href="#" style="display: none;" id="mainPage">
							<img src="assets/images/home.gif" /></a>
						</div>
					</div>
					<div id="widgetDiv">
						<img id="coverImage" />
					</div>
				</div>
				<div class="sixteen columns" id="progressDiv" style="height: 30px; font-weight: bold;">
					<div style="background: white; height: 20px; border: solid #000000; border-width: 1px; margin: 5px 6px 5px 0;" id="progressBarBorder">
						<div style="display: inline; float: right; margin-right: 3px;" id="goalTarget">&nbsp;</div>
						<div style="display: inline; float: left; margin-left: 3px;" id="goalTotalAmount">&nbsp;</div>
						<div style="background: #4D84B2; width: 10%; height: 100%;" id="progressBar"></div>
					</div>
				</div>
				<div class="sixteen columns" id="about"
					style="display: none; margin-top: 5px;">
					<p align="center">
						<textarea id="aboutDescription" style="resize: none; width: 80%;"
							rows="6" maxlength="1000" readonly></textarea>
					</p>
					<div style="margin-top: -15px;" id="urlDiv">
						<a id="imageURLId" style="display: none;" target="_blank"><%=ApplicationProperties.getProperty(Constants.GO_TO_HOMEPAGE)%></a>
					</div>
				</div>
				<div class="three columns">
					<input type="number" id="amountsList" name="amountsList" placeholder="Enter Amount" min="1" style="width: 100%; text-align:center;"/>
				</div>
				<div class="thirteen columns">
					<img onClick="performDonate();" id="donateImgDiv" src="https://www.paypal.com/en_US/i/btn/btn_donate_LG.gif" 
						alt="PayPal - The safer, easier way to pay online" />
				</div>
				<div class="sixteen columns" id="logoDiv">
					<!-- Twitter -->
 					<a target="_blank" title="twitter" 
 						href="https://twitter.com/share?url=<%=widgetURL%>&text=<%=URLEncoder.encode(ApplicationProperties.getProperty(Constants.TWITTER_TEXT), "UTF-8")%>">
 				       	<img title="twitter" alt="twitter" src="assets/images/icon-twitter.gif"></a>
					<img alt="Paypal" src="assets/images/paypal_logo.png" style="float: right; width: 50px; height: 13px;" />
				</div>
			</div>
		</div>
	</form>
</body>
</html>