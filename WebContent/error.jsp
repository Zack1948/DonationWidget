<!DOCTYPE html>
<%@page import="com.paypal.donation.utils.Utility"%>
<%@page import="com.paypal.donation.utils.Constants"%>
<%@page import="com.paypal.donation.services.ApplicationProperties"%>
<html>
<head>
	<title><%=ApplicationProperties.getProperty(Constants.WIDGET_TITLE) %></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
	<link rel="stylesheet" media="screen" href="assets/css/bootstrap.css" />
    <link rel="shortcut icon" href="assets/images/pp_favicon_x.ico">
    
    <style type="text/css">
    .pageNotFoundHero {
    	background: url("./assets/images/404_hero.png") no-repeat scroll center bottom transparent;
    	height: 300px;
    	position: relative;
	}
    </style>
</head>
<body>
	<!-- Header -->
	<%@ include file="header.html" %>
	<!-- End Header -->
	
	<div style="background-color: #F5F5F5;">
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span10 offset1">
					<h1 class="content contentHeadline" style="padding-top: 35px;">Page not found </h1><hr/>
					<p class="lead" style="color: #0079C1;"> Oops. Something went wrong. Dont worry, just <a href="<%=Utility.getServerUrl()%>" class="btn btn-info">click here</a> to start again.</p>
					<div class="pageNotFoundHero">
					</div>
				</div>
			</div>
		</div>
		<div class="row-fluid pad">
		</div>
	</div>
	<!-- Footer -->
	<%@ include file="footer.html" %>
	<!-- End Footer -->
</body>
</html>