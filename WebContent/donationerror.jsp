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
    <%
    	String errorCode = request.getParameter("c");
    	System.out.println(errorCode);
    %>
</head>
<body>
	<!-- Header -->
	<%@ include file="header.html" %>
	<!-- End Header -->
	
	<div style="background-color: #F5F5F5;">
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span10 offset1">
					<h1 class="content contentHeadline" style="padding-top: 35px;">Oops. Something went wrong.</h1><hr/>
					<%if(Integer.parseInt(errorCode)==1){ %>
						<p class="lead" style="color: #0079C1;">Donation amount cannot be Empty. Minimum value it should have is 1.</p>
					<%}else if(Integer.parseInt(errorCode)==2){ %>
						<p class="lead" style="color: #0079C1;">Donation amount cannot be less than 1. Minimum value it should have is 1.</p>
					<%} else if(Integer.parseInt(errorCode)==3){%>
						<p class="lead" style="color: #0079C1;">Donation amount should be a number, with a minimum value of 1.</p>
					<%} %>
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