<!DOCTYPE html>
<%@page import="com.paypal.donation.utils.Constants"%>
<%@page import="com.paypal.donation.services.ApplicationProperties"%>
<html>
  <head>
    <title><%=ApplicationProperties.getProperty(Constants.WIDGET_TITLE) %></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="description" content="<%=ApplicationProperties.getProperty(Constants.META_DESCRIPTION) %>" >
    <meta name="keywords" content="<%=ApplicationProperties.getProperty(Constants.META_KEYWORDS) %>">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <link rel="stylesheet" media="screen" href="assets/css/bootstrap.css" />
    <link rel="stylesheet" media="screen" href="assets/css/bootstrap-responsive.css" />
    <link rel="shortcut icon" href="assets/images/pp_favicon_x.ico">
    
    <style type="text/css">
		.pad{padding-top:30px}
		.content{color:#666}
		.contentpara{font-size:13px;line-height:19px;color:#666}
		.contentHeadline{font-size:22px;line-height:28px;font-weight:300;margin:0;padding:0}
		.footer a{color:#fff}
		.copyright{clear:both;float:left;margin:0 0 10px;padding:0 10px 0 0;color:white;font-size:.846em;font-family:Arial,Helvetica,sans-serif}
		.footertext{margin-top:12px;color:white;font-size:.846em;font-family:Arial,Helvetica,sans-serif}
	</style>
  </head>
  
  <body>
	<!-- Header -->
	<div style="background: url('https://www.paypalobjects.com/webstatic/i/sparta/scr/scr_gray-bkgd.png') repeat scroll 0 0 #6E6D71; height: 44px;"></div>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span10 offset1">
				<a href="https://www.paypal-labs.com" target="_blank">
				<img src="assets/images/labslogo.png" style="margin-top: 15px;" alt="Paypal Labs Logo"></a>
				<a href="ac?method=create" class="btn btn-warning btn-large pull-right" style="margin-top: 10px;">Get Started</a>
			</div>
		</div>
		<br/>
	</div>
	<!-- End Header -->

	<!-- Shut down message -->
	<div class="container-fluid" style="background-color: #F5F5F5;">
		<div class="row-fluid">
			<div class="span10 offset1">
				<div class="text-error lead" style="padding-top: 10px;"><b>IMPORTANT ACCOUNCEMENT:  This application will be removed from service starting May, 15th 2013.  
					If you have any questions, please contact PayPal Labs at labs@paypal.com</b></div>
			</div>
		</div>
	<!-- End Shut down message -->
	
	<!-- Start Image -->
		<div class="row-fluid" style="background-color: #F5F5F5;">
			<div class="span12">
				<img src="assets/images/splash.png" style="max-width: 100%; width: 100%;" alt="Paypal"/>
			</div>
		</div>
	</div>
	<!-- End Image -->
	
	<div class="container-fluid" style="background-color: #F5F5F5;">
		<div class="row-fluid">
			<div class="span5 pad offset1">
				<p class="lead" style="color: #0079C1;"><b>Create one widget and your supporters can donate from their</b></p>
				<ul class="content">
				  <li>iPhones<br/></li>
				  <li>iPads<br/></li>
				  <li>Android Phones and Tablets<br/></li>
				  <li>Blackberries<br/></li>
				  <li>Laptop's and Desktop Computers<br/></li>
				</ul>
				<span class="content">Receive lower processing rates once verified as a 501(c)(3)</span>
			</div>
			<div class="span5">
				<img src="assets/images/splash1.png" class="container" alt="Paypal"/>
			</div>
		</div>
		<br/><hr/>
		<div class="row-fluid">
			<div class="span5 pad offset1">
				<img src="assets/images/splash2.png" class="container" alt="Paypal"/>
			</div>
			<div class="span5">
				<p class="lead" style="color: #0079C1;"><b>
				Generate a Scan-to-Donate QR code for accepting donations via posters, mailers, business cards and envelopes at events.</b></p>
				<ul class="content">
				  <li>Accept all major credit cards<br/></li>
				  <li>Your donor's don't even need a PayPal account<br/></li>
				  <li>No Monthly Fees, No Credit Application<br/></li>
				  <li>Secure payments flow directly to your PayPal account. <br/>
				</ul>
				<span class="content contentHeadline">Need a PayPal account? &nbsp;&nbsp;&nbsp;
				  	<a class="btn btn-primary btn-large" href="https://www.paypal.com/cgi-bin/webscr?cmd=_registration-run" style="color: #FFFFFF">Sign up for free.</a></span>
			</div>
			<div class="span1"></div>
		</div>
		<br/><hr/>
		<div class="row-fluid">
			<div class="span3 content offset1">
				<h3 class="contentHeadline">Simple Setup</h3>
				<span class="content contentpara">Add our 'Donation Widget' button to your site in about 5 minutes. You don't need any programming skills.</span>
			</div>
			<div class="span3 content">
				<h3 class="contentHeadline">More ways to fundraise.</h3>
				<span class="contentpara">Receive credit card and PayPal donations online, by email and on your smartphone.</span>
			</div>
			<div class="span4 content">
				<h3 class="contentHeadline">Trusted by your supporters.</h3>
				<span class="contentpara">If there's one thing people know about PayPal, it's how seriously we take security.</span>
			</div>
			<div class="span1"></div>
		</div>
		<br/><br/><br/>
	</div>
	<!-- Footer -->
	<%@ include file="footer.html" %>
	<!-- End Footer -->
  </body>
</html>