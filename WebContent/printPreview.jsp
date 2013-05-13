<%@page import="com.paypal.donation.services.WidgetService"%>
<%@page import="com.paypal.donation.utils.Constants"%>
<%@page import="com.paypal.donation.services.ApplicationProperties"%>
<%@page import="com.paypal.donation.models.*"%>

<%
	final String QRCodeSize = "115";
	String externalId = request.getParameter("i");
	String size = request.getParameter("s");
	if(size != null) {
		try {
			Integer.parseInt(size);
		} catch (Exception exc) {size = QRCodeSize;}
	} else {
		size = QRCodeSize;
	}
	String errorMessage = "";
	Widgetui widgetUI = null;
	if(null == externalId || "".equalsIgnoreCase(externalId)){
		errorMessage = "Invalid Request";
	}else{
		widgetUI = WidgetService.getInstance().findWidgetuiByExternalId(externalId);
		if(null == widgetUI){
			errorMessage = "Widget not found";	
		}
	}
%>
<!DOCTYPE html>
<html>
<head>
	<title><%=ApplicationProperties.getProperty(Constants.WIDGET_TITLE)%></title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	
	<link rel="stylesheet" media="screen" type="text/css" href="assets/css/bootstrap.css">
	<link rel="stylesheet" media="print" type="text/css" href="assets/css/bootstrap.css">
	<link rel="shortcut icon" href="assets/images/pp_favicon_x.ico">
	
	<style type="text/css">
	.outerContainer {
		/*background-image: url("assets/images/themes/blue.jpg");*/
		margin: 5px;
		border: solid #000;
		border-width: 1px;
		-webkit-border-radius: 5px;
		-moz-border-radius: 5px;
		border-radius: 5px;
		-webkit-box-shadow: 2px 3px 2px 3px #D3D3D3;
		-moz-box-shadow: 2px 3px 2px 3px #D3D3D3;
		box-shadow: 2px 3px 2px 3px #D3D3D3;
	}
	
	.content {
		color: #666666;
	}
	</style>
</head>
<body>
	<div class="container-fluid outerContainer">
	<%if(errorMessage.length()==0){ %>
		<br />
		<!-- Title -->
		<div class="row-fluid">
			<div class="span10 text-center offset1">
				<h1><%=widgetUI.getTitle()%></h1>
			</div>
		</div>
		<br/>
		<!-- Image -->
		<%if(!("".equalsIgnoreCase(widgetUI.getCoverimageurl()) || null==widgetUI.getCoverimageurl())) {%>
		<div class="row-fluid">
			<div class="span10 text-center offset1">
				<img src='<%=widgetUI.getCoverimageurl()%>' alt="Cover Image" title="Cover Image"/>
			</div>
		</div>
		<%} %>
		<br/>
		<!-- Description -->	
		<%if(!("".equalsIgnoreCase(widgetUI.getCause()) || null==widgetUI.getCause())) {%>	
			<div class="row-fluid">
				<div class="span10 text-center offset1">
					<textarea class="img-polaroid input-block-level" id="aboutDescription" 
						style="resize: none; border: 0px;" readonly><%=widgetUI.getCause() %></textarea>
				</div>
			</div>
		<%} %>
		<br />
		<!-- QR Code -->
		<div class="row-fluid">
			<div class="span10 text-center offset1">
				<p class="lead">Scan the QR code below, to make a Donation!!</p>
				<img id="qrCode" class="img-polaroid"
					src="https://chart.googleapis.com/chart?cht=qr&amp;chs=<%=size%>x<%=size%>&amp;chld=|0&amp;chl=<%=ApplicationProperties.getProperty(Constants.APP_URL) %>/r?i=<%=externalId%>" />
			</div>
		</div>
		<br />
		<%}else{ %>
		<!-- Error Message -->
		<div class="row-fluid">
			<div class="span10 offset1">
				<h4 class="text-center"><%=errorMessage %></h4>
			</div>
		</div>
		<%} %>
		<!-- Paypal Logo -->
		<div class="row-fluid">
			<div class="span10 offset1">
				<img src="assets/images/labslogo.png" style="width:200px;" alt="Paypal Labs" class="pull-right" title="Paypal Labs">
			</div>
		</div>
		<br />
	</div>
</body>
</html>
