<%@page import="com.paypal.donation.utils.Constants"%>
<%@page import="com.paypal.donation.services.ApplicationProperties"%>
<!DOCTYPE html>
<html>
  <head>
    <title><%=ApplicationProperties.getProperty(Constants.WIDGET_TITLE) %></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <link href="assets/css/bootstrap.css" rel="stylesheet" media="screen">
    <link rel="shortcut icon" href="assets/images/pp_favicon_x.ico">
  </head>
<body>
	<!-- Header -->
	<%@ include file="header.html" %>
	<!-- End Header -->
	
	<div style="background-color: #F5F5F5;">
	<br/>
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span10 offset1">
					<p class="content"><a href="assets/images/7.WidgetTips.pdf">View</a> 7 tips to increase Donations.</p>
					<ol>
						<li>Can I add funds collected from other sources to the widget total ?<br/>
							At this point in time there is no way of adding funds collected from other sources (online/offline) can be added to the widget total. Only 
							funds collected from the widget are taken into account while calculating the funds received.<br/><br/>
						</li>
						<li>Do I need to have a PayPal account in order to create a fundraising widget?<br/>
						   A PayPal account is required to deposit the widget donations. Your PayPal account should be set up as a <b>Premier or Business</b> account.
		 				   <br/><br/>
						</li>
						<li>How do I add my image to the giving widget ?<br/>
							We do not host photos, so you will need to provide the URL to where your image resides (for example, Photobucket, Flickr, Google Images, 
							Bing Images or your own web server). Paste the URL into the "widget cover image" textbox. Your URL should not include any spaces.
							If the image is correct you will see it appear on the widget.<br><br>If you store your photos on an image-hosting site such as Photobucket and Flickr, 
							make sure that the URL you are using links directly to your image and not a webpage (most images will have an extension such as .jpg, .gif, .png.). 
							To get the URL for just the image, you might need to do the following:<br><br>1. Go to the webpage that contains your image. 
							<br>2. Right-Click on the image. If you are using Firefox, select "Copy Image Location." This will give you the actual URL to use for your image on the giving widget.
							<br>3. If you are using Internet Explorer, select "Properties." In the "Address URL" field, the value contains the actual URL to use for your image on the giving widget.
							<br><br><img src="assets/images/faq/widgetImage.png" alt="Adding Cover Image"><br/><br/>
						</li>
						<li>I am having trouble loading an image that resides on a hosting site such as Photobucket, Flickr, and Picasa ?<br/>
							Ensure that the URL you are using links directly to your image and not a webpage for the hosting website. To get the URL for just the image, 
							you might need to do the following: <br>Go to the webpage that contains your image. <br>Right-Click on the image If you are using Firefox, select "Copy Image Location."
							This will give you the actual URL to use for your image on the giving widget. <br>If you are using Internet Explorer, select "Properties." 
							In the "Address URL" field, the value contains the actual URL to use for your image on the giving widget.<br/><br/>
						</li>
						<li>Do I have to specify a financial goal ?<br/>
							If you choose not to specify a goal amount, the goal meter will always be displayed at 50% progress (as depicted in the image below). 
							The actual money raised will still be displayed as a total to the left.<br><br>If you do choose to specify a fundraising goal,
							it will be displayed to the right of the meter. The progress bar will then reflect the total amount currently raised as a percentage of your selected goal amount.
							<br><br><img src="assets/images/faq/goal.png" alt="Adding Goal"/><br/><br/>
						</li>
						<li>When I update my widget, will the copies of my widget also get updated ?<br/>
							All the copies do get updated. For example, when you replace the widget image, the new image will appear on all copies of the widget.<br/><br/>
						</li>
						<li>What currencies are supported ?<br/>
						<ul>
							<li>Canadian Dollars</li>
							<li>Euros</li>
							<li>Pounds Sterling</li><li>U.S. Dollars</li>
							<li>Yen</li><li>Australian Dollars</li>
							<li>New Zealand Dollars</li><li>Swiss Francs</li>
							<li>Hong Kong Dollars</li><li>Singapore Dollars</li><li>Swedish Kronor</li><li>Danish Kroner</li><li>Polish Zloty</li>
							<li>Norwegian Kroner</li><li>Hungarian Forint</li><li>Czech Koruna</li><li>Israeli Shekels</li><li>Mexican Pesos</li>
							<li>Brazilian Real</li><li>Philippine Peso</li><li>Taiwan New Dollars</li><li>Thai Baht</li>
						</ul>The default currency specified is US Dollars.<br/><br/>
						</li>
						<li>Can I change currency after I build the widget ?<br/>
							You can only select a fundraising currency when you first create your widget. If you decide that you need a different currency than the one you 
							originally specified, you will need to build a new widget with the desired currency and post it to your chosen location online.<br/><br/>
						</li>
						<li>How do I publish my giving widget to my personal website or Blogger blog ?<br/>
							Once you have saved your widget, you are directed to the page where you see the lists of widgets created.
							Click on download button. You will see a dialog. Copy the HTML and manually paste on your blog or webpage. 
							<img src="assets/images/faq/save2.png" alt="Saving Widget"/><img src="assets/images/faq/save1.png" alt="Saving Widget"/>
							<br/><br/>
						</li>
						<li>Where do I need to update the giving widget ?<br/>
							You can update your widget by clicking on the title of the widget. Once done editing click on the save button. 
							If you want to continue editing the widget at a later time, your changes will be saved automatically<br/><br/>
						</li>
						<li>What happens when I delete a widget ?<br/>Deleting your widget will cause all copies of it to become inactive. 
							 All widgets will need to be manually removed from the webpages where they are posted. 
							 Widgets left online after deletion will display a notification stating that the widget is not in use.<br>
							 <b>Note : </b> that removing the widget HTML from your blog or website is not the same as deleting the widget. 
							 <br/><br/>
						</li>
						<li>When a widget is copied and a contributor gives through the copied widget, to which PayPal account are the funds deposited?<br/>
							The funds are deposited to the PayPal account that created the original widget. Supporters who copy the widget will not receive the funds<br/><br/>
						</li>
						<li>Can I create a widget to fundraise on behalf of an established nonprofit I want to support ?<br/>
							The best way to fundraise for an established nonprofit is to contact them directly and ask them to create a PayPal Donation widget using their 
							own account. 
							This way, the nonprofit can enter in their EIN to receive a preferred rate on the donations processed. 
							You could then support their fundraiser by copying their widget and posting it on your own website. 
							Funds contributed to their widget and all copies of their widget would go directly to the nonprofit's PayPal account.<br><br>
							If you would like donated funds to go to an account different from the one on which the widget was created, 
							you will need to transfer the funds from the 
							widget owner's Paypal ccount. If you are fundraising on behalf of a nonprofit organization, 
							you are encouraged to make sure you have their permission prior 
							to starting your fundraising campaign. Organizations will usually provide a subordination letter giving individual's 
							permission to fundraise on their behalf.<br/><br/>
						</li>
						</ol>
					</div>	
				<div class="span1"></div>
			</div>
		</div>
	</div>
	<!-- Footer -->
	<%@ include file="footer.html" %>
	<!-- End Footer -->
</body>
</html>